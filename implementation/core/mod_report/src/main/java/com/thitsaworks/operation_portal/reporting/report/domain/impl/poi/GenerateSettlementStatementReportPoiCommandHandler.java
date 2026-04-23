package com.thitsaworks.operation_portal.reporting.report.domain.impl.poi;

import com.thitsaworks.operation_portal.component.misc.logging.NoLogging;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementStatementReportCommand;
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
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Primary
@NoLogging
public class GenerateSettlementStatementReportPoiCommandHandler
    implements GenerateSettlementStatementReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateSettlementStatementReportPoiCommandHandler.class);

    private static final int DEFAULT_ROW_WINDOW = 200;

    private static final int DEFAULT_LIMIT = Integer.MAX_VALUE;

    private static final int MYSQL_STREAM_FETCH_SIZE = Integer.MIN_VALUE;

    private static final String[] COLUMN_HEADERS = {
        "Date Time",
        "DFSP ID",
        "DFSP Name",
        "Process Description",
        "Funds In",
        "Funds Out",
        "Balance",
        "NDC %",
        "NDC",
        "Currency",
        "Settlement Bank Account"};

    private static final int[] COLUMN_WIDTHS = {
        32,
        32,
        40,
        50,
        18,
        18,
        20,
        15,
        20,
        12,
        50};

    private static final DateTimeFormatter HEADER_DATE_FORMAT = DateTimeFormatter.ofPattern(
        "yyyy-MM-dd'T'HH:mm:ssXXX");

    private final JdbcTemplate jdbcTemplate;

    public GenerateSettlementStatementReportPoiCommandHandler(
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

            LOG.error("Error generating settlement statement report with POI", exception);
            throw new ReportException(ReportErrors.STATEMENT_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
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

            LOG.error("Error generating full settlement statement report with POI", exception);
            throw new ReportException(ReportErrors.STATEMENT_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public int countRows(CountInput input) {

        Integer rowCount = this.countSettlementStatementRows(input.fspId(), input.startDate(),
            input.endDate(), input.currencyId(), input.timeZoneOffset());
        return rowCount == null ? 0 : rowCount;
    }

    private byte[] exportSingleChunkXlsx(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("settlement-statement-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("DFSPSettlementStatementReport");

            CellStyle headerLabelStyle = this.headerLabelStyle(workbook);
            CellStyle headerValueStyle = this.headerValueStyle(workbook);
            CellStyle columnHeaderStyle = this.columnHeaderStyle(workbook);
            CellStyle textCellStyle = this.textCellStyle(workbook);
            CellStyle dateTimeCellStyle = this.dateTimeCellStyle(workbook);
            CellStyle rightAlignedTextCellStyle = this.rightAlignedTextCellStyle(workbook);
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
            this.streamRows(input,
                row -> this.writeDataRow(sheet.createRow(rowCursor.next()), row, dateTimeCellStyle,
                    textCellStyle, rightAlignedTextCellStyle, amountCellStyle));
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

        Path tempFile = Files.createTempFile("settlement-statement-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("DFSPSettlementStatementReport");

            CellStyle headerLabelStyle = this.headerLabelStyle(workbook);
            CellStyle headerValueStyle = this.headerValueStyle(workbook);
            CellStyle columnHeaderStyle = this.columnHeaderStyle(workbook);
            CellStyle textCellStyle = this.textCellStyle(workbook);
            CellStyle dateTimeCellStyle = this.dateTimeCellStyle(workbook);
            CellStyle rightAlignedTextCellStyle = this.rightAlignedTextCellStyle(workbook);
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
                Input chunkInput = new Input(input.fspId(), input.dfspName(), input.startDate(),
                    input.endDate(), input.filetype(), input.currencyId(), input.timeZoneOffset(),
                    baseOffset + offset, limit);

                this.streamRows(chunkInput,
                    row -> this.writeDataRow(sheet.createRow(rowCursor.next()), row,
                        dateTimeCellStyle, textCellStyle, rightAlignedTextCellStyle,
                        amountCellStyle));
                this.flushSheet(sheet);
            }

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

    private byte[] exportSingleChunkCsv(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("settlement-statement-", ".csv");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {

            this.writeCsvHeader(writer, input);

            RowCounter rowCounter = new RowCounter();
            this.streamRows(input, row -> {
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

        Path tempFile = Files.createTempFile("settlement-statement-", ".csv");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {

            this.writeCsvHeader(writer, input);

            RowCounter rowCounter = new RowCounter();
            int baseOffset = input.offset() == null ? 0 : input.offset();
            for (int offset = 0; offset < totalRowCount; offset += pageSize) {
                int limit = Math.min(pageSize, totalRowCount - offset);
                Input chunkInput = new Input(input.fspId(), input.dfspName(), input.startDate(),
                    input.endDate(), input.filetype(), input.currencyId(), input.timeZoneOffset(),
                    baseOffset + offset, limit);

                this.streamRows(chunkInput, row -> {
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

        writer.write(this.csvLine("DFSP ID", input.fspId()));
        writer.write(this.csvLine("DFSP Name", input.dfspName()));
        writer.write(this.csvLine("From Date",
            this.formatHeaderDate(input.startDate(), input.timeZoneOffset())));
        writer.write(this.csvLine("To Date",
            this.formatHeaderDate(input.endDate(), input.timeZoneOffset())));
        writer.write(this.csvLine("Currency", input.currencyId()));
        writer.write(this.csvLine("TimeZoneOffSet", this.displayOffset(input.timeZoneOffset())));
        writer.newLine();
        writer.write(this.csvLine(COLUMN_HEADERS));
    }

    private String[] toCsvValues(SettlementStatementRow row) {

        return new String[]{
            row.createdDate(),
            row.dfspId(),
            row.dfspName(),
            row.processDescription(),
            this.amountOrDashText(row.fundsIn()),
            this.amountOrDashText(row.fundsOut()),
            this.amountOrDashText(row.balance()),
            row.ndcPercent(),
            this.amountOrDashText(row.ndc()),
            row.currency(),
            row.settlementBankAccount()};
    }

    private void writeDataRow(Row row,
                              SettlementStatementRow data,
                              CellStyle dateTimeCellStyle,
                              CellStyle textCellStyle,
                              CellStyle rightAlignedTextCellStyle,
                              CellStyle amountCellStyle) {

        this.writeTextCell(row, 0, data.createdDate(), dateTimeCellStyle);
        this.writeTextCell(row, 1, data.dfspId(), textCellStyle);
        this.writeTextCell(row, 2, data.dfspName(), textCellStyle);
        this.writeTextCell(row, 3, data.processDescription(), textCellStyle);
        this.writeAmountOrDashCell(
            row, 4, data.fundsIn(), rightAlignedTextCellStyle, amountCellStyle);
        this.writeAmountOrDashCell(
            row, 5, data.fundsOut(), rightAlignedTextCellStyle, amountCellStyle);
        this.writeAmountOrDashCell(row, 6, data.balance(), rightAlignedTextCellStyle, amountCellStyle);
        this.writeTextCell(row, 7, data.ndcPercent(), rightAlignedTextCellStyle);
        this.writeAmountOrDashCell(row, 8, data.ndc(), rightAlignedTextCellStyle, amountCellStyle);
        this.writeTextCell(row, 9, data.currency(), textCellStyle);
        this.writeTextCell(row, 10, data.settlementBankAccount(), textCellStyle);
    }

    private void streamRows(Input input, SettlementStatementRowConsumer consumer) {

        List<Object> parameters = new ArrayList<>();
        String reportQuery = this.settlementStatementQuery();
        String startDate = input.startDate().toString();
        String endDate = input.endDate().toString();

        parameters.add(input.fspId());
        parameters.add(input.fspId());
        parameters.add(input.timeZoneOffset());
        parameters.add(startDate);
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(startDate);
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(endDate);
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(endDate);
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.currencyId());
        parameters.add(input.currencyId());

        parameters.add(input.fspId());
        parameters.add(input.fspId());
        parameters.add(input.timeZoneOffset());
        parameters.add(startDate);
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(startDate);
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(endDate);
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(endDate);
        parameters.add(input.timeZoneOffset());
        parameters.add(input.timeZoneOffset());
        parameters.add(input.currencyId());
        parameters.add(input.currencyId());

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

        parameters.add(input.offset() == null ? 0 : input.offset());
        parameters.add(input.limit() == null ? DEFAULT_LIMIT : input.limit());

        try {
            this.jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(reportQuery,
                    ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                statement.setFetchDirection(ResultSet.FETCH_FORWARD);
                statement.setFetchSize(MYSQL_STREAM_FETCH_SIZE);
                for (int index = 0; index < parameters.size(); index++) {
                    statement.setObject(index + 1, parameters.get(index));
                }
                return statement;
            }, resultSet -> {
                try {
                    consumer.accept(this.mapRow(resultSet));
                } catch (IOException exception) {
                    throw new IOExceptionRuntimeException(exception);
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
            sheet, rowIndex, "DFSP ID", input.fspId(), labelStyle, valueStyle);
        rowIndex = this.writeHeaderRow(
            sheet, rowIndex, "DFSP Name", input.dfspName(), labelStyle, valueStyle);
        rowIndex = this.writeHeaderRow(sheet, rowIndex, "From Date",
            this.formatHeaderDate(input.startDate(), input.timeZoneOffset()), labelStyle,
            valueStyle);
        rowIndex = this.writeHeaderRow(sheet, rowIndex, "To Date",
            this.formatHeaderDate(input.endDate(), input.timeZoneOffset()), labelStyle, valueStyle);
        rowIndex = this.writeHeaderRow(
            sheet, rowIndex, "Currency", input.currencyId(), labelStyle, valueStyle);
        return this.writeHeaderRow(sheet, rowIndex, "TimeZoneOffSet",
            this.displayOffset(input.timeZoneOffset()), labelStyle, valueStyle);
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
        valueCell.setCellValue(value == null ? "" : value);
        valueCell.setCellStyle(valueStyle);
        return rowIndex;
    }

    private void writeTextCell(Row row, int columnIndex, String value, CellStyle style) {

        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value == null ? "" : value);
        cell.setCellStyle(style);
        row.setHeight((short) -1);
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
        style.setWrapText(true);
        var font = workbook.createFont();
        font.setBold(false);
        style.setFont(font);
        return style;
    }

    private CellStyle columnHeaderStyle(SXSSFWorkbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.cloneStyleFrom(this.headerLabelStyle(workbook));
        style.setBorderBottom(BorderStyle.THIN);
        return style;
    }

    private CellStyle textCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setFont(this.reportDataFont(workbook));
        return style;
    }

    private CellStyle dateTimeCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.textCellStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    private CellStyle rightAlignedTextCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.textCellStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    private CellStyle amountCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.textCellStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
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

    private String amountOrDashText(BigDecimal value) {

        if (value == null || BigDecimal.ZERO.compareTo(value) == 0) {
            return "-";
        }

        return value.stripTrailingZeros().toPlainString();
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

    private SettlementStatementRow mapRow(ResultSet resultSet) throws SQLException {

        return new SettlementStatementRow(resultSet.getString("createdDate"),
            resultSet.getString("dfspId"), resultSet.getString("dfspName"),
            resultSet.getString("processDescription"), resultSet.getBigDecimal("fundsIn"),
            resultSet.getBigDecimal("fundsOut"), resultSet.getBigDecimal("balance"),
            resultSet.getString("ndcPercent"), resultSet.getBigDecimal("ndc"),
            resultSet.getString("currency"), resultSet.getString("settlementBankAccount"));
    }

    private String settlementStatementQuery() {

        return """
            WITH base AS (
              SELECT * FROM (
                SELECT
                  tp.createdDate AS createdDate,
                  p.name AS dfspId,
                  IFNULL(op.description, p.name) AS dfspName,
                  COALESCE(tscIn.reason, tscOut.reason) AS processDescription,
                  ROUND(CASE WHEN tp.amount < 0 THEN -tp.amount END, 2) AS fundsIn,
                  ROUND(CASE WHEN tp.amount > 0 THEN tp.amount END, 2) AS fundsOut,
                  ROUND(ppc.value, 2) AS balance,
                  '-' AS ndcPercent,
                  NULL AS ndc,
                  pc.currencyId AS currency,
                  IFNULL(CONCAT(IFNULL(lp.bank_name, ''), ' - ', IFNULL(lp.account_name, ''),
                                ' - ', IFNULL(lp.account_number, '')), '') AS settlementBankAccount
                FROM participant p
                INNER JOIN participantCurrency pc ON p.participantId = pc.participantId
                INNER JOIN ledgerAccountType lat ON lat.ledgerAccountTypeId = pc.ledgerAccountTypeId
                INNER JOIN transferParticipant tp ON tp.participantCurrencyId = pc.participantCurrencyId
                LEFT JOIN transferStateChange tscOut
                  ON tp.transferId = tscOut.transferId
                 AND tscOut.transferStateChangeId = (
                    SELECT MAX(transferStateChangeId)
                    FROM transferStateChange tscOut1
                    WHERE tscOut1.transferId = tp.transferId
                      AND tscOut1.transferStateId IN ('RESERVED', 'ABORTED_REJECTED')
                 )
                LEFT JOIN transferState tsOut ON tscOut.transferStateId = tsOut.transferStateId
                LEFT JOIN transferStateChange tscIn
                  ON tp.transferId = tscIn.transferId
                 AND tscIn.transferStateChangeId = (
                    SELECT MAX(transferStateChangeId)
                    FROM transferStateChange tscIn1
                    WHERE tscIn1.transferId = tp.transferId
                      AND tscIn1.transferStateId IN ('COMMITTED', 'ABORTED_REJECTED')
                 )
                LEFT JOIN transferState tsIn ON tscIn.transferStateId = tsIn.transferStateId
                INNER JOIN participantPosition pp ON pp.participantCurrencyId = pc.participantCurrencyId
                INNER JOIN participantPositionChange ppc ON ppc.participantPositionId = pp.participantPositionId
                INNER JOIN currency c ON c.currencyId = pc.currencyId
                LEFT JOIN transferExtension tex ON tex.transferId = tp.transferId
                LEFT JOIN operation_portal.tbl_participant op ON op.participant_name = p.name
                LEFT JOIN operation_portal.tbl_liquidity_profile lp
                  ON lp.currency = pc.currencyId
                 AND is_active = 1
                 AND op.participant_id = lp.participant_id
                WHERE (? = 'All' OR p.name = ?)
                  AND p.name != 'Hub'
                  AND (tscIn.transferStateChangeId = ppc.transferStateChangeId
                       OR tscOut.transferStateChangeId = ppc.transferStateChangeId)
                  AND tex.transferExtensionId = (
                    SELECT MAX(transferExtensionId)
                    FROM transferExtension tex
                    WHERE tex.transferId = tp.transferId
                      AND tex.key = 'externalReference')
                  AND (tp.createdDate BETWEEN
                    (CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                        CONVERT_TZ(?,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00')
                     ELSE
                        CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)),'+00:00')
                     END)
                    AND
                    (CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                        CONVERT_TZ(?,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00')
                     ELSE
                        CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)),'+00:00')
                     END))
                  AND (? = 'All' OR pc.currencyId = ?)
                GROUP BY
                  tp.createdDate, p.participantId, p.name, tscIn.reason, tscOut.reason,
                  tp.amount, ppc.value, pc.currencyId, pc.participantCurrencyId,
                  op.description, lp.bank_name, lp.account_name, lp.account_number

                UNION ALL

                SELECT
                  pl.createdDate AS createdDate,
                  p.name AS dfspId,
                  IFNULL(op.description, p.name) AS dfspName,
                  CASE
                    WHEN ar.requested_action = 'UPDATE_NDC_FIXED' THEN 'NDC Amount Update'
                    WHEN ar.requested_action = 'UPDATE_NDC_PERCENTAGE' THEN 'NDC % Update'
                    WHEN ar.requested_action = 'DEPOSIT' THEN 'NDC Amount Updated Due to Deposit'
                    WHEN ar.requested_action = 'WITHDRAW' THEN 'NDC Amount Updated Due to Withdrawal'
                    WHEN (ar.requested_action IS NULL
                          AND ndc_percent.dfspCode = p.name
                          AND ndc_percent.currency = pc.currencyId
                          AND ABS(TIMESTAMPDIFF(SECOND, ndc_percent.updatedDate, pl.createdDate)) <= 2)
                      THEN 'NDC Amount Update Due to Settlement'
                    ELSE 'NDC Amount Update Due to Initial Onboarding'
                  END AS processDescription,
                  NULL AS fundsIn,
                  NULL AS fundsOut,
                  NULL AS balance,
                  CASE
                    WHEN ndc_percent.ndcPercent IS NULL OR ndc_percent.ndcPercent = 0
                      THEN '-'
                    ELSE CONCAT(ROUND(IFNULL(ndc_percent.ndcPercent,0),0), '%')
                  END AS ndcPercent,
                  IFNULL(ROUND(IFNULL(pl.value,0),2),0) AS ndc,
                  pc.currencyId AS currency,
                  IFNULL(CONCAT(IFNULL(lp.bank_name, ''), ' - ', IFNULL(lp.account_name, ''),
                                ' - ', IFNULL(lp.account_number, '')), '') AS settlementBankAccount
                FROM participant p
                INNER JOIN participantCurrency pc ON p.participantId = pc.participantId
                INNER JOIN ledgerAccountType lat ON lat.ledgerAccountTypeId = pc.ledgerAccountTypeId
                INNER JOIN currency c ON c.currencyId = pc.currencyId
                LEFT JOIN participantLimit pl ON pl.participantCurrencyId = pc.participantCurrencyId
                LEFT JOIN (
                  SELECT * FROM (
                    SELECT participant_name AS dfspCode, currency, ndc_percent AS ndcPercent,
                           FROM_UNIXTIME(updated_date) AS updatedDate
                    FROM operation_portal.tbl_participant_ndc
                    UNION
                    SELECT participant_name AS dfspCode, currency, ndc_percent AS ndcPercent,
                           FROM_UNIXTIME(updated_date) AS updatedDate
                    FROM operation_portal.tbl_participant_ndc_history
                  ) Q
                ) ndc_percent
                  ON ndc_percent.dfspCode = p.name
                 AND ndc_percent.currency = pc.currencyId
                 AND ABS(TIMESTAMPDIFF(SECOND, ndc_percent.updatedDate, pl.createdDate)) <= 2
                LEFT JOIN operation_portal.tbl_participant op ON op.participant_name = p.name
                LEFT JOIN operation_portal.tbl_approval_request ar
                  ON ar.participant_name = p.name
                 AND ar.participant_currency = pc.currencyId
                 AND ABS(TIMESTAMPDIFF(SECOND, FROM_UNIXTIME(ar.updated_date), pl.createdDate)) <= 2
                LEFT JOIN operation_portal.tbl_liquidity_profile lp
                  ON lp.currency = pc.currencyId
                 AND is_active = 1
                 AND op.participant_id = lp.participant_id
                WHERE (? = 'All' OR p.name = ?)
                  AND p.name != 'Hub'
                  AND (pl.createdDate BETWEEN
                    (CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                        CONVERT_TZ(?,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00')
                     ELSE
                        CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)),'+00:00')
                     END)
                    AND
                    (CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                        CONVERT_TZ(?,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00')
                     ELSE
                        CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)),'+00:00')
                     END))
                  AND (? = 'All' OR pc.currencyId = ?)
              ) Q
            ),
            seq AS (
              SELECT
                b.*,
                SUM(CASE WHEN b.processDescription NOT LIKE 'NDC%' THEN 1 ELSE 0 END)
                  OVER (
                    PARTITION BY b.dfspId, b.currency
                    ORDER BY b.createdDate
                    ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW
                  ) AS grp
              FROM base b
            ),
            final AS (
              SELECT
                s.*,
                CASE
                  WHEN s.processDescription = 'NDC Amount Update Due to Settlement' THEN
                    COALESCE(
                      (
                        SELECT st.createdDate
                        FROM seq st
                        WHERE st.dfspId = s.dfspId
                          AND st.currency = s.currency
                          AND st.processDescription LIKE 'Settlement:%'
                          AND ABS(TIMESTAMPDIFF(SECOND, st.createdDate, s.createdDate)) <= 2
                        ORDER BY ABS(TIMESTAMPDIFF(SECOND, st.createdDate, s.createdDate)),
                                 st.createdDate
                        LIMIT 1
                      ),
                      s.createdDate
                    )
                  WHEN s.processDescription = 'NDC Amount Updated Due to Deposit' THEN
                    COALESCE(
                      (
                        SELECT st.createdDate
                        FROM seq st
                        WHERE st.dfspId = s.dfspId
                          AND st.currency = s.currency
                          AND st.processDescription = 'Deposit'
                          AND ABS(TIMESTAMPDIFF(SECOND, st.createdDate, s.createdDate)) <= 2
                        ORDER BY ABS(TIMESTAMPDIFF(SECOND, st.createdDate, s.createdDate)),
                                 st.createdDate
                        LIMIT 1
                      ),
                      s.createdDate
                    )
                  WHEN s.processDescription = 'NDC Amount Updated Due to Withdrawal' THEN
                    COALESCE(
                      (
                        SELECT st.createdDate
                        FROM seq st
                        WHERE st.dfspId = s.dfspId
                          AND st.currency = s.currency
                          AND st.processDescription = 'Withdrawal'
                          AND ABS(TIMESTAMPDIFF(SECOND, st.createdDate, s.createdDate)) <= 2
                        ORDER BY ABS(TIMESTAMPDIFF(SECOND, st.createdDate, s.createdDate)),
                                 st.createdDate
                        LIMIT 1
                      ),
                      s.createdDate
                    )
                  ELSE s.createdDate
                END AS pair_createdDate,
                CASE
                  WHEN s.processDescription = 'NDC Amount Update Due to Settlement' THEN
                    (
                      SELECT ABS(st.balance)
                      FROM seq st
                      WHERE st.dfspId = s.dfspId
                        AND st.currency = s.currency
                        AND st.processDescription LIKE 'Settlement:%'
                        AND ABS(TIMESTAMPDIFF(SECOND, st.createdDate, s.createdDate)) <= 2
                      ORDER BY ABS(TIMESTAMPDIFF(SECOND, st.createdDate, s.createdDate)),
                               st.createdDate
                      LIMIT 1
                    )
                  WHEN s.processDescription = 'NDC Amount Updated Due to Deposit' THEN
                    (
                      SELECT ABS(st.balance)
                      FROM seq st
                      WHERE st.dfspId = s.dfspId
                        AND st.currency = s.currency
                        AND st.processDescription = 'Deposit'
                        AND ABS(TIMESTAMPDIFF(SECOND, st.createdDate, s.createdDate)) <= 2
                      ORDER BY ABS(TIMESTAMPDIFF(SECOND, st.createdDate, s.createdDate)),
                               st.createdDate
                      LIMIT 1
                    )
                  WHEN s.processDescription = 'NDC Amount Updated Due to Withdrawal' THEN
                    (
                      SELECT ABS(st.balance)
                      FROM seq st
                      WHERE st.dfspId = s.dfspId
                        AND st.currency = s.currency
                        AND st.processDescription = 'Withdrawal'
                        AND ABS(TIMESTAMPDIFF(SECOND, st.createdDate, s.createdDate)) <= 2
                      ORDER BY ABS(TIMESTAMPDIFF(SECOND, st.createdDate, s.createdDate)),
                               st.createdDate
                      LIMIT 1
                    )
                  ELSE NULL
                END AS pair_balance,
                CASE
                  WHEN s.processDescription LIKE 'Settlement:%' THEN 1
                  WHEN s.processDescription = 'NDC Amount Update Due to Settlement' THEN 2
                  WHEN s.processDescription = 'Deposit' THEN 1
                  WHEN s.processDescription = 'NDC Amount Updated Due to Deposit' THEN 2
                  WHEN s.processDescription = 'Withdrawal' THEN 1
                  WHEN s.processDescription = 'NDC Amount Updated Due to Withdrawal' THEN 2
                  ELSE 3
                END AS pair_sort
              FROM seq s
            )
            SELECT
              CONCAT(
                DATE_FORMAT(
                  CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                      CONVERT_TZ(createdDate, '+00:00',
                                 CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2)))
                   ELSE
                      CONVERT_TZ(createdDate, '+00:00',
                                 CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)))
                  END,
                  '%Y-%m-%dT%H:%i:%s'
                ),
                CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                    CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2))
                 ELSE
                    CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2))
                END
              ) AS createdDate,
              dfspId,
              dfspName,
              processDescription,
              fundsIn,
              fundsOut,
              CASE
                WHEN processDescription IN (
                  'NDC Amount Update Due to Settlement',
                  'NDC Amount Updated Due to Deposit',
                  'NDC Amount Updated Due to Withdrawal'
                )
                  THEN COALESCE(pair_balance, 0)
                WHEN processDescription LIKE 'NDC%' THEN
                  COALESCE(
                    MAX(CASE WHEN processDescription NOT LIKE 'NDC%' THEN ABS(balance) END)
                      OVER (PARTITION BY dfspId, currency, grp),
                    0
                  )
                ELSE ABS(balance)
              END AS balance,
              ndcPercent,
              ndc,
              currency,
              settlementBankAccount
            FROM final
            WHERE NOT (
              COALESCE(NULLIF(fundsIn, ''), 0) = 0
              AND COALESCE(NULLIF(fundsOut, ''), 0) = 0
              AND COALESCE(NULLIF(balance, ''), 0) = 0
              AND (ndcPercent IS NULL OR ndcPercent = '' OR ndcPercent = '-' OR ndcPercent = 0)
              AND COALESCE(NULLIF(ndc, ''), 0) = 0
            )
            ORDER BY
              dfspId,
              currency,
              pair_createdDate,
              pair_sort,
              createdDate
            LIMIT ?, ?
            """;
    }

    private Integer countSettlementStatementRows(String fspId,
                                                 Instant startDate,
                                                 Instant endDate,
                                                 String currencyId,
                                                 String timeZoneOffset) {

        String startDateValue = startDate.toString();
        String endDateValue = endDate.toString();

        String reportQuery = """
            SELECT Count(createdDate) As rowCount FROM (
              SELECT
                tp.createdDate AS createdDate,
                ROUND(CASE WHEN tp.amount < 0 THEN -tp.amount END, 2) AS fundsIn,
                ROUND(CASE WHEN tp.amount > 0 THEN  tp.amount END, 2) AS fundsOut,
                ROUND(ppc.value, 2) AS balance,
                '-' AS ndcPercent,
                NULL AS ndc
              FROM participant p
              INNER JOIN participantCurrency pc ON p.participantId = pc.participantId
              INNER JOIN ledgerAccountType lat ON lat.ledgerAccountTypeId = pc.ledgerAccountTypeId
              INNER JOIN transferParticipant tp ON tp.participantCurrencyId = pc.participantCurrencyId
              LEFT JOIN transferStateChange tscOut
                ON tp.transferId = tscOut.transferId
               AND tscOut.transferStateChangeId = (
                  SELECT MAX(transferStateChangeId)
                  FROM transferStateChange tscOut1
                  WHERE tscOut1.transferId = tp.transferId
                    AND tscOut1.transferStateId IN ('RESERVED', 'ABORTED_REJECTED')
               )
              LEFT JOIN transferStateChange tscIn
                ON tp.transferId = tscIn.transferId
               AND tscIn.transferStateChangeId = (
                  SELECT MAX(transferStateChangeId)
                  FROM transferStateChange tscIn1
                  WHERE tscIn1.transferId = tp.transferId
                    AND tscIn1.transferStateId IN ('COMMITTED', 'ABORTED_REJECTED')
               )
              INNER JOIN participantPosition pp ON pp.participantCurrencyId = pc.participantCurrencyId
              INNER JOIN participantPositionChange ppc ON ppc.participantPositionId = pp.participantPositionId
              LEFT JOIN transferExtension tex ON tex.transferId = tp.transferId
              WHERE (? ='All' OR p.name = ?) AND p.name != 'Hub'
                AND (tscIn.transferStateChangeId = ppc.transferStateChangeId
                     OR tscOut.transferStateChangeId = ppc.transferStateChangeId)
                AND tex.transferExtensionId = (
                  SELECT MAX(transferExtensionId)
                  FROM transferExtension tex
                  WHERE tex.transferId = tp.transferId
                    AND tex.key = 'externalReference')
                AND (tp.createdDate BETWEEN
                  (CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                      CONVERT_TZ(?,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00')
                   ELSE
                      CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)),'+00:00')
                   END)
                  AND
                  (CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                      CONVERT_TZ(? ,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00')
                   ELSE
                      CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)),'+00:00')
                   END))
                AND (? = 'All' OR pc.currencyId = ?)
              GROUP BY
                tp.createdDate, p.participantId, p.name, tscIn.reason, tscOut.reason,
                tp.amount, ppc.value, pc.currencyId, pc.participantCurrencyId

              UNION ALL

              SELECT
                pl.createdDate AS createdDate,
                NULL AS fundsIn,
                NULL AS fundsOut,
                NULL AS balance,
                CASE
                  WHEN ndc_percent.ndcPercent IS NULL OR ndc_percent.ndcPercent = 0
                    THEN '-'
                  ELSE CONCAT(ROUND(IFNULL(ndc_percent.ndcPercent,0),0), '%')
                END AS ndcPercent,
                IFNULL(ROUND(IFNULL(pl.value,0),2),0) AS ndc
              FROM participant p
              INNER JOIN participantCurrency pc ON p.participantId = pc.participantId
              INNER JOIN ledgerAccountType lat ON lat.ledgerAccountTypeId = pc.ledgerAccountTypeId
              LEFT JOIN participantLimit pl ON pl.participantCurrencyId = pc.participantCurrencyId
              LEFT JOIN (
                SELECT * FROM (
                  SELECT participant_name AS dfspCode, currency, ndc_percent AS ndcPercent,
                         FROM_UNIXTIME(updated_date) AS updatedDate
                  FROM operation_portal.tbl_participant_ndc
                  UNION
                  SELECT participant_name AS dfspCode, currency, ndc_percent AS ndcPercent,
                         FROM_UNIXTIME(updated_date) AS updatedDate
                  FROM operation_portal.tbl_participant_ndc_history
                ) Q
              ) ndc_percent
                ON ndc_percent.dfspCode = p.name
               AND ndc_percent.currency = pc.currencyId
               AND ABS(TIMESTAMPDIFF(SECOND, ndc_percent.updatedDate, pl.createdDate)) <= 2
              LEFT JOIN operation_portal.tbl_approval_request ar
                ON ar.participant_name = p.name
               AND ar.participant_currency = pc.currencyId
               AND ABS(TIMESTAMPDIFF(SECOND, FROM_UNIXTIME(ar.updated_date), pl.createdDate)) <= 2
              WHERE (? ='All' OR p.name = ?) AND p.name != 'Hub'
                AND (pl.createdDate BETWEEN
                  (CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                      CONVERT_TZ(?,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00')
                   ELSE
                      CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)),'+00:00')
                   END)
                  AND
                  (CASE WHEN SUBSTRING(?,1,1) = '-' THEN
                      CONVERT_TZ(? ,CONCAT(SUBSTRING(?,1,3),':',SUBSTRING(?,4,2)),'+00:00')
                   ELSE
                      CONVERT_TZ(?,CONCAT('+',SUBSTRING(?,1,2),':',SUBSTRING(?,3,2)),'+00:00')
                   END))
                AND (? = 'All' OR pc.currencyId = ?)
            ) Q
            WHERE NOT (
              COALESCE(NULLIF(fundsIn, ''), 0) = 0
              AND COALESCE(NULLIF(fundsOut, ''), 0) = 0
              AND COALESCE(NULLIF(balance, ''), 0) = 0
              AND (ndcPercent IS NULL OR ndcPercent = '' OR ndcPercent = '-' OR ndcPercent = 0)
              AND COALESCE(NULLIF(ndc, ''), 0) = 0
            )
            """;

        String countQuery = "SELECT * FROM (" + reportQuery + ") x";

        return this.jdbcTemplate.queryForObject(countQuery, new Object[]{
            fspId,
            fspId,
            timeZoneOffset,
            startDateValue,
            timeZoneOffset,
            timeZoneOffset,
            startDateValue,
            timeZoneOffset,
            timeZoneOffset,
            timeZoneOffset,
            endDateValue,
            timeZoneOffset,
            timeZoneOffset,
            endDateValue,
            timeZoneOffset,
            timeZoneOffset,
            currencyId,
            currencyId,
            fspId,
            fspId,
            timeZoneOffset,
            startDateValue,
            timeZoneOffset,
            timeZoneOffset,
            startDateValue,
            timeZoneOffset,
            timeZoneOffset,
            timeZoneOffset,
            endDateValue,
            timeZoneOffset,
            timeZoneOffset,
            endDateValue,
            timeZoneOffset,
            timeZoneOffset,
            currencyId,
            currencyId}, Integer.class);
    }

    private void flushSheet(Sheet sheet) throws IOException {

        if (sheet instanceof SXSSFSheet streamingSheet) {
            streamingSheet.flushRows(DEFAULT_ROW_WINDOW);
        }
    }

    private record SettlementStatementRow(String createdDate,
                                          String dfspId,
                                          String dfspName,
                                          String processDescription,
                                          BigDecimal fundsIn,
                                          BigDecimal fundsOut,
                                          BigDecimal balance,
                                          String ndcPercent,
                                          BigDecimal ndc,
                                          String currency,
                                          String settlementBankAccount) { }

    @FunctionalInterface
    private interface SettlementStatementRowConsumer {

        void accept(SettlementStatementRow row) throws IOException;

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
