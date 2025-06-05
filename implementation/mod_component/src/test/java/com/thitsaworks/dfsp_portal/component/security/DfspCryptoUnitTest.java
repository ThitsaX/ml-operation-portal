package com.thitsaworks.dfsp_portal.component.security;

import com.google.common.io.BaseEncoding;
import com.thitsaworks.dfsp_portal.component.test.EnvAwareUnitTest;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class DfspCryptoUnitTest extends EnvAwareUnitTest {

    @Test
    public void test_hmacsha256() {

        String method = "POST";

        String uri = "/secured/generate_settlement_report";
        String signatureOfPayload = DigestUtils.sha256Hex(
                                                       "<BLANK>").toUpperCase();

        String message = method + "|" + uri + "|" + signatureOfPayload;

        System.out.println("signatureOfPayload : " + signatureOfPayload);
        System.out.println("message : " + message);
        System.out.println("header : " + BaseEncoding.base16().encode(DfspCrypto.hmacSha256(
                "09711c47-b5e6-4a5c-a9c7-23985c4e89f5".getBytes(StandardCharsets.UTF_8),
                message.getBytes(StandardCharsets.UTF_8))));

    }

}
