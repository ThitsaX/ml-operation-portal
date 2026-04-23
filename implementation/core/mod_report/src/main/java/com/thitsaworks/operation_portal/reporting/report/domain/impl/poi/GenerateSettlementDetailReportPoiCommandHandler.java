package com.thitsaworks.operation_portal.reporting.report.domain.impl.poi;

import com.thitsaworks.operation_portal.component.misc.logging.NoLogging;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementDetailReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Primary
@NoLogging
public class GenerateSettlementDetailReportPoiCommandHandler implements GenerateSettlementDetailReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementDetailReportPoiCommandHandler.class);

    private static final int DEFAULT_ROW_WINDOW = 200;

    private static final int DEFAULT_LIMIT = Integer.MAX_VALUE;

    private static final int MYSQL_STREAM_FETCH_SIZE = Integer.MIN_VALUE;
    private static final int[] MAX_COLUMN_WIDTHS = {
            32,
            40,
            32,
            40,
            32,
            18,
            32,
            18,
            26,
            18,
            26,
            18,
            18,
            22,
            24,
            18,
            12
    };

    private static final String[] COLUMN_HEADERS = {
            "Sender DFSP ID",
            "Sender DFSP Name",
            "Receiver DFSP ID",
            "Receiver DFSP Name",
            "Transfer ID",
            "Tx Type",
            "Transaction Date",
            "Sender ID Type",
            "Sender ID",
            "Receiver ID Type",
            "Receiver ID",
            "Received Amount",
            "Sent Amount",
            "Payee DFSP Fee",
            "Payee DFSP Commission",
            "HUB Fee",
            "Currency"};

    private final JdbcTemplate jdbcTemplate;

    public GenerateSettlementDetailReportPoiCommandHandler(
            @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        String fileType = normalizeFileType(input.fileType());
        try {
            if ("xlsx".equalsIgnoreCase(fileType)) {
                return new Output(exportSingleChunkXlsx(input));
            }
            if ("csv".equalsIgnoreCase(fileType)) {
                return new Output(exportSingleChunkCsv(input));
            }
            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);
        } catch (ReportException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Error generating settlement detail report with POI", e);
            throw new ReportException(ReportErrors.SETTLEMENT_DETAIL_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public int countRows(CountInput input) {

        Integer rowCount = this.jdbcTemplate.queryForObject("""
                                                                    SELECT COUNT(*) AS rowCount
                                                                    FROM transferFulfilment tF
                                                                    INNER JOIN transfer t ON t.transferId = tF.transferId
                                                                    LEFT JOIN (
                                                                        SELECT transferId, CreatedDate FROM
                                                                        (
                                                                            SELECT transferId, CreatedDate, ROW_NUMBER() OVER (PARTITION BY transferId ORDER BY transferStateChangeId DESC) AS rn
                                                                            FROM transferStateChange tsa
                                                                            WHERE EXISTS (
                                                                                SELECT 1
                                                                                  FROM transferFulfilment tF
                                                                                  JOIN settlementSettlementWindow sSW
                                                                                ON sSW.settlementWindowId = tF.settlementWindowId
                                                                               AND sSW.settlementId = ?
                                                                              WHERE tF.isValid AND tF.transferId = tsa.transferId)
                                                                        ) x
                                                                        WHERE x.rn = 1
                                                                    ) latestState ON latestState.transferId = tF.transferId
                                                                    INNER JOIN transferParticipant tPPayer ON tPPayer.transferId = tF.transferId
                                                                        AND tPPayer.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYER_DFSP')
                                                                    INNER JOIN participantCurrency pCPayer ON pCPayer.participantCurrencyId = tPPayer.participantCurrencyId
                                                                    INNER JOIN participant pPayer ON pPayer.participantId = pCPayer.participantId
                                                                    INNER JOIN transferParticipant tPPayee ON tPPayee.transferId = tF.transferId
                                                                        AND tPPayee.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYEE_DFSP')
                                                                    INNER JOIN participantCurrency pCPayee ON pCPayee.participantCurrencyId = tPPayee.participantCurrencyId
                                                                    INNER JOIN participant pPayee ON pPayee.participantId = pCPayee.participantId
                                                                    INNER JOIN settlementWindow sW on sW.settlementWindowId = tF.settlementWindowId
                                                                    INNER JOIN settlementSettlementWindow sSW on tF.settlementWindowId = sSW.settlementWindowId
                                                                    INNER JOIN settlementWindowStateChange sWSC on sW.currentStateChangeId = sWSC.settlementWindowStateChangeId
                                                                    INNER JOIN settlement s on sSW.settlementId = s.settlementId
                                                                    INNER JOIN currency c ON c.currencyId = pCPayer.currencyId
                                                                    INNER JOIN quote q on q.transactionReferenceId = tF.transferId
                                                                    LEFT JOIN quoteResponse qR ON qR.quoteId = q.quoteId
                                                                    INNER JOIN quoteParty qpPayer on qpPayer.quoteId = q.quoteId AND qpPayer.partyTypeId = (SELECT partyTypeId FROM partyType WHERE name = 'PAYER')
                                                                    INNER JOIN partyIdentifierType pITPayer ON pITPayer.partyIdentifierTypeId = qpPayer.partyIdentifierTypeId
                                                                    INNER JOIN quoteParty qpPayee on qpPayee.quoteId = q.quoteId AND qpPayee.partyTypeId = (SELECT partyTypeId FROM partyType WHERE name = 'PAYEE')
                                                                    INNER JOIN partyIdentifierType pITPayee ON pITPayee.partyIdentifierTypeId = qpPayee.partyIdentifierTypeId
                                                                    INNER JOIN transactionScenario tScenario on tScenario.transactionScenarioId = q.transactionScenarioId
                                                                    LEFT JOIN transactionSubScenario tSub on tSub.transactionSubScenarioId = q.transactionSubScenarioId
                                                                    WHERE tF.isValid
                                                                      AND s.settlementId = ?
                                                                      AND (pPayee.name = ? OR pPayer.name = ?)
                                                                    """,
                                                            new Object[]{
                                                                    input.settlementId(),
                                                                    input.settlementId(),
                                                                    input.fspId(),
                                                                    input.fspId()},
                                                            Integer.class);
        return rowCount == null ? 0 : rowCount;
    }

    private byte[] exportSingleChunkXlsx(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("settlement-detail-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);

             OutputStream outputStream = Files.newOutputStream(tempFile)) {
            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("DFSPSettlementDetailReport");
            if (sheet instanceof SXSSFSheet streamingSheet) {
                streamingSheet.trackAllColumnsForAutoSizing();
            }

            CellStyle headerLabelStyle = headerLabelStyle(workbook);
            CellStyle headerValueStyle = headerValueStyle(workbook);
            CellStyle columnHeaderStyle = columnHeaderStyle(workbook);
            CellStyle textCellStyle = textCellStyle(workbook);
            CellStyle amountCellStyle = amountCellStyle(workbook);
            CellStyle rightTextCellStyle = rightTextCellStyle(workbook);

            int rowIndex = 0;
            rowIndex = writeMeta(sheet,
                                 rowIndex,
                                 "Settlement ID",
                                 input.settlementId(),
                                 headerLabelStyle,
                                 headerValueStyle);
            rowIndex = writeMeta(sheet, rowIndex, "Settlement Created Date", "", headerLabelStyle, headerValueStyle);
            rowIndex = writeMeta(sheet, rowIndex, "DFSP ID", input.fspId(), headerLabelStyle, headerValueStyle);
            rowIndex = writeMeta(sheet, rowIndex, "DFSP Name", input.dfspName(), headerLabelStyle, headerValueStyle);
            rowIndex = writeMeta(sheet,
                                 rowIndex,
                                 "TimeZoneOffSet",
                                 displayOffset(input.timezoneOffset()),
                                 headerLabelStyle,
                                 headerValueStyle);
            rowIndex++;
            Row settlementCreatedRow = sheet.getRow(1);
            Cell settlementCreatedCell =
                settlementCreatedRow == null ? null : settlementCreatedRow.getCell(1);

            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < COLUMN_HEADERS.length; i++) {
                Cell c = headerRow.createCell(i);
                c.setCellValue(COLUMN_HEADERS[i]);
                c.setCellStyle(columnHeaderStyle);
            }

            int freezeRow = rowIndex;
            MetadataCapture metadataCapture = new MetadataCapture();
            RowCounter counter = new RowCounter();
            RowCursor cursor = new RowCursor(rowIndex);
            streamRows(input, row -> {
                metadataCapture.capture(row);
                if (settlementCreatedCell != null && !metadataCapture.isWritten()) {
                    settlementCreatedCell.setCellValue(metadataCapture.settlementCreatedDate());
                    settlementCreatedCell.setCellStyle(headerValueStyle);
                    metadataCapture.markWritten();
                }
                writeDataRow(sheet.createRow(cursor.next()), row, textCellStyle, amountCellStyle, rightTextCellStyle);
                counter.increment();
            });
            flushSheet(sheet);

            if (counter.value() == 0) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            for (int i = 0; i < COLUMN_HEADERS.length; i++) {

                int maxWidth = MAX_COLUMN_WIDTHS[i] * 256;
                sheet.setColumnWidth(i, maxWidth);
            }
            sheet.createFreezePane(0, freezeRow);

            workbook.write(outputStream);
            return Files.readAllBytes(tempFile);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private byte[] exportSingleChunkCsv(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("settlement-detail-", ".csv");
        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {
            MetadataCapture metadata = new MetadataCapture();
            List<SettlementDetailRow> rows = new ArrayList<>();
            streamRows(input, row -> {
                metadata.capture(row);
                rows.add(row);
            });
            if (rows.isEmpty()) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            writer.write(csvLine("Settlement ID", input.settlementId()));
            writer.write(csvLine("Settlement Created Date", metadata.settlementCreatedDate()));
            writer.write(csvLine("DFSP ID", input.fspId()));
            writer.write(csvLine("DFSP Name", input.dfspName()));
            writer.write(csvLine("TimeZoneOffSet", displayOffset(input.timezoneOffset())));
            writer.newLine();
            writer.write(csvLine(COLUMN_HEADERS));

            for (SettlementDetailRow row : rows) {
                writer.write(csvLine(row.payerFspId(),
                                     row.payerFspName(),
                                     row.payeeFspId(),
                                     row.payeeFspName(),
                                     row.transferId(),
                                     row.transactionType(),
                                     row.lastModifiedDate(),
                                     row.payerIdentifierType(),
                                     preserveAsText(row.payerIdentifierValue()),
                                     row.payeeIdentifierType(),
                                     preserveAsText(row.payeeIdentifierValue()),
                                     numberText(row.receivedAmount()),
                                     numberText(row.sentAmount()),
                                     numberOrDashText(row.payeeDfspFeeAmount()),
                                     numberOrDashText(row.payeeDfspCommissionAmount()),
                                     "-",
                                     row.currencyId()));
            }
            writer.flush();
            return Files.readAllBytes(tempFile);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private void writeDataRow(Row row,
                              SettlementDetailRow data,
                              CellStyle textCellStyle,
                              CellStyle amountCellStyle,
                              CellStyle rightTextCellStyle) {

        writeTextCell(row, 0, data.payerFspId(), textCellStyle);
        writeTextCell(row, 1, data.payerFspName(), textCellStyle);
        writeTextCell(row, 2, data.payeeFspId(), textCellStyle);
        writeTextCell(row, 3, data.payeeFspName(), textCellStyle);
        writeTextCell(row, 4, data.transferId(), textCellStyle);
        writeTextCell(row, 5, data.transactionType(), textCellStyle);
        writeTextCell(row, 6, data.lastModifiedDate(), rightTextCellStyle);
        writeTextCell(row, 7, data.payerIdentifierType(), textCellStyle);
        writeTextCell(row, 8, data.payerIdentifierValue(), textCellStyle);
        writeTextCell(row, 9, data.payeeIdentifierType(), textCellStyle);
        writeTextCell(row, 10, data.payeeIdentifierValue(), textCellStyle);
        writeAmountCell(row, 11, data.receivedAmount(), amountCellStyle);
        writeAmountCell(row, 12, data.sentAmount(), amountCellStyle);
        writeAmountOrDashCell(row, 13, data.payeeDfspFeeAmount(), rightTextCellStyle, amountCellStyle);
        writeAmountOrDashCell(row, 14, data.payeeDfspCommissionAmount(), rightTextCellStyle, amountCellStyle);
        writeTextCell(row, 15, "-", rightTextCellStyle);
        writeTextCell(row, 16, data.currencyId(), textCellStyle);
    }

    private void streamRows(Input input, SettlementDetailRowConsumer consumer) {

        List<Object> parameters = new ArrayList<>();
        parameters.add(input.fspId());
        addTimezoneDateFormatParams(parameters, input.timezoneOffset());
        parameters.add(input.fspId());
        parameters.add(input.fspId());
        addTimezoneDateFormatParams(parameters, input.timezoneOffset());
        addTimezoneOffsetOnlyParams(parameters, input.timezoneOffset());
        parameters.add(input.settlementId());
        parameters.add(input.settlementId());
        parameters.add(input.fspId());
        parameters.add(input.fspId());
        parameters.add(input.offset() == null ? 0 : input.offset());
        parameters.add(input.limit() == null ? DEFAULT_LIMIT : input.limit());

        try {
            this.jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(settlementDetailQuery(),
                                                                          ResultSet.TYPE_FORWARD_ONLY,
                                                                          ResultSet.CONCUR_READ_ONLY);
                statement.setFetchDirection(ResultSet.FETCH_FORWARD);
                statement.setFetchSize(MYSQL_STREAM_FETCH_SIZE);
                for (int i = 0; i < parameters.size(); i++) {
                    statement.setObject(i + 1, parameters.get(i));
                }
                return statement;
            }, resultSet -> {

                    try {
                        consumer.accept(mapRow(resultSet));
                    } catch (IOException e) {
                        throw new IOExceptionRuntimeException(e);
                    }

            });
        } catch (IOExceptionRuntimeException e) {
            throw e;
        }
    }

    private SettlementDetailRow mapRow(ResultSet rs) throws SQLException {

        return new SettlementDetailRow(rs.getString("settlementId"),
                                       rs.getString("settlementCreatedDate"),
                                       rs.getString("transferId"),
                                       rs.getString("lastModifiedDate"),
                                       rs.getString("payerFspId"),
                                       rs.getString("payerFspName"),
                                       rs.getString("payeeFspId"),
                                       rs.getString("payeeFspName"),
                                       rs.getString("payerIdentifierType"),
                                       rs.getString("payerIdentifierValue"),
                                       rs.getString("payeeIdentifierType"),
                                       rs.getString("payeeIdentifierValue"),
                                       rs.getString("transactionType"),
                                       rs.getBigDecimal("receivedAmount"),
                                       rs.getBigDecimal("sentAmount"),
                                       rs.getBigDecimal("payeeDfspFeeAmount"),
                                       rs.getBigDecimal("payeeDfspCommissionAmount"),
                                       rs.getString("currencyId"));
    }

    private String settlementDetailQuery() {

        return """
                SELECT
                  ( SELECT participantId FROM participant WHERE NAME = ? AND name != 'Hub') AS participantId,
                  IFNULL(pPayer.name,'') AS payerFspId,
                  IFNULL(pPayer.description,pPayer.name) AS payerFspName,
                  IFNULL(pPayee.name,'') AS payeeFspId,
                  IFNULL(pPayee.description,pPayee.name) AS payeeFspName,
                  tF.transferId,
              tS.name AS transactionType,
              tSub.name AS transactionNature,
              CONCAT(
                DATE_FORMAT(CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                CONVERT_TZ(latestState.CreatedDate, '+00:00', CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2)))
                ELSE CONVERT_TZ(latestState.CreatedDate, '+00:00', CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)))
                END,'%Y-%m-%dT%H:%i:%s'),
                CASE WHEN SUBSTRING(?,1,1) = '-' THEN CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2))
                ELSE CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)) END
                ) AS lastModifiedDate,
                  pITPayer.name AS payerIdentifierType,
                  CAST(qpPayer.partyIdentifierValue AS CHAR) AS payerIdentifierValue,
                  pITPayee.name AS payeeIdentifierType,
                  CAST(qpPayee.partyIdentifierValue AS CHAR) AS payeeIdentifierValue,
                  ROUND(IF(pPayee.name = ?  , t.amount, 0),2) AS receivedAmount,
                  ROUND( IF(pPayer.name = ?   , t.amount, 0),2) AS sentAmount,
                  c.currencyId,
                  CAST(s.settlementId AS CHAR) AS settlementId,
                  CONCAT(
                    DATE_FORMAT(CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                    CONVERT_TZ(s.createdDate, '+00:00', CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2)))
                    ELSE CONVERT_TZ(s.createdDate, '+00:00', CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)))
                    END,'%Y-%m-%dT%H:%i:%s'),
                    CASE WHEN SUBSTRING(?,1,1) = '-' THEN CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2))
                    ELSE CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)) END
                    ) AS settlementCreatedDate, sSW.settlementWindowId,
                  CASE WHEN SUBSTRING(?,1,1) = '-' THEN CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2)) ELSE CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)) END AS timezoneoffset ,
                  IF(qR.payeeFspFeeAmount IS NOT NULL, ROUND(qR.payeeFspFeeAmount, 2), NULL) AS payeeDfspFeeAmount,
                  IF(qR.payeeFspCommissionAmount IS NOT NULL, ROUND(qR.payeeFspCommissionAmount, 2), NULL) AS payeeDfspCommissionAmount
                FROM transferFulfilment tF
                INNER JOIN transfer t ON t.transferId = tF.transferId
                LEFT JOIN (
                    SELECT transferId, CreatedDate FROM
                    (
                        SELECT transferId, CreatedDate, ROW_NUMBER() OVER (PARTITION BY transferId ORDER BY transferStateChangeId DESC) AS rn
                        FROM transferStateChange tsa
                        WHERE EXISTS (
                            SELECT 1
                              FROM transferFulfilment tF
                              JOIN settlementSettlementWindow sSW
                            ON sSW.settlementWindowId = tF.settlementWindowId
                           AND sSW.settlementId = ?
                          WHERE tF.isValid AND tF.transferId = tsa.transferId)
                      ) x
                      WHERE x.rn = 1
                ) latestState ON latestState.transferId = tF.transferId
                INNER JOIN transferParticipant tPPayer ON tPPayer.transferId = tF.transferId
                AND tPPayer.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYER_DFSP')
                INNER JOIN participantCurrency pCPayer ON pCPayer.participantCurrencyId = tPPayer.participantCurrencyId
                INNER JOIN participant pPayer ON pPayer.participantId = pCPayer.participantId
                INNER JOIN transferParticipant tPPayee ON tPPayee.transferId = tF.transferId
                AND tPPayee.transferParticipantRoleTypeId = (SELECT transferParticipantRoleTypeId from transferParticipantRoleType WHERE name = 'PAYEE_DFSP')
                INNER JOIN participantCurrency pCPayee ON pCPayee.participantCurrencyId = tPPayee.participantCurrencyId
                INNER JOIN participant pPayee ON pPayee.participantId = pCPayee.participantId
                INNER JOIN settlementWindow sW on sW.settlementWindowId = tF.settlementWindowId
                INNER JOIN settlementSettlementWindow sSW on tF.settlementWindowId = sSW.settlementWindowId
                INNER JOIN settlementWindowStateChange sWSC on sW.currentStateChangeId = sWSC.settlementWindowStateChangeId
                INNER JOIN settlement s on sSW.settlementId = s.settlementId
                INNER JOIN currency c ON c.currencyId = pCPayer.currencyId
                INNER JOIN quote q on q.transactionReferenceId = tF.transferId
                LEFT JOIN quoteResponse qR ON qR.quoteId = q.quoteId
                INNER JOIN quoteParty qpPayer on qpPayer.quoteId = q.quoteId AND qpPayer.partyTypeId = (SELECT partyTypeId FROM partyType WHERE name = 'PAYER')
                INNER JOIN partyIdentifierType pITPayer ON pITPayer.partyIdentifierTypeId = qpPayer.partyIdentifierTypeId
                INNER JOIN quoteParty qpPayee on qpPayee.quoteId = q.quoteId AND qpPayee.partyTypeId = (SELECT partyTypeId FROM partyType WHERE name = 'PAYEE')
                INNER JOIN partyIdentifierType pITPayee ON pITPayee.partyIdentifierTypeId = qpPayee.partyIdentifierTypeId
                INNER JOIN transactionScenario tS on tS.transactionScenarioId = q.transactionScenarioId
            LEFT JOIN transactionSubScenario tSub on tSub.transactionSubScenarioId = q.transactionSubScenarioId
                WHERE tF.isValid AND s.settlementId = ? AND (pPayee.name = ?  OR pPayer.name = ?)
                ORDER BY latestState.CreatedDate ASC, tF.transferId ASC
                LIMIT ?, ?
                """;
    }

    private void addTimezoneDateFormatParams(List<Object> params, String timezoneOffset) {

        for (int i = 0; i < 10; i++) {params.add(timezoneOffset);}
    }

    private void addTimezoneOffsetOnlyParams(List<Object> params, String timezoneOffset) {

        for (int i = 0; i < 5; i++) {params.add(timezoneOffset);}
    }

    private int writeMeta(Sheet s, int i, String k, String v, CellStyle ks, CellStyle vs) {

        Row r = s.createRow(i++);
        Cell kc = r.createCell(0);
        kc.setCellValue(k);
        kc.setCellStyle(ks);
        Cell vc = r.createCell(1);
        vc.setCellValue(v == null ? "" : v);
        vc.setCellStyle(vs);
        return i;
    }

    private void writeTextCell(Row row, int col, String val, CellStyle style) {

        Cell cell = row.createCell(col);
        cell.setCellValue(val == null ? "" : val);
        cell.setCellStyle(style);
    }

    private void writeAmountCell(Row row, int col, BigDecimal val, CellStyle style) {

        Cell cell = row.createCell(col);
        if (val != null) {cell.setCellValue(val.doubleValue());} else {cell.setCellValue("");}
        cell.setCellStyle(style);
    }

    private void writeAmountOrDashCell(Row row, int col, BigDecimal val, CellStyle textStyle, CellStyle amountStyle) {

        if (val == null || BigDecimal.ZERO.compareTo(val) == 0) {
            writeTextCell(row, col, "-", textStyle);
        } else {
            writeAmountCell(row, col, val, amountStyle);
        }
    }

    private CellStyle headerLabelStyle(SXSSFWorkbook wb) {

        CellStyle s = wb.createCellStyle();
        s.setBorderTop(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);
        s.setAlignment(HorizontalAlignment.LEFT);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        var f = wb.createFont();
        f.setBold(true);
        s.setFont(f);
        return s;
    }

    private CellStyle headerValueStyle(SXSSFWorkbook wb) {

        CellStyle s = wb.createCellStyle();
        s.cloneStyleFrom(headerLabelStyle(wb));
        s.setWrapText(true);
        var f = wb.createFont();
        f.setBold(false);
        s.setFont(f);
        return s;
    }

    private CellStyle columnHeaderStyle(SXSSFWorkbook wb) {

        CellStyle s = wb.createCellStyle();
        s.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        s.cloneStyleFrom(headerLabelStyle(wb));
        s.setWrapText(false);
        return s;
    }

    private CellStyle textCellStyle(org.apache.poi.ss.usermodel.Workbook wb) {

        CellStyle s = wb.createCellStyle();
        s.setBorderTop(BorderStyle.THIN);
        s.setBorderRight(BorderStyle.THIN);
        s.setBorderBottom(BorderStyle.THIN);
        s.setBorderLeft(BorderStyle.THIN);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setAlignment(HorizontalAlignment.LEFT);
        s.setWrapText(true);
        var f = wb.createFont();
        f.setFontName("Calibri");
        f.setFontHeightInPoints((short) 11);
        s.setFont(f);
        return s;
    }

    private CellStyle amountCellStyle(org.apache.poi.ss.usermodel.Workbook wb) {

        CellStyle s = wb.createCellStyle();
        s.cloneStyleFrom(textCellStyle(wb));
        s.setAlignment(HorizontalAlignment.RIGHT);
        s.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
        return s;
    }

    private CellStyle rightTextCellStyle(org.apache.poi.ss.usermodel.Workbook wb) {

        CellStyle s = wb.createCellStyle();
        s.cloneStyleFrom(textCellStyle(wb));
        s.setAlignment(HorizontalAlignment.RIGHT);
        return s;
    }

    private String normalizeFileType(String fileType) {

        if (fileType == null) {return "";}
        String normalized = fileType.trim().toLowerCase(Locale.ROOT);
        return normalized.startsWith(".") ? normalized.substring(1) : normalized;
    }

    private String displayOffset(String rawOffset) {

        ZoneOffset zoneOffset = this.parseOffset(rawOffset);
        return zoneOffset.getId().equals("Z") ? "+00:00" : zoneOffset.getId();
    }

    private ZoneOffset parseOffset(String rawOffset) {

        if (rawOffset == null || rawOffset.isBlank()) {
            return ZoneOffset.UTC;
        }

        String normalized = rawOffset.trim();
        if (normalized.matches("[+-]\\d{4}")) {
            normalized = normalized.substring(0, 3) + ":" + normalized.substring(3);
        } else if (normalized.matches("\\d{4}")) {
            normalized = "+" + normalized.substring(0, 2) + ":" + normalized.substring(2);
        }

        return ZoneOffset.of(normalized);
    }


    private String preserveAsText(String value) {return value == null ? "" : value + "\t";}

    private String numberText(BigDecimal value) {

        return value == null ? "" : value.stripTrailingZeros().toPlainString();
    }

    private String numberOrDashText(BigDecimal value) {

        return (value == null || BigDecimal.ZERO.compareTo(value) == 0) ? "-" : numberText(value);
    }

    private String csvLine(String... values) {

        StringBuilder line = new StringBuilder();
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {line.append(',');}
            line.append(escapeCsv(values[i]));
        }
        line.append(System.lineSeparator());
        return line.toString();
    }

    private String escapeCsv(String value) {

        String safe = value == null ? "" : value;
        if (!safe.contains(",") && !safe.contains("\"") && !safe.contains("\n") && !safe.contains("\r")) {return safe;}
        return "\"" + safe.replace("\"", "\"\"") + "\"";
    }

    private void flushSheet(Sheet sheet) throws IOException {

        if (sheet instanceof SXSSFSheet streamingSheet) {streamingSheet.flushRows(DEFAULT_ROW_WINDOW);}
    }

    private record SettlementDetailRow(String settlementId,
                                       String settlementCreatedDate,
                                       String transferId,
                                       String lastModifiedDate,
                                       String payerFspId,
                                       String payerFspName,
                                       String payeeFspId,
                                       String payeeFspName,
                                       String payerIdentifierType,
                                       String payerIdentifierValue,
                                       String payeeIdentifierType,
                                       String payeeIdentifierValue,
                                       String transactionType,
                                       BigDecimal receivedAmount,
                                       BigDecimal sentAmount,
                                       BigDecimal payeeDfspFeeAmount,
                                       BigDecimal payeeDfspCommissionAmount,
                                       String currencyId) {}

    private static final class MetadataCapture {

        private String settlementCreatedDate = "";

        private boolean written;

        private void capture(SettlementDetailRow row) {

            if (this.settlementCreatedDate == null || this.settlementCreatedDate.isBlank()) {
                this.settlementCreatedDate = row.settlementCreatedDate();
            }
        }

        private String settlementCreatedDate() {

            return this.settlementCreatedDate == null ? "" : this.settlementCreatedDate;
        }

        private boolean isWritten() {

            return this.written;
        }

        private void markWritten() {

            this.written = true;
        }

    }

    @FunctionalInterface
    private interface SettlementDetailRowConsumer {

        void accept(SettlementDetailRow row) throws IOException;

    }

    private static final class IOExceptionRuntimeException extends RuntimeException {

        private IOExceptionRuntimeException(IOException cause) {super(cause);}

    }

    private static final class RowCursor {

        private int current;

        private RowCursor(int start) {this.current = start;}

        private int next() {return this.current++;}

    }

    private static final class RowCounter {

        private int value;

        private void increment() {this.value++;}

        private int value() {return this.value;}

    }

}
