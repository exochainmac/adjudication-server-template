/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.cryptoengine.jpa;

import io.axoniq.gdpr.cryptoengine.AbstractEngineTestSet;
import io.axoniq.gdpr.cryptoengine.CryptoEngine;
import io.axoniq.gdpr.cryptoengine.jpa.JpaCryptoEngine;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPATest extends AbstractEngineTestSet {

    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("myPersistenceUnit");

    protected CryptoEngine getCryptoEngine() {
        return new JpaCryptoEngine(emf);
    }

}
