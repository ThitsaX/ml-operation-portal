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
import com.thitsaworks.operation_portal.reporting.report.domain.GeneratePacsFeeAmountSwiftReportCommand;
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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
public class GeneratePacsFeeAmountSwiftReportCommandHandler
    implements GeneratePacsFeeAmountSwiftReportCommand {

    private static final String DEFAULT_SETTLEMENT_DATE = "000000";

    private static final String DEFAULT_CURRENCY = "XXX";

    private static final String DEFAULT_SENDER_BIC = "REPCGNGA";

    private static final String DEFAULT_SENDER_CLEARING_SYSTEM_CODE = "GINPA";

    private static final String DEFAULT_SENDER_CLEARING_MEMBER_ID = "REPCGNGAASM";

    private static final String DEFAULT_RECEIVER_BIC = "REPCGNGA";

    private static final String PACS029_TEMPLATE = "/com/thitsaworks/operation_portal/reporting/report/report/"
        + "feeAmountPacs029Template.xml";

    private static final String PACS029_MOVEMENT_RECORD_TEMPLATE = "/com/thitsaworks/operation_portal/reporting/report/"
        + "report/feeAmountPacs029MovementRecordTemplate.xml";

    private final JdbcTemplate jdbcTemplate;

    private final ReportConfiguration.Settings reportSettings;

    @Autowired
    public GeneratePacsFeeAmountSwiftReportCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate,
        ReportConfiguration.Settings reportSettings) {

        this.jdbcTemplate = jdbcTemplate;
        this.reportSettings = reportSettings;
    }

    @Override
    public Output execute(Input input)
        throws ReportException {

        try {
            List<DirectionalFeeRow> feeRows = this.jdbcTemplate.query(
                """
                    WITH fee_per_quote AS (
                      SELECT
                        qe.quoteId,
                        MAX(CASE WHEN qe.key = 'payerfee'  THEN CAST(qe.value AS DECIMAL(18,4)) END) AS payerFee,
                        MAX(CASE WHEN qe.key = 'payeefee'  THEN CAST(qe.value AS DECIMAL(18,4)) END) AS payeeFee,
                        MAX(CASE WHEN qe.key = 'schemeFee' THEN CAST(qe.value AS DECIMAL(18,4)) END) AS hubFee
                      FROM quoteExtension qe
                      GROUP BY qe.quoteId
                    ),
                    settlement_transfers AS (
                      SELECT DISTINCT
                        tf.transferId,
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
                      JOIN settlementSettlementWindow ssw
                        ON ssw.settlementId = s.settlementId
                      JOIN transferFulfilment tf
                        ON tf.settlementWindowId = ssw.settlementWindowId
                      WHERE s.settlementId = ?
                    ),
                    directional AS (
                      SELECT
                        pPayer.name AS payerDFSP,
                        pPayee.name AS payeeDFSP,
                        q.currencyId AS currency,
                        st.settlementDate,
                        st.settlementCreationDate,
                        COUNT(DISTINCT t.transferId) AS totalTransactions,
                        SUM(t.amount) AS totalAmount,
                        SUM(COALESCE(f.payerFee, 0)) AS payerFee,
                        SUM(COALESCE(f.payeeFee, 0)) AS payeeFee,
                        SUM(COALESCE(f.hubFee, 0)) AS hubFee
                      FROM settlement_transfers st
                      JOIN transfer t
                        ON t.transferId = st.transferId
                      JOIN transferParticipant tpPayer
                        ON tpPayer.transferId = t.transferId
                       AND tpPayer.transferParticipantRoleTypeId = (
                          SELECT transferParticipantRoleTypeId
                          FROM transferParticipantRoleType
                          WHERE name = 'PAYER_DFSP'
                       )
                      JOIN participantCurrency pcPayer
                        ON pcPayer.participantCurrencyId = tpPayer.participantCurrencyId
                      JOIN participant pPayer
                        ON pPayer.participantId = pcPayer.participantId
                      JOIN transferParticipant tpPayee
                        ON tpPayee.transferId = t.transferId
                       AND tpPayee.transferParticipantRoleTypeId = (
                          SELECT transferParticipantRoleTypeId
                          FROM transferParticipantRoleType
                          WHERE name = 'PAYEE_DFSP'
                       )
                      JOIN participantCurrency pcPayee
                        ON pcPayee.participantCurrencyId = tpPayee.participantCurrencyId
                      JOIN participant pPayee
                        ON pPayee.participantId = pcPayee.participantId
                      JOIN quote q
                        ON q.transactionReferenceId = t.transferId
                      LEFT JOIN fee_per_quote f
                        ON f.quoteId = q.quoteId
                      GROUP BY
                        pPayer.name,
                        pPayee.name,
                        q.currencyId,
                        st.settlementDate,
                        st.settlementCreationDate
                    )
                    SELECT
                      d.payerDFSP,
                      COALESCE(payer_lp.account_number, '') AS payerAccountNumber,
                      CASE
                        WHEN payer_op.parent_participant_name IS NULL OR payer_op.parent_participant_name = ''
                          THEN COALESCE(payer_lp.account_number, '')
                        ELSE COALESCE(payer_parent_lp.account_number, '')
                      END AS payerSettlementAgentAccountNumber,
                      CASE
                        WHEN payer_op.parent_participant_name IS NULL OR payer_op.parent_participant_name = ''
                          THEN 0
                        ELSE 1
                      END AS payerIndirectParticipant,
                      d.payeeDFSP,
                      COALESCE(payee_lp.account_number, '') AS payeeAccountNumber,
                      CASE
                        WHEN payee_op.parent_participant_name IS NULL OR payee_op.parent_participant_name = ''
                          THEN COALESCE(payee_lp.account_number, '')
                        ELSE COALESCE(payee_parent_lp.account_number, '')
                      END AS payeeSettlementAgentAccountNumber,
                      CASE
                        WHEN payee_op.parent_participant_name IS NULL OR payee_op.parent_participant_name = ''
                          THEN 0
                        ELSE 1
                      END AS payeeIndirectParticipant,
                      COALESCE(hub_lp.account_number, '') AS hubAccountNumber,
                      CASE
                        WHEN hub_op.parent_participant_name IS NULL OR hub_op.parent_participant_name = ''
                          THEN COALESCE(hub_lp.account_number, '')
                        ELSE COALESCE(hub_parent_lp.account_number, '')
                      END AS hubSettlementAgentAccountNumber,
                      CASE
                        WHEN hub_op.parent_participant_name IS NULL OR hub_op.parent_participant_name = ''
                          THEN 0
                        ELSE 1
                      END AS hubIndirectParticipant,
                      d.currency,
                      d.settlementDate,
                      d.settlementCreationDate,
                      d.totalTransactions,
                      d.totalAmount,
                      GREATEST(d.payerFee - COALESCE(r.payerFee, 0), 0) AS payerFee,
                      GREATEST(d.payeeFee - COALESCE(r.payeeFee, 0), 0) AS payeeFee,
                      d.hubFee
                    FROM directional d
                    LEFT JOIN directional r
                      ON r.payerDFSP = d.payeeDFSP
                     AND r.payeeDFSP = d.payerDFSP
                     AND r.currency = d.currency
                    LEFT JOIN operation_portal.tbl_participant payer_op
                      ON payer_op.participant_name = d.payerDFSP
                    LEFT JOIN operation_portal.tbl_participant payer_parent_op
                      ON payer_parent_op.participant_name = payer_op.parent_participant_name
                    LEFT JOIN operation_portal.tbl_liquidity_profile payer_lp
                      ON payer_lp.participant_id = payer_op.participant_id
                     AND payer_lp.currency = d.currency
                     AND payer_lp.is_active = 1
                    LEFT JOIN operation_portal.tbl_liquidity_profile payer_parent_lp
                      ON payer_parent_lp.participant_id = payer_parent_op.participant_id
                     AND payer_parent_lp.currency = d.currency
                     AND payer_parent_lp.is_active = 1
                    LEFT JOIN operation_portal.tbl_participant payee_op
                      ON payee_op.participant_name = d.payeeDFSP
                    LEFT JOIN operation_portal.tbl_participant payee_parent_op
                      ON payee_parent_op.participant_name = payee_op.parent_participant_name
                    LEFT JOIN operation_portal.tbl_liquidity_profile payee_lp
                      ON payee_lp.participant_id = payee_op.participant_id
                     AND payee_lp.currency = d.currency
                     AND payee_lp.is_active = 1
                    LEFT JOIN operation_portal.tbl_liquidity_profile payee_parent_lp
                      ON payee_parent_lp.participant_id = payee_parent_op.participant_id
                     AND payee_parent_lp.currency = d.currency
                     AND payee_parent_lp.is_active = 1
                    LEFT JOIN operation_portal.tbl_participant hub_op
                      ON hub_op.participant_name = 'hub'
                    LEFT JOIN operation_portal.tbl_participant hub_parent_op
                      ON hub_parent_op.participant_name = hub_op.parent_participant_name
                    LEFT JOIN operation_portal.tbl_liquidity_profile hub_lp
                      ON hub_lp.participant_id = hub_op.participant_id
                     AND hub_lp.currency = d.currency
                     AND hub_lp.is_active = 1
                    LEFT JOIN operation_portal.tbl_liquidity_profile hub_parent_lp
                      ON hub_parent_lp.participant_id = hub_parent_op.participant_id
                     AND hub_parent_lp.currency = d.currency
                     AND hub_parent_lp.is_active = 1
                    ORDER BY
                      d.payerDFSP,
                      d.payeeDFSP,
                      d.currency;
                    """, (rs, rowNum) -> new DirectionalFeeRow(
                    rs.getString("payerDFSP"),
                    rs.getString("payerAccountNumber"),
                    rs.getString("payerSettlementAgentAccountNumber"),
                    rs.getBoolean("payerIndirectParticipant"),
                    rs.getString("payeeDFSP"),
                    rs.getString("payeeAccountNumber"),
                    rs.getString("payeeSettlementAgentAccountNumber"),
                    rs.getBoolean("payeeIndirectParticipant"),
                    rs.getString("hubAccountNumber"),
                    rs.getString("hubSettlementAgentAccountNumber"),
                    rs.getBoolean("hubIndirectParticipant"),
                    rs.getString("currency"),
                    rs.getString("settlementDate"),
                    rs.getString("settlementCreationDate"),
                    rs.getBigDecimal("payerFee"),
                    rs.getBigDecimal("hubFee")),
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
                input.settlementId());

            List<SwiftParticipantFeeAmountRow> rows = this.buildSwiftParticipantFeeRows(feeRows, input);

            if (rows == null || rows.isEmpty()) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            String xmlReport = this.buildPacs029XmlReport(input.settlementId(), input.timezone(), rows);
            return new Output(xmlReport.getBytes(StandardCharsets.UTF_8));

        } catch (ReportException e) {
            throw e;
        } catch (Exception e) {
            throw new ReportException(ReportErrors.FEE_AMOUNT_REPORT_FAILURE_EXCEPTION);
        }
    }

    private List<SwiftParticipantFeeAmountRow> buildSwiftParticipantFeeRows(List<DirectionalFeeRow> feeRows, Input input) {

        if (feeRows == null || feeRows.isEmpty()) {
            return List.of();
        }

        Map<ParticipantFeeAmountKey, SwiftParticipantFeeAmountRow> participantFeeAmounts = new LinkedHashMap<>();

        for (DirectionalFeeRow feeRow : feeRows) {
            if (!this.matchesCurrencyFilter(feeRow.currency(), input.currency())) {
                continue;
            }

            BigDecimal payerFee = this.valueOrZero(feeRow.payerFee());
            BigDecimal hubFee = this.valueOrZero(feeRow.hubFee());
            String settlementDate = this.hasText(feeRow.settlementDate())
                ? feeRow.settlementDate()
                : DEFAULT_SETTLEMENT_DATE;
            String settlementCreationDate = feeRow.settlementCreationDate();

            this.addParticipantFeeAmount(
                participantFeeAmounts,
                feeRow.payerDFSP(),
                feeRow.currency(),
                payerFee,
                feeRow.payerAccountNumber(),
                feeRow.payerSettlementAgentAccountNumber(),
                feeRow.payerIndirectParticipant(),
                settlementDate,
                settlementCreationDate);
            this.addParticipantFeeAmount(
                participantFeeAmounts,
                "hub",
                feeRow.currency(),
                hubFee,
                feeRow.hubAccountNumber(),
                feeRow.hubSettlementAgentAccountNumber(),
                feeRow.hubIndirectParticipant(),
                settlementDate,
                settlementCreationDate);
            this.addParticipantFeeAmount(
                participantFeeAmounts,
                feeRow.payeeDFSP(),
                feeRow.currency(),
                payerFee.add(hubFee).negate(),
                feeRow.payeeAccountNumber(),
                feeRow.payeeSettlementAgentAccountNumber(),
                feeRow.payeeIndirectParticipant(),
                settlementDate,
                settlementCreationDate);
        }

        return participantFeeAmounts.values()
                                 .stream()
                                 .filter(row -> row.feeAmount() != null && row.feeAmount().signum() != 0)
                                 .sorted(Comparator.comparing(
                                                       SwiftParticipantFeeAmountRow::accountNumber)
                                                   .thenComparing(
                                                       SwiftParticipantFeeAmountRow::participantName)
                                                   .thenComparing(
                                                       SwiftParticipantFeeAmountRow::currencyId))
                                 .toList();
    }

    private void addParticipantFeeAmount(Map<ParticipantFeeAmountKey, SwiftParticipantFeeAmountRow> participantFeeAmounts,
                                         String participantName,
                                         String currencyId,
                                         BigDecimal feeAmount,
                                         String accountNumber,
                                         String settlementAgentAccountNumber,
                                         boolean indirectParticipant,
                                         String settlementDate,
                                         String settlementCreationDate) {

        if (feeAmount == null || feeAmount.signum() == 0) {
            return;
        }

        ParticipantFeeAmountKey key = new ParticipantFeeAmountKey(
            participantName,
            currencyId,
            accountNumber,
            settlementAgentAccountNumber,
            indirectParticipant,
            settlementDate,
            settlementCreationDate);

        SwiftParticipantFeeAmountRow current = participantFeeAmounts.get(key);
        BigDecimal currentFeeAmount = current == null ? BigDecimal.ZERO : current.feeAmount();
        participantFeeAmounts.put(
            key,
            new SwiftParticipantFeeAmountRow(
                participantName,
                currencyId,
                currentFeeAmount.add(feeAmount),
                accountNumber,
                settlementAgentAccountNumber,
                indirectParticipant,
                settlementDate,
                settlementCreationDate));
    }

    private boolean matchesCurrencyFilter(String rowCurrency, String inputCurrency) {

        return !this.hasText(inputCurrency)
            || "ALL".equalsIgnoreCase(inputCurrency)
            || (this.hasText(rowCurrency) && rowCurrency.equalsIgnoreCase(inputCurrency));
    }

    private BigDecimal valueOrZero(BigDecimal value) {

        return value == null ? BigDecimal.ZERO : value;
    }

    private String buildPacs029XmlReport(String settlementId, String timezone, List<SwiftParticipantFeeAmountRow> rows) {

        String settlementDate = rows
                                    .stream()
                                    .map(SwiftParticipantFeeAmountRow::settlementDate)
                                    .filter(this::hasText)
                                    .findFirst()
                                    .orElse(DEFAULT_SETTLEMENT_DATE);

        String feeMtid = this.calculateFeeMtid(settlementId);
        String messageId = "NimbaPayT-" + settlementDate + "-" + feeMtid;
        String movementReferenceNumber = settlementDate + "/" + feeMtid;
        String creationDate = this.resolveCreationDate(rows, timezone);
        String controlSum = this.toXmlAmount(this.calculateControlSum(rows));

        return this.populateTemplate(
            this.loadTemplate(PACS029_TEMPLATE),
            Map.of(
                "senderBic", this.escapeXml(DEFAULT_SENDER_BIC),
                "senderClearingSystemCode", this.escapeXml(DEFAULT_SENDER_CLEARING_SYSTEM_CODE),
                "senderClearingMemberId", this.escapeXml(DEFAULT_SENDER_CLEARING_MEMBER_ID),
                "receiverBic", this.escapeXml(this.resolveReceiverBic()),
                "messageId", this.escapeXml(messageId),
                "creationDate", creationDate,
                "controlSum", this.escapeXml(controlSum),
                "feeMtid", this.escapeXml(feeMtid),
                "movementRecordCount", String.valueOf(rows.size()),
                "movementRecords", this.buildMovementRecords(rows, movementReferenceNumber)));
    }

    private String buildMovementRecords(List<SwiftParticipantFeeAmountRow> rows, String referenceNumber) {

        String movementRecordTemplate = this.loadTemplate(PACS029_MOVEMENT_RECORD_TEMPLATE);
        StringBuilder movementRecords = new StringBuilder(rows.size() * 512);

        int sequenceNumber = 1;
        for (SwiftParticipantFeeAmountRow row : rows) {
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
                                       SwiftParticipantFeeAmountRow row,
                                       String referenceNumber,
                                       int sequenceNumber) {

        String currency = this.normalizeCurrency(row.currencyId());
        String creditDebit = this.creditDebitIndicator(row.feeAmount());
        String feeAmount = this.toXmlAmount(row.feeAmount());
        String accountNumber = this.normalizeAccountNumber(row.accountNumber());
        String settlementAgentAccountNumber = this.normalizeAccountNumber(row.settlementAgentAccountNumber());

        return this.populateTemplate(
            movementRecordTemplate,
            Map.of(
                "movementId", this.escapeXml(referenceNumber + "/" + sequenceNumber),
                "sequenceNumber", String.valueOf(sequenceNumber),
                "currency", this.escapeXml(currency),
                "amount", this.escapeXml(feeAmount),
                "creditDebit", creditDebit,
                "settlementAgentBic", this.escapeXml(settlementAgentAccountNumber),
                "participantXml", this.buildParticipantXml(row, accountNumber)));
    }

    private String buildParticipantXml(SwiftParticipantFeeAmountRow row, String accountNumber) {

        if (row.indirectParticipant()) {
            return """
            <Ptcpt>
              <Id>
                <OrgId>
                  <Othr>
                    <Id>%s</Id>
                  </Othr>
                </OrgId>
              </Id>
            </Ptcpt>""".formatted(this.escapeXml(accountNumber));
        }

        return """
            <Ptcpt>
              <Id>
                <OrgId>
                  <AnyBIC>%s</AnyBIC>
                </OrgId>
              </Id>
            </Ptcpt>""".formatted(this.escapeXml(accountNumber));
    }

    private BigDecimal calculateControlSum(List<SwiftParticipantFeeAmountRow> rows) {

        return rows.stream()
                   .map(SwiftParticipantFeeAmountRow::feeAmount)
                   .map(amount -> amount == null ? BigDecimal.ZERO : amount.abs())
                   .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private String resolveReceiverBic() {

        if (!this.hasText(this.reportSettings.receiverBIC())) {
            return DEFAULT_RECEIVER_BIC;
        }
        return this.normalizeReceiverBicFi(this.reportSettings.receiverBIC());
    }

    private String calculateFeeMtid(String settlementId) {

        if (settlementId == null || settlementId.isBlank()) {
            throw new IllegalArgumentException("Settlement ID cannot be null or empty.");
        }

        try {
            return new BigInteger(settlementId)
                       .multiply(BigInteger.TWO)
                       .toString();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Settlement ID must be a valid number: " + settlementId, e);
        }

    }

    private String normalizeReceiverBicFi(String receiverBic) {

        String normalized = this.normalizeBicFi(receiverBic, DEFAULT_RECEIVER_BIC);
        if (normalized.startsWith("I971") && normalized.length() > 4) {
            normalized = normalized.substring(4);
        }
        if (normalized.endsWith("XXXXN") && normalized.length() > 8) {
            return normalized.substring(0, 8);
        }
        return normalized;
    }

    private String normalizeCurrency(String currencyId) {

        if (!this.hasText(currencyId)) {
            return DEFAULT_CURRENCY;
        }

        String normalized = currencyId.trim().toUpperCase(Locale.ROOT);
        return normalized.length() > 3 ? normalized.substring(0, 3) : normalized;
    }

    private String normalizeBicFi(String participantSwiftCode, String participantName) {

        String base = this.hasText(participantSwiftCode) ? participantSwiftCode : participantName;
        if (!this.hasText(base)) {
            return "UNKNOWN";
        }

        String compact = base.trim()
                             .toUpperCase(Locale.ROOT)
                             .replaceAll("[^A-Z0-9]", "");
        return compact.isEmpty() ? "UNKNOWN" : compact;
    }

    private String normalizeAccountNumber(String accountNumber) {

        if (!this.hasText(accountNumber)) {
            return "";
        }
        return accountNumber.trim();
    }

    private String creditDebitIndicator(BigDecimal feeAmount) {

        if (feeAmount == null) {
            return "CRDT";
        }
        return feeAmount.signum() < 0 ? "DBIT" : "CRDT";
    }

    private String toXmlAmount(BigDecimal feeAmount) {

        BigDecimal value = feeAmount == null ? BigDecimal.ZERO : feeAmount.abs().stripTrailingZeros();
        return value.toPlainString();
    }

    private String resolveCreationDate(List<SwiftParticipantFeeAmountRow> rows, String timezone) {

        String creationDate = rows.stream()
                                  .map(SwiftParticipantFeeAmountRow::settlementCreationDate)
                                  .filter(this::hasText)
                                  .findFirst()
                                  .orElse(null);

        if (this.hasText(creationDate)) {
            return creationDate + this.toIsoTimezoneOffset(timezone);
        }

        return "1970-01-01T00:00:00+00:00";
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

    private record SwiftParticipantFeeAmountRow(String participantName,
                                                String currencyId,
                                                BigDecimal feeAmount,
                                                String accountNumber,
                                                String settlementAgentAccountNumber,
                                                boolean indirectParticipant,
                                                String settlementDate,
                                                String settlementCreationDate) { }

    private record DirectionalFeeRow(String payerDFSP,
                                     String payerAccountNumber,
                                     String payerSettlementAgentAccountNumber,
                                     boolean payerIndirectParticipant,
                                     String payeeDFSP,
                                     String payeeAccountNumber,
                                     String payeeSettlementAgentAccountNumber,
                                     boolean payeeIndirectParticipant,
                                     String hubAccountNumber,
                                     String hubSettlementAgentAccountNumber,
                                     boolean hubIndirectParticipant,
                                     String currency,
                                     String settlementDate,
                                     String settlementCreationDate,
                                     BigDecimal payerFee,
                                     BigDecimal hubFee) { }

    private record ParticipantFeeAmountKey(String participantName,
                                           String currencyId,
                                           String accountNumber,
                                           String settlementAgentAccountNumber,
                                           boolean indirectParticipant,
                                           String settlementDate,
                                           String settlementCreationDate) { }

}
