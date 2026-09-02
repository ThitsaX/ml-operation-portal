/*
 * Copyright (c) 2024-2026 ThitsaWorks Pte. Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.thitsaworks.operation_portal.reporting.report.domain.impl;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.ReportConfiguration;
import com.thitsaworks.operation_portal.reporting.report.domain.GeneratePacsTransactionAmountSwiftReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class GeneratePacsTransactionAmountSwiftReportCommandHandler
    implements GeneratePacsTransactionAmountSwiftReportCommand {

    private static final String DEFAULT_SETTLEMENT_DATE = "000000";

    private static final String DEFAULT_CURRENCY = "XXX";

    private static final String DEFAULT_SENDER_CLEARING_SYSTEM_CODE = "GINPA";

    private static final String DEFAULT_SENDER_BIC = "REPCGNGA";

    private static final String DEFAULT_SENDER_CLEARING_MEMBER_ID = "REPCGNGAASM";

    private static final String DEFAULT_RECEIVER_BIC = "REPCGNGA";

    private static final String PACS029_TEMPLATE = "/com/thitsaworks/operation_portal/reporting/report/report/"
        + "transactionAmountPacs029Template.xml";

    private static final String PACS029_MOVEMENT_RECORD_TEMPLATE = "/com/thitsaworks/operation_portal/reporting/report/"
        + "report/transactionAmountPacs029MovementRecordTemplate.xml";

    private final JdbcTemplate jdbcTemplate;

    private final ReportConfiguration.Settings reportSettings;

    @Autowired
    public GeneratePacsTransactionAmountSwiftReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate,
        ReportConfiguration.Settings reportSettings) {

        this.jdbcTemplate = jdbcTemplate;
        this.reportSettings = reportSettings;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        try {
            List<SwiftParticipantAmountRow> rows = this.jdbcTemplate.query(
                """
                    SELECT
                        result.participantName,
                        result.participantBic,
                        result.participantAccountNumber,
                        result.settlementAgentBic,
                        result.isIndirectParticipant,
                        result.currencyId,
                        SUM(result.amount) AS amount,
                        result.settlementDate,
                        result.settlementCreationDate
                    FROM (
                        SELECT
                            COALESCE(op.participant_name, p.name) AS participantName,
                            op.participant_id AS participantBic,
                            COALESCE(lp.account_number, '') AS participantAccountNumber,
                            CASE
                                WHEN op.parent_participant_name IS NULL OR op.parent_participant_name = ''
                                    THEN 0
                                ELSE 1
                            END AS isIndirectParticipant,
                            CASE
                                WHEN op.parent_participant_name IS NULL OR op.parent_participant_name = ''
                                    THEN COALESCE(lp.account_number, '')
                                ELSE COALESCE(parent_lp.account_number, '')
                            END AS settlementAgentBic,
                    
                            pc.currencyId,
                    
                            tp.amount,
                    
                            DATE_FORMAT(
                                CASE
                                    WHEN SUBSTRING(?, 1, 1) = '-'
                                        THEN CONVERT_TZ(
                                            s.createdDate,
                                            '+00:00',
                                            CONCAT('-', SUBSTRING(?, 2, 2), ':', SUBSTRING(?, 4, 2))
                                        )
                                    ELSE CONVERT_TZ(
                                        s.createdDate,
                                        '+00:00',
                                        CONCAT('+', SUBSTRING(?, 1, 2), ':', SUBSTRING(?, 3, 2))
                                    )
                                END,
                                '%y%m%d'
                            ) AS settlementDate,

                            DATE_FORMAT(
                                CASE
                                    WHEN SUBSTRING(?, 1, 1) = '-'
                                        THEN CONVERT_TZ(
                                            s.createdDate,
                                            '+00:00',
                                            CONCAT('-', SUBSTRING(?, 2, 2), ':', SUBSTRING(?, 4, 2))
                                        )
                                    ELSE CONVERT_TZ(
                                        s.createdDate,
                                        '+00:00',
                                        CONCAT('+', SUBSTRING(?, 1, 2), ':', SUBSTRING(?, 3, 2))
                                    )
                                END,
                                '%Y-%m-%dT%H:%i:%s'
                            ) AS settlementCreationDate
                    
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
                          AND ( ? = 'ALL' OR pc.currencyId = ? )
                          AND lat.name = 'POSITION'
                    ) result
                    
                    GROUP BY
                        result.participantName,
                        result.participantBic,
                        result.participantAccountNumber,
                        result.settlementAgentBic,
                        result.isIndirectParticipant,
                        result.currencyId,
                        result.settlementDate,
                        result.settlementCreationDate
                        HAVING SUM(result.amount) <> 0
                    
                    ORDER BY result.participantBic ASC;
                    """,
                (rs, rowNum) -> new SwiftParticipantAmountRow(
                    rs.getString("participantName"),
                    rs.getString("participantBic"),
                    rs.getString("participantAccountNumber"),
                    rs.getString("settlementAgentBic"),
                    rs.getBoolean("isIndirectParticipant"),
                    rs.getString("currencyId"),
                    rs.getBigDecimal("amount"),
                    rs.getString("settlementDate"),
                    rs.getString("settlementCreationDate")),
                input.timezone(),
                input.timezone(),
                input.timezone(),
                input.timezone(),
                input.timezone(),
                input.timezone(),
                input.timezone(),
                input.timezone(),
                input.timezone(),
                input.timezone(),
                input.settlementId(),
                input.currency(),
                input.currency());

            if (rows == null || rows.isEmpty()) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            String xmlReport = this.buildPacs029XmlReport(input.settlementId(), input.timezone(), rows);
            return new Output(xmlReport.getBytes(StandardCharsets.UTF_8));

        } catch (ReportException e) {
            throw e;
        } catch (Exception e) {
            throw new ReportException(ReportErrors.TRANSACTION_AMOUNT_REPORT_FAILURE_EXCEPTION);
        }
    }

    private String buildPacs029XmlReport(String settlementId, String timezone, List<SwiftParticipantAmountRow> rows) {

        String settlementDate = rows.stream()
                                    .map(SwiftParticipantAmountRow::settlementDate)
                                    .filter(this::hasText)
                                    .findFirst()
                                    .orElse(DEFAULT_SETTLEMENT_DATE);

        String transactionMtid = this.calculateTransactionMtid(settlementId);
        String messageId = "NimbaPayT-" + settlementDate + "-" + transactionMtid;
        String movementReferenceNumber = settlementDate + "/" + transactionMtid;
        String creationDate = this.resolveCreationDate(rows, timezone);
        String controlSum = this.toXmlAmount(this.calculateControlSum(rows));

        return this.populateTemplate(
            this.loadTemplate(PACS029_TEMPLATE),
            Map.of(
                "senderBic", this.escapeXml(this.reportSettings.senderGuiMBIC()),
                "senderClearingSystemCode", this.escapeXml(DEFAULT_SENDER_CLEARING_SYSTEM_CODE),
                "senderClearingMemberId", this.escapeXml(this.reportSettings.settlementMemberId()),
                "receiverBic", this.escapeXml(this.reportSettings.receiverBCRGBIC()),
                "messageId", this.escapeXml(messageId),
                "creationDate", creationDate,
                "controlSum", this.escapeXml(controlSum),
                "transactionMtid", this.escapeXml(transactionMtid),
                "movementRecordCount", String.valueOf(rows.size()),
                "movementRecords", this.buildMovementRecords(rows, movementReferenceNumber)));
    }

    private String buildMovementRecords(List<SwiftParticipantAmountRow> rows, String referenceNumber) {

        String movementRecordTemplate = this.loadTemplate(PACS029_MOVEMENT_RECORD_TEMPLATE);
        StringBuilder movementRecords = new StringBuilder(rows.size() * 512);

        int sequenceNumber = 1;
        for (SwiftParticipantAmountRow row : rows) {
            movementRecords.append(this.buildMovementRecord(movementRecordTemplate, row, referenceNumber, sequenceNumber));
            movementRecords.append("\n");
            sequenceNumber++;
        }

        if (!movementRecords.isEmpty()) {
            movementRecords.setLength(movementRecords.length() - 1);
        }
        return movementRecords.toString();
    }

    private String buildMovementRecord(String movementRecordTemplate,
                                       SwiftParticipantAmountRow row,
                                       String referenceNumber,
                                       int sequenceNumber) {

        String currency = this.normalizeCurrency(row.currencyId());
        String creditDebit = this.creditDebitIndicator(row.amount());
        String amount = this.toXmlAmount(row.amount());
        String settlementAgentBic = this.normalizeAccountNumber(row.settlementAgentBic());
        String participantXml = this.buildParticipantXml(row);

        return this.populateTemplate(
            movementRecordTemplate,
            Map.of(
                "movementId", this.escapeXml(referenceNumber + "/" + sequenceNumber),
                "sequenceNumber", String.valueOf(sequenceNumber),
                "currency", this.escapeXml(currency),
                "amount", this.escapeXml(amount),
                "creditDebit", creditDebit,
                "settlementAgentBic", this.escapeXml(settlementAgentBic),
                "participantXml", participantXml));
    }

    private String buildParticipantXml(SwiftParticipantAmountRow row) {

        if (row.isIndirectParticipant()) {
            return """
            <Ptcpt>
              <Id>
                <OrgId>
                  <Othr>
                    <Id>%s</Id>
                  </Othr>
                </OrgId>
              </Id>
            </Ptcpt>""".formatted(this.escapeXml(this.normalizeAccountNumber(row.participantAccountNumber())));
        }

        return """
            <Ptcpt>
              <Id>
                <OrgId>
                  <AnyBIC>%s</AnyBIC>
                </OrgId>
              </Id>
            </Ptcpt>""".formatted(this.escapeXml(this.normalizeAccountNumber(row.participantAccountNumber())));
    }

    private BigDecimal calculateControlSum(List<SwiftParticipantAmountRow> rows) {

        return rows.stream()
                   .map(SwiftParticipantAmountRow::amount)
                   .map(amount -> amount == null ? BigDecimal.ZERO : amount.abs())
                   .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String calculateTransactionMtid(String settlementId) {

        if (settlementId == null || settlementId.isBlank()) {
            throw new IllegalArgumentException("Settlement ID cannot be null or empty.");
        }

        try {
            return new BigInteger(settlementId)
                       .multiply(BigInteger.TWO)
                       .subtract(BigInteger.ONE)
                       .toString();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Settlement ID must be a valid number: " + settlementId, e);
        }
    }

    private String normalizeCurrency(String currencyId) {

        if (!this.hasText(currencyId)) {
            return DEFAULT_CURRENCY;
        }

        String
            normalized =
            currencyId.trim()
                      .toUpperCase(Locale.ROOT);
        return normalized.length() > 3 ? normalized.substring(0, 3) : normalized;
    }

    private String normalizeAccountNumber(String accountNumber) {

        if (!this.hasText(accountNumber)) {
            return "";
        }
        return accountNumber.trim();
    }

    private String creditDebitIndicator(BigDecimal amount) {

        if (amount == null) {
            return "DBIT";
        }
        return amount.signum() < 0 ? "CRDT" : "DBIT";
    }

    private String toXmlAmount(BigDecimal amount) {

        BigDecimal
            value =
            amount == null ? BigDecimal.ZERO : amount.abs()
                                                     .stripTrailingZeros();
        return value.toPlainString();
    }

    private String resolveCreationDate(List<SwiftParticipantAmountRow> rows, String timezone) {

        String creationDate = rows.stream()
                                  .map(SwiftParticipantAmountRow::settlementCreationDate)
                                  .filter(this::hasText)
                                  .findFirst()
                                  .orElse(null);

        if (this.hasText(creationDate)) {
            return creationDate + this.toIsoTimezoneOffset(timezone);
        }

        return "";
    }

    private String toIsoTimezoneOffset(String timezone) {

        if (!this.hasText(timezone)) {
            return "+00:00";
        }

        String normalized = timezone.trim();
        if (normalized.matches("[+-]\\d{2}:\\d{2}")) {
            return normalized;
        }
        if (normalized.matches("[+-]\\d{4}")) {
            return normalized.substring(0, 3) + ":" + normalized.substring(3, 5);
        }
        if (normalized.matches("\\d{4}")) {
            return "+" + normalized.substring(0, 2) + ":" + normalized.substring(2, 4);
        }
        return "+00:00";
    }

    private String escapeXml(String value) {

        if (value == null) {
            return "";
        }

        return value.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&apos;");
    }

    private String loadTemplate(String path) {

        try (InputStream inputStream = this.getClass().getResourceAsStream(path)) {
            if (inputStream == null) {
                throw new IllegalStateException("Report XML template not found: " + path);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read report XML template: " + path, e);
        }
    }

    private String populateTemplate(String template, Map<String, String> values) {

        String populated = template;
        for (Map.Entry<String, String> entry : values.entrySet()) {
            populated = populated.replace("${" + entry.getKey() + "}", entry.getValue());
        }
        return populated;
    }

    private boolean hasText(String value) {

        return value != null && !value.isBlank();
    }

    private record SwiftParticipantAmountRow(String participantName,
                                             String participantBic,
                                             String participantAccountNumber,
                                             String settlementAgentBic,
                                             boolean isIndirectParticipant,
                                             String currencyId,
                                             BigDecimal amount,
                                             String settlementDate,
                                             String settlementCreationDate) { }

}
