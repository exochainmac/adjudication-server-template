/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.cryptoengine;

import org.junit.Assume;
import org.junit.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.nio.charset.StandardCharsets;

/*
 * To make this test work on any given machine, install SoftHSM (https://www.opendnssec.org/softhsm/),
 * initialize a token, and adjust the configuration below accordingly.
 *
 * The test will be ignored if the native lib can't be found.
 */
public class PKCS11Test extends AbstractEngineTestSet {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final String SOFTHSM_NATIVE_LIB = "C:\\dev\\tools\\SoftHSM2\\lib\\softhsm2-x64.dll";
    private static final String TOKEN_PIN = "1234";
    private static final CryptoEngine cryptoEngine;

    static {
        if(new File(SOFTHSM_NATIVE_LIB).exists()) {
            StringBuilder sb = new StringBuilder();
            sb.append("name = SoftHSM").append("\r\n");
            sb.append("library = ").append(SOFTHSM_NATIVE_LIB).append("\r\n");
            InputStream configStream = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
            cryptoEngine = new PKCS11CryptoEngine(configStream, TOKEN_PIN.toCharArray());
        } else {
            logger.warn("SoftHSM native lib not found - all PKCS#11 test will be skipped");
            cryptoEngine = null;
        }
    }

    @Before
    public void beforeMethod() {
        Assume.assumeTrue(cryptoEngine != null);
    }

    protected CryptoEngine getCryptoEngine() {
        return cryptoEngine;
    }
}
