package com.thitsaworks.operation_portal.report;

import com.thitsaworks.operation_portal.component.ComponentConfiguration;
import com.thitsaworks.operation_portal.datasource.persistence.CentralLedgerReadDbConfiguration;
import com.thitsaworks.operation_portal.datasource.persistence.MySqlReadDbConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@ComponentScan("com.thitsaworks.operation_portal.report")
@Import(value = {
        ComponentConfiguration.class, CentralLedgerReadDbConfiguration.class, MySqlReadDbConfiguration.class})
public class ReportConfiguration {

//    @Autowired
//    private DfspVault dfspVault;
//
//    @Autowired
//    private Environment env;
//
//    @Bean
//    public CentralLedgerReadDbConfiguration.Settings centralLedgerReadDbConfigurationSettings() {
//
//        CentralLedgerReadDbConfiguration.Settings settings =
//                this.dfspVault.get("central_ledger/read_db/settings", CentralLedgerReadDbConfiguration.Settings.class);
//
//        return settings;
//    }
//
//    @Bean
//    public DriverManagerDataSource reportingDatasource(
//            @Qualifier("centralLedgerReadDbConfigurationSettings") CentralLedgerReadDbConfiguration.Settings settings) {
//
//        DriverManagerDataSource ds = new DriverManagerDataSource();
//        ds.setUrl(settings.getUrl());
//        ds.setUsername(settings.getUsername());
//        ds.setPassword(settings.getPassword());
//        ds.setDriverClassName(com.mysql.cj.jdbc.Driver.class.getName());
//
//        return ds;
//    }
//
//    @Bean(name = "reportingJdbcTemplate")
//    public JdbcTemplate reportingJdbcTemplate(DataSource reportingDatasource) {
//
//        return new JdbcTemplate(reportingDatasource);
//    }

}
