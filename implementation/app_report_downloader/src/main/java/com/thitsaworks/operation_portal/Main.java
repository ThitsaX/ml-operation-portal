package com.thitsaworks.operation_portal;

import com.thitsaworks.operation_portal.report.domain.DfspDailyTransactionTrendSummary;
import com.thitsaworks.operation_portal.report.domain.SettlementDetailReport;
import com.thitsaworks.operation_portal.report.domain.SettlementReport;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException, JRException, SQLException {
        String fileType = ".xlsx";

        String fileName = new SimpleDateFormat("yyyyMMddHHmm").format(new Date());

        // Generate SettlementReport
        SettlementReport settlementReport = new SettlementReport();
        FileOutputStream fout = new FileOutputStream(new File("D:\\Project\\Report download\\Detail" + fileName + fileType));
        settlementReport.generate("mmdokdollar", "1", null, null);


        DfspDailyTransactionTrendSummary trendSummary = new DfspDailyTransactionTrendSummary();
        FileOutputStream fouttrend =
                new FileOutputStream(new File("D:\\Project\\Report download\\Trend" + fileName + fileType));
        trendSummary.generate("2022-10-17T00:00:00", "2022-10-17T23:59:59", "0630", fileType, fouttrend);


        SettlementDetailReport detailReport = new SettlementDetailReport();
        FileOutputStream foutdetail =
                new FileOutputStream(new File("D:\\Project\\Report download\\Detail" + fileName + fileType));
        detailReport.generate("1", "mmdokdollar", fileType, ""); //foutdetail


    }

}