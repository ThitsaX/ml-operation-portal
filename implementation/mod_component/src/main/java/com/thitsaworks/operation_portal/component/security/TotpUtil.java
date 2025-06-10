package com.thitsaworks.operation_portal.component.security;

import com.google.common.io.BaseEncoding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import de.taimos.totp.TOTP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.SecureRandom;

public class TotpUtil {

    public static String secretKey() {

        byte[] forTotpSecretKey = new byte[20];

        SecureRandom secureRandom = new SecureRandom();

        secureRandom.nextBytes(forTotpSecretKey);

        return BaseEncoding.base32().encode(forTotpSecretKey);

    }

    public static String otpCode(String secret) {

        return TOTP.getOTP(BaseEncoding.base16().encode(BaseEncoding.base32().decode(secret)));

    }

    public static String googleAuthenticatorBarCode(String secretKey, String issuer, String account) {

        try {

            return "otpauth://totp/" + URLEncoder.encode(issuer + ":" + account, "UTF-8").replace("+", "%20")
                    + "?secret=" + URLEncoder.encode(secretKey, "UTF-8").replace("+", "%20") + "&issuer="
                    + URLEncoder.encode(issuer, "UTF-8").replace("+", "%20");

        } catch (UnsupportedEncodingException e) {

            throw new IllegalStateException(e);

        }

    }

    public static String base64QrCode(String barCode) {

        final int WIDTH = 256;
        final int HEIGHT = 256;

        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        try {

            BitMatrix matrix = new MultiFormatWriter().encode(barCode, BarcodeFormat.QR_CODE, WIDTH, HEIGHT);

            MatrixToImageWriter.writeToStream(matrix, "png", bout);

            byte[] qrBytes = bout.toByteArray();

            return BaseEncoding.base64().encode(qrBytes);

        } catch (IOException | WriterException e) {

            throw new IllegalArgumentException(e);

        } finally {

            if (bout != null) {

                try {

                    bout.close();

                } catch (IOException e) {

                }

            }

        }

    }

    public static boolean verify(String secretKey, String code) {

        return TOTP.validate(BaseEncoding.base16().encode(BaseEncoding.base32().decode(secretKey)), code);

    }

}
