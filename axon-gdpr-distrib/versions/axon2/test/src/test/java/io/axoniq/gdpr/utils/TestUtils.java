/*
 * Copyright (c) 2017-2018. AxonIQ B.V.
 */
package io.axoniq.gdpr.utils;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Given the nature of our protobuf format, encrypted fields will always start with these 3 bytes:
 * 8  ->   indicating that the next value is field 1 of type varint
 * 1  ->   indicating the value 1, the version
 * 21 ->   indicating that the next value is field 2 and is fixed64
 *
 * OR, when its a storage field:
 * 8  ->   indicating that the next value is field 1 of type varint
 * 1  ->   indicating the value 1, the version
 * 22 ->   indicating that the next value is field 2 and is fixed64
 *
 *
 * In Base64, this translates to the 4 characters "CAEV" or "CAES"
 *
 * We can exploit this in our unit tests to have an easy way to detect encryption (albeit not with 100,0%
 * certainty.) This utils class provides methods doing just that.
 */
public abstract class TestUtils {

    private TestUtils() {
        throw new Error("non-instantiable utils class");
    }

    public static final String ENCRYPTED_STRING_START = "CAEV";
    public static final byte[] ENCRYPTED_BYTEARRAYSTART = new byte[] {8, 1, 21};
    public static final String ENCRYPTED_SERIALIZED_STRING_START = "CAES";
    public static final byte[] ENCRYPTED_SERIALIZED_BYTEARRAYSTART = new byte[] {8, 1, 22};

    public static boolean isEncrypted(String value) {
        return value.startsWith(ENCRYPTED_STRING_START);
    }

    public static boolean isEncrypted(byte[] value) {
        if(value.length < ENCRYPTED_BYTEARRAYSTART.length) return false;
        for(int i = 0; i < ENCRYPTED_BYTEARRAYSTART.length; i++) {
            if(value[i] != ENCRYPTED_BYTEARRAYSTART[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean isSerializedEncrypted(String value) {
        return value.startsWith(ENCRYPTED_SERIALIZED_STRING_START);
    }

    public static boolean isSerializedEncrypted(byte[] value) {
        if(value.length < ENCRYPTED_SERIALIZED_BYTEARRAYSTART.length) return false;
        for(int i = 0; i < ENCRYPTED_SERIALIZED_BYTEARRAYSTART.length; i++) {
            if(value[i] != ENCRYPTED_SERIALIZED_BYTEARRAYSTART[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean isClear(byte[] value) {
        return value != null && value.length != 0 && !isEncrypted(value);
    }

    public static boolean isClear(String value) {
        return value != null && value.length() != 0 && !isEncrypted(value);
    }

    public static <T> ArrayList<T> list(T... x) {
        return new ArrayList<T>(Arrays.asList(x));
    }
}
