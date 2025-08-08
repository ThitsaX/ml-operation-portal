package com.thitsaworks.operation_portal.component.security;

import com.google.common.io.BaseEncoding;
import com.thitsaworks.operation_portal.component.misc.security.OperationPortalCrypto;
import com.thitsaworks.operation_portal.component.test.EnvAwareUnitTest;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

public class OperationPortalCryptoUnitTest extends EnvAwareUnitTest {

    @Test
    public void test_hmacsha256() {

        String method = "POST";

        String uri = "/secured/create_new_user";
        String signatureOfPayload = DigestUtils.sha256Hex(
                                                       "{\"name\":\"wallet2 admin\",\"email\":\"wallet2admin@gmail.com\",\"password\":\"123456\",\"first_name\":\"Wallet 2\",\"last_name\":\"admin\",\"job_title\":\"PM\",\"participant_id\":\"486552745708363776\",\"user_role_type\":\"ADMIN\",\"is_active\":true}").toUpperCase();

        String message = method + "|" + uri + "|" + signatureOfPayload;

        System.out.println("signatureOfPayload : " + signatureOfPayload);
        System.out.println("message : " + message);
        System.out.println("header : " + BaseEncoding.base16().encode(OperationPortalCrypto.hmacSha256(
                "ea3184c0-0c70-4ab5-af24-adb3ac3b6885".getBytes(StandardCharsets.UTF_8),
                message.getBytes(StandardCharsets.UTF_8))));

    }

}
