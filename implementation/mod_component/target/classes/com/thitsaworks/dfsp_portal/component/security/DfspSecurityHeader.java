package com.thitsaworks.dfsp_portal.component.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.Payload;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import lombok.Value;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.ParseException;

@Component
public class DfspSecurityHeader {

    @Autowired
    private ObjectMapper objectMapper;

    public String sign(String uri, String payload, byte[] secretKey) throws JOSEException, JsonProcessingException {

        payload = payload == null || payload.isEmpty() ? "{}" : payload;

        Envelope envelope = new Envelope(uri, payload);

        payload = this.objectMapper.writeValueAsString(envelope);

        JWSSigner signer = new MACSigner(secretKey);
        JWSObject jwsObject = new JWSObject(new JWSHeader(JWSAlgorithm.HS256), new Payload(payload));

        jwsObject.sign(signer);

        return jwsObject.serialize();
    }

    public boolean verify(String jwsToken, String uri, String payload, byte[] secretKey)
            throws JOSEException, ParseException, JsonProcessingException {

        if (secretKey.length > 32) {
            secretKey = DigestUtils.sha256(secretKey);
        }

        JWSVerifier verifier = new MACVerifier(secretKey);
        JWSObject jwsObject = JWSObject.parse(jwsToken);

        if (!jwsObject.verify(verifier)) {

            return false;
        }

        String verifyingSignature = this.sign(uri, payload, secretKey);

        return verifyingSignature.equals(jwsToken);
    }

    @Value
    private static class Envelope implements Serializable {

        String uri;

        String payload;

    }

}
