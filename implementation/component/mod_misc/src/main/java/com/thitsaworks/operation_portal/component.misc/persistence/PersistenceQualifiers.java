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

    public static class Common {

        public static final String DATA_SOURCE = Shared.WRITE_DATA_SOURCE;

        public static final String ENTITY_MANAGER_FACTORY = "commonEntityManagerFactory";

        public static final String TRANSACTION_MANAGER = "commonTransactionManager";

        public static final String QUERYDSL_CONFIGURATION = "commonQuerydslConfiguration";

        public static final String JPA_QUERY_FACTORY = "commonJpaQueryFactory";

        public static final String JDBC_TEMPLATE = "commonJdbcTemplate";

    }

    public static class Operation {

        public static final String DATA_SOURCE = Shared.WRITE_DATA_SOURCE;

        public static final String ENTITY_MANAGER_FACTORY = "operationEntityManagerFactory";

        public static final String TRANSACTION_MANAGER = "operationTransactionManager";

        public static final String QUERYDSL_CONFIGURATION = "operationQuerydslConfiguration";

        public static final String JPA_QUERY_FACTORY = "operationJpaQueryFactory";

    }

    public static class central_ledger {

        public static final String WRITE_SETTINGS = "overlapWriteSettings";

        public static final String WRITE_POOL_SIZES = "overlapWritePoolSizes";

        public static final String WRITE_DATA_SOURCE = Shared.WRITE_DATA_SOURCE;

        public static final String WRITE_JDBC_TEMPLATE = "centralLedgerWriteJdbcTemplate";

        public static final String DATA_SOURCE = Shared.READ_DATA_SOURCE;

        public static final String JDBC_TEMPLATE = "centralLedgerReadJdbcTemplate";

    }

}
