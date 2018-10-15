/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.cryptoengine;

import org.hsqldb.jdbc.JDBCDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class JDBCTest extends AbstractEngineTestSet {

    private static JdbcCryptoEngine cryptoEngine;

    static {
        JDBCDataSource jdbcDataSource = new JDBCDataSource();
        jdbcDataSource.setURL("jdbc:hsqldb:mem:mydb");
        cryptoEngine = new JdbcCryptoEngine(jdbcDataSource);

        try(Connection connection = jdbcDataSource.getConnection()) {
            connection.prepareStatement(cryptoEngine.getCreateTableStatement()).execute();
        } catch(SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

    protected CryptoEngine getCryptoEngine() {
        return cryptoEngine;
    }

}
