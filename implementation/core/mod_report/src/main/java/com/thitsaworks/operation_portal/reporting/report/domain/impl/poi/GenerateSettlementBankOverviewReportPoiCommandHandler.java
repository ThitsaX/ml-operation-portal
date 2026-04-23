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
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementBankOverviewReportCommand;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportErrors;
import com.thitsaworks.operation_portal.reporting.report.exception.ReportException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
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
public class GenerateSettlementBankOverviewReportPoiCommandHandler
    implements GenerateSettlementBankOverviewReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateSettlementBankOverviewReportPoiCommandHandler.class);

    private static final int DEFAULT_ROW_WINDOW = 200;

    private static final int MYSQL_STREAM_FETCH_SIZE = Integer.MIN_VALUE;
    private static final float PDF_LEFT_MARGIN = 10f;
    private static final float PDF_RIGHT_MARGIN = 10f;
    private static final float PDF_TOP_MARGIN = 24f;
    private static final float PDF_BOTTOM_MARGIN = 32f;

    private static final String ALL_CURRENCY = "All";

    private static final String DEFAULT_DFSP_ID = "hub";

    private static final String[] COLUMN_HEADERS = {
        "Participant",
        "Parent Participant",
        "Settlement Bank Account",
        "Settlement Transfer",
        "Currency"
    };

    private static final int[] COLUMN_WIDTHS = {40, 40, 45, 22, 12};
    private static final float[] PDF_MAIN_COLUMN_WIDTHS = {40f, 40f, 45f, 22f, 12f};
    private static final float PDF_META_WIDTH_PERCENTAGE = (80f / 159f) * 100f;
    private static final float[] PDF_META_COLUMN_WIDTHS = {40f, 40f};

    private static final String FOOTER_NOTE =
        "The symbols '+' and '-' indicate whether funds are added to or deducted from the DFSP's liquidity balance for settlement.";

    private final JdbcTemplate jdbcTemplate;

    public GenerateSettlementBankOverviewReportPoiCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int countRows(CountInput input) {

        String currency = this.normalizeCurrency(input.currencyId());
        String dfspId = this.normalizeDfspId(input.dfspId());

        Integer rowCount = jdbcTemplate.queryForObject("""
                                                           SELECT COUNT(*) FROM (
                                                               SELECT p.name
                                                               FROM settlement s
                                                               INNER JOIN settlementSettlementWindow ssw ON ssw.settlementId = s.settlementId
                                                               INNER JOIN transferFulfilment tf ON tf.settlementWindowId = ssw.settlementWindowId
                                                               INNER JOIN transferParticipant tp ON tp.transferId = tf.transferId
                                                               INNER JOIN participantCurrency pc ON tp.participantCurrencyId = pc.participantCurrencyId
                                                               INNER JOIN participant p ON p.participantId = pc.participantId
                                                               LEFT JOIN operation_portal.tbl_participant op ON op.participant_name COLLATE UTF8MB4_UNICODE_CI = p.name COLLATE UTF8MB4_UNICODE_CI
                                                               LEFT JOIN operation_portal.tbl_participant opp ON op.parent_participant_name = opp.participant_name
                                                               LEFT JOIN operation_portal.tbl_liquidity_profile lp ON lp.currency COLLATE UTF8MB4_UNICODE_CI = pc.currencyId COLLATE UTF8MB4_UNICODE_CI AND is_active = 1 AND op.participant_id COLLATE UTF8MB4_UNICODE_CI = lp.participant_id COLLATE UTF8MB4_UNICODE_CI
                                                               INNER JOIN ledgerAccountType lat ON lat.ledgerAccountTypeId = pc.ledgerAccountTypeId
                                                               WHERE s.settlementId = ? AND lat.name = 'POSITION'
                                                                 AND (? = 'All' OR pc.currencyId COLLATE UTF8MB4_UNICODE_CI = ?)
                                                                 AND (? = 'hub' OR (op.parent_participant_name COLLATE UTF8MB4_UNICODE_CI = ? OR op.participant_name COLLATE UTF8MB4_UNICODE_CI = ?))
                                                               GROUP BY p.name, pc.participantCurrencyId,
                                                                        lp.bank_name, lp.account_name, lp.account_number,
                                                                        op.description, opp.participant_name, opp.parent_participant_name,
                                                                        op.parent_participant_name, opp.description
                                                           ) x
                                                           """,
                                                       new Object[]{
                                                           input.settlementId(),
                                                           currency,
                                                           currency,
                                                           dfspId,
                                                           dfspId,
                                                           dfspId},
                                                       Integer.class);

        return rowCount == null ? 0 : rowCount;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        String fileType = this.normalizeFileType(input.fileType());

        try {
            if ("xlsx".equalsIgnoreCase(fileType)) {
                return new Output(this.exportXlsx(input));
            }

            if ("pdf".equalsIgnoreCase(fileType)) {
                return new Output(this.exportPdf(input));
            }

            if ("csv".equalsIgnoreCase(fileType)) {
                return new Output(this.exportCsv(input));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

        } catch (ReportException e) {

            throw e;

        } catch (Exception e) {

            LOG.error("Error generating settlement bank overview report with POI", e);
            throw new ReportException(ReportErrors.SETTLEMENT_BANK_REPORT_FAILURE_EXCEPTION);
        }
    }

    private byte[] exportXlsx(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("settlement-bank-overview-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("DFSPSettlementOverviewReport");
            if (sheet instanceof SXSSFSheet streamingSheet) {
                streamingSheet.trackAllColumnsForAutoSizing();
            }

            CellStyle metaLabelStyle = this.metaLabelStyle(workbook);
            CellStyle metaValueStyle = this.metaValueStyle(workbook);
            CellStyle columnHeaderStyle = this.columnHeaderStyle(workbook);
            CellStyle textCellStyle = this.textCellStyle(workbook);
            CellStyle amountCellStyle = this.amountCellStyle(workbook);
            CellStyle rightTextCellStyle = this.rightTextCellStyle(workbook);
            CellStyle noteCellStyle = this.noteCellStyle(workbook);

            int rowIndex = 0;
            rowIndex = this.writeMeta(sheet, rowIndex, "Settlement ID", input.settlementId(), metaLabelStyle, metaValueStyle);
            rowIndex = this.writeMeta(sheet, rowIndex, "Settlement Created Date", "", metaLabelStyle, metaValueStyle);
            rowIndex = this.writeMeta(sheet, rowIndex, "Currency", this.normalizeCurrency(input.currencyId()), metaLabelStyle, metaValueStyle);
            rowIndex = this.writeMeta(sheet, rowIndex, "TimeZoneOffSet", this.displayOffset(input.timezoneOffset()), metaLabelStyle, metaValueStyle);
            rowIndex++;

            Row settlementCreatedRow = sheet.getRow(1);
            Cell settlementCreatedCell = settlementCreatedRow == null ? null : settlementCreatedRow.getCell(1);

            Row headerRow = sheet.createRow(rowIndex++);
            for (int i = 0; i < COLUMN_HEADERS.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(COLUMN_HEADERS[i]);
                cell.setCellStyle(columnHeaderStyle);
            }

            MetadataCapture metadataCapture = new MetadataCapture();
            RowCursor cursor = new RowCursor(rowIndex);

            this.streamRows(input, row -> {
                metadataCapture.capture(row);
                if (settlementCreatedCell != null && !metadataCapture.isWritten()) {
                    settlementCreatedCell.setCellValue(metadataCapture.createdDate());
                    settlementCreatedCell.setCellStyle(metaValueStyle);
                    metadataCapture.markWritten();
                }

                this.writeDataRow(sheet.createRow(cursor.next()), row, textCellStyle, amountCellStyle, rightTextCellStyle);
            });
            this.flushSheet(sheet);

            if (cursor.current() == rowIndex) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            rowIndex = cursor.current() + 2;
            Row noteRow = sheet.createRow(rowIndex);
            Cell noteCell = noteRow.createCell(0);
            noteCell.setCellValue(FOOTER_NOTE);
            noteCell.setCellStyle(noteCellStyle);
            sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex, 0, COLUMN_HEADERS.length - 1));

            for (int i = 0; i < COLUMN_WIDTHS.length; i++) {
                sheet.setColumnWidth(i, COLUMN_WIDTHS[i] * 256);
            }

            workbook.write(outputStream);
            workbook.dispose();
            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private byte[] exportPdf(Input input) throws IOException, ReportException, DocumentException {

        List<SettlementBankOverviewRow> rows = new ArrayList<>();
        MetadataCapture metadataCapture = new MetadataCapture();

        this.streamRows(input, row -> {
            rows.add(row);
            metadataCapture.capture(row);
        });

        if (rows.isEmpty()) {
            throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
        }

        Path tempFile = Files.createTempFile("settlement-bank-overview-", ".pdf");

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
            this.addPdfMetaRow(metaTable, "Settlement ID", input.settlementId(), labelFont, normalFont);
            this.addPdfMetaRow(metaTable, "Settlement Created Date", metadataCapture.createdDate(), labelFont, normalFont);
            this.addPdfMetaRow(metaTable, "Currency", this.normalizeCurrency(input.currencyId()), labelFont, normalFont);
            this.addPdfMetaRow(metaTable, "TimeZoneOffSet", this.displayOffset(input.timezoneOffset()), labelFont, normalFont);
            document.add(metaTable);

            PdfPTable mainTable = new PdfPTable(PDF_MAIN_COLUMN_WIDTHS);
            mainTable.setWidthPercentage(100);
            mainTable.setSpacingAfter(20f);
            for (String header : COLUMN_HEADERS) {
                mainTable.addCell(this.pdfCell(header, labelFont, Element.ALIGN_LEFT));
            }

            for (SettlementBankOverviewRow row : rows) {
                mainTable.addCell(this.pdfCell(this.safe(row.participant()), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(this.pdfCell(this.safe(row.parentParticipant()), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(this.pdfCell(this.safe(row.settlementBankAccount()), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(this.pdfCell(this.amountOrDashText(row.transfer()), normalFont, Element.ALIGN_RIGHT));
                mainTable.addCell(this.pdfCell(this.safe(row.currency()), normalFont, Element.ALIGN_LEFT));
            }
            document.add(mainTable);

            PdfPTable noteTable = new PdfPTable(new float[]{1f});
            noteTable.setWidthPercentage(100);
            PdfPCell noteCell = new PdfPCell(new Phrase(FOOTER_NOTE, labelFont));
            noteCell.setBorder(0);
            noteCell.setPaddingTop(6f);
            noteCell.setPaddingBottom(6f);
            noteCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            noteCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            noteTable.addCell(noteCell);
            document.add(noteTable);

            Phrase footer = new Phrase(this.buildPrintedByText(input.userName(), input.timezoneOffset()), normalFont);
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

    private byte[] exportCsv(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("settlement-bank-overview-", ".csv");

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile, StandardCharsets.UTF_8)) {

            writer.write(this.csvLine("Settlement ID", input.settlementId()));
            writer.write(this.csvLine("Settlement Created Date", ""));
            writer.write(this.csvLine("Currency", this.normalizeCurrency(input.currencyId())));
            writer.write(this.csvLine("TimeZoneOffSet", this.displayOffset(input.timezoneOffset())));
            writer.newLine();
            writer.write(this.csvLine(COLUMN_HEADERS));

            MetadataCapture metadataCapture = new MetadataCapture();
            RowCounter rowCounter = new RowCounter();
            this.streamRows(input, row -> {
                metadataCapture.capture(row);
                writer.write(this.csvLine(
                    row.participant(),
                    row.parentParticipant(),
                    row.settlementBankAccount(),
                    this.amountOrDashText(row.transfer()),
                    row.currency()));
                rowCounter.increment();
            });

            if (rowCounter.value() == 0) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            writer.write(this.csvLine("Settlement Created Date", metadataCapture.createdDate()));
            writer.newLine();
            writer.write(this.csvLine(FOOTER_NOTE));
            writer.flush();
            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private void streamRows(Input input, SettlementBankOverviewRowConsumer consumer) {

        String timezoneOffset = this.normalizeTimezoneOffset(input.timezoneOffset());
        String currency = this.normalizeCurrency(input.currencyId());
        String dfspId = this.normalizeDfspId(input.dfspId());

        List<Object> parameters = new ArrayList<>();
        this.addTimezoneDateFormatParams(parameters, timezoneOffset);
        this.addTimezoneOffsetOnlyParams(parameters, timezoneOffset);
        parameters.add(input.settlementId());
        parameters.add(currency);
        parameters.add(currency);
        parameters.add(dfspId);
        parameters.add(dfspId);
        parameters.add(dfspId);

        String sqlQuery = this.mainQuery();
        if (input.limit() != null && input.offset() != null) {
            sqlQuery += " LIMIT ? OFFSET ?";
            parameters.add(input.limit());
            parameters.add(input.offset());
        }

        final String finalSqlQuery = sqlQuery;

        this.jdbcTemplate.query(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                finalSqlQuery,
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
                    consumer.accept(this.mapRow(resultSet));

            } catch (IOException e) {

                throw new IOExceptionRuntimeException(e);

            }
        });
    }

    private SettlementBankOverviewRow mapRow(ResultSet resultSet) throws SQLException {

        return new SettlementBankOverviewRow(
            resultSet.getString("createdDate"),
            resultSet.getString("participant"),
            resultSet.getString("parentParticipant"),
            resultSet.getString("settlementBankAccount"),
            resultSet.getBigDecimal("transfer"),
            resultSet.getString("currency"),
            resultSet.getString("timezoneoffset"));
    }

    private String mainQuery() {

        return """
            SELECT CONCAT(
              DATE_FORMAT(CASE WHEN SUBSTRING(?,1,1) = '-' THEN CONVERT_TZ(s.createdDate, '+00:00', CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2)))
                     ELSE CONVERT_TZ(s.createdDate, '+00:00', CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)))
                    END,'%Y-%m-%dT%H:%i:%s'),
              CASE WHEN SUBSTRING(?,1,1) = '-' THEN CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2))
                   ELSE CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)) END
            ) AS createdDate,
            IFNULL(CONCAT(IFNULL(p.name COLLATE UTF8MB4_UNICODE_CI, ''), ' - ', IFNULL(op.description COLLATE UTF8MB4_UNICODE_CI, '')), '') AS participant,
            CASE WHEN op.parent_participant_name IS NULL THEN '' ELSE
              IFNULL(CONCAT(IFNULL(opp.participant_name COLLATE UTF8MB4_UNICODE_CI, ''), ' - ', IFNULL(opp.description COLLATE UTF8MB4_UNICODE_CI, '')), '') END AS parentParticipant,
            IFNULL(CONCAT(IFNULL(lp.bank_name, ''), ' - ', IFNULL(lp.account_name, ''), ' - ', IFNULL(lp.account_number, '')), '') AS settlementBankAccount,
            SUM(tp.amount) * -1 AS transfer,
            pc.currencyId AS currency,
            CASE WHEN SUBSTRING(?,1,1) = '-' THEN CONCAT('-', SUBSTRING(?,2,2), ':', SUBSTRING(?,4,2)) ELSE CONCAT('+', SUBSTRING(?,1,2), ':', SUBSTRING(?,3,2)) END AS timezoneoffset
            FROM settlement s
            INNER JOIN settlementSettlementWindow ssw ON ssw.settlementId = s.settlementId
            INNER JOIN transferFulfilment tf ON tf.settlementWindowId = ssw.settlementWindowId
            INNER JOIN transferParticipant tp ON tp.transferId = tf.transferId
            INNER JOIN participantCurrency pc ON tp.participantCurrencyId = pc.participantCurrencyId
            INNER JOIN participant p ON p.participantId = pc.participantId
            LEFT JOIN operation_portal.tbl_participant op ON op.participant_name COLLATE UTF8MB4_UNICODE_CI = p.name COLLATE UTF8MB4_UNICODE_CI
            LEFT JOIN operation_portal.tbl_participant opp ON op.parent_participant_name = opp.participant_name
            LEFT JOIN operation_portal.tbl_liquidity_profile lp ON lp.currency COLLATE UTF8MB4_UNICODE_CI = pc.currencyId COLLATE UTF8MB4_UNICODE_CI AND is_active = 1 AND op.participant_id COLLATE UTF8MB4_UNICODE_CI = lp.participant_id COLLATE UTF8MB4_UNICODE_CI
            INNER JOIN ledgerAccountType lat ON lat.ledgerAccountTypeId = pc.ledgerAccountTypeId
            WHERE s.settlementId = ? AND lat.name = 'POSITION'
              AND (? = 'All' OR pc.currencyId COLLATE UTF8MB4_UNICODE_CI = ?)
              AND (? = 'hub' OR (op.parent_participant_name COLLATE UTF8MB4_UNICODE_CI = ? OR op.participant_name COLLATE UTF8MB4_UNICODE_CI = ?))
            GROUP BY p.name, pc.participantCurrencyId,
                     lp.bank_name, lp.account_name, lp.account_number,
                     op.description, opp.participant_name, opp.parent_participant_name,
                     op.parent_participant_name, opp.description
            ORDER BY participant ASC, parentParticipant ASC, settlementBankAccount ASC, pc.currencyId ASC
            """;
    }

    private void writeDataRow(Row row,
                              SettlementBankOverviewRow data,
                              CellStyle textCellStyle,
                              CellStyle amountCellStyle,
                              CellStyle rightTextCellStyle) {

        this.writeTextCell(row, 0, data.participant(), textCellStyle);
        this.writeTextCell(row, 1, data.parentParticipant(), textCellStyle);
        this.writeTextCell(row, 2, data.settlementBankAccount(), textCellStyle);
        this.writeAmountOrDashCell(row, 3, data.transfer(), rightTextCellStyle, amountCellStyle);
        this.writeTextCell(row, 4, data.currency(), textCellStyle);
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
        cell.setCellValue(this.safe(value));
        cell.setCellStyle(style);
    }

    private void writeAmountCell(Row row, int col, BigDecimal value, CellStyle style) {

        Cell cell = row.createCell(col);
        if (value != null) {
            cell.setCellValue(value.doubleValue());
        } else {
            cell.setCellValue("");
        }
        cell.setCellStyle(style);
    }

    private void writeAmountOrDashCell(Row row,
                                       int col,
                                       BigDecimal value,
                                       CellStyle rightTextStyle,
                                       CellStyle amountStyle) {

        if (value == null || BigDecimal.ZERO.compareTo(value) == 0) {
            this.writeTextCell(row, col, "-", rightTextStyle);
            return;
        }

        this.writeAmountCell(row, col, value, amountStyle);
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

    private void addPdfMetaRow(PdfPTable table,
                               String label,
                               String value,
                               Font labelFont,
                               Font valueFont) {

        table.addCell(this.pdfCell(label, labelFont, Element.ALIGN_LEFT));
        table.addCell(this.pdfCell(this.safe(value), valueFont, Element.ALIGN_LEFT));
    }

    private CellStyle metaLabelStyle(SXSSFWorkbook workbook) {

        CellStyle style = workbook.createCellStyle();
        this.applyCommonBorder(style);
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(false);

        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private CellStyle metaValueStyle(SXSSFWorkbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.metaLabelStyle(workbook));

        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setBold(false);
        style.setFont(font);
        return style;
    }

    private CellStyle columnHeaderStyle(SXSSFWorkbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.cloneStyleFrom(this.metaLabelStyle(workbook));
        style.setWrapText(false);
        return style;
    }

    private CellStyle textCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        this.applyCommonBorder(style);
        style.setVerticalAlignment(VerticalAlignment.TOP);
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
        style.cloneStyleFrom(this.textCellStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00"));
        return style;
    }

    private CellStyle rightTextCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.textCellStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
        return style;
    }

    private CellStyle noteCellStyle(org.apache.poi.ss.usermodel.Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        font.setBold(true);
        style.setFont(font);
        return style;
    }

    private void applyCommonBorder(CellStyle style) {

        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
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

        if (ALL_CURRENCY.equalsIgnoreCase(currencyId.trim())) {
            return ALL_CURRENCY;
        }

        return currencyId.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeDfspId(String dfspId) {

        if (dfspId == null || dfspId.isBlank()) {
            return DEFAULT_DFSP_ID;
        }

        return dfspId.trim();
    }

    private String normalizeTimezoneOffset(String rawOffset) {

        if (rawOffset == null || rawOffset.isBlank()) {
            return "0000";
        }

        String normalized = rawOffset.trim().replace(":", "");
        if (normalized.matches("[+-]\\d{4}")) {
            return normalized;
        }
        if (normalized.matches("\\d{4}")) {
            return normalized;
        }
        return "0000";
    }

    private String displayOffset(String rawOffset) {

        String normalized = this.normalizeTimezoneOffset(rawOffset);
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

        String user = this.safe(userName);
        String offset = this.normalizeTimezoneOffset(timezoneOffset);

        long millis = System.currentTimeMillis();
        int sign = offset.startsWith("-") ? -1 : 1;
        String abs = offset.replace(":", "").replace("-", "").replace("+", "");

        int hours = Integer.parseInt(abs.substring(0, 2));
        int minutes = Integer.parseInt(abs.substring(2, 4));

        long adjustedMillis = millis + sign * ((hours * 60L * 60L * 1000L) + (minutes * 60L * 1000L));
        String formatted = new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(new Date(adjustedMillis));

        return "Printed by: " + user + " on " + formatted;
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

    private void flushSheet(Sheet sheet) throws IOException {

        if (sheet instanceof SXSSFSheet streamingSheet) {
            streamingSheet.flushRows(DEFAULT_ROW_WINDOW);
        }
    }

    private record SettlementBankOverviewRow(String createdDate,
                                             String participant,
                                             String parentParticipant,
                                             String settlementBankAccount,
                                             BigDecimal transfer,
                                             String currency,
                                             String timezoneoffset) {
    }

    private static final class MetadataCapture {

        private String createdDate = "";

        private boolean written;

        private void capture(SettlementBankOverviewRow row) {

            if (this.createdDate == null || this.createdDate.isBlank()) {
                this.createdDate = row.createdDate();
            }
        }

        private String createdDate() {

            return this.createdDate == null ? "" : this.createdDate;
        }

        private boolean isWritten() {

            return this.written;
        }

        private void markWritten() {

            this.written = true;
        }
    }

    @FunctionalInterface
    private interface SettlementBankOverviewRowConsumer {

        void accept(SettlementBankOverviewRow row) throws IOException;
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
