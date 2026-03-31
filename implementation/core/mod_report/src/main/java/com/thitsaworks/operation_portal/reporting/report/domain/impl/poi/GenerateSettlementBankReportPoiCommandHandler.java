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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Primary
@NoLogging
public class GenerateSettlementBankReportPoiCommandHandler implements GenerateSettlementBankReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateSettlementBankReportPoiCommandHandler.class);

    private static final int DEFAULT_ROW_WINDOW = 200;

    private static final int DEFAULT_LIMIT = Integer.MAX_VALUE;

    private static final int MYSQL_STREAM_FETCH_SIZE = Integer.MIN_VALUE;

    private static final String[] COLUMN_HEADERS = {
        "Participant",
        "Parent Participant",
        "Settlement Bank Account",
        "Settlement Transfer",
        "Currency"
    };

    private static final String[] NET_SUMMARY_HEADERS = {
        "Settlement Bank Account", "Settlement Transfer", "Currency"
    };

    private final JdbcTemplate jdbcTemplate;

    public GenerateSettlementBankReportPoiCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        String fileType = this.normalizeFileType(input.fileType());

        try {
            if ("xlsx".equalsIgnoreCase(fileType)) {
                return new Output(this.exportSingleChunkXlsx(input));
            }

            if ("pdf".equalsIgnoreCase(fileType)) {
                return new Output(this.exportPdf(input));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

        } catch (ReportException e) {

            throw e;

        } catch (Exception e) {

            LOG.error("Error generating settlement bank report with POI", e);
            throw new ReportException(ReportErrors.SETTLEMENT_BANK_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public int countRows(CountInput input) {

        Integer rowCount = this.jdbcTemplate.queryForObject(
            """
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
                    GROUP BY p.name, pc.participantCurrencyId, lp.bank_name, lp.account_name, lp.account_number,
                             op.description, opp.participant_name, opp.parent_participant_name,
                             op.parent_participant_name, opp.description
                ) x
                """,
            new Object[]{
                input.settlementId(),
                this.normalizeCurrency(input.currencyId()),
                this.normalizeCurrency(input.currencyId())
            },
            Integer.class);

        return rowCount == null ? 0 : rowCount;
    }

    private byte[] exportSingleChunkXlsx(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("settlement-bank-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);
            Sheet sheet = workbook.createSheet("SettlementBankReport");
            if (sheet instanceof SXSSFSheet streamingSheet) {
                streamingSheet.trackAllColumnsForAutoSizing();
            }

            CellStyle metaLabelStyle = this.metaLabelStyle(workbook);
            CellStyle metaValueStyle = this.metaValueStyle(workbook);
            CellStyle columnHeaderStyle = this.columnHeaderStyle(workbook);
            CellStyle textCellStyle = this.textCellStyle(workbook);
            CellStyle amountCellStyle = this.amountCellStyle(workbook);
            CellStyle rightTextCellStyle = this.rightTextCellStyle(workbook);

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
            RowCounter rowCounter = new RowCounter();
            RowCursor cursor = new RowCursor(rowIndex);
            Map<NetSummaryKey, BigDecimal> netSummary = new LinkedHashMap<>();

            this.streamRows(input, row -> {
                metadataCapture.capture(row);
                if (settlementCreatedCell != null && !metadataCapture.isWritten()) {
                    settlementCreatedCell.setCellValue(metadataCapture.createdDate());
                    settlementCreatedCell.setCellStyle(metaValueStyle);
                    metadataCapture.markWritten();
                }

                this.writeDataRow(sheet.createRow(cursor.next()), row, textCellStyle, amountCellStyle, rightTextCellStyle);
                this.aggregateNetSummary(netSummary, row);
                rowCounter.increment();
            });

            this.flushSheet(sheet);

            if (rowCounter.value() == 0) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            rowIndex = cursor.current();
            rowIndex++;

            rowIndex = this.writeSummaryTitle(sheet, rowIndex, "Net Summary", metaLabelStyle, metaValueStyle);
            rowIndex = this.writeSummaryHeader(sheet, rowIndex, columnHeaderStyle);
            this.writeSummaryRows(sheet, rowIndex, netSummary, textCellStyle, amountCellStyle, rightTextCellStyle);

            for (int i = 0; i < COLUMN_HEADERS.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(outputStream);
            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private byte[] exportPdf(Input input) throws IOException, ReportException, DocumentException {

        List<SettlementBankRow> rows = new ArrayList<>();
        MetadataCapture metadataCapture = new MetadataCapture();
        Map<NetSummaryKey, BigDecimal> netSummary = new LinkedHashMap<>();

        this.streamRows(input, row -> {
            rows.add(row);
            metadataCapture.capture(row);
            this.aggregateNetSummary(netSummary, row);
        });

        if (rows.isEmpty()) {
            throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
        }

        Path tempFile = Files.createTempFile("settlement-bank-", ".pdf");

        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 11);

        try (OutputStream outputStream = Files.newOutputStream(tempFile)) {
            Document document = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            PdfPTable metaTable = new PdfPTable(new float[]{2f, 3f});
            metaTable.setWidthPercentage(52);
            metaTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            metaTable.setSpacingAfter(16f);
            this.addPdfMetaRow(metaTable, "Settlement ID", input.settlementId(), labelFont, normalFont);
            this.addPdfMetaRow(metaTable, "Settlement Created Date", metadataCapture.createdDate(), labelFont, normalFont);
            this.addPdfMetaRow(metaTable, "Currency", this.normalizeCurrency(input.currencyId()), labelFont, normalFont);
            this.addPdfMetaRow(metaTable, "TimeZoneOffSet", this.displayOffset(input.timezoneOffset()), labelFont, normalFont);
            document.add(metaTable);

            PdfPTable mainTable = new PdfPTable(new float[]{3f, 3f, 3.0f, 1.6f, 1.2f});
            mainTable.setWidthPercentage(100);
            mainTable.setSpacingAfter(14f);
            for (String header : COLUMN_HEADERS) {
                mainTable.addCell(this.pdfCell(header, labelFont, Element.ALIGN_LEFT));
            }

            for (SettlementBankRow row : rows) {
                mainTable.addCell(this.pdfCell(this.safe(row.participant()), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(this.pdfCell(this.safe(row.parentParticipant()), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(this.pdfCell(this.safe(row.settlementBankAccount()), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(this.pdfCell(this.amountOrDashText(row.transfer()), normalFont, Element.ALIGN_RIGHT));
                mainTable.addCell(this.pdfCell(this.safe(row.currency()), normalFont, Element.ALIGN_LEFT));
            }
            document.add(mainTable);

            PdfPTable summaryTitleTable = new PdfPTable(new float[]{3f, 3f, 3f});
            summaryTitleTable.setWidthPercentage(75);
            summaryTitleTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            summaryTitleTable.setSpacingAfter(0f);
            summaryTitleTable.addCell(this.pdfCell("Net Summary", labelFont, Element.ALIGN_LEFT));
            summaryTitleTable.addCell(this.pdfCell("", normalFont, Element.ALIGN_LEFT));
            summaryTitleTable.addCell(this.pdfCell("", normalFont, Element.ALIGN_LEFT));
            document.add(summaryTitleTable);

            PdfPTable summaryTable = new PdfPTable(new float[]{3f, 3f, 3f});
            summaryTable.setWidthPercentage(75);
            summaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            summaryTable.setSpacingBefore(0f);
            for (String header : NET_SUMMARY_HEADERS) {
                summaryTable.addCell(this.pdfCell(header, labelFont, Element.ALIGN_LEFT));
            }

            for (Map.Entry<NetSummaryKey, BigDecimal> entry : netSummary.entrySet()) {
                summaryTable.addCell(this.pdfCell(this.safe(entry.getKey().settlementBankAccount()), normalFont, Element.ALIGN_LEFT));
                summaryTable.addCell(this.pdfCell(this.amountOrDashText(entry.getValue()), normalFont, Element.ALIGN_RIGHT));
                summaryTable.addCell(this.pdfCell(this.safe(entry.getKey().currency()), normalFont, Element.ALIGN_LEFT));
            }
            document.add(summaryTable);
            document.close();

            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
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

    private void streamRows(Input input, SettlementBankRowConsumer consumer) {

        String timezoneOffset = this.normalizeTimezoneOffset(input.timezoneOffset());
        List<Object> parameters = new ArrayList<>();
        this.addTimezoneDateFormatParams(parameters, timezoneOffset);
        this.addTimezoneOffsetOnlyParams(parameters, timezoneOffset);
        parameters.add(input.settlementId());
        parameters.add(this.normalizeCurrency(input.currencyId()));
        parameters.add(this.normalizeCurrency(input.currencyId()));
        parameters.add(input.offset() == null ? 0 : input.offset());
        parameters.add(input.limit() == null ? DEFAULT_LIMIT : input.limit());

        this.jdbcTemplate.query(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                this.mainQuery(),
                ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY);
            statement.setFetchDirection(ResultSet.FETCH_FORWARD);
            statement.setFetchSize(MYSQL_STREAM_FETCH_SIZE);
            for (int i = 0; i < parameters.size(); i++) {
                statement.setObject(i + 1, parameters.get(i));
            }
            return statement;
        }, resultSet -> {
            while (resultSet.next()) {
                try {
                    consumer.accept(this.mapRow(resultSet));
                } catch (IOException e) {
                    throw new IOExceptionRuntimeException(e);
                }
            }
        });
    }

    private SettlementBankRow mapRow(ResultSet rs) throws SQLException {

        return new SettlementBankRow(
            rs.getString("createdDate"),
            rs.getString("participant"),
            rs.getString("parentParticipant"),
            rs.getString("settlementBankAccount"),
            rs.getBigDecimal("transfer"),
            rs.getString("currency"),
            rs.getString("timezoneoffset"));
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
            IFNULL(CONCAT(IFNULL(p.name COLLATE UTF8MB4_UNICODE_CI , ''), ' - ', IFNULL(op.description COLLATE UTF8MB4_UNICODE_CI , '')),'') AS participant,
            CASE WHEN op.parent_participant_name IS NULL THEN '' ELSE
              IFNULL(CONCAT(IFNULL(opp.participant_name COLLATE UTF8MB4_UNICODE_CI , ''), ' - ', IFNULL(opp.description COLLATE UTF8MB4_UNICODE_CI , '')),'') END AS parentParticipant,
            IFNULL(CONCAT(IFNULL(lp.bank_name, ''), ' - ', IFNULL(lp.account_name, ''), ' - ', IFNULL(lp.account_number, '')),'') AS settlementBankAccount,
            SUM(tp.amount) AS transfer,
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
            GROUP BY p.name, pc.participantCurrencyId,
                     lp.bank_name, lp.account_name, lp.account_number,
                     op.description, opp.participant_name, opp.parent_participant_name,
                     op.parent_participant_name, opp.description
            ORDER BY p.name ASC, settlementBankAccount ASC, pc.currencyId ASC
            LIMIT ?, ?
            """;
    }

    private void aggregateNetSummary(Map<NetSummaryKey, BigDecimal> summary, SettlementBankRow row) {

        NetSummaryKey key = new NetSummaryKey(row.settlementBankAccount(), row.currency());
        BigDecimal current = summary.getOrDefault(key, BigDecimal.ZERO);
        BigDecimal value = row.transfer() == null ? BigDecimal.ZERO : row.transfer();
        summary.put(key, current.add(value));
    }

    private int writeSummaryTitle(Sheet sheet,
                                  int rowIndex,
                                  String title,
                                  CellStyle titleStyle,
                                  CellStyle valueStyle) {

        Row row = sheet.createRow(rowIndex++);
        Cell titleCell = row.createCell(0);
        titleCell.setCellValue(title);
        titleCell.setCellStyle(titleStyle);

        Cell filler = row.createCell(1);
        filler.setCellValue("");
        filler.setCellStyle(valueStyle);

        Cell filler2 = row.createCell(2);
        filler2.setCellValue("");
        filler2.setCellStyle(valueStyle);

        return rowIndex;
    }

    private int writeSummaryHeader(Sheet sheet, int rowIndex, CellStyle headerStyle) {

        Row row = sheet.createRow(rowIndex++);
        for (int i = 0; i < NET_SUMMARY_HEADERS.length; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(NET_SUMMARY_HEADERS[i]);
            cell.setCellStyle(headerStyle);
        }

        return rowIndex;
    }

    private int writeSummaryRows(Sheet sheet,
                                 int rowIndex,
                                 Map<NetSummaryKey, BigDecimal> summary,
                                 CellStyle textStyle,
                                 CellStyle amountStyle,
                                 CellStyle rightTextStyle) {

        for (Map.Entry<NetSummaryKey, BigDecimal> entry : summary.entrySet()) {
            Row row = sheet.createRow(rowIndex++);

            this.writeTextCell(row, 0, entry.getKey().settlementBankAccount(), textStyle);
            this.writeAmountOrDashCell(row, 1, entry.getValue(), rightTextStyle, amountStyle);
            this.writeTextCell(row, 2, entry.getKey().currency(), rightTextStyle);
        }

        return rowIndex;
    }

    private void writeDataRow(Row row,
                              SettlementBankRow data,
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

    private int writeMeta(Sheet s, int i, String key, String value, CellStyle keyStyle, CellStyle valueStyle) {

        Row r = s.createRow(i++);
        Cell kc = r.createCell(0);
        kc.setCellValue(key);
        kc.setCellStyle(keyStyle);

        Cell vc = r.createCell(1);
        vc.setCellValue(value == null ? "" : value);
        vc.setCellStyle(valueStyle);
        return i;
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

    private CellStyle metaLabelStyle(SXSSFWorkbook wb) {

        CellStyle s = wb.createCellStyle();
        this.applyCommonBorder(s);
        s.setAlignment(HorizontalAlignment.LEFT);
        s.setVerticalAlignment(VerticalAlignment.CENTER);
        s.setWrapText(false);
        var f = wb.createFont();
        f.setFontName("Calibri");
        f.setFontHeightInPoints((short) 11);
        f.setBold(true);
        s.setFont(f);
        return s;
    }

    private CellStyle metaValueStyle(SXSSFWorkbook wb) {

        CellStyle s = wb.createCellStyle();
        s.cloneStyleFrom(this.metaLabelStyle(wb));
        var f = wb.createFont();
        f.setFontName("Calibri");
        f.setFontHeightInPoints((short) 11);
        f.setBold(false);
        s.setFont(f);
        return s;
    }

    private CellStyle columnHeaderStyle(SXSSFWorkbook wb) {

        CellStyle s = wb.createCellStyle();
        s.cloneStyleFrom(this.metaLabelStyle(wb));
        s.setWrapText(false);
        return s;
    }

    private CellStyle textCellStyle(org.apache.poi.ss.usermodel.Workbook wb) {

        CellStyle s = wb.createCellStyle();
        this.applyCommonBorder(s);
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
        s.cloneStyleFrom(this.textCellStyle(wb));
        s.setAlignment(HorizontalAlignment.RIGHT);
        s.setDataFormat(wb.createDataFormat().getFormat("#,##0.00"));
        return s;
    }

    private CellStyle rightTextCellStyle(org.apache.poi.ss.usermodel.Workbook wb) {

        CellStyle s = wb.createCellStyle();
        s.cloneStyleFrom(this.textCellStyle(wb));
        s.setAlignment(HorizontalAlignment.RIGHT);
        return s;
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
            return "ALL";
        }

        if ("all".equalsIgnoreCase(currencyId.trim())) {
            return "ALL";
        }

        return currencyId.trim().toUpperCase(Locale.ROOT);
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

    private void flushSheet(Sheet sheet) throws IOException {

        if (sheet instanceof SXSSFSheet streamingSheet) {
            streamingSheet.flushRows(DEFAULT_ROW_WINDOW);
        }
    }

    private record SettlementBankRow(String createdDate,
                                     String participant,
                                     String parentParticipant,
                                     String settlementBankAccount,
                                     BigDecimal transfer,
                                     String currency,
                                     String timezoneoffset) {
    }

    private record NetSummaryKey(String settlementBankAccount, String currency) {
    }

    private static final class MetadataCapture {

        private String createdDate = "";

        private boolean written;

        private void capture(SettlementBankRow row) {

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
    private interface SettlementBankRowConsumer {

        void accept(SettlementBankRow row) throws IOException;
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
