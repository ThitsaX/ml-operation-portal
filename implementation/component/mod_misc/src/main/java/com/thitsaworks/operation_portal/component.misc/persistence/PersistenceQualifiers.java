package com.thitsaworks.operation_portal.component.misc.persistence;

public class PersistenceQualifiers {

    public static class Shared {

        public static final String WRITE_SETTINGS = "sharedWriteSettings";

        public static final String WRITE_POOL_SIZES = "sharedWritePoolSizes";

        public static final String WRITE_DATA_SOURCE = "sharedWriteDataSource";

        public static final String WRITE_JDBC_TEMPLATE = "sharedWriteJdbcTemplate";

        public static final String READ_SETTINGS = "sharedReadSettings";

        public static final String READ_POOL_SIZES = "sharedReadPoolSizes";

        public static final String READ_DATA_SOURCE = "sharedReadDataSource";

        public static final String READ_JDBC_TEMPLATE = "sharedReadJdbcTemplate";

        public static final String ENTITY_MANAGER_FACTORY = "entityManagerFactory";

        public static final String TRANSACTION_MANAGER = "transactionManager";

    }

    public static class Core {

        public static final String WRITE_SETTINGS = "coreWriteSettings";

        public static final String WRITE_POOL_SIZES = "coreWritePoolSizes";

        public static final String WRITE_DATA_SOURCE = "coreWriteDataSource";

        public static final String READ_SETTINGS = "coreReadSettings";

        public static final String READ_POOL_SIZES = "coreReadPoolSizes";

        public static final String READ_DATA_SOURCE = "coreReadDataSource";

        public static final String READ_JDBC_TEMPLATE = "coreReadJdbcTemplate";

        public static final String WRITE_JDBC_TEMPLATE = "coreWriteJdbcTemplate";

        public static final String ENTITY_MANAGER_FACTORY = "coreEntityManagerFactory";

        public static final String TRANSACTION_MANAGER = "coreTransactionManager";

        public static final String QUERYDSL_CONFIGURATION = "coreQuerydslConfiguration";

        public static final String JPA_QUERY_FACTORY = "coreJpaQueryFactory";

        public static final String DATA_SOURCE = "coreDataSource";

    }

    public static class Reporting {

        public static final String WRITE_SETTINGS = "reportingWriteSettings";

        public static final String WRITE_POOL_SIZES = "reportingWritePoolSizes";

        public static final String WRITE_DATA_SOURCE = "reportingWriteDataSource";

        public static final String READ_SETTINGS = "reportingReadSettings";

        public static final String READ_POOL_SIZES = "reportingReadPoolSizes";

        public static final String READ_DATA_SOURCE = "reportingReadDataSource";

        public static final String READ_JDBC_TEMPLATE = "reportingReadJdbcTemplate";

        public static final String WRITE_JDBC_TEMPLATE = "reportingWriteJdbcTemplate";

        public static final String ENTITY_MANAGER_FACTORY = "reportingEntityManagerFactory";

        public static final String TRANSACTION_MANAGER = "reportingTransactionManager";

        public static final String QUERYDSL_CONFIGURATION = "reportingQuerydslConfiguration";

        public static final String JPA_QUERY_FACTORY = "reportingJpaQueryFactory";

        public static final String DATA_SOURCE = "reportingDataSource";

    }

}
