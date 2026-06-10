package com.thitsaworks.operation_portal.reporting.report.domain.impl.poi;

<<<<<<< Updated upstream
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.thitsaworks.operation_portal.component.misc.annotation.NoLogging;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateFeeSummaryReportCommand;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
=======
import com.thitsaworks.operation_portal.component.misc.annotation.NoLogging;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import com.thitsaworks.operation_portal.reporting.report.domain.GenerateFeeSummaryReportCommand;
>>>>>>> Stashed changes
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
import org.apache.poi.ss.usermodel.Workbook;
<<<<<<< Updated upstream
import org.apache.poi.ss.util.CellRangeAddress;
=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
=======
>>>>>>> Stashed changes
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
<<<<<<< Updated upstream
import java.util.Date;
=======
>>>>>>> Stashed changes
import java.util.List;
import java.util.Locale;

@Service
@Primary
@NoLogging
public class GenerateFeeSummaryReportPoiCommandHandler implements GenerateFeeSummaryReportCommand {

<<<<<<< Updated upstream
    private static final Logger LOG = LoggerFactory.getLogger(
        GenerateFeeSummaryReportPoiCommandHandler.class);

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

    private static final int[]
        COLUMN_WIDTHS =
        {
            (int) 29.36, (int) 29.36, (int) 15.36, (int) 17.36, (int) 15.36, (int) 17.36, (int) 21.36, (int) 23.36,
            (int) 23.36, (int) 11.36};

    private static final DateTimeFormatter HEADER_DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
=======
    private static final Logger LOG = LoggerFactory.getLogger(GenerateFeeSummaryReportPoiCommandHandler.class);

    private static final int DEFAULT_ROW_WINDOW = 200;
    private static final int DEFAULT_LIMIT = Integer.MAX_VALUE;
    private static final int MYSQL_STREAM_FETCH_SIZE = Integer.MIN_VALUE;

    private static final DateTimeFormatter META_DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");
    private static final DateTimeFormatter QUERY_DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final float[] COLUMN_WIDTHS =
        {18f, 18f, 23f, 14f, 18f, 14f, 16f, 16f, 16f, 10f};
>>>>>>> Stashed changes

    private final JdbcTemplate jdbcTemplate;

    public GenerateFeeSummaryReportPoiCommandHandler(
        @Qualifier(PersistenceQualifiers.Hub.READ_JDBC_TEMPLATE) JdbcTemplate jdbcTemplate) {

        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Output execute(Input input) throws ReportException {

        try {
            String fileType = this.normalizeFileType(input.filetype());
<<<<<<< Updated upstream

            Input normalizedInput = new Input(
                input.startDate(), input.endDate(), input.dfspId(), fileType,
                input.timeZoneOffset(), input.offset(), input.limit());
=======
            Input normalizedInput = this.normalizeInput(input, fileType);
>>>>>>> Stashed changes

            if ("xlsx".equalsIgnoreCase(fileType)) {
                return new Output(this.exportSingleChunkXlsx(normalizedInput));
            }

<<<<<<< Updated upstream
            if ("pdf".equalsIgnoreCase(fileType)) {
              //  return new Output(this.exportPdf(normalizedInput));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

=======
            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);
>>>>>>> Stashed changes
        } catch (ReportException exception) {
            throw exception;
        } catch (Exception exception) {
            LOG.error("Error generating fee summary report", exception);
            throw new ReportException(ReportErrors.FEE_SUMMARY_REPORT_FAILURE_EXCEPTION);
        }
    }

    @Override
    public Output exportAll(Input input, int totalRowCount, int pageSize) throws ReportException {

        if (totalRowCount <= 0) {
            throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
        }

        try {
            String fileType = this.normalizeFileType(input.filetype());
<<<<<<< Updated upstream

            Input normalizedInput = new Input(
                input.startDate(), input.endDate(), input.dfspId(), fileType,
                input.timeZoneOffset(), input.offset(), input.limit());
=======
            Input normalizedInput = this.normalizeInput(input, fileType);

>>>>>>> Stashed changes
            if ("xlsx".equalsIgnoreCase(fileType)) {
                return new Output(this.exportAllXlsx(normalizedInput, totalRowCount, pageSize));
            }

<<<<<<< Updated upstream
            if ("pdf".equalsIgnoreCase(fileType)) {
              //  return new Output(this.exportPdf(normalizedInput));
            }

            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);

        } catch (ReportException exception) {
            throw exception;
        } catch (Exception exception) {
            LOG.error("Error generating full settlement summary report", exception);
            throw new ReportException(ReportErrors.SETTLEMENT_REPORT_FAILURE_EXCEPTION);
=======
            throw new ReportException(ReportErrors.FILE_FORMAT_NOT_ALLOWED_EXCEPTION);
        } catch (ReportException exception) {
            throw exception;
        } catch (Exception exception) {
            LOG.error("Error generating full fee summary report", exception);
            throw new ReportException(ReportErrors.FEE_SUMMARY_REPORT_FAILURE_EXCEPTION);
>>>>>>> Stashed changes
        }
    }

