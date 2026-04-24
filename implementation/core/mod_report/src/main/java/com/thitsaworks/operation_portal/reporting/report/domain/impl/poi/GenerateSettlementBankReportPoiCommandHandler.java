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
import com.thitsaworks.operation_portal.component.misc.logging.NoLogging;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementBankReportCommand;
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

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
@Primary
@NoLogging
public class GenerateSettlementBankReportPoiCommandHandler implements GenerateSettlementBankReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(GenerateSettlementBankReportPoiCommandHandler.class);

    private static final int DEFAULT_ROW_WINDOW = 200;
    private static final int DEFAULT_LIMIT = Integer.MAX_VALUE;
    private static final int MYSQL_STREAM_FETCH_SIZE = Integer.MIN_VALUE;
    private static final float PDF_LEFT_MARGIN = 10f;
    private static final float PDF_RIGHT_MARGIN = 10f;
    private static final float PDF_TOP_MARGIN = 24f;
    private static final float PDF_BOTTOM_MARGIN = 32f;
    private static final String ALL_CURRENCY = "ALL";
    private static final String[] COLUMN_HEADERS = {
            "Participant", "Settlement Bank Account", "Balance", "Settlement Transfer", "Currency"
    };

    private static final int[] COLUMN_WIDTHS = {41, 41, 20, 22, 16};

    private static final float[] PDF_MAIN_COLUMN_WIDTHS = {200f, 300f, 105f, 200f, 129f};

    private static final float[] PDF_META_COLUMN_WIDTHS = {200f, 300f};

    private static final float PDF_META_WIDTH_PERCENTAGE = (500f / 934f) * 100f;

    private final JdbcTemplate jdbcTemplate;

    public GenerateSettlementBankReportPoiCommandHandler(
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

            if ("pdf".equalsIgnoreCase(fileType)) {
                return new Output(exportPdf(input));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);
        } catch (ReportException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Error generating settlement bank report with Apache POI", e);
            throw new ReportException(ReportErrors.SETTLEMENT_BANK_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public int countRows(CountInput input) {
        Integer rowCount = jdbcTemplate.queryForObject("""
                                                               SELECT COUNT(*) FROM (
                                                                   SELECT p.name
                                                                   FROM settlement s
                                                                   INNER JOIN settlementSettlementWindow ssw ON ssw.settlementId = s.settlementId
                                                                   INNER JOIN transferFulfilment tf ON tf.settlementWindowId = ssw.settlementWindowId
                                                                   INNER JOIN transferParticipant tp ON tp.transferId = tf.transferId
                                                                   INNER JOIN participantCurrency pc ON tp.participantCurrencyId = pc.participantCurrencyId
                                                                   INNER JOIN participant p ON p.participantId = pc.participantId
                                                                   LEFT JOIN operation_portal.tbl_participant op
                                                                       ON op.participant_name COLLATE UTF8MB4_UNICODE_CI = p.name COLLATE UTF8MB4_UNICODE_CI
                                                                   LEFT JOIN operation_portal.tbl_liquidity_profile lp
                                                                       ON lp.currency COLLATE UTF8MB4_UNICODE_CI = pc.currencyId COLLATE UTF8MB4_UNICODE_CI
                                                                      AND is_active = 1
                                                                      AND op.participant_id COLLATE UTF8MB4_UNICODE_CI = lp.participant_id COLLATE UTF8MB4_UNICODE_CI
                                                                   INNER JOIN ledgerAccountType lat ON lat.ledgerAccountTypeId = pc.ledgerAccountTypeId
                                                                   WHERE s.settlementId = ?
                                                                     AND lat.name = 'POSITION'
                                                                     AND (? = 'All' OR pc.currencyId COLLATE UTF8MB4_UNICODE_CI = ?)
                                                                   GROUP BY p.name, pc.participantCurrencyId, lp.bank_name, lp.account_name,
                                                                            lp.account_number, op.description
                                                               ) x
                                                               """,
                                                       new Object[]{
                                                               input.settlementId(),
                                                               normalizeCurrency(input.currencyId()),
                                                               normalizeCurrency(input.currencyId())},
                                                       Integer.class);

        return rowCount == null ? 0 : rowCount;
    }

    public Output exportAll(Input input, int totalRowCount, int pageSize) throws ReportException {
        if (totalRowCount <= 0) {
            throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
        }

        if (pageSize <= 0) {
            throw new ReportException(ReportErrors.SETTLEMENT_BANK_REPORT_FAILURE_EXCEPTION);
        }

        String fileType = normalizeFileType(input.fileType());

        try {
            if ("xlsx".equalsIgnoreCase(fileType)) {
                return new Output(exportAllXlsx(input, totalRowCount, pageSize));
            }

            if ("pdf".equalsIgnoreCase(fileType)) {
                return new Output(exportAllPdf(input, totalRowCount, pageSize));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);
        } catch (ReportException e) {
            throw e;
        } catch (Exception e) {
            LOG.error("Error generating full settlement bank report with Apache POI", e);
            throw new ReportException(ReportErrors.SETTLEMENT_BANK_REPORT_FAILURE_EXCEPTION);
        }
    }

    private byte[] exportSingleChunkXlsx(Input input) throws IOException, ReportException {

        List<SettlementBankRow> rows = new ArrayList<>();
        MetadataCapture metadata = new MetadataCapture();

        streamRows(input, row -> {
            rows.add(row);
            metadata.capture(row);
        });

        return buildXlsx(input, rows, metadata);
    }

    private byte[] exportAllXlsx(Input input, int totalRowCount, int pageSize) throws IOException, ReportException {

        List<SettlementBankRow> rows = fetchRowsAcrossPages(input, totalRowCount, pageSize);
        MetadataCapture metadata = new MetadataCapture();
        rows.forEach(metadata::capture);
        return buildXlsx(input, rows, metadata);
    }

    private byte[] exportPdf(Input input) throws IOException, ReportException, DocumentException {

        List<SettlementBankRow> rows = new ArrayList<>();
        MetadataCapture metadata = new MetadataCapture();

        streamRows(input, row -> {
            rows.add(row);
            metadata.capture(row);
        });

        return buildPdf(input, rows, metadata);
    }

    private byte[] exportAllPdf(Input input, int totalRowCount, int pageSize)
            throws IOException, ReportException, DocumentException {

        List<SettlementBankRow> rows = fetchRowsAcrossPages(input, totalRowCount, pageSize);
        MetadataCapture metadata = new MetadataCapture();
        rows.forEach(metadata::capture);
        return buildPdf(input, rows, metadata);
    }

    private byte[] buildXlsx(Input input, List<SettlementBankRow> rows, MetadataCapture metadata)
            throws IOException, ReportException {

        if (rows.isEmpty()) {
            throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
        }

        Path tempFile = Files.createTempFile("settlement-bank-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);

            Sheet sheet = workbook.createSheet("SettlementBankReport");
            if (sheet instanceof SXSSFSheet streamingSheet) {
                streamingSheet.trackAllColumnsForAutoSizing();
            }

            CellStyle metaLabelStyle = metaLabelStyle(workbook);
            CellStyle metaValueStyle = metaValueStyle(workbook);
            CellStyle headerStyle = columnHeaderStyle(workbook);
            CellStyle textStyle = textCellStyle(workbook);
            CellStyle amountStyle = amountCellStyle(workbook);
            CellStyle rightTextStyle = rightTextCellStyle(workbook);

            int rowIndex = 0;
            rowIndex = writeMeta(sheet,
                                 rowIndex,
                                 "Settlement ID",
                                 input.settlementId(),
                                 metaLabelStyle,
                                 metaValueStyle);
            rowIndex = writeMeta(sheet, rowIndex, "Settlement Created Date", metadata.createdDate(), metaLabelStyle,
                                 metaValueStyle);
            rowIndex = writeMeta(sheet, rowIndex, "Currency", normalizeCurrency(input.currencyId()), metaLabelStyle,
                                 metaValueStyle);
            rowIndex = writeMeta(sheet, rowIndex, "TimeZoneOffSet", displayOffset(input.timezoneOffset()),
                                 metaLabelStyle, metaValueStyle);
            rowIndex++;

            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < COLUMN_HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMN_HEADERS[i]);
                cell.setCellStyle(headerStyle);
            }
            int freezeRow = rowIndex;

            for (SettlementBankRow row : rows) {
                Row dataRow = sheet.createRow(rowIndex++);
                writeDataRow(dataRow, row, textStyle, amountStyle, rightTextStyle);
            }

            for (int i = 0; i < COLUMN_WIDTHS.length; i++) {
                sheet.setColumnWidth(i, COLUMN_WIDTHS[i] * 256);
            }

            sheet.createFreezePane(0, freezeRow);
            flushSheet(sheet);
            workbook.write(outputStream);
            workbook.dispose();

            return Files.readAllBytes(tempFile);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private byte[] buildPdf(Input input, List<SettlementBankRow> rows, MetadataCapture metadata)
            throws IOException, ReportException, DocumentException {
        if (rows.isEmpty()) {
            throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
        }

        Path tempFile = Files.createTempFile("settlement-bank-", ".pdf");
        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        try (OutputStream outputStream = Files.newOutputStream(tempFile)) {
            Document document = new Document(PageSize.A4.rotate(),
                                             PDF_LEFT_MARGIN,
                                             PDF_RIGHT_MARGIN,
                                             PDF_TOP_MARGIN,
                                             PDF_BOTTOM_MARGIN);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            PdfPTable metaTable = new PdfPTable(PDF_META_COLUMN_WIDTHS);
            metaTable.setWidthPercentage(PDF_META_WIDTH_PERCENTAGE);
            metaTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            metaTable.setSpacingAfter(16f);

            addPdfMetaRow(metaTable, "Settlement ID", input.settlementId(), labelFont, normalFont);
            addPdfMetaRow(metaTable, "Settlement Created Date", metadata.createdDate(), labelFont, normalFont);
            addPdfMetaRow(metaTable, "Currency", normalizeCurrency(input.currencyId()), labelFont, normalFont);
            addPdfMetaRow(metaTable, "TimeZoneOffSet", displayOffset(input.timezoneOffset()), labelFont, normalFont);

            document.add(metaTable);

            PdfPTable mainTable = new PdfPTable(PDF_MAIN_COLUMN_WIDTHS);
            mainTable.setWidthPercentage(100);
            mainTable.setSpacingAfter(14f);

            for (String header : COLUMN_HEADERS) {
                mainTable.addCell(pdfCell(header, labelFont, Element.ALIGN_LEFT));
            }

            for (SettlementBankRow row : rows) {
                mainTable.addCell(pdfCell(safe(row.participant()), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(pdfCell(safe(row.settlementBankAccount()), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(pdfCell("", normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(pdfCell(amountOrDashText(row.transfer()), normalFont, Element.ALIGN_RIGHT));
                mainTable.addCell(pdfCell(safe(row.currency()), normalFont, Element.ALIGN_LEFT));
            }

            document.add(mainTable);

            Phrase footer = new Phrase(buildPrintedByText(input.userName(), input.timezoneOffset()), normalFont);
            PdfPTable footerTable = new PdfPTable(1);
            footerTable.setTotalWidth(document.right() - document.left());
            PdfPCell footerCell = new PdfPCell(footer);
            footerCell.setBorder(0);
            footerCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            footerCell.setPaddingBottom(0f);
            footerTable.addCell(footerCell);
            footerTable.writeSelectedRows(0, -1, document.left(), document.bottom(), writer.getDirectContent());

            document.close();
            return Files.readAllBytes(tempFile);
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private List<SettlementBankRow> fetchRowsAcrossPages(Input input, int totalRowCount, int pageSize) {
        List<SettlementBankRow> rows = new ArrayList<>();
        if (pageSize <= 0) {
            return rows;
        }

        int baseOffset = input.offset() == null ? 0 : input.offset();

        for (int offset = 0; offset < totalRowCount; offset += pageSize) {
            int limit = Math.min(pageSize, totalRowCount - offset);
            Input pageInput = new Input(input.settlementId(),
                                        input.currencyId(),
                                        input.fileType(),
                                        input.timezoneOffset(),
                                        input.userName(),
                                        input.dfspId(),
                                        input.isParent(),
                                        baseOffset + offset,
                                        limit);
            streamRows(pageInput, rows::add);
        }

        return rows;
    }

    private void streamRows(Input input, SettlementBankRowConsumer consumer) {
        String timezoneOffset = normalizeTimezoneOffset(input.timezoneOffset());
        String currency = normalizeCurrency(input.currencyId());

        List<Object> parameters = new ArrayList<>();
        addTimezoneDateFormatParams(parameters, timezoneOffset);
        addTimezoneOffsetOnlyParams(parameters, timezoneOffset);
        parameters.add(input.settlementId());
        parameters.add(currency);
        parameters.add(currency);
        parameters.add(input.limit() == null ? DEFAULT_LIMIT : input.limit());
        parameters.add(input.offset() == null ? 0 : input.offset());

        final int[] fetchedRowCount = {0};

        this.jdbcTemplate.query(connection -> {
            PreparedStatement statement = connection.prepareStatement(mainQuery(),
                                                                      ResultSet.TYPE_FORWARD_ONLY,
                                                                      ResultSet.CONCUR_READ_ONLY);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setFetchSize(MYSQL_STREAM_FETCH_SIZE);

            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }

            return statement;
        }, resultSet -> {

            fetchedRowCount[0]++;
                try {
                    SettlementBankRow row = mapRow(resultSet);
                    LOG.info("Fetched row [{}] participant=[{}], account=[{}], currency=[{}]",
                             fetchedRowCount[0],
                             row.participant(),
                             row.settlementBankAccount(),
                             row.currency());
                    consumer.accept(row);
                } catch (IOException e) {
                    throw new IOExceptionRuntimeException(e);
                }

        });

        LOG.info("SettlementBank result set row count = [{}], settlementId=[{}], currency=[{}], offset=[{}], limit=[{}]",
                 fetchedRowCount[0],
                 input.settlementId(),
                 currency,
                 input.offset(),
                 input.limit());
    }

    private SettlementBankRow mapRow(ResultSet rs) throws SQLException {
        return new SettlementBankRow(rs.getString("createdDate"),
                                     rs.getString("participant"),
                                     rs.getString("settlementBankAccount"),
                                     rs.getBigDecimal("transfer"),
                                     rs.getString("currency"),
                                     rs.getString("timezoneoffset"));
    }

    private String mainQuery() {
        return """
                SELECT CONCAT(
                    DATE_FORMAT(
                        CASE
                            WHEN SUBSTRING(?,1,1) = '-'
                                THEN CONVERT_TZ(s.createdDate, '+00:00', CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2)))
                            ELSE CONVERT_TZ(s.createdDate, '+00:00', CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)))
                        END,
                        '%Y-%m-%dT%H:%i:%s'
                    ),
                    CASE
                        WHEN SUBSTRING(?,1,1) = '-'
                            THEN CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2))
                        ELSE CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2))
                    END
                ) AS createdDate,
                IFNULL(CONCAT(IFNULL(p.name COLLATE UTF8MB4_UNICODE_CI, ''), ' - ', IFNULL(op.description COLLATE UTF8MB4_UNICODE_CI, '')), '') AS participant,
                IFNULL(CONCAT(IFNULL(lp.bank_name, ''), ' - ', IFNULL(lp.account_name, ''), ' - ', IFNULL(lp.account_number, '')), '') AS settlementBankAccount,
                SUM(tp.amount) AS transfer,
                pc.currencyId AS currency,
                CASE
                    WHEN SUBSTRING(?,1,1) = '-'
                        THEN CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2))
                    ELSE CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2))
                END AS timezoneoffset
                FROM settlement s
                INNER JOIN settlementSettlementWindow ssw ON ssw.settlementId = s.settlementId
                INNER JOIN transferFulfilment tf ON tf.settlementWindowId = ssw.settlementWindowId
                INNER JOIN transferParticipant tp ON tp.transferId = tf.transferId
                INNER JOIN participantCurrency pc ON tp.participantCurrencyId = pc.participantCurrencyId
                INNER JOIN participant p ON p.participantId = pc.participantId
                LEFT JOIN operation_portal.tbl_participant op
                    ON op.participant_name COLLATE UTF8MB4_UNICODE_CI = p.name COLLATE UTF8MB4_UNICODE_CI
                LEFT JOIN operation_portal.tbl_liquidity_profile lp
                    ON lp.currency COLLATE UTF8MB4_UNICODE_CI = pc.currencyId COLLATE UTF8MB4_UNICODE_CI
                   AND is_active = 1
                   AND op.participant_id COLLATE UTF8MB4_UNICODE_CI = lp.participant_id COLLATE UTF8MB4_UNICODE_CI
                INNER JOIN ledgerAccountType lat ON lat.ledgerAccountTypeId = pc.ledgerAccountTypeId
                WHERE s.settlementId = ?
                  AND lat.name = 'POSITION'
                  AND (? = 'All' OR pc.currencyId COLLATE UTF8MB4_UNICODE_CI = ?)
                GROUP BY p.name, pc.participantCurrencyId, lp.bank_name, lp.account_name, lp.account_number, op.description
                ORDER BY participant ASC, settlementBankAccount ASC, pc.currencyId ASC
                LIMIT ? OFFSET ?
                """;
    }

    private void writeDataRow(Row row,
                              SettlementBankRow data,
                              CellStyle textCellStyle,
                              CellStyle amountCellStyle,
                              CellStyle rightTextCellStyle) {
        writeTextCell(row, 0, data.participant(), textCellStyle);
        writeTextCell(row, 1, data.settlementBankAccount(), textCellStyle);
        writeTextCell(row, 2, "", textCellStyle);
        writeAmountOrDashCell(row, 3, data.transfer(), rightTextCellStyle, amountCellStyle);
        writeTextCell(row, 4, data.currency(), textCellStyle);
    }

    private int writeMeta(Sheet sheet,
                          int rowIndex,
                          String key,
                          String value,
                          CellStyle keyStyle,
                          CellStyle valueStyle) {
        Row row = sheet.createRow(rowIndex++);
        Cell keyCell = row.createCell(0);
        keyCell.setCellValue(key);
        keyCell.setCellStyle(keyStyle);

        Cell valueCell = row.createCell(1);
        valueCell.setCellValue(value == null ? "" : value);
        valueCell.setCellStyle(valueStyle);
        return rowIndex;
    }

    private void writeTextCell(Row row, int col, String value, CellStyle style) {
        Cell cell = row.createCell(col);
        cell.setCellValue(safe(value));
        cell.setCellStyle(style);
    }

    private void writeAmountCell(Row row, int col, BigDecimal value, CellStyle style) {
        Cell cell = row.createCell(col);
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        } else {
            cell.setBlank();
        }
        cell.setCellStyle(style);
    }

    private void writeAmountOrDashCell(Row row,
                                       int col,
                                       BigDecimal value,
                                       CellStyle rightTextStyle,
                                       CellStyle amountStyle) {
        if (value == null || BigDecimal.ZERO.compareTo(value) == 0) {
            writeTextCell(row, col, "-", rightTextStyle);
            return;
        }
        writeAmountCell(row, col, value, amountStyle);
    }

    private PdfPCell pdfCell(String text, Font font, int horizontalAlignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorderWidth(0.5f);
        cell.setPaddingTop(4f);
        cell.setPaddingBottom(4f);
        cell.setPaddingLeft(4f);
        cell.setPaddingRight(4f);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        return cell;
    }

    private void addPdfMetaRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        table.addCell(pdfCell(label, labelFont, Element.ALIGN_LEFT));
        table.addCell(pdfCell(safe(value), valueFont, Element.ALIGN_LEFT));
    }

    private CellStyle metaLabelStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        applyCommonBorder(style);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);

        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private CellStyle metaValueStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(metaLabelStyle(workbook));

        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setBold(false);
        style.setFont(font);
        return style;
    }

    private CellStyle columnHeaderStyle(SXSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(metaLabelStyle(workbook));
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setWrapText(false);
        return style;
    }

    private CellStyle textCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        applyCommonBorder(style);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setWrapText(true);

        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }

    private CellStyle amountCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(textCellStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);

        DataFormat dataFormat = workbook.createDataFormat();
        style.setDataFormat(dataFormat.getFormat("#,##0.00"));
        return style;
    }

    private CellStyle rightTextCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(textCellStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    private void applyCommonBorder(CellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
    }

    private void addTimezoneDateFormatParams(List<Object> params, String timezoneOffset) {
        for (int i = 0; i < 10; i++) {
            params.add(timezoneOffset);
        }
    }

    private void addTimezoneOffsetOnlyParams(List<Object> params, String timezoneOffset) {
        for (int i = 0; i < 5; i++) {
            params.add(timezoneOffset);
        }
    }

    private void flushSheet(Sheet sheet) throws IOException {
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

    private String normalizeCurrency(String currencyId) {
        if (currencyId == null || currencyId.isBlank()) {
            return ALL_CURRENCY;
        }

        if ("ALL".equalsIgnoreCase(currencyId.trim()) || "All".equalsIgnoreCase(currencyId.trim())) {
            return ALL_CURRENCY;
        }

        return currencyId.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeTimezoneOffset(String rawOffset) {
        if (rawOffset == null || rawOffset.isBlank()) {
            return "0000";
        }

        String normalized = rawOffset.trim().replace(":", "");
        if (normalized.matches("[+-]\\d{4}") || normalized.matches("\\d{4}")) {
            return normalized;
        }

        return "0000";
    }

    private String displayOffset(String rawOffset) {
        String normalized = normalizeTimezoneOffset(rawOffset);
        if (normalized.matches("[+-]\\d{4}")) {
            return normalized.substring(0, 3) + ":" + normalized.substring(3);
        }
        return "+" + normalized.substring(0, 2) + ":" + normalized.substring(2);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }

    private String amountOrDashText(BigDecimal value) {
        if (value == null || BigDecimal.ZERO.compareTo(value) == 0) {
            return "-";
        }

        DecimalFormat format = new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(Locale.US));
        return format.format(value);
    }

    private String buildPrintedByText(String userName, String timezoneOffset) {
        String user = safe(userName);
        String offset = normalizeTimezoneOffset(timezoneOffset);

        long millis = System.currentTimeMillis();
        int sign = offset.startsWith("-") ? -1 : 1;
        String abs = offset.replace(":", "").replace("-", "").replace("+", "");
        int hours = Integer.parseInt(abs.substring(0, 2));
        int minutes = Integer.parseInt(abs.substring(2, 4));
        long adjustedMillis = millis + sign * ((hours * 60L * 60L * 1000L) + (minutes * 60L * 1000L));
        String formatted = new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(new Date(adjustedMillis));

        return "Printed by: " + user + " on " + formatted;
    }

    private record SettlementBankRow(String createdDate,
                                     String participant,
                                     String settlementBankAccount,
                                     BigDecimal transfer,
                                     String currency,
                                     String timezoneoffset) {
    }

    private static final class MetadataCapture {
        private String createdDate = "";

        private void capture(SettlementBankRow row) {
            if (createdDate == null || createdDate.isBlank()) {
                createdDate = row.createdDate();
            }
        }

        private String createdDate() {
            return createdDate == null ? "" : createdDate;
        }
    }

    @FunctionalInterface
    private interface SettlementBankRowConsumer {
        void accept(SettlementBankRow row) throws IOException;
    }

    private static final class IOExceptionRuntimeException extends RuntimeException {
        private IOExceptionRuntimeException(IOException cause) {
            super(cause);
        }
    }
}
