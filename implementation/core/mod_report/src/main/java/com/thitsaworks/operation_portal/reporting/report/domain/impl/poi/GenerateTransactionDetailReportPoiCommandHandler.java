package com.thitsaworks.operation_portal.reporting.report.domain.impl.poi;

import com.thitsaworks.operation_portal.component.misc.logging.NoLogging;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateTransactionDetailReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
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
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
@NoLogging
public class GenerateTransactionDetailReportPoiCommandHandler
    implements GenerateTransactionDetailReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateTransactionDetailReportPoiCommandHandler.class);

    private static final int DEFAULT_ROW_WINDOW = 200;

    private static final int DEFAULT_LIMIT = Integer.MAX_VALUE;

    // MySQL streams rows only when the statement is forward-only and fetch size is MIN_VALUE.
    private static final int MYSQL_STREAM_FETCH_SIZE = Integer.MIN_VALUE;

    private static final String[] COLUMN_HEADERS = {
        "Transfer ID",
        "Date",
        "Sender DFSP ID",
        "Sender DFSP Name",
        "Receiver DFSP ID",
        "Receiver DFSP Name",
        "Transfer Type",
        "Amount",
        "Payee Received Amount",
        "Fee",
        "Commission",
        "Currency",
        "Status"};

    private static final int[] COLUMN_WIDTHS = {
        40,
        32,
        18,
        49,
        18,
        49,
        16,
        24,
        24,
        18,
        18,
        18,
        18};

    private static final DateTimeFormatter HEADER_DATE_FORMAT = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd'T'HH:mm:ssXXX");

    private final JdbcTemplate jdbcTemplate;

    public GenerateTransactionDetailReportPoiCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        try {
            if ("xlsx".equalsIgnoreCase(input.filetype())) {
                return new Output(this.exportSingleChunkXlsx(input));
            }

            if ("csv".equalsIgnoreCase(input.filetype())) {
                return new Output(this.exportSingleChunkCsv(input));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

        } catch (ReportException exception) {

            throw exception;

        } catch (Exception exception) {

            LOG.error("Error generating transaction detail report with POI", exception);
            throw new ReportException(ReportErrors.TRANSACTION_DETAIL_REPORT_FAILURE_EXCEPTION);
        }
    }

    public Output exportAll(Input input, int totalRowCount, int pageSize) throws ReportException {

        if (totalRowCount <= 0) {
            throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
        }

        try {
            if ("xlsx".equalsIgnoreCase(input.filetype())) {
                return new Output(this.exportAllXlsx(input, totalRowCount, pageSize));
            }

            if ("csv".equalsIgnoreCase(input.filetype())) {
                return new Output(this.exportAllCsv(input, totalRowCount, pageSize));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

        } catch (ReportException exception) {

            throw exception;

        } catch (Exception exception) {

            LOG.error("Error generating full transaction detail report with POI", exception);
            throw new ReportException(ReportErrors.TRANSACTION_DETAIL_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public int countRows(CountInput input) {

        Integer rowCount = this.countTransactionDetailRows(
            input.startDate(), input.endDate(),
            input.state(), input.dfspId(), input.timeZoneOffset());
        return rowCount == null ? 0 : rowCount;
    }

    private byte[] exportSingleChunkXlsx(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("transaction-detail-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("TransactionDetailReport");

            CellStyle headerLabelStyle = this.headerLabelStyle(workbook);
            CellStyle headerValueStyle = this.headerValueStyle(workbook);
            CellStyle columnHeaderStyle = this.columnHeaderStyle(workbook);
            CellStyle textCellStyle = this.textCellStyle(workbook);
            CellStyle amountCellStyle = this.amountCellStyle(workbook);

            int headerEndRow = this.writeReportHeader(
                sheet, input, headerLabelStyle, headerValueStyle);
            int rowIndex = headerEndRow + 1;

            Row columnHeaderRow = sheet.createRow(rowIndex++);
            for (int index = 0; index < COLUMN_HEADERS.length; index++) {
                Cell cell = columnHeaderRow.createCell(index);
                cell.setCellValue(COLUMN_HEADERS[index]);
                cell.setCellStyle(columnHeaderStyle);
            }

            RowCursor rowCursor = new RowCursor(rowIndex);
            this.streamRows(
                input,
                row -> this.writeDataRow(
                    sheet.createRow(rowCursor.next()), row, textCellStyle,
                    amountCellStyle));
            this.flushSheet(sheet);

            if (rowCursor.current() == rowIndex) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            for (int index = 0; index < COLUMN_WIDTHS.length; index++) {
                sheet.setColumnWidth(index, COLUMN_WIDTHS[index] * 256);
            }

            sheet.createFreezePane(0, rowIndex);

            workbook.write(outputStream);
            workbook.dispose();
            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private byte[] exportAllXlsx(Input input, int totalRowCount, int pageSize)
        throws IOException, ReportException {

        Path tempFile = Files.createTempFile("transaction-detail-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("TransactionDetailReport");

            CellStyle headerLabelStyle = this.headerLabelStyle(workbook);
            CellStyle headerValueStyle = this.headerValueStyle(workbook);
            CellStyle columnHeaderStyle = this.columnHeaderStyle(workbook);
            CellStyle textCellStyle = this.textCellStyle(workbook);
            CellStyle amountCellStyle = this.amountCellStyle(workbook);

            int headerEndRow = this.writeReportHeader(
                sheet, input, headerLabelStyle, headerValueStyle);
            int rowIndex = headerEndRow + 1;

            Row columnHeaderRow = sheet.createRow(rowIndex++);
            for (int index = 0; index < COLUMN_HEADERS.length; index++) {
                Cell cell = columnHeaderRow.createCell(index);
                cell.setCellValue(COLUMN_HEADERS[index]);
                cell.setCellStyle(columnHeaderStyle);
            }

            RowCursor rowCursor = new RowCursor(rowIndex);
            int baseOffset = input.offset() == null ? 0 : input.offset();
            for (int offset = 0; offset < totalRowCount; offset += pageSize) {
                int limit = Math.min(pageSize, totalRowCount - offset);
                Input chunkInput = new Input(
                    input.startDate(), input.endDate(), input.state(),
                    input.dfspId(), input.filetype(), input.timeZoneOffset(), baseOffset + offset,
                    limit);

                this.streamRows(
                    chunkInput,
                    row -> this.writeDataRow(
                        sheet.createRow(rowCursor.next()), row, textCellStyle,
                        amountCellStyle));
                this.flushSheet(sheet);
            }

            if (rowCursor.current() == rowIndex) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            for (int index = 0; index < COLUMN_WIDTHS.length; index++) {
                sheet.setColumnWidth(index, COLUMN_WIDTHS[index] * 256);
            }

            //  sheet.createFreezePane(0, rowIndex);

            workbook.write(outputStream);
            workbook.dispose();
            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private byte[] exportSingleChunkCsv(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("transaction-detail-", ".csv");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {

            this.writeCsvHeader(writer, input);

            RowCounter rowCounter = new RowCounter();
            this.streamRows(
                input, row -> {
                    writer.write(this.csvLine(this.toCsvValues(row)));
                    rowCounter.increment();
                });

            if (rowCounter.value() == 0) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            writer.flush();
            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private byte[] exportAllCsv(Input input, int totalRowCount, int pageSize)
        throws IOException, ReportException {

        Path tempFile = Files.createTempFile("transaction-detail-", ".csv");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {

            this.writeCsvHeader(writer, input);

            RowCounter rowCounter = new RowCounter();
            int baseOffset = input.offset() == null ? 0 : input.offset();
            for (int offset = 0; offset < totalRowCount; offset += pageSize) {
                int limit = Math.min(pageSize, totalRowCount - offset);
                Input chunkInput = new Input(
                    input.startDate(), input.endDate(), input.state(),
                    input.dfspId(), input.filetype(), input.timeZoneOffset(), baseOffset + offset,
                    limit);

                this.streamRows(
                    chunkInput, row -> {
                        writer.write(this.csvLine(this.toCsvValues(row)));
                        rowCounter.increment();
                    });
            }

            if (rowCounter.value() == 0) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            writer.flush();
            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private void writeCsvHeader(BufferedWriter writer, Input input) throws IOException {

        writer.write(this.csvLine(
            "Start Date",
            this.formatHeaderDate(input.startDate(), input.timeZoneOffset())));
        writer.write(this.csvLine(
            "End Date",
            this.formatHeaderDate(input.endDate(), input.timeZoneOffset())));
        writer.write(this.csvLine("Status", input.state()));
        writer.write(this.csvLine("TimeZoneOffSet", this.displayOffset(input.timeZoneOffset())));
        writer.newLine();
        writer.write(this.csvLine(COLUMN_HEADERS));
    }

    private String[] toCsvValues(TransactionDetailRow row) {

        return new String[]{
            row.transferId(),
            row.transactionDate(),
            row.senderDfspId(),
            row.senderDfspName(),
            row.receiverDfspId(),
            row.receiverDfspName(),
            row.transferType(),
            this.amountText(row.amount()),
            this.amountText(row.payeeReceivedAmount()),
            this.amountOrDashText(row.payeeDfspFeeAmount()),
            this.amountOrDashText(row.payeeDfspCommissionAmount()),
            row.currencyId(),
            row.status()};
    }

    private void writeDataRow(Row row,
                              TransactionDetailRow data,
                              CellStyle textCellStyle,
                              CellStyle amountCellStyle) {

        this.writeTextCell(row, 0, data.transferId(), textCellStyle);
        this.writeTextCell(row, 1, data.transactionDate(), textCellStyle);
        this.writeTextCell(row, 2, data.senderDfspId(), textCellStyle);
        this.writeTextCell(row, 3, data.senderDfspName(), textCellStyle);
        this.writeTextCell(row, 4, data.receiverDfspId(), textCellStyle);
        this.writeTextCell(row, 5, data.receiverDfspName(), textCellStyle);
        this.writeTextCell(row, 6, data.transferType(), textCellStyle);
        this.writeAmountCell(row, 7, data.amount(), amountCellStyle);
        this.writeAmountCell(row, 8, data.payeeReceivedAmount(), amountCellStyle);
        this.writeAmountOrDashCell(
            row, 9, data.payeeDfspFeeAmount(), textCellStyle,
            amountCellStyle);
        this.writeAmountOrDashCell(
            row, 10, data.payeeDfspCommissionAmount(), textCellStyle,
            amountCellStyle);
        this.writeTextCell(row, 11, data.currencyId(), textCellStyle);
        this.writeTextCell(row, 12, data.status(), textCellStyle);
    }

    private void streamRows(Input input, TransactionDetailRowConsumer consumer) {

        List<Object> parameters = new ArrayList<>();
        String reportQuery = this.transactionDetailQuery();

        parameters.add(input.timeZoneOffset());
        parameters.add(input.startDate());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.startDate());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());

        parameters.add(input.timeZoneOffset());
        parameters.add(input.endDate());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.endDate());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());

        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());

        parameters.add(input.state());
        parameters.add(input.state());
        parameters.add(input.dfspId());
        parameters.add(input.dfspId());
        parameters.add(input.dfspId());
        parameters.add(input.offset() == null ? 0 : input.offset());
        parameters.add(input.limit() == null ? DEFAULT_LIMIT : input.limit());

        try {
            this.jdbcTemplate.query(
                connection -> {
                    PreparedStatement statement = connection.prepareStatement(
                        reportQuery,
                        ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                    statement.setFetchDirection(ResultSet.FETCH_FORWARD);
                    statement.setFetchSize(MYSQL_STREAM_FETCH_SIZE);
                    for (int index = 0; index < parameters.size(); index++) {
                        statement.setObject(index + 1, parameters.get(index));
                    }
                    return statement;
                }, resultSet -> {
                    while (resultSet.next()) {
                        try {
                            consumer.accept(this.mapRow(resultSet));
                        } catch (IOException exception) {
                            throw new IOExceptionRuntimeException(exception);
                        }
                    }
                });
        } catch (IOExceptionRuntimeException exception) {
            throw exception;
        }
    }

    private int writeReportHeader(Sheet sheet,
                                  Input input,
                                  CellStyle labelStyle,
                                  CellStyle valueStyle) {

        int rowIndex = 0;
        rowIndex = this.writeHeaderRow(
            sheet, rowIndex, "Start Date",
            this.formatHeaderDate(input.startDate(), input.timeZoneOffset()), labelStyle,
            valueStyle);
        rowIndex = this.writeHeaderRow(
            sheet, rowIndex, "End Date",
            this.formatHeaderDate(input.endDate(), input.timeZoneOffset()), labelStyle, valueStyle);
        rowIndex = this.writeHeaderRow(
            sheet, rowIndex, "Status", input.state(), labelStyle, valueStyle);
        return this.writeHeaderRow(
            sheet, rowIndex, "TimeZoneOffSet", this.displayOffset(input.timeZoneOffset()),
            labelStyle, valueStyle);
    }

    private int writeHeaderRow(Sheet sheet,
                               int rowIndex,
                               String label,
                               String value,
                               CellStyle labelStyle,
                               CellStyle valueStyle) {

        Row row = sheet.createRow(rowIndex++);
        Cell labelCell = row.createCell(0);
        labelCell.setCellValue(label);
        labelCell.setCellStyle(labelStyle);

        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value);
        valueCell.setCellStyle(valueStyle);
        return rowIndex;
    }

    private void writeTextCell(Row row, int columnIndex, String value, CellStyle style) {

        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value == null ? "" : value);
        cell.setCellStyle(style);
    }

    private void writeAmountCell(Row row, int columnIndex, BigDecimal value, CellStyle style) {

        Cell cell = row.createCell(columnIndex);
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(style);
    }

    private void writeAmountOrDashCell(Row row,
                                       int columnIndex,
                                       BigDecimal value,
                                       CellStyle textStyle,
                                       CellStyle amountStyle) {

        if (value == null || BigDecimal.ZERO.compareTo(value) == 0) {
            this.writeTextCell(row, columnIndex, "-", textStyle);
            return;
        }

        this.writeAmountCell(row, columnIndex, value, amountStyle);
    }

    private CellStyle headerLabelStyle(SXSSFWorkbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        var font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private CellStyle headerValueStyle(SXSSFWorkbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.headerLabelStyle(workbook));

        var font = workbook.createFont();
        font.setBold(false);
        style.setFont(font);
        return style;
    }

    private CellStyle columnHeaderStyle(SXSSFWorkbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.headerLabelStyle(workbook));
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle textCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setWrapText(true);
        style.setFont(this.reportDataFont(workbook));

        return style;
    }

    private CellStyle amountCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.textCellStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setVerticalAlignment(VerticalAlignment.TOP);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        return style;
    }

    private org.apache.poi.ss.usermodel.Font reportDataFont(org.apache.poi.ss.usermodel.Workbook workbook) {

        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        return font;
    }

    private String formatHeaderDate(Instant instant, String rawOffset) {

        ZoneOffset zoneOffset = this.parseOffset(rawOffset);
        return instant
                   .atOffset(ZoneOffset.UTC)
                   .withOffsetSameLocal(zoneOffset)
                   .format(HEADER_DATE_FORMAT);
    }

    private String displayOffset(String rawOffset) {

        ZoneOffset zoneOffset = this.parseOffset(rawOffset);
        String id = zoneOffset.getId().equals("Z") ? "+00:00" : zoneOffset.getId();
        return " " + id; // leading space prevents formula interpretation
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

    private String amountText(BigDecimal value) {

        return value == null ? "" : value.stripTrailingZeros().toPlainString();
    }

    private String amountOrDashText(BigDecimal value) {

        if (value == null || BigDecimal.ZERO.compareTo(value) == 0) {
            return "-";
        }

        return this.amountText(value);
    }

    private String csvLine(String... values) {

        StringBuilder line = new StringBuilder();
        for (int index = 0; index < values.length; index++) {
            if (index > 0) {
                line.append(',');
            }
            line.append(this.escapeCsv(values[index]));
        }
        line.append(System.lineSeparator());
        return line.toString();
    }

    private String escapeCsv(String value) {

        String safeValue = value == null ? "" : value;
        if (!safeValue.contains(",") && !safeValue.contains("\"") && !safeValue.contains("\n") &&
                !safeValue.contains("\r")) {
            return safeValue;
        }

        return "\"" + safeValue.replace("\"", "\"\"") + "\"";
    }

    private TransactionDetailRow mapRow(ResultSet resultSet) throws SQLException {

        return new TransactionDetailRow(
            resultSet.getString("transferId"), resultSet.getString("transactionDate"),
            resultSet.getString("senderDfspId"), resultSet.getString("senderDfspName"),
            resultSet.getString("receiverDfspId"), resultSet.getString("receiverDfspName"),
            resultSet.getString("transferType"), resultSet.getBigDecimal("amount"),
            resultSet.getBigDecimal("payeeReceivedAmount"),
            resultSet.getBigDecimal("payeeDfspFeeAmount"),
            resultSet.getBigDecimal("payeeDfspCommissionAmount"), resultSet.getString("currencyId"),
            resultSet.getString("status"));
    }

    private String transactionDetailQuery() {

        return """
            WITH bounds_base AS (
              SELECT
                CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                    CONVERT_TZ(?, CONCAT(SUBSTRING(?,1,3), ':', SUBSTRING(?,4,2)), '+00:00')
                ELSE
                    CONVERT_TZ(?, CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)), '+00:00')
                END AS startUtc,
                CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                    CONVERT_TZ(?, CONCAT(SUBSTRING(?,1,3), ':', SUBSTRING(?,4,2)), '+00:00')
                ELSE
                    CONVERT_TZ(?, CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)), '+00:00')
                END AS endUtc
            ),
            bounds AS (
              SELECT
                startUtc,
                endUtc,
                DATE_ADD(endUtc, INTERVAL 1 MINUTE) AS endUtcPlus1Min,
                DATE_ADD(startUtc, INTERVAL -1 MINUTE) AS startUtcMinus1Min
              FROM bounds_base
            )
            SELECT
              t.transferId,
              CONCAT(
                DATE_FORMAT(
                  CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                    CONVERT_TZ(IFNULL(tss.createdDate, t.createdDate), '+00:00',
                               CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2)))
                  ELSE
                    CONVERT_TZ(IFNULL(tss.createdDate, t.createdDate), '+00:00',
                               CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)))
                  END,
                  '%Y-%m-%dT%H:%i:%s'
                ),
                CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                    CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2))
                ELSE
                    CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2))
                END
              ) AS transactionDate,
              IFNULL(payer.name, '') AS senderDfspId,
              IFNULL(payer.description, payer.name) AS senderDfspName,
              IFNULL(payee.name, '') AS receiverDfspId,
              IFNULL(payee.description, payee.name) AS receiverDfspName,
              IFNULL(a.name, '') AS transferType,
              ROUND(qR.transferAmount, 2) AS amount,
              ROUND(qR.payeeReceiveAmount, 2) AS payeeReceivedAmount,
              IF(qR.payeeFspFeeAmount IS NOT NULL, ROUND(qR.payeeFspFeeAmount, 2), NULL)
                  AS payeeDfspFeeAmount,
              IF(qR.payeeFspCommissionAmount IS NOT NULL,
                 ROUND(qR.payeeFspCommissionAmount, 2), NULL) AS payeeDfspCommissionAmount,
              c.currencyId,
              IFNULL(tst.enumeration, '') AS status
            FROM transfer t
            LEFT JOIN transferParticipant tppayer
              ON t.transferId = tppayer.transferId
             AND tppayer.transferParticipantRoleTypeId = (
                SELECT transferParticipantRoleTypeId
                FROM transferParticipantRoleType
                WHERE name = 'PAYER_DFSP'
             )
            LEFT JOIN participantCurrency payercurrency
              ON payercurrency.participantCurrencyId = tppayer.participantCurrencyId
            LEFT JOIN participant payer
              ON payer.participantId = payercurrency.participantId
            LEFT JOIN transferParticipant tppayee
              ON t.transferId = tppayee.transferId
             AND tppayee.transferParticipantRoleTypeId = (
                SELECT transferParticipantRoleTypeId
                FROM transferParticipantRoleType
                WHERE name = 'PAYEE_DFSP'
             )
            LEFT JOIN participantCurrency payeecurrency
              ON payeecurrency.participantCurrencyId = tppayee.participantCurrencyId
            LEFT JOIN participant payee
              ON payee.participantId = payeecurrency.participantId
            LEFT JOIN quote q
              ON q.transactionReferenceId = t.transferId
            LEFT JOIN quoteResponse qR
              ON qR.quoteId = q.quoteId
            LEFT JOIN (
              SELECT t.transferId, t.transferStateId, t.createdDate
              FROM transferStateChange t
              JOIN (
                SELECT transferId, MAX(transferStateChangeId) AS maxId
                FROM transferStateChange
                JOIN bounds b
                WHERE createdDate BETWEEN b.startUtcMinus1Min AND b.endUtcPlus1Min
                GROUP BY transferId
              ) m
                ON m.transferId = t.transferId
               AND m.maxId = t.transferStateChangeId
            ) tss
              ON tss.transferId = t.transferId
            LEFT JOIN transferState tst
              ON tst.transferStateId = tss.transferStateId
            LEFT JOIN amountType a
              ON a.amountTypeId = q.amountTypeId
            INNER JOIN currency c
              ON c.currencyId = payercurrency.currencyId
            JOIN bounds b
            WHERE IFNULL(tss.createdDate, t.createdDate) BETWEEN b.startUtc AND b.endUtc
              AND (? = 'All' OR tst.enumeration = ?)
              AND ((? = 'All') OR (payee.name = ? OR payer.name = ?))
            LIMIT ?, ?
            """;
    }

    private Integer countTransactionDetailRows(Instant startDate,
                                               Instant endDate,
                                               String state,
                                               String dfspId,
                                               String timeZoneOffset) {

        String reportQuery = """
            WITH bounds_base AS (
              SELECT
                CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                    CONVERT_TZ(?,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00')
                ELSE
                    CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)),'+00:00')
                END AS startUtc,
                CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                    CONVERT_TZ(?,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00')
                ELSE
                    CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)),'+00:00')
                END AS endUtc
            ),
            bounds AS (
              SELECT
                startUtc,
                endUtc,
                DATE_ADD(endUtc, INTERVAL 1 MINUTE) AS endUtcPlus1Min,
                DATE_ADD(startUtc, INTERVAL -1 MINUTE) AS startUtcMinus1Min
              FROM bounds_base
            )
            SELECT COUNT(*) AS rowCount
            FROM transfer t
            LEFT JOIN transferParticipant tppayer
              ON t.transferId = tppayer.transferId
             AND tppayer.transferParticipantRoleTypeId = (
                SELECT transferParticipantRoleTypeId
                FROM transferParticipantRoleType
                WHERE name = 'PAYER_DFSP'
             )
            LEFT JOIN participantCurrency payercurrency
              ON payercurrency.participantCurrencyId = tppayer.participantCurrencyId
            LEFT JOIN participant payer
              ON payer.participantId = payercurrency.participantId
            LEFT JOIN transferParticipant tppayee
              ON t.transferId = tppayee.transferId
             AND tppayee.transferParticipantRoleTypeId = (
                SELECT transferParticipantRoleTypeId
                FROM transferParticipantRoleType
                WHERE name = 'PAYEE_DFSP'
             )
            LEFT JOIN participantCurrency payeecurrency
              ON payeecurrency.participantCurrencyId = tppayee.participantCurrencyId
            LEFT JOIN participant payee
              ON payee.participantId = payeecurrency.participantId
            LEFT JOIN quote q
              ON q.transactionReferenceId = t.transferId
            LEFT JOIN quoteResponse qR
              ON qR.quoteId = q.quoteId
            LEFT JOIN (
              SELECT t.transferId, t.transferStateId, t.createdDate
              FROM transferStateChange t
              JOIN (
                SELECT transferId, MAX(transferStateChangeId) AS maxId
                FROM transferStateChange
                JOIN bounds b
                WHERE createdDate BETWEEN b.startUtcMinus1Min AND b.endUtcPlus1Min
                GROUP BY transferId
              ) m
                ON m.transferId = t.transferId
               AND m.maxId = t.transferStateChangeId
            ) tss
              ON tss.transferId = t.transferId
            LEFT JOIN transferState tst
              ON tst.transferStateId = tss.transferStateId
            LEFT JOIN amountType a
              ON a.amountTypeId = q.amountTypeId
            INNER JOIN currency c
              ON c.currencyId = payercurrency.currencyId
            JOIN bounds b
            WHERE IFNULL(tss.createdDate, t.createdDate) BETWEEN b.startUtc AND b.endUtc
              AND (? = 'All' OR tst.enumeration = ?)
              AND ((? = 'All') OR (payee.name = ? OR payer.name = ?))
            """;

        return this.jdbcTemplate.queryForObject(
            reportQuery, new Object[]{
                timeZoneOffset,
                startDate,
                timeZoneOffset,
                timeZoneOffset,
                startDate,
                timeZoneOffset,
                timeZoneOffset,
                timeZoneOffset,
                endDate,
                timeZoneOffset,
                timeZoneOffset,
                endDate,
                timeZoneOffset,
                timeZoneOffset,
                state,
                state,
                dfspId,
                dfspId,
                dfspId}, Integer.class);
    }

    private void flushSheet(Sheet sheet) throws IOException {

        if (sheet instanceof SXSSFSheet streamingSheet) {
            streamingSheet.flushRows(DEFAULT_ROW_WINDOW);
        }
    }

    private record TransactionDetailRow(String transferId,
                                        String transactionDate,
                                        String senderDfspId,
                                        String senderDfspName,
                                        String receiverDfspId,
                                        String receiverDfspName,
                                        String transferType,
                                        BigDecimal amount,
                                        BigDecimal payeeReceivedAmount,
                                        BigDecimal payeeDfspFeeAmount,
                                        BigDecimal payeeDfspCommissionAmount,
                                        String currencyId,
                                        String status) { }

    @FunctionalInterface
    private interface TransactionDetailRowConsumer {

        void accept(TransactionDetailRow row) throws IOException;

    }

    private static final class IOExceptionRuntimeException extends RuntimeException {

        private IOExceptionRuntimeException(IOException cause) {

            super(cause);
        }

    }

    private static final class RowCursor {

        private int current;

        private RowCursor(int start) {

            this.current = start;
        }

        private int next() {

            return this.current++;
        }

        private int current() {

            return this.current;
        }

    }

    private static final class RowCounter {

        private int value;

        private void increment() {

            this.value++;
        }

        private int value() {

            return this.value;
        }

    }

}
