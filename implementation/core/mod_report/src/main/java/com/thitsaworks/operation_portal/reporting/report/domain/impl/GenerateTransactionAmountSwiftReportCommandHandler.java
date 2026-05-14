package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateTransactionAmountSwiftReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;

@Service
public class GenerateTransactionAmountSwiftReportCommandHandler implements GenerateTransactionAmountSwiftReportCommand {

    private static final String DEFAULT_SETTLEMENT_DATE = "000000";
    private static final String DEFAULT_CURRENCY = "XXX";
    private static final String DEFAULT_RECEIVER_BIC = "UNKNOWNBIC";
    private static final String DEFAULT_SENDER_BLOCK = "default-sender_block";
    private static final String DEFAULT_SENDER_BLOCK_PARTICIPANT_ID = "1111111111111111";

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenerateTransactionAmountSwiftReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        try {
            List<SwiftParticipantAmountRow> rows = this.jdbcTemplate.query(
                """
                    SELECT
                        result.participantName,
                        result.participantSwiftCode,
                        result.currencyId,
                        SUM(result.amount) AS amount,
                        result.accountNumber,
                        result.settlementDate
                    FROM (
                        SELECT
                            COALESCE(op.parent_participant_name, op.participant_name) AS participantName,
                    
                            COALESCE(parent_op.participant_id, op.participant_id) AS participantSwiftCode,
                    
                            pc.currencyId,
                    
                            tp.amount,
                    
                            COALESCE(parent_lp.account_number, lp.account_number, '') AS accountNumber,
                    
                            DATE_FORMAT(
                                CASE
                                    WHEN SUBSTRING('?', 1, 1) = '-'
                                        THEN CONVERT_TZ(
                                            s.createdDate,
                                            '+00:00',
                                            CONCAT('-', SUBSTRING('?', 2, 2), ':', SUBSTRING('?', 4, 2))
                                        )
                                    ELSE CONVERT_TZ(
                                        s.createdDate,
                                        '+00:00',
                                        CONCAT('+', SUBSTRING('?', 1, 2), ':', SUBSTRING('?', 3, 2))
                                    )
                                END,
                                '%y%m%d'
                            ) AS settlementDate
                    
                        FROM settlement s
                    
                        INNER JOIN settlementSettlementWindow ssw
                            ON ssw.settlementId = s.settlementId
                    
                        INNER JOIN transferFulfilment tf
                            ON tf.settlementWindowId = ssw.settlementWindowId
                    
                        INNER JOIN transferParticipant tp
                            ON tp.transferId = tf.transferId
                    
                        INNER JOIN participantCurrency pc
                            ON tp.participantCurrencyId = pc.participantCurrencyId
                    
                        INNER JOIN participant p
                            ON p.participantId = pc.participantId
                    
                        LEFT JOIN operation_portal.tbl_participant op
                            ON op.participant_name = p.name
                    
                        LEFT JOIN operation_portal.tbl_participant parent_op
                            ON parent_op.participant_name = op.parent_participant_name
                    
                        LEFT JOIN operation_portal.tbl_liquidity_profile lp
                            ON lp.participant_id = op.participant_id
                           AND lp.currency = pc.currencyId
                           AND lp.is_active = 1
                    
                        LEFT JOIN operation_portal.tbl_liquidity_profile parent_lp
                            ON parent_lp.participant_id = parent_op.participant_id
                           AND parent_lp.currency = pc.currencyId
                           AND parent_lp.is_active = 1
                    
                        INNER JOIN ledgerAccountType lat
                            ON lat.ledgerAccountTypeId = pc.ledgerAccountTypeId
                    
                        WHERE s.settlementId = ?
                          AND pc.currencyId = ?
                          AND lat.name = 'POSITION'
                    ) result
                    
                    GROUP BY
                        result.participantName,
                        result.participantSwiftCode,
                        result.currencyId,
                        result.accountNumber,
                        result.settlementDate
                        HAVING SUM(result.amount) <> 0
                    
                    ORDER BY result.participantSwiftCode ASC;
                    """,
                (rs, rowNum) -> new SwiftParticipantAmountRow(
                    rs.getString("participantName"),
                    rs.getString("participantSwiftCode"),
                    rs.getString("currencyId"),
                    rs.getBigDecimal("amount"),
                    rs.getString("accountNumber"),
                    rs.getString("settlementDate")),
                input.timezone(),
                input.timezone(),
                input.timezone(),
                input.timezone(),
                input.timezone(),
                input.settlementId(),
                input.currency());

            if (rows == null || rows.isEmpty()) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            String senderBlock = this.resolveSenderBlock();
            String swiftMessage = this.buildMt971Message(input.settlementId(), rows, senderBlock);
            return new Output(swiftMessage.getBytes(StandardCharsets.UTF_8));

        } catch (ReportException e) {
            throw e;
        } catch (Exception e) {
            throw new ReportException(ReportErrors.SETTLEMENT_BANK_REPORT_FAILURE_EXCEPTION);
        }
    }

    private String buildMt971Message(String settlementId, List<SwiftParticipantAmountRow> rows, String senderBlock) {

        String settlementDate = rows.stream()
                                    .map(SwiftParticipantAmountRow::settlementDate)
                                    .filter(this::hasText)
                                    .findFirst()
                                    .orElse(DEFAULT_SETTLEMENT_DATE);

        String referenceNumber = "SETTL-Tnx-" + settlementId + "-" + settlementDate;
        String receiverBic = this.normalizeSwiftCode(rows.get(0).participantSwiftCode(), rows.get(0).participantName());

        StringBuilder swift = new StringBuilder(512);
        swift.append(senderBlock).append("\n");
        swift.append("{2:I971")
             .append(this.hasText(receiverBic) ? receiverBic : DEFAULT_RECEIVER_BIC)
             .append("N}")
             .append("\n");
        swift.append("{3:{108:SETTL-TNX/").append(settlementId).append("}}").append("\n");
        swift.append("{4:").append("\n");
        swift.append(":20:").append(referenceNumber).append("\n");

        for (SwiftParticipantAmountRow row : rows) {
            String participantCode = this.normalizeSwiftCode(row.participantSwiftCode(), row.participantName());
            String currency = this.normalizeCurrency(row.currencyId());
            String dcMark = this.debitCreditMark(row.amount());
            String amount = this.toSwiftAmount(row.amount());

            swift.append(":25:").append(row.accountNumber()).append("\n");
            swift.append(":62F:")
                 .append(dcMark)
                 .append(settlementDate)
                 .append(currency)
                 .append(amount)
                 .append("\n");
        }

        swift.append("-}");
        return swift.toString();
    }

    private String resolveSenderBlock() {

        List<String> senderBlocks = this.jdbcTemplate.query(
            """
                SELECT account_number
                FROM operation_portal.tbl_liquidity_profile
                WHERE participant_id = ?
                LIMIT 1
                """,
            (rs, rowNum) -> rs.getString("account_number"),
            DEFAULT_SENDER_BLOCK_PARTICIPANT_ID);

        if (senderBlocks == null || senderBlocks.isEmpty()) {
            return DEFAULT_SENDER_BLOCK;
        }

        String senderBlock = senderBlocks.get(0);
        if (this.hasText(senderBlock)) {
            return "{1:" + senderBlock + "}";
        }
        return DEFAULT_SENDER_BLOCK;
    }

    private String normalizeCurrency(String currencyId) {

        if (!this.hasText(currencyId)) {
            return DEFAULT_CURRENCY;
        }

        String normalized = currencyId.trim().toUpperCase(Locale.ROOT);
        return normalized.length() > 3 ? normalized.substring(0, 3) : normalized;
    }

    private String normalizeSwiftCode(String participantSwiftCode, String participantName) {

        String base = this.hasText(participantSwiftCode) ? participantSwiftCode : participantName;
        if (!this.hasText(base)) {
            return "UNKNOWN";
        }

        String compact = base.trim().toUpperCase(Locale.ROOT).replaceAll("[^A-Z0-9]", "");
        return compact.isEmpty() ? "UNKNOWN" : compact;
    }

    private String debitCreditMark(BigDecimal amount) {

        if (amount == null) {
            return "C";
        }
        return amount.signum() < 0 ? "D" : "C";
    }

    private String toSwiftAmount(BigDecimal amount) {

        BigDecimal value = amount == null ? BigDecimal.ZERO : amount.abs().stripTrailingZeros();
        String asPlain = value.toPlainString();
        return asPlain.replace('.', ',');
    }

    private boolean hasText(String value) {

        return value != null && !value.isBlank();
    }

    private record SwiftParticipantAmountRow(String participantName,
                                             String participantSwiftCode,
                                             String currencyId,
                                             BigDecimal amount,
                                             String accountNumber,
                                             String settlementDate) { }
}
