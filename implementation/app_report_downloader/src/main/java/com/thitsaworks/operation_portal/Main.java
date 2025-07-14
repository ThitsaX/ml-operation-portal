//package com.thitsaworks.operation_portal;
//
//
//import com.thitsaworks.operation_portal.reporting.report.domain.GenerateDfspDailyTransactionTrendSummaryCommand;
//import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementDetailReportCommand;
//import com.thitsaworks.operation_portal.reporting.report.domain.GenerateSettlementReportCommand;
//import com.thitsaworks.operation_portal.reporting.report.domain.impl.GenerateDfspDailyTransactionTrendSummaryCommandHandler;
//import com.thitsaworks.operation_portal.reporting.report.domain.impl.GenerateSettlementDetailReportCommandHandler;
//import com.thitsaworks.operation_portal.reporting.report.domain.impl.GenerateSettlementReportCommandHandler;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class Main {
//
//    public static void main(String[] args) throws Exception {
//        String fileType = ".xlsx";
//
//        String fileName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());
//
//        // Generate SettlementReport
//        GenerateSettlementReportCommandHandler settlementReport =
//                new GenerateSettlementReportCommandHandler(new JdbcTemplate());
//
//        FileOutputStream fout = new FileOutputStream(new File("D:\\Project\\Report download\\Detail" + fileName + fileType));
//
//        settlementReport.execute(new GenerateSettlementReportCommand.Input("okdollar", "1", null, null));
//
//        GenerateDfspDailyTransactionTrendSummaryCommandHandler
//                trendSummary = new GenerateDfspDailyTransactionTrendSummaryCommandHandler(new JdbcTemplate());
//
//        FileOutputStream fouttrend =
//                new FileOutputStream(new File("D:\\Project\\Report download\\Trend" + fileName + fileType));
//
//        trendSummary.execute(new GenerateDfspDailyTransactionTrendSummaryCommand.Input("2022-10-17T00:00:00",
//                                                                                       "2022-10-17T23:59:59",
//                                                                                       "0630",
//                                                                                       fileType,
//                                                                                       fouttrend));
//
//        GenerateSettlementDetailReportCommandHandler detailReport = new GenerateSettlementDetailReportCommandHandler(
//                new JdbcTemplate());
//
//        FileOutputStream foutdetail =
//                new FileOutputStream(new File("D:\\Project\\Report download\\Detail" + fileName + fileType));
//
//        detailReport.execute(new GenerateSettlementDetailReportCommand.Input("1",
//                                                                             "mmdokdollar",
//                                                                             fileType,
//                                                                             "")); //foutdetail
//
//
//    }
//
//}