package com.thitsaworks.operation_portal.reporting.report.domain.impl.poi;

import com.thitsaworks.operation_portal.component.misc.logging.NoLogging;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
import com.thitsaworks.operation_portal.reporting.report.domain.impl.GenerateSettlementReportCommandHandler;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
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

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
@Primary
@NoLogging
public class GenerateSettlementSummaryReportPoiCommandHandler implements GenerateSettlementReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateSettlementSummaryReportPoiCommandHandler.class);

    private static final int DEFAULT_ROW_WINDOW = 200;

    private static final int DEFAULT_LIMIT = Integer.MAX_VALUE;

    private static final int MYSQL_STREAM_FETCH_SIZE = Integer.MIN_VALUE;

    private static final String[] COLUMN_HEADERS = {
        "DFSP ID",
        "DFSP Name",
        "Sent Volume",
        "Sent Value",
        "Received Volume",
        "Received Value",
        "Total Transaction Volume",
        "Total Value of All Transactions",
        "Net Position vs. Each DFSP",
        "Currency"
    };

    private static final int[] COLUMN_WIDTHS = {18, 32, 16, 18, 16, 18, 22, 24, 24, 12};

    private static final DateTimeFormatter HEADER_DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    private final JdbcTemplate jdbcTemplate;

    private final GenerateSettlementReportCommandHandler jasperHandler;

    public GenerateSettlementSummaryReportPoiCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate,
        GenerateSettlementReportCommandHandler jasperHandler) {

        this.jdbcTemplate = jdbcTemplate;
        this.jasperHandler = jasperHandler;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        String fileType = this.normalizeFileType(input.filetype());
        Input normalizedInput = new Input(
            input.fspId(),
            input.fspName(),
            input.settlementId(),
            fileType,
            input.timezoneOffset(),
            input.userName(),
            input.offset(),
            input.limit());

        try {
            if ("xlsx".equalsIgnoreCase(fileType)) {
                return new Output(this.exportXlsx(normalizedInput));
            }

            if ("pdf".equalsIgnoreCase(fileType)) {
                return this.jasperHandler.execute(normalizedInput);
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

        } catch (ReportException exception) {
            throw exception;
        } catch (Exception exception) {
            LOG.error("Error generating settlement summary report with POI", exception);
            throw new ReportException(ReportErrors.SETTLEMENT_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public int countRows(CountInput input) {

        Integer rowCount = this.jdbcTemplate.queryForObject(
            """
                SELECT COUNT(*) FROM (
                    SELECT s3.participantId, s3.currencyId
                    FROM (
                        SELECT IF(senderName != ?, senderId, receiverId) AS participantId,
                               s.currencyId
                        FROM (
                            SELECT MAX(CASE WHEN tP.amount > 0 THEN p.participantId END) AS senderId,
                                   MAX(CASE WHEN tP.amount < 0 THEN p.participantId END) AS receiverId,
                                   MAX(CASE WHEN tP.amount > 0 THEN p.name END) AS senderName,
                                   MAX(CASE WHEN tP.amount < 0 THEN p.name END) AS receiverName,
                                   pC.currencyId
                            FROM transferParticipant tP
                            INNER JOIN transferFulfilment tF ON tP.transferId = tF.transferId
                            INNER JOIN settlementSettlementWindow sSW ON tF.settlementWindowId = sSW.settlementWindowId
                            INNER JOIN settlementWindowStateChange sWSC ON sSW.settlementWindowId = sWSC.settlementWindowId
                            INNER JOIN settlement s ON sSW.settlementId = s.settlementId
                            INNER JOIN participantCurrency pC ON tP.participantCurrencyId = pC.participantCurrencyId
                            INNER JOIN participant p ON pC.participantId = p.participantId
                            WHERE tF.isValid
                              AND sWSC.settlementWindowStateId = 'CLOSED'
                              AND s.settlementId = ?
                            GROUP BY tF.transferId, s.settlementId, pC.currencyId
                        ) s
                        WHERE s.senderName = ? OR s.receiverName = ?
                        GROUP BY IF(senderName != ?, senderId, receiverId), s.currencyId
                    ) s3
                ) x
                """,
            new Object[]{
                input.fspId(),
                input.settlementId(),
                input.fspId(),
                input.fspId(),
                input.fspId()
            },
            Integer.class);

        return rowCount == null ? 0 : rowCount;
    }

    private byte[] exportXlsx(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("settlement-summary-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);

            Sheet mainSheet = workbook.createSheet("DFSPSettlementReport");
            Sheet summarySheet = workbook.createSheet("Summary");
            this.trackColumns(mainSheet);
            this.trackColumns(summarySheet);

            CellStyle labelStyle = this.labelStyle(workbook);
            CellStyle valueStyle = this.valueStyle(workbook);
            CellStyle headerStyle = this.headerStyle(workbook);
            CellStyle textStyle = this.textStyle(workbook);
            CellStyle amountStyle = this.amountStyle(workbook);
            CellStyle volumeStyle = this.volumeStyle(workbook);

            String settlementCreatedDate = this.loadSettlementCreatedDate(
                input.settlementId(), input.timezoneOffset());

            int rowIndex = 0;
            rowIndex = this.writeMeta(mainSheet, rowIndex, "Settlement ID", input.settlementId(),
                                      labelStyle, valueStyle);
            rowIndex = this.writeMeta(mainSheet, rowIndex, "Settlement Created Date",
                                      settlementCreatedDate, labelStyle, valueStyle);
            rowIndex = this.writeMeta(mainSheet, rowIndex, "DFSP ID", input.fspId(),
                                      labelStyle, valueStyle);
            rowIndex = this.writeMeta(mainSheet, rowIndex, "DFSP Name", input.fspName(),
                                      labelStyle, valueStyle);
            rowIndex = this.writeMeta(mainSheet, rowIndex, "TimeZoneOffSet",
                                      this.displayOffset(input.timezoneOffset()),
                                      labelStyle, valueStyle);
            rowIndex++;

            Row headerRow = mainSheet.createRow(rowIndex++);
            for (int index = 0; index < COLUMN_HEADERS.length; index++) {
                Cell cell = headerRow.createCell(index);
                cell.setCellValue(COLUMN_HEADERS[index]);
                cell.setCellStyle(headerStyle);
            }

            RowCursor rowCursor = new RowCursor(rowIndex);
            RowCounter rowCounter = new RowCounter();
            this.streamRows(input, row -> {
                this.writeDataRow(
                    mainSheet.createRow(rowCursor.next()), row, textStyle, amountStyle, volumeStyle);
                rowCounter.increment();
            });

            if (rowCounter.value() == 0) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            this.flush(mainSheet);
            for (int index = 0; index < COLUMN_WIDTHS.length; index++) {
                mainSheet.setColumnWidth(index, COLUMN_WIDTHS[index] * 256);
            }
            mainSheet.createFreezePane(0, rowIndex);

            int summaryRowIndex = 0;
            summaryRowIndex = this.writeMeta(summarySheet, summaryRowIndex, "Aggregated Net Positions", "",
                                             labelStyle, valueStyle);
            Row summaryHeaderRow = summarySheet.createRow(summaryRowIndex++);
            summaryHeaderRow.createCell(0).setCellValue("Currency");
            summaryHeaderRow.createCell(1).setCellValue("Net Position Amount");
            summaryHeaderRow.getCell(0).setCellStyle(headerStyle);
            summaryHeaderRow.getCell(1).setCellStyle(headerStyle);

            RowCursor summaryCursor = new RowCursor(summaryRowIndex);
            this.streamSummaryRows(input, row -> this.writeSummaryRow(
                summarySheet.createRow(summaryCursor.next()), row, textStyle, amountStyle));

            this.flush(summarySheet);
            summarySheet.setColumnWidth(0, 18 * 256);
            summarySheet.setColumnWidth(1, 20 * 256);
            summarySheet.createFreezePane(0, summaryRowIndex);

            workbook.write(outputStream);
            workbook.dispose();
            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private void streamRows(Input input, SettlementSummaryRowConsumer consumer) {

        List<Object> params = new ArrayList<>();
        params.add(input.fspId());
        params.add(input.fspId());
        params.add(input.fspId());
        params.add(input.fspId());
        params.add(input.fspId());
        params.add(input.settlementId());
        params.add(input.fspId());
        params.add(input.fspId());
        params.add(input.offset() == null ? 0 : input.offset());
        params.add(input.limit() == null ? DEFAULT_LIMIT : input.limit());

        try {
            this.jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                    this.mainQuery(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                statement.setFetchDirection(ResultSet.FETCH_FORWARD);
                statement.setFetchSize(MYSQL_STREAM_FETCH_SIZE);
                for (int index = 0; index < params.size(); index++) {
                    statement.setObject(index + 1, params.get(index));
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

    private void streamSummaryRows(Input input, SettlementSummaryNetPositionConsumer consumer) {

        List<Object> params = new ArrayList<>();
        params.add(input.fspId());
        params.add(input.fspId());
        params.add(input.settlementId());
        params.add(input.fspId());
        params.add(input.fspId());
        params.add(input.offset() == null ? 0 : input.offset());
        params.add(input.limit() == null ? DEFAULT_LIMIT : input.limit());

        try {
            this.jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                    this.summaryQuery(), ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
                statement.setFetchDirection(ResultSet.FETCH_FORWARD);
                statement.setFetchSize(MYSQL_STREAM_FETCH_SIZE);
                for (int index = 0; index < params.size(); index++) {
                    statement.setObject(index + 1, params.get(index));
                }
                return statement;
            }, resultSet -> {
                while (resultSet.next()) {
                    try {
                        consumer.accept(this.mapSummaryRow(resultSet));
                    } catch (IOException exception) {
                        throw new IOExceptionRuntimeException(exception);
                    }
                }
            });
        } catch (IOExceptionRuntimeException exception) {
            throw exception;
        }
    }

    private SettlementSummaryRow mapRow(ResultSet resultSet) throws SQLException {

        return new SettlementSummaryRow(
            resultSet.getString("participantId"),
            resultSet.getString("dfspName"),
            resultSet.getBigDecimal("sentVolume"),
            resultSet.getBigDecimal("sentAmount"),
            resultSet.getBigDecimal("receivedVolume"),
            resultSet.getBigDecimal("receivedAmount"),
            resultSet.getBigDecimal("totalVolume"),
            resultSet.getBigDecimal("totalAmount"),
            resultSet.getBigDecimal("netAmount"),
            resultSet.getString("currencyId"));
    }

    private SettlementSummaryNetPositionRow mapSummaryRow(ResultSet resultSet) throws SQLException {

        return new SettlementSummaryNetPositionRow(
            resultSet.getString("currencyId"),
            resultSet.getBigDecimal("netPositionAmount"));
    }

    private String mainQuery() {

        return """
            SELECT p.name AS participantId,
                   IFNULL(op.description, p.name) AS dfspName,
                   s3.currencyId,
                   ROUND(s3.sentAmount, 2) AS sentAmount,
                   s3.sentVolume,
                   ROUND(s3.receivedAmount, 2) AS receivedAmount,
                   s3.receivedVolume,
                   ROUND((s3.sentAmount + s3.receivedAmount), 2) AS totalAmount,
                   (s3.sentVolume + s3.receivedVolume) AS totalVolume,
                   ROUND((s3.receivedAmount - s3.sentAmount), 2) AS netAmount
            FROM participant p
            INNER JOIN (
                SELECT settlementId,
                       MAX(currencyId) AS currencyId,
                       participantId,
                       SUM(sentAmount) AS sentAmount,
                       SUM(sentVolume) AS sentVolume,
                       SUM(receivedAmount) AS receivedAmount,
                       SUM(receivedVolume) AS receivedVolume
                FROM (
                    SELECT settlementId,
                           MAX(currencyId) AS currencyId,
                           IF(senderName != ?, senderId, receiverId) AS participantId,
                           SUM(IF(senderName = ?, amount, 0)) AS sentAmount,
                           SUM(IF(senderName = ?, volume, 0)) AS sentVolume,
                           SUM(IF(receiverName = ?, amount, 0)) AS receivedAmount,
                           SUM(IF(receiverName = ?, volume, 0)) AS receivedVolume
                    FROM (
                        SELECT MAX(CASE WHEN tP.amount > 0 THEN p.participantId END) AS senderId,
                               MAX(CASE WHEN tP.amount < 0 THEN p.participantId END) AS receiverId,
                               MAX(CASE WHEN tP.amount > 0 THEN p.name END) AS senderName,
                               MAX(CASE WHEN tP.amount < 0 THEN p.name END) AS receiverName,
                               MAX(tP.amount) AS amount,
                               MAX(c.currencyId) AS currencyId,
                               COUNT(DISTINCT tF.transferId) AS volume,
                               s.settlementId
                        FROM transferParticipant tP
                        INNER JOIN transferFulfilment tF ON tP.transferId = tF.transferId
                        INNER JOIN settlementSettlementWindow sSW ON tF.settlementWindowId = sSW.settlementWindowId
                        INNER JOIN settlementWindowStateChange sWSC ON sSW.settlementWindowId = sWSC.settlementWindowId
                        INNER JOIN settlement s ON sSW.settlementId = s.settlementId
                        INNER JOIN participantCurrency pC ON tP.participantCurrencyId = pC.participantCurrencyId
                        INNER JOIN currency c ON c.currencyId = pC.currencyId
                        INNER JOIN participant p ON pC.participantId = p.participantId
                        WHERE tF.isValid
                          AND sWSC.settlementWindowStateId = 'CLOSED'
                          AND s.settlementId = ?
                        GROUP BY tF.transferId, s.settlementId, pC.currencyId
                    ) s
                    WHERE s.senderName = ? OR s.receiverName = ?
                    GROUP BY settlementId, senderId, receiverId, senderName, receiverName, s.currencyId
                ) s2
                GROUP BY settlementId, participantId, s2.currencyId
            ) s3 ON p.participantId = s3.participantId
            LEFT JOIN operation_portal.tbl_participant op ON op.participant_name = p.name
            WHERE p.name != 'Hub'
            ORDER BY p.name
            LIMIT ?, ?
            """;
    }

    private String summaryQuery() {

        return """
            SELECT currencyId,
                   ROUND((SUM(receivedAmount) - SUM(sentAmount)), 2) AS netPositionAmount
            FROM (
                SELECT MAX(currencyId) AS currencyId,
                       SUM(sentAmount) AS sentAmount,
                       SUM(receivedAmount) AS receivedAmount
                FROM (
                    SELECT MAX(currencyId) AS currencyId,
                           SUM(IF(senderName = ?, amount, 0)) AS sentAmount,
                           SUM(IF(receiverName = ?, amount, 0)) AS receivedAmount
                    FROM (
                        SELECT MAX(CASE WHEN tP.amount > 0 THEN p.name END) AS senderName,
                               MAX(CASE WHEN tP.amount < 0 THEN p.name END) AS receiverName,
                               MAX(tP.amount) AS amount,
                               MAX(c.currencyId) AS currencyId
                        FROM transferParticipant tP
                        INNER JOIN transferFulfilment tF ON tP.transferId = tF.transferId
                        INNER JOIN settlementSettlementWindow sSW ON tF.settlementWindowId = sSW.settlementWindowId
                        INNER JOIN settlementWindowStateChange sWSC ON sSW.settlementWindowId = sWSC.settlementWindowId
                        INNER JOIN settlement s ON sSW.settlementId = s.settlementId
                        INNER JOIN participantCurrency pC ON tP.participantCurrencyId = pC.participantCurrencyId
                        INNER JOIN currency c ON c.currencyId = pC.currencyId
                        INNER JOIN participant p ON pC.participantId = p.participantId
                        WHERE tF.isValid
                          AND sWSC.settlementWindowStateId = 'CLOSED'
                          AND s.settlementId = ?
                        GROUP BY tF.transferId, s.settlementId, pC.currencyId
                    ) s
                    WHERE s.senderName = ? OR s.receiverName = ?
                    GROUP BY senderName, receiverName, s.currencyId
                    ORDER BY receiverName
                    LIMIT ?, ?
                ) s2
                GROUP BY s2.currencyId
            ) s3
            GROUP BY currencyId
            ORDER BY currencyId
            """;
    }

    private String loadSettlementCreatedDate(String settlementId, String timezoneOffset) {

        Timestamp timestamp = this.jdbcTemplate.query(
            "SELECT createdDate FROM settlement WHERE settlementId = ?",
            resultSet -> resultSet.next() ? resultSet.getTimestamp("createdDate") : null,
            settlementId);

        if (timestamp == null) {
            return "";
        }

        ZoneOffset zoneOffset = this.parseOffset(timezoneOffset);
        Instant instant = timestamp.toInstant();
        return instant.atOffset(ZoneOffset.UTC)
                      .withOffsetSameInstant(zoneOffset)
                      .format(HEADER_DATE_FORMAT);
    }

    private int writeMeta(Sheet sheet,
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

    private void writeDataRow(Row row,
                              SettlementSummaryRow data,
                              CellStyle textStyle,
                              CellStyle amountStyle,
                              CellStyle volumeStyle) {

        this.writeTextCell(row, 0, data.participantId(), textStyle);
        this.writeTextCell(row, 1, data.dfspName(), textStyle);
        this.writeNumberCell(row, 2, data.sentVolume(), volumeStyle);
        this.writeNumberCell(row, 3, data.sentAmount(), amountStyle);
        this.writeNumberCell(row, 4, data.receivedVolume(), volumeStyle);
        this.writeNumberCell(row, 5, data.receivedAmount(), amountStyle);
        this.writeNumberCell(row, 6, data.totalVolume(), volumeStyle);
        this.writeNumberCell(row, 7, data.totalAmount(), amountStyle);
        this.writeNumberCell(row, 8, data.netAmount(), amountStyle);
        this.writeTextCell(row, 9, data.currencyId(), textStyle);
    }

    private void writeSummaryRow(Row row,
                                 SettlementSummaryNetPositionRow data,
                                 CellStyle textStyle,
                                 CellStyle amountStyle) {

        this.writeTextCell(row, 0, data.currencyId(), textStyle);
        this.writeNumberCell(row, 1, data.netPositionAmount(), amountStyle);
    }

    private void writeTextCell(Row row, int columnIndex, String value, CellStyle style) {

        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value == null ? "" : value);
        cell.setCellStyle(style);
    }

    private void writeNumberCell(Row row, int columnIndex, BigDecimal value, CellStyle style) {

        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value == null ? 0 : value.doubleValue());
        cell.setCellStyle(style);
    }

    private CellStyle labelStyle(SXSSFWorkbook workbook) {

        CellStyle style = workbook.createCellStyle();
        this.applyBorder(style);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private CellStyle valueStyle(SXSSFWorkbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.labelStyle(workbook));
        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }

    private CellStyle headerStyle(SXSSFWorkbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.labelStyle(workbook));
        style.setWrapText(true);
        return style;
    }

    private CellStyle textStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        this.applyBorder(style);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }

    private CellStyle amountStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.textStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00;(#,##0.00)"));
        return style;
    }

    private CellStyle volumeStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.textStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
        return style;
    }

    private void applyBorder(CellStyle style) {

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }

    private void trackColumns(Sheet sheet) {

        if (sheet instanceof SXSSFSheet streamingSheet) {
            streamingSheet.trackAllColumnsForAutoSizing();
        }
    }

    private void flush(Sheet sheet) throws IOException {

        if (sheet instanceof SXSSFSheet streamingSheet) {
            streamingSheet.flushRows(DEFAULT_ROW_WINDOW);
        }
    }

    private String normalizeFileType(String fileType) {

        if (fileType == null) {
            return "";
        }

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

    private record SettlementSummaryRow(String participantId,
                                        String dfspName,
                                        BigDecimal sentVolume,
                                        BigDecimal sentAmount,
                                        BigDecimal receivedVolume,
                                        BigDecimal receivedAmount,
                                        BigDecimal totalVolume,
                                        BigDecimal totalAmount,
                                        BigDecimal netAmount,
                                        String currencyId) {
    }

    private record SettlementSummaryNetPositionRow(String currencyId,
                                                   BigDecimal netPositionAmount) {
    }

    @FunctionalInterface
    private interface SettlementSummaryRowConsumer {

        void accept(SettlementSummaryRow row) throws IOException;
    }

    @FunctionalInterface
    private interface SettlementSummaryNetPositionConsumer {

        void accept(SettlementSummaryNetPositionRow row) throws IOException;
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