    @Override
    public int countRows(CountInput input) {

        Integer rowCount = this.jdbcTemplate.queryForObject(
<<<<<<< Updated upstream
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
                input.startDate(),
                input.endDate(),
                input.dfspId(),
                input.timeZoneOffset()

            },
=======
            this.countQuery(),
            this.queryParameters(new Input(
                input.startDate(),
                input.endDate(),
                input.dfspId(),
                "",
                input.timeZoneOffset(),
                0,
                0),
                false)
                .toArray(),
>>>>>>> Stashed changes
            Integer.class);

        return rowCount == null ? 0 : rowCount;
    }

<<<<<<< Updated upstream


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
=======
    private Input normalizeInput(Input input, String fileType) {

        return new Input(
            input.startDate(),
            input.endDate(),
            input.fspId(),
            fileType,
            this.normalizeTimezoneOffset(input.timeZoneOffset()),
            input.offset(),
            input.limit());
>>>>>>> Stashed changes
    }

    private byte[] exportSingleChunkXlsx(Input input) throws IOException, ReportException {

<<<<<<< Updated upstream
        Path tempFile = Files.createTempFile("settlement-summary-", ".xlsx");
=======
        Path tempFile = Files.createTempFile("fee-summary-", ".xlsx");
>>>>>>> Stashed changes

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);

<<<<<<< Updated upstream
            Sheet sheet = workbook.createSheet("DFSPSettlementReport");
            this.trackColumns(sheet);

            CellStyle labelStyle = this.labelStyle(workbook);
            CellStyle valueStyle = this.valueStyle(workbook);
            CellStyle headerStyle = this.headerStyle(workbook);
            CellStyle textStyle = this.textStyle(workbook);
            CellStyle amountStyle = this.amountStyle(workbook);
            CellStyle volumeStyle = this.volumeStyle(workbook);



            int rowIndex = this.writeMetaBlock(sheet, input,
                                               labelStyle, valueStyle);
            rowIndex++;

            int freezeRow = this.writeColumnHeaders(sheet, rowIndex, headerStyle);
            rowIndex = freezeRow;

            RowCursor rowCursor = new RowCursor(rowIndex);
            this.streamRows(input, row -> this.writeDataRow(
                sheet.createRow(rowCursor.next()), row, textStyle, amountStyle, volumeStyle));
=======
            Sheet sheet = workbook.createSheet("FeeSummaryReport");
            this.trackColumns(sheet);

            CellStyle metaLabelStyle = this.labelStyle(workbook);
            CellStyle metaValueStyle = this.valueStyle(workbook);
            CellStyle headerStyle = this.headerStyle(workbook);
            CellStyle textStyle = this.textStyle(workbook);
            CellStyle amountStyle = this.amountStyle(workbook);
            CellStyle countStyle = this.countStyle(workbook);

            int rowIndex = this.writeMetaBlock(sheet, input, metaLabelStyle, metaValueStyle);
            rowIndex++;

            int headerRowIndex = this.writeColumnHeaders(sheet, rowIndex, headerStyle);
            rowIndex = headerRowIndex + 1;

            RowCursor rowCursor = new RowCursor(rowIndex);
            this.streamRows(input, row -> this.writeDataRow(
                sheet.createRow(rowCursor.next()),
                row,
                textStyle,
                amountStyle,
                countStyle));
>>>>>>> Stashed changes

            if (rowCursor.current() == rowIndex) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

            this.flush(sheet);
