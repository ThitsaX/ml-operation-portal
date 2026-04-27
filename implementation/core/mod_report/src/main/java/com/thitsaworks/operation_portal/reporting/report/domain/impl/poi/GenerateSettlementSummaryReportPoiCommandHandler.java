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
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Service
@Primary
@NoLogging
public class GenerateSettlementSummaryReportPoiCommandHandler implements GenerateSettlementReportCommand {

    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateSettlementSummaryReportPoiCommandHandler.class);

    private static final int DEFAULT_ROW_WINDOW = 200;

    private static final int DEFAULT_LIMIT = Integer.MAX_VALUE;

    private static final int MYSQL_STREAM_FETCH_SIZE = Integer.MIN_VALUE;
    private static final float[] MAIN_TABLE_RELATIVE_WIDTHS =
        {2.8f, 3f, 1.2f, 1.8f, 1.2f, 1.8f, 1.5f, 1.8f, 1.8f, 1.2f};

    private static final String[] COLUMN_HEADERS_ROW1 = {
        "DFSP ID",
        "DFSP Name",
        "Sent to FSP", null,
        "Received from FSP", null,
        "Total Transaction Volume",
        "Total Value of All Transactions",
        "Net Position vs. Each DFSP",
        "Currency"
    };

    private static final String[] COLUMN_HEADERS_ROW2 = {
        null, null,
        "Volume", "Value",
        "Volume", "Value",
        null, null, null, null
    };

    private static final int[] COLUMN_WIDTHS = {(int) 29.36, (int) 29.36, (int) 15.36, (int) 17.36, (int) 15.36, (int) 17.36, (int) 21.36, (int) 23.36, (int) 23.36, (int) 11.36};

    private static final DateTimeFormatter HEADER_DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");

    private final JdbcTemplate jdbcTemplate;

    public GenerateSettlementSummaryReportPoiCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        try {
            String fileType = this.normalizeFileType(input.filetype());

            Input normalizedInput = new Input(
                input.fspId(), input.fspName(), input.settlementId(), fileType,
                input.timezoneOffset(), input.userName(), input.offset(), input.limit());

            if ("xlsx".equalsIgnoreCase(fileType)) {
                return new Output(this.exportSingleChunkXlsx(normalizedInput));
            }

            if ("pdf".equalsIgnoreCase(fileType)) {
                return new Output(this.exportPdf(normalizedInput));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

        } catch (ReportException exception) {
            throw exception;
        } catch (Exception exception) {
            LOG.error("Error generating settlement summary report", exception);
            throw new ReportException(ReportErrors.SETTLEMENT_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public Output exportAll(Input input, int totalRowCount, int pageSize) throws ReportException {

        if (totalRowCount <= 0) {
            throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
        }

        try {
            String fileType = this.normalizeFileType(input.filetype());

            Input normalizedInput = new Input(
                input.fspId(), input.fspName(), input.settlementId(), fileType,
                input.timezoneOffset(), input.userName(), input.offset(), input.limit());

            if ("xlsx".equalsIgnoreCase(fileType)) {
                return new Output(this.exportAllXlsx(normalizedInput, totalRowCount, pageSize));
            }

            if ("pdf".equalsIgnoreCase(fileType)) {
                return new Output(this.exportPdf(normalizedInput));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

        } catch (ReportException exception) {
            throw exception;
        } catch (Exception exception) {
            LOG.error("Error generating full settlement summary report", exception);
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

    private byte[] exportPdf(Input input) throws Exception {

        List<SettlementSummaryRow> rows = new ArrayList<>();
        this.streamRows(input, rows::add);

        if (rows.isEmpty()) {
            throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
        }

        List<SettlementSummaryNetPositionRow> summaryRows = new ArrayList<>();
        Input summaryInput = new Input(
            input.fspId(), input.fspName(), input.settlementId(), input.filetype(),
            input.timezoneOffset(), input.userName(), 0, DEFAULT_LIMIT);
        this.streamSummaryRows(summaryInput, summaryRows::add);

        String settlementCreatedDate = this.loadSettlementCreatedDate(
            input.settlementId(), input.timezoneOffset());

        Path tempFile = Files.createTempFile("settlement-summary-", ".pdf");

        Font labelFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 9);
        Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 9);

        try (OutputStream outputStream = Files.newOutputStream(tempFile)) {
            Document document = new Document(PageSize.A4.rotate(), 10, 10, 10, 10);
            PdfWriter.getInstance(document, outputStream);
            document.open();
            float pageContentWidth = document.right() - document.left();
            float[] mainTableAbsoluteWidths = this.scaleToTotal(MAIN_TABLE_RELATIVE_WIDTHS, pageContentWidth);
            float[] leftBlockAbsoluteWidths = new float[]{mainTableAbsoluteWidths[0], mainTableAbsoluteWidths[1]};

            PdfPTable metaTable = new PdfPTable(2);
            metaTable.setTotalWidth(leftBlockAbsoluteWidths);
            metaTable.setLockedWidth(true);
            metaTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            metaTable.setSpacingAfter(15f);

            this.addPdfMetaRow(metaTable, "Settlement ID", input.settlementId(), labelFont, normalFont);
            this.addPdfMetaRow(metaTable, "Settlement Created Date", settlementCreatedDate, labelFont, normalFont);
            this.addPdfMetaRow(metaTable, "DFSP ID", input.fspId(), labelFont, normalFont);
            this.addPdfMetaRow(metaTable, "DFSP Name", input.fspName(), labelFont, normalFont);
            this.addPdfMetaRow(metaTable, "TimeZoneOffSet", this.displayOffset(input.timezoneOffset()), labelFont, normalFont);
            document.add(metaTable);

            PdfPTable mainTable = new PdfPTable(10);
            mainTable.setTotalWidth(mainTableAbsoluteWidths);
            mainTable.setLockedWidth(true);

            // Row 1 Headers
            mainTable.addCell(this.pdfCell("DFSP ID", labelFont, Element.ALIGN_CENTER, 1, 2));
            mainTable.addCell(this.pdfCell("DFSP Name", labelFont, Element.ALIGN_CENTER, 1, 2));
            mainTable.addCell(this.pdfCell("Sent to FSP", labelFont, Element.ALIGN_CENTER, 2, 1));
            mainTable.addCell(this.pdfCell("Received from FSP", labelFont, Element.ALIGN_CENTER, 2, 1));
            mainTable.addCell(this.pdfCell("Total Transaction Volume", labelFont, Element.ALIGN_CENTER, 1, 2));
            mainTable.addCell(this.pdfCell("Total Value of All Transactions", labelFont, Element.ALIGN_CENTER, 1, 2));
            mainTable.addCell(this.pdfCell("Net Position vs. Each DFSP", labelFont, Element.ALIGN_CENTER, 1, 2));
            mainTable.addCell(this.pdfCell("Currency", labelFont, Element.ALIGN_CENTER, 1, 2));

            // Row 2 Headers
            mainTable.addCell(this.pdfCell("Volume", labelFont, Element.ALIGN_CENTER));
            mainTable.addCell(this.pdfCell("Value", labelFont, Element.ALIGN_CENTER));
            mainTable.addCell(this.pdfCell("Volume", labelFont, Element.ALIGN_CENTER));
            mainTable.addCell(this.pdfCell("Value", labelFont, Element.ALIGN_CENTER));

            for (SettlementSummaryRow row : rows) {
                mainTable.addCell(this.pdfCell(row.participantId(), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(this.pdfCell(row.dfspName(), normalFont, Element.ALIGN_LEFT));
                mainTable.addCell(this.pdfCell(this.formatCount(row.sentVolume()), normalFont, Element.ALIGN_RIGHT));
                mainTable.addCell(this.pdfCell(this.formatAmount(row.sentAmount()), normalFont, Element.ALIGN_RIGHT));
                mainTable.addCell(this.pdfCell(this.formatCount(row.receivedVolume()), normalFont, Element.ALIGN_RIGHT));
                mainTable.addCell(this.pdfCell(this.formatAmount(row.receivedAmount()), normalFont, Element.ALIGN_RIGHT));
                mainTable.addCell(this.pdfCell(this.formatCount(row.totalVolume()), normalFont, Element.ALIGN_RIGHT));
                mainTable.addCell(this.pdfCell(this.formatAmount(row.totalAmount()), normalFont, Element.ALIGN_RIGHT));
                mainTable.addCell(this.pdfCell(this.formatAmount(row.netAmount()), normalFont, Element.ALIGN_RIGHT));
                mainTable.addCell(this.pdfCell(row.currencyId(), normalFont, Element.ALIGN_LEFT));
            }
            document.add(mainTable);

            // Aggregated Net Positions
            PdfPTable summaryTable = new PdfPTable(2);
            summaryTable.setTotalWidth(leftBlockAbsoluteWidths);
            summaryTable.setLockedWidth(true);
            summaryTable.setHorizontalAlignment(Element.ALIGN_LEFT);
            summaryTable.setSpacingBefore(15f);
            summaryTable.addCell(this.pdfCell("Aggregated Net Positions", labelFont, Element.ALIGN_LEFT));
            summaryTable.addCell(this.pdfCell("", normalFont, Element.ALIGN_LEFT));

            for (SettlementSummaryNetPositionRow row : summaryRows) {
                summaryTable.addCell(this.pdfCell(row.currencyId(), normalFont, Element.ALIGN_LEFT));
                summaryTable.addCell(this.pdfCell(this.formatAmount(row.netPositionAmount()), normalFont, Element.ALIGN_RIGHT));
            }
            document.add(summaryTable);

            document.close();
            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private PdfPCell pdfCell(String text, Font font, int horizontalAlignment) {

        return this.pdfCell(text, font, horizontalAlignment, 1, 1);
    }

    private PdfPCell pdfCell(String text,
                             Font font,
                             int horizontalAlignment,
                             int colspan,
                             int rowspan) {

        PdfPCell cell = new PdfPCell(new Phrase(text == null ? "" : text, font));
        cell.setBorderWidth(0.5f);
        cell.setPadding(4f);
        cell.setHorizontalAlignment(horizontalAlignment);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setColspan(colspan);
        cell.setRowspan(rowspan);
        return cell;
    }

    private float[] scaleToTotal(float[] relativeWidths, float totalWidth) {

        float sum = 0f;
        for (float width : relativeWidths) {
            sum += width;
        }
        float[] absolute = new float[relativeWidths.length];
        for (int i = 0; i < relativeWidths.length; i++) {
            absolute[i] = (relativeWidths[i] / sum) * totalWidth;
        }
        return absolute;
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
        DecimalFormat format = new DecimalFormat("#,##0.00;(#,##0.00)", DecimalFormatSymbols.getInstance(Locale.US));
        return format.format(amount);
    }

    private String formatCount(BigDecimal count) {

        if (count == null) {
            return "0";
        }
        DecimalFormat format = new DecimalFormat("#,##0", DecimalFormatSymbols.getInstance(Locale.US));
        return format.format(count);
    }

    private byte[] exportSingleChunkXlsx(Input input) throws IOException, ReportException {

        Path tempFile = Files.createTempFile("settlement-summary-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);

            Sheet sheet = workbook.createSheet("DFSPSettlementReport");
            this.trackColumns(sheet);

            CellStyle labelStyle = this.labelStyle(workbook);
            CellStyle valueStyle = this.valueStyle(workbook);
            CellStyle headerStyle = this.headerStyle(workbook);
            CellStyle textStyle = this.textStyle(workbook);
            CellStyle amountStyle = this.amountStyle(workbook);
            CellStyle volumeStyle = this.volumeStyle(workbook);

            String settlementCreatedDate = this.loadSettlementCreatedDate(
                input.settlementId(), input.timezoneOffset());

            int rowIndex = this.writeMetaBlock(sheet, input, settlementCreatedDate,
                                                labelStyle, valueStyle);
            rowIndex++;

            int freezeRow = this.writeColumnHeaders(sheet, rowIndex, headerStyle);
            rowIndex = freezeRow;

            RowCursor rowCursor = new RowCursor(rowIndex);
            this.streamRows(input, row -> this.writeDataRow(
                sheet.createRow(rowCursor.next()), row, textStyle, amountStyle, volumeStyle));

            if (rowCursor.current() == rowIndex) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            this.flush(sheet);

            // Aggregated Net Positions appended at bottom of same sheet
            int netRow = rowCursor.current();
            netRow++; // blank row separator

            Input summaryInput = new Input(
                input.fspId(), input.fspName(), input.settlementId(), input.filetype(),
                input.timezoneOffset(), input.userName(), 0, DEFAULT_LIMIT);
            netRow = this.writeNetPositionBlock(
                sheet, netRow, summaryInput, labelStyle, headerStyle, textStyle, amountStyle);

            this.flush(sheet);
            this.applyColumnWidths(sheet);
            sheet.createFreezePane(0, freezeRow);

            workbook.write(outputStream);
            workbook.dispose();
            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private byte[] exportAllXlsx(Input input, int totalRowCount, int pageSize)
        throws IOException, ReportException {

        Path tempFile = Files.createTempFile("settlement-summary-", ".xlsx");

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);

            Sheet sheet = workbook.createSheet("DFSPSettlementReport");
            this.trackColumns(sheet);

            CellStyle labelStyle = this.labelStyle(workbook);
            CellStyle valueStyle = this.valueStyle(workbook);
            CellStyle headerStyle = this.headerStyle(workbook);
            CellStyle textStyle = this.textStyle(workbook);
            CellStyle amountStyle = this.amountStyle(workbook);
            CellStyle volumeStyle = this.volumeStyle(workbook);

            String settlementCreatedDate = this.loadSettlementCreatedDate(
                input.settlementId(), input.timezoneOffset());

            int rowIndex = this.writeMetaBlock(sheet, input, settlementCreatedDate,
                                                labelStyle, valueStyle);
            rowIndex++;

            int freezeRow = this.writeColumnHeaders(sheet, rowIndex, headerStyle);
            rowIndex = freezeRow;

            RowCursor rowCursor = new RowCursor(rowIndex);
            for (int offset = 0; offset < totalRowCount; offset += pageSize) {
                int limit = Math.min(pageSize, totalRowCount - offset);
                Input chunkInput = new Input(
                    input.fspId(), input.fspName(), input.settlementId(), input.filetype(),
                    input.timezoneOffset(), input.userName(), offset, limit);

                this.streamRows(chunkInput, row -> this.writeDataRow(
                    sheet.createRow(rowCursor.next()), row, textStyle, amountStyle, volumeStyle));
                this.flush(sheet);
            }

            if (rowCursor.current() == rowIndex) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            // Aggregated Net Positions appended at bottom of same sheet
            int netRow = rowCursor.current();
            netRow++; // blank row separator

            Input summaryInput = new Input(
                input.fspId(), input.fspName(), input.settlementId(), input.filetype(),
                input.timezoneOffset(), input.userName(), 0, DEFAULT_LIMIT);
            netRow = this.writeNetPositionBlock(
                sheet, netRow, summaryInput, labelStyle, headerStyle, textStyle, amountStyle);

            this.flush(sheet);
            this.applyColumnWidths(sheet);
            sheet.createFreezePane(0, freezeRow);

            workbook.write(outputStream);
            workbook.dispose();
            return Files.readAllBytes(tempFile);

        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private int writeColumnHeaders(Sheet sheet, int startRow, CellStyle headerStyle) {

        Row row1 = sheet.createRow(startRow);
        Row row2 = sheet.createRow(startRow + 1);

        for (int col = 0; col < COLUMN_HEADERS_ROW1.length; col++) {
            Cell cell1 = row1.createCell(col);
            cell1.setCellStyle(headerStyle);
            if (COLUMN_HEADERS_ROW1[col] != null) {
                cell1.setCellValue(COLUMN_HEADERS_ROW1[col]);
            }

            Cell cell2 = row2.createCell(col);
            cell2.setCellStyle(headerStyle);
            if (COLUMN_HEADERS_ROW2[col] != null) {
                cell2.setCellValue(COLUMN_HEADERS_ROW2[col]);
            }
        }

        // Merge "DFSP ID" (rows)
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, 0, 0));
        // Merge "DFSP Name" (rows)
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, 1, 1));
        // Merge "Sent to FSP" (cols 2-3)
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 2, 3));
        // Merge "Received from FSP" (cols 4-5)
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow, 4, 5));
        // Merge "Total Transaction Volume" (rows)
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, 6, 6));
        // Merge "Total Value of All Transactions" (rows)
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, 7, 7));
        // Merge "Net Position vs. Each DFSP" (rows)
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, 8, 8));
        // Merge "Currency" (rows)
        sheet.addMergedRegion(new CellRangeAddress(startRow, startRow + 1, 9, 9));

        return startRow + 2;
    }


    private int writeNetPositionBlock(Sheet sheet,
                                       int startRow,
                                       Input summaryInput,
                                       CellStyle labelStyle,
                                       CellStyle headerStyle,
                                       CellStyle textStyle,
                                       CellStyle amountStyle) {

        Row titleRow = sheet.createRow(startRow);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Aggregated Net Positions");
        titleCell.setCellStyle(labelStyle);
        
        // Add right field cell with bold border style even when empty
        Cell rightFieldCell = titleRow.createCell(1);
        rightFieldCell.setCellValue("");
        rightFieldCell.setCellStyle(labelStyle);
        
        int rowIndex = startRow + 1;

        RowCursor cursor = new RowCursor(rowIndex);
        this.streamSummaryRows(summaryInput, row -> {
            Row dataRow = sheet.createRow(cursor.next());
            this.writeTextCell(dataRow, 0, row.currencyId(), textStyle);
            this.writeNumberCell(dataRow, 1, row.netPositionAmount(), amountStyle);
        });

        return cursor.current();
    }


    private void streamRows(Input input, SettlementSummaryRowConsumer consumer) {

        List<Object> params = List.of(
            input.fspId(), input.fspId(), input.fspId(), input.fspId(), input.fspId(),
            input.settlementId(), input.fspId(), input.fspId(),
            input.limit() == null ? DEFAULT_LIMIT : input.limit(),
            input.offset() == null ? 0 : input.offset());

        String query = this.mainQuery();

        try {
            this.jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                    query,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
                statement.setFetchDirection(ResultSet.FETCH_FORWARD);
                statement.setFetchSize(MYSQL_STREAM_FETCH_SIZE);
                for (int index = 0; index < params.size(); index++) {
                    statement.setObject(index + 1, params.get(index));
                }
                return statement;
            }, resultSet -> {

                    consumer.accept(this.mapRow(resultSet));
            });
        } catch (IOExceptionRuntimeException exception) {
            throw exception;
        }
    }

    private void streamSummaryRows(Input input, SettlementSummaryNetPositionConsumer consumer) {

        List<Object> params = List.of(
            input.fspId(), input.fspId(), input.settlementId(), input.fspId(), input.fspId(),
            input.limit() == null ? DEFAULT_LIMIT : input.limit(),
            input.offset() == null ? 0 : input.offset());

        String query = this.summaryQuery();

        try {
            this.jdbcTemplate.query(connection -> {
                PreparedStatement statement = connection.prepareStatement(
                    query,
                    ResultSet.TYPE_FORWARD_ONLY,
                    ResultSet.CONCUR_READ_ONLY);
                statement.setFetchDirection(ResultSet.FETCH_FORWARD);
                statement.setFetchSize(MYSQL_STREAM_FETCH_SIZE);
                for (int index = 0; index < params.size(); index++) {
                    statement.setObject(index + 1, params.get(index));
                }
                return statement;
            }, resultSet -> {
                while (resultSet.next()) {
                    consumer.accept(this.mapSummaryRow(resultSet));
                }
                return null;
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
            SELECT settlementId,
                   p.name AS participantId,
                   IFNULL(op.description, p.name) AS dfspName,
                   s3.currencyId,
                   s3.currencyScale,
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
                       MAX(currencyScale) AS currencyScale,
                       participantId,
                       SUM(sentAmount) AS sentAmount,
                       SUM(sentVolume) AS sentVolume,
                       SUM(receivedAmount) AS receivedAmount,
                       SUM(receivedVolume) AS receivedVolume
                FROM (
                    SELECT settlementId,
                           MAX(currencyId) AS currencyId,
                           MAX(currencyScale) AS currencyScale,
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
                               MAX(c.scale) AS currencyScale,
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
            LIMIT ? OFFSET ?
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
                    LIMIT ? OFFSET ?
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

    private int writeMetaBlock(Sheet sheet,
                                Input input,
                                String settlementCreatedDate,
                                CellStyle labelStyle,
                                CellStyle valueStyle) {

        int rowIndex = 0;
        rowIndex = this.writeMeta(sheet, rowIndex, "Settlement ID", input.settlementId(),
                                   labelStyle, valueStyle);
        rowIndex = this.writeMeta(sheet, rowIndex, "Settlement Created Date",
                                   settlementCreatedDate, labelStyle, valueStyle);
        rowIndex = this.writeMeta(sheet, rowIndex, "DFSP ID", input.fspId(),
                                   labelStyle, valueStyle);
        rowIndex = this.writeMeta(sheet, rowIndex, "DFSP Name", input.fspName(),
                                   labelStyle, valueStyle);
        rowIndex = this.writeMeta(sheet, rowIndex, "TimeZoneOffSet",
                                   this.displayOffset(input.timezoneOffset()),
                                   labelStyle, valueStyle);
        return rowIndex;
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
        style.setWrapText(true);
        var font = workbook.createFont();
        font.setFontName("Calibri");
        font.setFontHeightInPoints((short) 11);
        style.setFont(font);
        return style;
    }

    private CellStyle headerStyle(SXSSFWorkbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.labelStyle(workbook));
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
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

    private void applyColumnWidths(Sheet sheet) {

        for (int index = 0; index < COLUMN_WIDTHS.length; index++) {
            sheet.setColumnWidth(index, COLUMN_WIDTHS[index] * 256);
        }
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

        String normalized = fileType.trim().toLowerCase(java.util.Locale.ROOT);
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

        void accept(SettlementSummaryRow row);
    }

    @FunctionalInterface
    private interface SettlementSummaryNetPositionConsumer {

        void accept(SettlementSummaryNetPositionRow row);
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

        public int current() {
            return this.current;
        }

        private int next() {

            return this.current++;
        }
    }
}
