package com.thitsaworks.operation_portal.reporting.report.domain.impl.poi;

import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateAuditReportCommand;
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
import java.util.stream.Collectors;

@Service
@Primary
public class GenerateAuditReportPoiCommandHandler implements GenerateAuditReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateAuditReportPoiCommandHandler.class);

    private static final int DEFAULT_ROW_WINDOW = 200;

    private static final int DEFAULT_LIMIT = Integer.MAX_VALUE;

    private static final int MYSQL_STREAM_FETCH_SIZE = Integer.MIN_VALUE;

    private static final String[] COLUMN_HEADERS = {
        "Date Time",
        "Action",
        "Made By"
    };

    private static final int[] COLUMN_WIDTHS = {28, 40, 40};

    private static final DateTimeFormatter HEADER_DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    private final JdbcTemplate jdbcTemplate;

    public GenerateAuditReportPoiCommandHandler(
        @Qualifier(PersistenceQualifiers.Core.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        try {
            if ("xlsx".equalsIgnoreCase(input.fileType())) {
                return new Output(this.exportSingleChunkXlsx(input));
            }

            if ("csv".equalsIgnoreCase(input.fileType())) {
                return new Output(this.exportSingleChunkCsv(input));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

        } catch (ReportException exception) {

            throw exception;

        } catch (Exception exception) {

            LOG.error("Error generating audit report with POI", exception);
            throw new ReportException(ReportErrors.AUDIT_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public Output exportAll(Input input, int totalRowCount, int pageSize)
        throws ReportException {

        if (totalRowCount <= 0) {
            throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
        }

        try {
            if ("xlsx".equalsIgnoreCase(input.fileType())) {
                return new Output(this.exportAllXlsx(input, totalRowCount, pageSize));
            }

            if ("csv".equalsIgnoreCase(input.fileType())) {
                return new Output(this.exportAllCsv(input, totalRowCount, pageSize));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

        } catch (ReportException exception) {

            throw exception;

        } catch (Exception exception) {

            LOG.error("Error generating full audit report with POI", exception);
            throw new ReportException(ReportErrors.AUDIT_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public int countRows(CountInput input) {

        Integer rowCount = this.countAuditRows(
            input.realmId(), input.fromDate(), input.toDate(), input.userId(), input.actionId(),
            input.grantedActionList());
        return rowCount == null ? 0 : rowCount;
    }

    private byte[] exportSingleChunkXlsx(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("audit-report-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("Audit");

            CellStyle headerLabelStyle = this.headerLabelStyle(workbook);
            CellStyle headerValueStyle = this.headerValueStyle(workbook);
            CellStyle columnHeaderStyle = this.columnHeaderStyle(workbook);
            CellStyle textCellStyle = this.textCellStyle(workbook);

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
            this.streamRows(input, row -> this.writeDataRow(
                sheet.createRow(rowCursor.next()), row, textCellStyle));
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

        Path tempFile = Files.createTempFile("audit-report-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("Audit");

            CellStyle headerLabelStyle = this.headerLabelStyle(workbook);
            CellStyle headerValueStyle = this.headerValueStyle(workbook);
            CellStyle columnHeaderStyle = this.columnHeaderStyle(workbook);
            CellStyle textCellStyle = this.textCellStyle(workbook);

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
            for (int offset = 0; offset < totalRowCount; offset += pageSize) {
                int limit = Math.min(pageSize, totalRowCount - offset);
                Input chunkInput = new Input(
                    input.realmId(), input.fromDate(), input.toDate(), input.timezoneOffset(),
                    input.userId(), input.actionId(), input.fileType(),
                    input.grantedActionList(), offset, limit);

                this.streamRows(chunkInput, row -> this.writeDataRow(
                    sheet.createRow(rowCursor.next()), row, textCellStyle));
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

        Path tempFile = Files.createTempFile("audit-report-", ".csv");

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

        Path tempFile = Files.createTempFile("audit-report-", ".csv");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {

            this.writeCsvHeader(writer, input);

            RowCounter rowCounter = new RowCounter();
            for (int offset = 0; offset < totalRowCount; offset += pageSize) {
                int limit = Math.min(pageSize, totalRowCount - offset);
                Input chunkInput = new Input(
                    input.realmId(), input.fromDate(), input.toDate(), input.timezoneOffset(),
                    input.userId(), input.actionId(), input.fileType(),
                    input.grantedActionList(), offset, limit);

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

        writer.write(this.csvLine(
            "From Date", this.formatHeaderDate(input.fromDate(), input.timezoneOffset())));
        writer.write(this.csvLine(
            "To Date", this.formatHeaderDate(input.toDate(), input.timezoneOffset())));
        writer.write(this.csvLine("Action", this.displayFilterValue(input.actionId())));
        writer.write(this.csvLine("Made By", this.displayFilterValue(input.userId())));
        writer.write(this.csvLine(
            "TimeZoneOffSet", this.displayOffset(input.timezoneOffset())));
        writer.newLine();
        writer.write(this.csvLine(COLUMN_HEADERS));
    }

    private String[] toCsvValues(AuditReportRow row) {

        return new String[]{row.dateTime(), row.action(), row.madeBy()};
    }

    private void writeDataRow(Row row, AuditReportRow data, CellStyle textCellStyle) {

        this.writeTextCell(row, 0, data.dateTime(), textCellStyle);
        this.writeTextCell(row, 1, data.action(), textCellStyle);
        this.writeTextCell(row, 2, data.madeBy(), textCellStyle);
    }

    private void streamRows(Input input, AuditReportRowConsumer consumer) {

        List<Object> parameters = new ArrayList<>();
        String reportQuery = this.auditReportQuery(input.grantedActionList());

        parameters.add(this.normalizedOffset(input.timezoneOffset()));
        parameters.add(this.normalizedOffset(input.timezoneOffset()));
        parameters.add(this.normalizedOffset(input.timezoneOffset()));

        parameters.add(input.realmId());
        parameters.add(input.realmId());
        parameters.add(input.realmId());

        parameters.add(input.fromDate() == null ? null : input.fromDate().getEpochSecond());
        parameters.add(input.toDate() == null ? null : input.toDate().getEpochSecond());
        parameters.add(input.fromDate() == null ? null : input.fromDate().getEpochSecond());
        parameters.add(input.toDate() == null ? null : input.toDate().getEpochSecond());

        parameters.add(input.userId());
        parameters.add(input.userId());
        parameters.add(input.userId());

        parameters.add(input.actionId());
        parameters.add(input.actionId());
        parameters.add(input.actionId());

        if (input.grantedActionList() != null && !input.grantedActionList().isEmpty()) {
            parameters.addAll(input.grantedActionList());
        }

        parameters.add(input.limit() == null ? DEFAULT_LIMIT : input.limit());
        parameters.add(input.offset() == null ? 0 : input.offset());

        try {
            this.jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                    reportQuery,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
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
            sheet, rowIndex, "From Date",
            this.formatHeaderDate(input.fromDate(), input.timezoneOffset()),
            labelStyle, valueStyle);
        rowIndex = this.writeHeaderRow(
            sheet, rowIndex, "To Date",
            this.formatHeaderDate(input.toDate(), input.timezoneOffset()),
            labelStyle, valueStyle);
        rowIndex = this.writeHeaderRow(
            sheet, rowIndex, "Action", this.displayFilterValue(input.actionId()),
            labelStyle, valueStyle);
        rowIndex = this.writeHeaderRow(
            sheet, rowIndex, "Made By", this.displayFilterValue(input.userId()),
            labelStyle, valueStyle);
        return this.writeHeaderRow(
            sheet, rowIndex, "TimeZoneOffSet", this.displayOffset(input.timezoneOffset()),
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
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private String formatHeaderDate(Instant instant, String rawOffset) {

        ZoneOffset zoneOffset = this.parseOffset(rawOffset);
        return instant.atOffset(ZoneOffset.UTC)
                      .withOffsetSameInstant(zoneOffset)
                      .format(HEADER_DATE_FORMAT);
    }

    private String displayOffset(String rawOffset) {

        ZoneOffset zoneOffset = this.parseOffset(rawOffset);
        return zoneOffset.getId().equals("Z") ? "+00:00" : zoneOffset.getId();
    }

    private String displayFilterValue(String value) {

        return value == null || value.isBlank() ? "All" : value;
    }

    private ZoneOffset parseOffset(String rawOffset) {

        return ZoneOffset.of(this.normalizedOffset(rawOffset));
    }

    private String normalizedOffset(String rawOffset) {

        if (rawOffset == null || rawOffset.isBlank()) {
            return "+00:00";
        }

        String normalized = rawOffset.trim();
        if (normalized.matches("[+-]\\d{4}")) {
            return normalized.substring(0, 3) + ":" + normalized.substring(3);
        }
        if (normalized.matches("\\d{4}")) {
            return "+" + normalized.substring(0, 2) + ":" + normalized.substring(2);
        }
        return normalized;
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
        if (!safeValue.contains(",") && !safeValue.contains("\"") &&
                !safeValue.contains("\n") && !safeValue.contains("\r")) {
            return safeValue;
        }

        return "\"" + safeValue.replace("\"", "\"\"") + "\"";
    }

    private AuditReportRow mapRow(ResultSet resultSet) throws SQLException {

        return new AuditReportRow(
            resultSet.getString("dateTime"),
            resultSet.getString("action"),
            resultSet.getString("madeBy"));
    }

    private String auditReportQuery(List<String> grantedActionList) {

        String grantedActionFilter = "";
        if (grantedActionList != null && !grantedActionList.isEmpty()) {
            String placeholders = grantedActionList.stream()
                                                   .map(id -> "?")
                                                   .collect(Collectors.joining(", "));
            grantedActionFilter = " AND tac.action_id IN (" + placeholders + ") ";
        }

        return """
            SELECT
              CONCAT(
                DATE_FORMAT(
                  CONVERT_TZ(FROM_UNIXTIME(ta.created_date), '+00:00', ?),
                  '%Y-%m-%dT%H:%i:%s'
                ),
                ?
              ) AS dateTime,
              tac.action_code AS action,
              CASE
                WHEN tac.action_code LIKE '%Scheduler' AND tu.email IS NULL
                  THEN CONCAT(
                    SUBSTRING(tac.action_code, 1, LENGTH(tac.action_code) - LENGTH('Scheduler')),
                    'Automatically'
                  )
                ELSE IFNULL(tu.email, '')
              END AS madeBy
            FROM tbl_audit AS ta
            LEFT JOIN tbl_participant AS tp ON tp.participant_id = ta.participant_id
            LEFT JOIN tbl_user AS tu ON tu.user_id = ta.user_id
            JOIN tbl_action AS tac ON tac.action_id = ta.action_id
            WHERE
              (? IS NULL OR ? = '' OR ta.participant_id = ?)
              AND (
                (? IS NULL OR ? IS NULL)
                OR (ta.created_date BETWEEN ? AND ?)
              )
              AND (? IS NULL OR ? = '' OR ta.user_id = ?)
              AND (? IS NULL OR ? = '' OR tac.action_id = ?)
            """ + grantedActionFilter + """
            ORDER BY ta.created_date DESC
            LIMIT ? OFFSET ?
            """;
    }

    private Integer countAuditRows(String realmId,
                                   Instant fromDate,
                                   Instant toDate,
                                   String userId,
                                   String actionId,
                                   List<String> grantedActionList) {

        String grantedActionFilter = "";
        if (grantedActionList != null && !grantedActionList.isEmpty()) {
            String placeholders = grantedActionList.stream()
                                                   .map(id -> "?")
                                                   .collect(Collectors.joining(", "));
            grantedActionFilter = " AND tac.action_id IN (" + placeholders + ") ";
        }

        String reportQuery = """
            SELECT COUNT(tac.action_code) AS rowCount
            FROM tbl_audit AS ta
            LEFT JOIN tbl_participant AS tp ON tp.participant_id = ta.participant_id
            LEFT JOIN tbl_user AS tu ON tu.user_id = ta.user_id
            JOIN tbl_action AS tac ON tac.action_id = ta.action_id
            WHERE
              (? IS NULL OR ? = '' OR ta.participant_id = ?)
              AND (
                (? IS NULL OR ? IS NULL)
                OR (ta.created_date BETWEEN ? AND ?)
              )
              AND (? IS NULL OR ? = '' OR ta.user_id = ?)
              AND (? IS NULL OR ? = '' OR tac.action_id = ?)
            """ + grantedActionFilter;

        List<Object> objectList = new ArrayList<>();
        objectList.add(realmId);
        objectList.add(realmId);
        objectList.add(realmId);

        objectList.add(fromDate == null ? null : fromDate.getEpochSecond());
        objectList.add(toDate == null ? null : toDate.getEpochSecond());
        objectList.add(fromDate == null ? null : fromDate.getEpochSecond());
        objectList.add(toDate == null ? null : toDate.getEpochSecond());

        objectList.add(userId);
        objectList.add(userId);
        objectList.add(userId);

        objectList.add(actionId);
        objectList.add(actionId);
        objectList.add(actionId);

        if (grantedActionList != null && !grantedActionList.isEmpty()) {
            objectList.addAll(grantedActionList);
        }

        return this.jdbcTemplate.queryForObject(reportQuery, objectList.toArray(), Integer.class);
    }

    private void flushSheet(Sheet sheet) throws IOException {

        if (sheet instanceof SXSSFSheet streamingSheet) {
            streamingSheet.flushRows(DEFAULT_ROW_WINDOW);
        }
    }

    private record AuditReportRow(String dateTime, String action, String madeBy) {
    }

    @FunctionalInterface
    private interface AuditReportRowConsumer {

        void accept(AuditReportRow row) throws IOException;
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
