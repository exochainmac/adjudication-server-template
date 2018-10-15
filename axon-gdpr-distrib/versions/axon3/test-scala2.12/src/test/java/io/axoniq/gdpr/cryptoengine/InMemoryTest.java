/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.cryptoengine;

public class InMemoryTest extends AbstractEngineTestSet {

    protected CryptoEngine getCryptoEngine() {
        return new InMemoryCryptoEngine();
    }

}
