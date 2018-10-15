package com.exochain.data.bcschema;

import com.exochain.api.bc.persistence.entities.ExoAccount;
import com.exochain.api.bc.persistence.entities.LoginChannel;
import com.exochain.api.bc.persistence.entities.LoginChannelAccount;
import com.exochain.data.adjudication.claimtype.query.ClaimTypeSummary;
import com.google.common.collect.ImmutableMap;
import io.axoniq.gdpr.cryptoengine.jpa.DefaultKeyEntity;
import org.axonframework.eventhandling.saga.repository.jpa.AssociationValueEntry;
import org.axonframework.eventhandling.saga.repository.jpa.SagaEntry;
import org.axonframework.eventhandling.tokenstore.jpa.TokenEntry;
import org.axonframework.eventsourcing.eventstore.jpa.DomainEventEntry;
import org.axonframework.eventsourcing.eventstore.jpa.SnapshotEventEntry;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.dialect.H2Dialect;
import org.hibernate.dialect.InnoDBStorageEngine;
import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.Oracle12cDialect;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;

import java.io.File;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class ExportApplication {

    enum DB_TYPE {
        MY_SQL_57_INNODB,
        H2,
        ORACLE_12,
    }

    // Classes that we need to generate schema's for
    static final Class[] jpaClasses= {
            // ======= BEGIN AXON FRAMEWORK STUFF ===========//
            // For event and snapshot storage - not needed if you fully run on AxonDB.
            DomainEventEntry.class,
            SnapshotEventEntry.class,
            // Needed for tracking event processors.
            TokenEntry.class,
            // Needed for saga's.
            SagaEntry.class,
            AssociationValueEntry.class,
            // Needed for encryption key storage
            DefaultKeyEntity.class,
            // ======= END AXON FRAMEWORK STUFF ===========//
            // BC API Web dependencies
            ExoAccount.class,
            LoginChannel.class,
            LoginChannelAccount.class,
            // EXO claim type projections
            ClaimTypeSummary.class
    };

    static final Map<String, Object> commonHibernateSettings;

    static {
        Map<String, Object> hibSettings = new HashMap<>(2);
        hibSettings.put("hibernate.implicit_naming_strategy", SpringImplicitNamingStrategy.class);
        hibSettings.put("hibernate.physical_naming_strategy", SpringPhysicalNamingStrategy.class);
        commonHibernateSettings = ImmutableMap.copyOf(hibSettings);
    }

    /* Set as appropriate. */
    static final DB_TYPE rdbms = DB_TYPE.MY_SQL_57_INNODB;

    public static void main(String[] args) {

        for(DB_TYPE dbType: DB_TYPE.values()) {
            generateSchema(dbType);
        }
    }

    private static void generateSchema(DB_TYPE dbType) {
        String dbName = dbType.name();
        switch (dbType) {
            case MY_SQL_57_INNODB:
                generateMySqlSchema(dbName);
                break;
            case ORACLE_12:
                generateOracle12Schema(dbName);
                break;
            case H2:
                generateH2Schema(dbName);
                break;
            default:
                throw new IllegalStateException("DB_TYPE is not understood");
        }
    }

    private static void generateMySqlSchema(String dbName) {
        Map<String, Object> settings = createCommonHibernateSettings();
        settings.put("hibernate.dialect", MySQL57Dialect.class);
        settings.put("hibernate.dialect.storage_engine", InnoDBStorageEngine.class);
        /* See  https://vladmihalcea.com/why-should-not-use-the-auto-jpa-generationtype-with-mysql-and-hibernate/
                https://hibernate.atlassian.net/browse/HHH-11014 */
        settings.put("hibernate.id.new_generator_mappings", false);

        String[] additionalMetadataSources = new String[0];

        doGenerateSchema(dbName, settings, additionalMetadataSources);
    }

    private static Map<String, Object> createCommonHibernateSettings() {
        Map<String, Object> settings = new HashMap<>();
        settings.putAll(commonHibernateSettings);
        return settings;
    }

    private static void generateH2Schema(String dbName) {
        Map<String, Object> settings = createCommonHibernateSettings();
        settings.put("hibernate.dialect", H2Dialect.class);
        settings.put("hibernate.id.new_generator_mappings", false);

        String[] additionalMetadataSources = new String[0];

        doGenerateSchema(dbName, settings, additionalMetadataSources);
    }

    private static void generateOracle12Schema(String dbName) {
        Map<String, Object> settings = createCommonHibernateSettings();
        settings.put("hibernate.dialect", Oracle12cDialect.class);

        // Look at file for explanation
        String[] additionalMetadataSources = {"META-INF/orm.xml"};

        doGenerateSchema(dbName, settings, additionalMetadataSources);
    }

    private static void doGenerateSchema(String dbName, Map<String, Object> settings, String[] additionalMetadataSources) {
        StandardServiceRegistry standardServiceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(settings)
                .build();

        MetadataSources metadataSources = new MetadataSources(standardServiceRegistry);

        // Add classes that we need to generate schema's for
        for(Class entityClass: jpaClasses) {
            metadataSources.addAnnotatedClass(entityClass);
        }

        // Add any additional metadata resources
        for(String metadataSource: additionalMetadataSources) {
            metadataSources.addResource(metadataSource);
        }

        Metadata metadata = metadataSources.buildMetadata();

        SchemaExport schemaExport = new SchemaExport();
        schemaExport.setFormat(true);
        schemaExport.setDelimiter(";");
        File outputFile = new File("V1__" + dbName + "_Initial_DB_Setup.sql");
        if(outputFile.exists()) {
            outputFile.delete();
        }
        schemaExport.setOutputFile(outputFile.getAbsolutePath());
        schemaExport.createOnly(EnumSet.of(TargetType.STDOUT, TargetType.SCRIPT), metadata);
    }
}