<<<<<<< Updated upstream

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
=======
            this.applyColumnWidths(sheet);
            sheet.createFreezePane(0, rowIndex);
>>>>>>> Stashed changes

            workbook.write(outputStream);
            workbook.dispose();
            return Files.readAllBytes(tempFile);
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

    private byte[] exportAllXlsx(Input input, int totalRowCount, int pageSize)
        throws IOException, ReportException {

<<<<<<< Updated upstream
        Path tempFile = Files.createTempFile("settlement-summary-", ".xlsx");
=======
        Path tempFile = Files.createTempFile("fee-summary-", ".xlsx");
>>>>>>> Stashed changes

        try (SXSSFWorkbook workbook = new SXSSFWorkbook(DEFAULT_ROW_WINDOW);
             OutputStream outputStream = Files.newOutputStream(tempFile)) {

            workbook.setCompressTempFiles(true);

<<<<<<< Updated upstream
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
=======
            Sheet sheet = workbook.createSheet("FeeSummaryReport");
            this.trackColumns(sheet);

            CellStyle metaLabelStyle = this.labelStyle(workbook);
            CellStyle metaValueStyle = this.valueStyle(workbook);
            CellStyle headerStyle = this.headerStyle(workbook);
            CellStyle textStyle = this.textStyle(workbook);
            CellStyle amountStyle = this.amountStyle(workbook);
            CellStyle countStyle = this.countStyle(workbook);

            int rowIndex = this.writeMetaBlock(sheet, input, metaLabelStyle, metaValueStyle);
            rowIndex++;

            int headerRowIndex = this.writeColumnHeaders(sheet, rowIndex, headerStyle);
            rowIndex = headerRowIndex + 1;
>>>>>>> Stashed changes

            RowCursor rowCursor = new RowCursor(rowIndex);
            for (int offset = 0; offset < totalRowCount; offset += pageSize) {
                int limit = Math.min(pageSize, totalRowCount - offset);
                Input chunkInput = new Input(
<<<<<<< Updated upstream
                    input.fspId(), input.fspName(), input.settlementId(), input.filetype(),
                    input.timezoneOffset(), input.userName(), offset, limit);

                this.streamRows(chunkInput, row -> this.writeDataRow(
                    sheet.createRow(rowCursor.next()), row, textStyle, amountStyle, volumeStyle));
=======
                    input.startDate(),
                    input.endDate(),
                    input.fspId(),
                    input.filetype(),
                    input.timeZoneOffset(),
                    offset,
                    limit);

                this.streamRows(chunkInput, row -> this.writeDataRow(
                    sheet.createRow(rowCursor.next()),
                    row,
                    textStyle,
                    amountStyle,
                    countStyle));
>>>>>>> Stashed changes
                this.flush(sheet);
            }

            if (rowCursor.current() == rowIndex) {
                throw new ReportException(ReportErrors.RESULT_NOT_FOUND_EXCEPTION);
            }

<<<<<<< Updated upstream
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
=======
            this.applyColumnWidths(sheet);
            sheet.createFreezePane(0, rowIndex);
>>>>>>> Stashed changes

            workbook.write(outputStream);
            workbook.dispose();
            return Files.readAllBytes(tempFile);
<<<<<<< Updated upstream

=======
>>>>>>> Stashed changes
        } finally {
            Files.deleteIfExists(tempFile);
        }
    }

<<<<<<< Updated upstream
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
        String formattedDate = instant.atOffset(ZoneOffset.UTC)
                                      .withOffsetSameInstant(zoneOffset)
                                      .format(HEADER_DATE_FORMAT);

        if (zoneOffset.equals(ZoneOffset.UTC)) {
            formattedDate = formattedDate.replace("Z", "+00:00");
        }

        return formattedDate;
    }

