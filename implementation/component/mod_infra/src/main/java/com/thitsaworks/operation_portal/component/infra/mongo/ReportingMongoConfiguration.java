package com.thitsaworks.operation_portal.component.infra.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ReadPreference;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.thitsaworks.operation_portal.component.misc.persistence.PersistenceQualifiers;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.lang.Nullable;

import java.util.concurrent.TimeUnit;

@Configuration
public class ReportingMongoConfiguration {

    public static final String READ_SETTINGS_PATH = "mongo/hub_data/read_db/settings";

    public static final String WRITE_SETTINGS_PATH = "mongo/hub_data/write_db/settings";

    // ---- READ ----

    @Bean(name = PersistenceQualifiers.Hub.MONGO_READ_CLIENT)
    @Qualifier(PersistenceQualifiers.Hub.MONGO_READ_CLIENT)
    public MongoClient readMongoClient(@Qualifier(PersistenceQualifiers.Hub.MONGO_READ_SETTINGS) Settings s) {

        MongoClientSettings settings = baseClientSettings(s)
                .readPreference(resolveReadPref(s.readPreference(), ReadPreference.secondaryPreferred()))
                .build();
        return MongoClients.create(settings);
    }

    @Bean(name = PersistenceQualifiers.Hub.MONGO_READ_FACTORY)
    @Qualifier(PersistenceQualifiers.Hub.MONGO_READ_FACTORY)
    public MongoDatabaseFactory readFactory(
            @Qualifier(PersistenceQualifiers.Hub.MONGO_READ_CLIENT) MongoClient client,
            @Qualifier(PersistenceQualifiers.Hub.MONGO_READ_SETTINGS) Settings s) {

        return new SimpleMongoClientDatabaseFactory(client, s.database());
    }

    @Bean(name = PersistenceQualifiers.Hub.MONGO_READ_TEMPLATE)
    @Qualifier(PersistenceQualifiers.Hub.MONGO_READ_TEMPLATE)
    @Primary
    public MongoTemplate reportingMongoReadTemplate(
            @Qualifier(PersistenceQualifiers.Hub.MONGO_READ_FACTORY) MongoDatabaseFactory reportingMongoReadFactory) {

        return new MongoTemplate(reportingMongoReadFactory);
    }

    // ---- WRITE ----

    @Bean(name = PersistenceQualifiers.Hub.MONGO_WRITE_CLIENT)
    @Qualifier(PersistenceQualifiers.Hub.MONGO_WRITE_CLIENT)
    public MongoClient writeMongoClient(@Qualifier(PersistenceQualifiers.Hub.MONGO_WRITE_SETTINGS) Settings s) {

        MongoClientSettings settings = baseClientSettings(s)
                .readPreference(resolveReadPref(s.readPreference(), ReadPreference.primary()))
                .build();
        return MongoClients.create(settings);
    }


    @Bean(name = PersistenceQualifiers.Hub.MONGO_WRITE_FACTORY)
    @Qualifier(PersistenceQualifiers.Hub.MONGO_WRITE_FACTORY)
    @Primary
    public MongoDatabaseFactory writeFactory(
            @Qualifier(PersistenceQualifiers.Hub.MONGO_WRITE_CLIENT) MongoClient client,
            @Qualifier(PersistenceQualifiers.Hub.MONGO_WRITE_SETTINGS) Settings s) {

        return new SimpleMongoClientDatabaseFactory(client, s.database());
    }

    @Bean(name = PersistenceQualifiers.Hub.MONGO_WRITE_TEMPLATE)
    @Qualifier(PersistenceQualifiers.Hub.MONGO_WRITE_TEMPLATE)
    public MongoTemplate writeTemplate(
            @Qualifier(PersistenceQualifiers.Hub.MONGO_WRITE_FACTORY) MongoDatabaseFactory factory) {

        return new MongoTemplate(factory);
    }

    // ---- helpers ----

    private MongoClientSettings.Builder baseClientSettings(Settings s) {
        return MongoClientSettings.builder()
                                  .applyConnectionString(new ConnectionString(s.uri()))
                                  .retryWrites(s.retryWrites()).applyToClusterSettings(b -> b.serverSelectionTimeout(0,
                                                                                                                     TimeUnit.MILLISECONDS))
                                  .applyToConnectionPoolSettings(b -> b.minSize(Math.max(0, s.minPoolSize()))
                                                                       .maxSize(Math.max(25, s.maxPoolSize()))
                                                                       .maxWaitTime(0, TimeUnit.MILLISECONDS))
                                  .applyToSocketSettings(b -> b.connectTimeout(0, TimeUnit.MILLISECONDS)
                                                               .readTimeout(0, TimeUnit.MILLISECONDS));
    }

    private static ReadPreference resolveReadPref(@Nullable String pref, ReadPreference def) {

        if (pref == null || pref.isBlank()) {return def;}
        return switch (pref.trim().toLowerCase()) {
            case "primary" -> ReadPreference.primary();
            case "primarypreferred" -> ReadPreference.primaryPreferred();
            case "secondary" -> ReadPreference.secondary();
            case "secondarypreferred" -> ReadPreference.secondaryPreferred();
            case "nearest" -> ReadPreference.nearest();
            default -> def;
        };
    }

    /**
     * Settings for one Mongo connection (read or write).
     * Provide two beans with qualifiers:
     * - PersistenceQualifiers.Reporting.MONGO_READ_SETTINGS
     * - PersistenceQualifiers.Reporting.MONGO_WRITE_SETTINGS
     * <p>
     * Typical URI:
     * mongodb://user:pass@host1,host2/db?replicaSet=rs0&authSource=admin
     */
    public record Settings(
            String uri,
            String database,
            int minPoolSize,
            int maxPoolSize,
            int maxWaitMs,
            int connectTimeoutMs,
            int readTimeoutMs,
            boolean retryWrites,
            String readPreference // e.g. "secondaryPreferred" (optional; overrides defaults above)
    ) {}

}

