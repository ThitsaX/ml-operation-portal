package com.thitsaworks.dfsp_portal.component.security;

import lombok.Getter;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentPBEConfig;
import org.jasypt.properties.PropertyValueEncryptionUtils;

@Getter
public class JasyptCrypto {

    private final StandardPBEStringEncryptor encryptor;

    public JasyptCrypto(String envName) {

        EnvironmentPBEConfig config = new EnvironmentPBEConfig();

        config.setAlgorithm("PBEWithMD5AndDES");

        String password = System.getenv(envName);

        if (password == null) {

            password = System.getProperty(envName);

        }

        config.setPassword(password);

        this.encryptor = new StandardPBEStringEncryptor();
        this.encryptor.setConfig(config);

    }

    public String decrypt(String encrypted) {

        return PropertyValueEncryptionUtils.decrypt(encrypted, this.encryptor);

    }

    public String encrypt(String plain) {

        return PropertyValueEncryptionUtils.encrypt(plain, this.encryptor);

    }

}