=======
>>>>>>> Stashed changes
    private int writeMetaBlock(Sheet sheet,
                               Input input,
                               CellStyle labelStyle,
                               CellStyle valueStyle) {

        int rowIndex = 0;
<<<<<<< Updated upstream
        rowIndex = this.writeMeta(sheet, rowIndex, "Start Date", input.startDate(),
                                  labelStyle, valueStyle);
        rowIndex = this.writeMeta(sheet, rowIndex, "End Date",
                                  input.endDate(), labelStyle, valueStyle);
        rowIndex = this.writeMeta(sheet, rowIndex, "DFSP Name", input.fspId(),
                                  labelStyle, valueStyle);
        rowIndex = this.writeMeta(sheet, rowIndex, "TimeZoneOffSet",
                                  this.displayOffset(input.timeZoneOffset()),
=======
        rowIndex = this.writeMeta(sheet, rowIndex, "Start Date",
                                  this.formatMetaDate(input.startDate(), input.timeZoneOffset()),
                                  labelStyle, valueStyle);
        rowIndex = this.writeMeta(sheet, rowIndex, "End Date",
                                  this.formatMetaDate(input.endDate(), input.timeZoneOffset()),
                                  labelStyle, valueStyle);
        rowIndex = this.writeMeta(sheet, rowIndex, "Settlement ID", "-",
                                  labelStyle, valueStyle);
        rowIndex = this.writeMeta(sheet, rowIndex, "DFSP Name", input.fspId(),
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
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
=======
    private int writeColumnHeaders(Sheet sheet, int startRow, CellStyle headerStyle) {

        Row row = sheet.createRow(startRow);
        for (int index = 0; index < this.columnHeaders().length; index++) {
            Cell cell = row.createCell(index);
            cell.setCellStyle(headerStyle);
            cell.setCellValue(this.columnHeaders()[index]);
        }
        return startRow;
    }

    private void writeDataRow(Row row,
                              FeeSummaryRow data,
                              CellStyle textStyle,
                              CellStyle amountStyle,
                              CellStyle countStyle) {

        this.writeTextCell(row, 0, data.senderDFSP(), textStyle);
        this.writeTextCell(row, 1, data.receiverDFSP(), textStyle);
        this.writeTextCell(row, 2, data.feePolicy(), textStyle);
        this.writeNumberCell(row, 3, data.totalTransactions(), countStyle);
        this.writeNumberCell(row, 4, data.totalAmount(), amountStyle);
        this.writeNumberCell(row, 5, data.totalFee(), amountStyle);
        this.writeNumberCell(row, 6, data.totalPayerFee(), amountStyle);
        this.writeNumberCell(row, 7, data.totalPayeeFee(), amountStyle);
        this.writeNumberCell(row, 8, data.totalSchemeFee(), amountStyle);
        this.writeTextCell(row, 9, data.currency(), textStyle);
    }

    private void streamRows(Input input, FeeSummaryRowConsumer consumer) {

        List<Object> params = this.queryParameters(input, true);

        this.jdbcTemplate.query(connection -> {
            PreparedStatement statement = connection.prepareStatement(
                this.mainQuery(),
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
                consumer.accept(this.mapRow(resultSet));
            }
            return null;
        });
    }

    private FeeSummaryRow mapRow(ResultSet resultSet) throws SQLException {

        return new FeeSummaryRow(
            resultSet.getString("senderDFSP"),
            resultSet.getString("receiverDFSP"),
            resultSet.getString("feePolicy"),
            resultSet.getBigDecimal("totalTransactions"),
            resultSet.getBigDecimal("totalAmount"),
            resultSet.getBigDecimal("totalFee"),
            resultSet.getBigDecimal("totalPayerFee"),
            resultSet.getBigDecimal("totalPayeeFee"),
            resultSet.getBigDecimal("totalSchemeFee"),
            resultSet.getString("currency"));
    }

    private String mainQuery() {

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
            ),
            fee_per_quote AS (
              SELECT
                qe.quoteId,
                MAX(CASE WHEN qe.`key` = 'feePolicy' THEN qe.`value` END) AS feePolicy,
                MAX(CASE WHEN qe.`key` = 'payerfee' THEN CAST(qe.`value` AS DECIMAL(18,4)) END) AS totalPayerFee,
                MAX(CASE WHEN qe.`key` = 'payeefee' THEN CAST(qe.`value` AS DECIMAL(18,4)) END) AS totalPayeeFee,
                MAX(CASE WHEN qe.`key` = 'schemeFee' THEN CAST(qe.`value` AS DECIMAL(18,4)) END) AS totalSchemeFee
              FROM quoteExtension qe
              GROUP BY qe.quoteId
            ),
            sender_receiver AS (
              SELECT
                qp.quoteId,
                MAX(CASE WHEN pt.name = 'PAYER' THEN p.name END) AS senderDFSP,
                MAX(CASE WHEN pt.name = 'PAYEE' THEN p.name END) AS receiverDFSP
              FROM quoteParty qp
              JOIN partyType pt
                ON pt.partyTypeId = qp.partyTypeId
              LEFT JOIN participant p
                ON p.participantId = qp.participantId
              GROUP BY qp.quoteId
            ),
            latest_tsc AS (
              SELECT tsc1.transferId, tsc1.transferStateId, tsc1.createdDate
              FROM transferStateChange tsc1
              JOIN (
                SELECT transferId, MAX(transferStateChangeId) AS maxId
                FROM transferStateChange
                GROUP BY transferId
              ) mx
                ON mx.transferId = tsc1.transferId
               AND mx.maxId = tsc1.transferStateChangeId
            )
            SELECT
              sr.senderDFSP AS senderDFSP,
              sr.receiverDFSP AS receiverDFSP,
              f.feePolicy AS feePolicy,
              COUNT(DISTINCT q.quoteId) AS totalTransactions,
              SUM(q.amount) AS totalAmount,
              SUM(
                COALESCE(f.totalPayerFee, 0) +
                COALESCE(f.totalPayeeFee, 0) +
                COALESCE(f.totalSchemeFee, 0)
              ) AS totalFee,
              SUM(COALESCE(f.totalPayerFee, 0)) AS totalPayerFee,
              SUM(COALESCE(f.totalPayeeFee, 0)) AS totalPayeeFee,
              SUM(COALESCE(f.totalSchemeFee, 0)) AS totalSchemeFee,
              q.currencyId AS currency
            FROM quote q
            JOIN transfer t
              ON t.transferId = q.transactionReferenceId
            LEFT JOIN latest_tsc tsc
              ON tsc.transferId = t.transferId
            JOIN bounds b
              ON IFNULL(tsc.createdDate, t.createdDate) BETWEEN b.startUtc AND b.endUtc
            JOIN sender_receiver sr
              ON sr.quoteId = q.quoteId
            LEFT JOIN fee_per_quote f
              ON f.quoteId = q.quoteId
            WHERE (? = 'All' OR sr.senderDFSP = ? OR sr.receiverDFSP = ?)
            GROUP BY
              sr.senderDFSP,
              sr.receiverDFSP,
              f.feePolicy,
              q.currencyId
            ORDER BY
              sr.senderDFSP, sr.receiverDFSP, f.feePolicy, q.currencyId
            LIMIT ?, ?
            """;
    }

    private String countQuery() {

        return """
            SELECT COUNT(*) FROM (
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
              ),
              fee_per_quote AS (
                SELECT
                  qe.quoteId,
                  MAX(CASE WHEN qe.`key` = 'feePolicy' THEN qe.`value` END) AS feePolicy,
                  MAX(CASE WHEN qe.`key` = 'payerfee' THEN CAST(qe.`value` AS DECIMAL(18,4)) END) AS totalPayerFee,
                  MAX(CASE WHEN qe.`key` = 'payeefee' THEN CAST(qe.`value` AS DECIMAL(18,4)) END) AS totalPayeeFee,
                  MAX(CASE WHEN qe.`key` = 'schemeFee' THEN CAST(qe.`value` AS DECIMAL(18,4)) END) AS totalSchemeFee
                FROM quoteExtension qe
                GROUP BY qe.quoteId
              ),
              sender_receiver AS (
                SELECT
                  qp.quoteId,
                  MAX(CASE WHEN pt.name = 'PAYER' THEN p.name END) AS senderDFSP,
                  MAX(CASE WHEN pt.name = 'PAYEE' THEN p.name END) AS receiverDFSP
                FROM quoteParty qp
                JOIN partyType pt
                  ON pt.partyTypeId = qp.partyTypeId
                LEFT JOIN participant p
                  ON p.participantId = qp.participantId
                GROUP BY qp.quoteId
              ),
              latest_tsc AS (
                SELECT tsc1.transferId, tsc1.transferStateId, tsc1.createdDate
                FROM transferStateChange tsc1
                JOIN (
                  SELECT transferId, MAX(transferStateChangeId) AS maxId
                  FROM transferStateChange
                  GROUP BY transferId
                ) mx
                  ON mx.transferId = tsc1.transferId
                 AND mx.maxId = tsc1.transferStateChangeId
              )
              SELECT
                sr.senderDFSP,
                sr.receiverDFSP,
                f.feePolicy,
                q.currencyId
              FROM quote q
              JOIN transfer t
                ON t.transferId = q.transactionReferenceId
              LEFT JOIN latest_tsc tsc
                ON tsc.transferId = t.transferId
              JOIN bounds b
                ON IFNULL(tsc.createdDate, t.createdDate) BETWEEN b.startUtc AND b.endUtc
              JOIN sender_receiver sr
                ON sr.quoteId = q.quoteId
              LEFT JOIN fee_per_quote f
                ON f.quoteId = q.quoteId
              WHERE (? = 'All' OR sr.senderDFSP = ? OR sr.receiverDFSP = ?)
              GROUP BY
                sr.senderDFSP,
                sr.receiverDFSP,
                f.feePolicy,
                q.currencyId
            ) x
            """;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
        font.setBold(false);
>>>>>>> Stashed changes
        style.setFont(font);
        return style;
    }

    private CellStyle headerStyle(SXSSFWorkbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.labelStyle(workbook));
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setWrapText(true);
<<<<<<< Updated upstream
=======
        style.setAlignment(HorizontalAlignment.CENTER);
>>>>>>> Stashed changes
        return style;
    }

    private CellStyle textStyle(Workbook workbook) {

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

    private CellStyle amountStyle(Workbook workbook) {

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.textStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
<<<<<<< Updated upstream
        style.setDataFormat(workbook.createDataFormat()
                                    .getFormat("#,##0.00;(#,##0.00)"));
        return style;
    }

    private CellStyle volumeStyle(Workbook workbook) {
=======
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0.00;(#,##0.00)"));
        return style;
    }

    private CellStyle countStyle(Workbook workbook) {
>>>>>>> Stashed changes

        CellStyle style = workbook.createCellStyle();
        style.cloneStyleFrom(this.textStyle(workbook));
        style.setAlignment(HorizontalAlignment.RIGHT);
<<<<<<< Updated upstream
        style.setDataFormat(workbook.createDataFormat()
                                    .getFormat("#,##0"));
=======
        style.setDataFormat(workbook.createDataFormat().getFormat("#,##0"));
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
            sheet.setColumnWidth(index, COLUMN_WIDTHS[index] * 256);
=======
            sheet.setColumnWidth(index, (int) (COLUMN_WIDTHS[index] * 256));
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
=======
    private String[] columnHeaders() {

        return new String[] {
            "Sender DFSP",
            "Receiver DFSP",
            "Fee Policy",
            "Total Transactions",
            "Total Amount",
            "Total Fee",
            "Total Payer Fee",
            "Total Payee Fee",
            "Total Scheme Fee",
            "Currency"
        };
    }

>>>>>>> Stashed changes
    private String normalizeFileType(String fileType) {

        if (fileType == null) {
            return "";
        }

<<<<<<< Updated upstream
        String
            normalized =
            fileType.trim()
                    .toLowerCase(Locale.ROOT);
        return normalized.startsWith(".") ? normalized.substring(1) : normalized;
    }

    private String displayOffset(String rawOffset) {

        ZoneOffset zoneOffset = this.parseOffset(rawOffset);
        return zoneOffset.getId()
                         .equals("Z") ? "+00:00" : zoneOffset.getId();
=======
        String normalized = fileType.trim().toLowerCase(Locale.ROOT);
        return normalized.startsWith(".") ? normalized.substring(1) : normalized;
    }

    private String normalizeTimezoneOffset(String rawOffset) {

        if (rawOffset == null || rawOffset.isBlank()) {
            return "+0000";
        }

        String normalized = rawOffset.trim().replace(":", "");
        if (normalized.matches("[+-]\\d{4}")) {
            return normalized;
        }
        if (normalized.matches("\\d{4}")) {
            return "+" + normalized;
        }
        return "+0000";
    }

    private String sqlTimezoneOffset(String rawOffset) {

        String normalized = this.normalizeTimezoneOffset(rawOffset);
        return normalized.startsWith("+") ? normalized.substring(1) : normalized;
    }

    private List<Object> queryParameters(Input input, boolean includePagination) {

        List<Object> params = new ArrayList<>();
        String sqlTimezoneOffset = this.sqlTimezoneOffset(input.timeZoneOffset());
        String startDate = this.formatQueryDate(input.startDate(), input.timeZoneOffset());
        String endDate = this.formatQueryDate(input.endDate(), input.timeZoneOffset());

        params.add(sqlTimezoneOffset);
        params.add(startDate);
        params.add(sqlTimezoneOffset);
        params.add(sqlTimezoneOffset);
        params.add(startDate);
        params.add(sqlTimezoneOffset);
        params.add(sqlTimezoneOffset);

        params.add(sqlTimezoneOffset);
        params.add(endDate);
        params.add(sqlTimezoneOffset);
        params.add(sqlTimezoneOffset);
        params.add(endDate);
        params.add(sqlTimezoneOffset);
        params.add(sqlTimezoneOffset);

        params.add(input.fspId());
        params.add(input.fspId());
        params.add(input.fspId());

        if (includePagination) {
            params.add(input.limit() == null ? DEFAULT_LIMIT : input.limit());
            params.add(input.offset() == null ? 0 : input.offset());
        }

        return params;
    }

    private String displayOffset(String rawOffset) {

        ZoneOffset zoneOffset = this.parseOffset(rawOffset);
        return zoneOffset.getId().equals("Z") ? "+00:00" : zoneOffset.getId();
>>>>>>> Stashed changes
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

<<<<<<< Updated upstream
    private String buildPrintedByText(String userName, String timezoneOffset) {

        String user = safe(userName);
        String offset = normalizeTimezoneOffset(timezoneOffset);

        long millis = System.currentTimeMillis();
        int sign = offset.startsWith("-") ? -1 : 1;
        String abs = offset.replace(":", "")
                           .replace("-", "")
                           .replace("+", "");
        int hours = Integer.parseInt(abs.substring(0, 2));
        int minutes = Integer.parseInt(abs.substring(2, 4));
        long adjustedMillis = millis + sign * ((hours * 60L * 60L * 1000L) + (minutes * 60L * 1000L));
        String formatted = new SimpleDateFormat("dd/MM/yyyy hh:mm a").format(new Date(adjustedMillis));

        return "Printed by: " + user + " on " + formatted;
    }

    private String normalizeTimezoneOffset(String rawOffset) {

        if (rawOffset == null || rawOffset.isBlank()) {
            return "0000";
        }

        String
            normalized =
            rawOffset.trim()
                     .replace(":", "");
        if (normalized.matches("[+-]\\d{4}") || normalized.matches("\\d{4}")) {
            return normalized;
        }

        return "0000";
    }

    private String safe(String value) {

        return value == null ? "" : value;
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

=======
    private String formatQueryDate(Instant instant, String timezoneOffset) {

        if (instant == null) {
            return null;
        }

        ZoneOffset zoneOffset = this.parseOffset(timezoneOffset);
        return instant.atOffset(zoneOffset).format(QUERY_DATE_FORMAT);
    }

    private String formatMetaDate(Instant instant, String timezoneOffset) {

        if (instant == null) {
            return "";
        }

        ZoneOffset zoneOffset = this.parseOffset(timezoneOffset);
        String formatted = instant.atOffset(ZoneOffset.UTC)
                                  .withOffsetSameInstant(zoneOffset)
                                  .format(META_DATE_FORMAT);
        return zoneOffset.equals(ZoneOffset.UTC) ? formatted.replace("Z", "+00:00") : formatted;
    }

    private record FeeSummaryRow(String senderDFSP,
                                 String receiverDFSP,
                                 String feePolicy,
                                 BigDecimal totalTransactions,
                                 BigDecimal totalAmount,
                                 BigDecimal totalFee,
                                 BigDecimal totalPayerFee,
                                 BigDecimal totalPayeeFee,
                                 BigDecimal totalSchemeFee,
                                 String currency) {
    }

    @FunctionalInterface
    private interface FeeSummaryRowConsumer {

        void accept(FeeSummaryRow row);
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream

    }

=======
    }
>>>>>>> Stashed changes
}
