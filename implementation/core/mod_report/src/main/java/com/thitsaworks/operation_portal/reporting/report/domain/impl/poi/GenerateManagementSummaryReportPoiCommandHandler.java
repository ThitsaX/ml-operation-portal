package com.thitsaworks.operation_portal.reporting.report.domain.impl.poi;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateManagementSummaryReportCommand;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Primary
public class GenerateManagementSummaryReportPoiCommandHandler
    implements GenerateManagementSummaryReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateManagementSummaryReportPoiCommandHandler.class);

    private static final int DEFAULT_ROW_WINDOW = 200;

    private static final int DEFAULT_LIMIT = Integer.MAX_VALUE;

    private static final int MYSQL_STREAM_FETCH_SIZE = Integer.MIN_VALUE;

    private static final String[] COLUMN_HEADERS = {
        "Payer DFSP ID",
        "Payer DFSP Name",
        "Payee DFSP ID",
        "Payee DFSP Name",
        "Number Of Transactions",
        "Total Amount",
        "Currency"
    };

    private static final int[] COLUMN_WIDTHS = {18, 28, 18, 28, 22, 18, 12};

    private static final DateTimeFormatter HEADER_DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    private final JdbcTemplate jdbcTemplate;

    public GenerateManagementSummaryReportPoiCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        try {
            if ("xlsx".equalsIgnoreCase(input.fileType())) {
                return new Output(this.exportSingleChunkXlsx(input));
            }

            if ("pdf".equalsIgnoreCase(input.fileType())) {
                return new Output(this.exportPdf(input, input.offset(), input.limit()));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

        } catch (ReportException exception) {

            throw exception;

        } catch (Exception exception) {

            LOG.error("Error generating management summary report", exception);
            throw new ReportException(ReportErrors.MANAGEMENT_SUMMARY_REPORT_FAILURE_EXCEPTION);
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

            if ("pdf".equalsIgnoreCase(input.fileType())) {
                return new Output(this.exportPdf(input, 0, totalRowCount));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

        } catch (ReportException exception) {

            throw exception;

        } catch (Exception exception) {

            LOG.error("Error generating full management summary report", exception);
            throw new ReportException(ReportErrors.MANAGEMENT_SUMMARY_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public int countRows(CountInput input) {

        Integer rowCount = this.jdbcTemplate.queryForObject(
            """
                SELECT COUNT(*) FROM (
                    SELECT
                        COUNT(1) AS NumberOfTransactions
                    FROM transfer t
                    JOIN transferStateChange tsc
                         ON t.transferId = tsc.transferId
                        AND tsc.transferStateId = 'COMMITTED'
                    INNER JOIN transferParticipant tpayer
                         ON t.transferId = tpayer.transferId
                        AND tpayer.transferParticipantRoleTypeId = 1
                    INNER JOIN participant payer
                         ON payer.participantId = tpayer.participantId
                    INNER JOIN transferParticipant tpayee
                         ON t.transferId = tpayee.transferId
                        AND tpayee.transferParticipantRoleTypeId = 2
                    INNER JOIN participant payee
                         ON payee.participantId = tpayee.participantId
                    INNER JOIN currency c
                         ON c.currencyId = t.currencyId
                    WHERE t.createdDate BETWEEN
                          STR_TO_DATE(?, '%Y-%m-%dT%T')
                      AND STR_TO_DATE(?, '%Y-%m-%dT%T')
                    GROUP BY
                        tpayer.participantId,
                        tpayee.participantId,
                        t.currencyId
                ) x
                """,
            new Object[]{input.startDate(), input.endDate()},
            Integer.class);

        return rowCount == null ? 0 : rowCount;
    }

    private byte[] exportSingleChunkXlsx(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("management-summary-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("ManagementSummaryReport");

            CellStyle headerLabelStyle = this.headerLabelStyle(workbook);
            CellStyle headerValueStyle = this.headerValueStyle(workbook);
            CellStyle columnHeaderStyle = this.columnHeaderStyle(workbook);
            CellStyle textCellStyle = this.textCellStyle(workbook);
            CellStyle countCellStyle = this.countCellStyle(workbook);
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
            this.streamRows(input, row -> this.writeDataRow(
                sheet.createRow(rowCursor.next()), row, textCellStyle, countCellStyle,
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

        Path tempFile = Files.createTempFile("management-summary-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("ManagementSummaryReport");

            CellStyle headerLabelStyle = this.headerLabelStyle(workbook);
            CellStyle headerValueStyle = this.headerValueStyle(workbook);
            CellStyle columnHeaderStyle = this.columnHeaderStyle(workbook);
            CellStyle textCellStyle = this.textCellStyle(workbook);
            CellStyle countCellStyle = this.countCellStyle(workbook);
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
            for (int offset = 0; offset < totalRowCount; offset += pageSize) {
                int limit = Math.min(pageSize, totalRowCount - offset);
                Input chunkInput = new Input(
                    input.startDate(), input.endDate(), input.timezoneOffset(),
                    input.fileType(), input.userName(), offset, limit);

                this.streamRows(chunkInput, row -> this.writeDataRow(
                    sheet.createRow(rowCursor.next()), row, textCellStyle, countCellStyle,
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

    private byte[] exportPdf(Input input, Integer offset, Integer limit) throws Exception {

        List<ManagementSummaryRow> rows = new ArrayList<>();
        Input chunkInput = new Input(
            input.startDate(), input.endDate(), input.timezoneOffset(),
            input.fileType(), input.userName(), offset, limit);

        this.streamRows(chunkInput, rows::add);

        if (rows.isEmpty()) {
            throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
        }

        Path tempFile = Files.createTempFile("management-summary-", ".pdf");

        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

        try (OutputStream outputStream = Files.newOutputStream(tempFile)) {
            Document document = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            PdfPTable metaTable = new PdfPTable(new float[]{2f, 5f});
            metaTable.setWidthPercentage(40);
            metaTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            metaTable.setSpacingAfter(15f);

            this.addPdfMetaRow(metaTable, "Start Date",
                this.formatHeaderDate(Instant.parse(input.startDate()), input.timezoneOffset()), labelFont, normalFont);
            this.addPdfMetaRow(metaTable, "End Date",
                this.formatHeaderDate(Instant.parse(input.endDate()), input.timezoneOffset()), labelFont, normalFont);
            this.addPdfMetaRow(metaTable, "TimeZoneOffSet",
                this.displayOffset(input.timezoneOffset()), labelFont, normalFont);
            document.add(metaTable);

            PdfPTable mainTable = new PdfPTable(new float[]{1.8f, 2.8f, 1.8f, 2.8f, 2.2f, 1.8f, 1.2f});
            mainTable.setWidthPercentage(100);
            for (String header : COLUMN_HEADERS) {
                mainTable.addCell(this.pdfCell(header, labelFont, Element.ALIGN_CENTER));
            }

            for (ManagementSummaryRow row : rows) {
                mainTable.addCell(this.pdfCell(row.payerDfsp(), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(this.pdfCell(row.payerDfspName(), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(this.pdfCell(row.payeeDfsp(), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(this.pdfCell(row.payeeDfspName(), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(this.pdfCell(this.formatCount(row.numberOfTransactions()), normalFont, Element.ALIGN_RIGHT));
                mainTable.addCell(this.pdfCell(this.formatAmount(row.totalAmount()), normalFont, Element.ALIGN_RIGHT));
                mainTable.addCell(this.pdfCell(row.currencyId(), normalFont, Element.ALIGN_LEFT));
            }
            document.add(mainTable);
            document.close();

            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private PdfPCell pdfCell(String text, Font font, int horizontalAlignment) {

        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
        cell.setBorderWidth(0.5f);
        cell.setPadding(4f);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private void addPdfMetaRow(PdfPTable table,
                               String label,
                               String value,
                               Font labelFont,
                               Font valueFont) {

        table.addCell(this.pdfCell(label, labelFont, Element.ALIGN_LEFT));
        table.addCell(this.pdfCell(value, valueFont, Element.ALIGN_LEFT));
    }

    private String formatAmount(BigDecimal amount) {

        if (amount == null) {
            return "0.00";
        }
        DecimalFormat format = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
        return format.format(amount);
    }

    private String formatCount(Long count) {

        if (count == null) {
            return "0";
        }
        DecimalFormat format = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.US));
        return format.format(count);
    }

    private void streamRows(Input input, ManagementSummaryRowConsumer consumer) {

        String query = """
            SELECT
                payer.name AS payerDfsp,
                payee.name AS payeeDfsp,
                IFNULL(payer.description, payer.name) AS payerDfspName,
                IFNULL(payee.description, payee.name) AS payeeDfspName,
                COUNT(1) AS numberOfTransactions,
                SUM(t.amount) AS totalAmount,
                t.currencyId AS currencyId,
                c.scale AS currencyScale
            FROM transfer t
            JOIN transferStateChange tsc
                 ON t.transferId = tsc.transferId
                AND tsc.transferStateId = 'COMMITTED'
            INNER JOIN transferParticipant tpayer
                 ON t.transferId = tpayer.transferId
                AND tpayer.transferParticipantRoleTypeId = 1
            INNER JOIN participant payer
                 ON payer.participantId = tpayer.participantId
            INNER JOIN transferParticipant tpayee
                 ON t.transferId = tpayee.transferId
                AND tpayee.transferParticipantRoleTypeId = 2
            INNER JOIN participant payee
                 ON payee.participantId = tpayee.participantId
            INNER JOIN currency c
                 ON c.currencyId = t.currencyId
            WHERE t.createdDate BETWEEN
                  STR_TO_DATE(?, '%Y-%m-%dT%T')
              AND STR_TO_DATE(?, '%Y-%m-%dT%T')
            GROUP BY
                tpayer.participantId,
                tpayee.participantId,
                t.currencyId
            ORDER BY
                tpayer.participantId,
                tpayee.participantId
            LIMIT ? OFFSET ?
            """;

        List<Object> parameters = List.of(
            input.startDate(),
            input.endDate(),
            input.limit() == null ? DEFAULT_LIMIT : input.limit(),
            input.offset() == null ? 0 : input.offset());

        try {
            this.jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                    query,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
                statement.setFetchDirection(ResultSet.FETCH_FORWARD);
                statement.setFetchSize(MYSQL_STREAM_FETCH_SIZE);
                for (int index = 0; index < parameters.size(); index++) {
                    statement.setObject(index + 1, parameters.get(index));
                }
                return statement;
            }, resultSet -> {

                    consumer.accept(this.mapRow(resultSet));

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
            this.formatHeaderDate(Instant.parse(input.startDate()), input.timezoneOffset()),
            labelStyle, valueStyle);
        rowIndex = this.writeHeaderRow(
            sheet, rowIndex, "End Date",
            this.formatHeaderDate(Instant.parse(input.endDate()), input.timezoneOffset()),
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

    private void writeDataRow(Row row,
                              ManagementSummaryRow data,
                              CellStyle textCellStyle,
                              CellStyle countCellStyle,
                              CellStyle amountCellStyle) {

        this.writeTextCell(row, 0, data.payerDfsp(), textCellStyle);
        this.writeTextCell(row, 1, data.payerDfspName(), textCellStyle);
        this.writeTextCell(row, 2, data.payeeDfsp(), textCellStyle);
        this.writeTextCell(row, 3, data.payeeDfspName(), textCellStyle);
        this.writeLongCell(row, 4, data.numberOfTransactions(), countCellStyle);
        this.writeAmountCell(row, 5, data.totalAmount(), amountCellStyle);
        this.writeTextCell(row, 6, data.currencyId(), textCellStyle);
    }

    private void writeTextCell(Row row, int columnIndex, String value, CellStyle style) {

        Cell cell = row.createCell(columnIndex);
        cell.setCellValue(value == null ? "" : value);
        cell.setCellStyle(style);
    }

    private void writeLongCell(Row row, int columnIndex, Long value, CellStyle style) {

        Cell cell = row.createCell(columnIndex);
        if (value != null) {
            cell.setCellValue(value);
        } else {
            cell.setCellValue("");
        }
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
        style.cloneStyleFrom(this.headerLabelStyle(workbook));
        return style;
    }

    private org.apache.poi.ss.usermodel.Font reportDataFont(
            org.apache.poi.ss.usermodel.Workbook workbook) {

        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        return font;
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

    private CellStyle countCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.textCellStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
        return style;
    }

    private CellStyle amountCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.textCellStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        return style;
    }

    private String formatHeaderDate(Instant instant, String rawOffset) {

        if (instant == null) {
            return "";
        }

        return this.formatInstant(instant, this.parseOffset(rawOffset));
    }

    private String displayOffset(String rawOffset) {

        ZoneOffset zoneOffset = this.parseOffset(rawOffset);
        return zoneOffset.getId().equals("Z") ? "+00:00" : zoneOffset.getId();

    }

    private String formatInstant(Instant instant, ZoneOffset zoneOffset) {

        String formatted = instant
                               .atOffset(ZoneOffset.UTC)
                               .withOffsetSameInstant(zoneOffset)
                               .format(HEADER_DATE_FORMAT);

        return "Z".equals(zoneOffset.getId()) ? formatted.replace("Z", "+00:00") : formatted;
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

    private ManagementSummaryRow mapRow(ResultSet resultSet) throws SQLException {

        return new ManagementSummaryRow(
            resultSet.getString("payerDfsp"),
            resultSet.getString("payerDfspName"),
            resultSet.getString("payeeDfsp"),
            resultSet.getString("payeeDfspName"),
            resultSet.getLong("numberOfTransactions"),
            resultSet.getBigDecimal("totalAmount"),
            resultSet.getString("currencyId"));
    }

    private void flushSheet(Sheet sheet) throws IOException {

        if (sheet instanceof SXSSFSheet streamingSheet) {
            streamingSheet.flushRows(DEFAULT_ROW_WINDOW);
        }
    }

    private record ManagementSummaryRow(String payerDfsp,
                                        String payerDfspName,
                                        String payeeDfsp,
                                        String payeeDfspName,
                                        Long numberOfTransactions,
                                        BigDecimal totalAmount,
                                        String currencyId) {
    }

    @FunctionalInterface
    private interface ManagementSummaryRowConsumer {

        void accept(ManagementSummaryRow row);
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
}
