package com.thitsaworks.operation_portal.component.infra.vault;

import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;

import java.net.URI;
import java.util.Objects;

public class Vault {

    private final String enginePath;

    private final VaultTemplate vaultTemplate;

    public Vault(String vaultAddress, String vaultToken, String enginePath) {

        try {

            VaultEndpoint vaultEndpoint = VaultEndpoint.from(new URI(vaultAddress));
            this.vaultTemplate = new VaultTemplate(vaultEndpoint, new TokenAuthentication(vaultToken));
            this.enginePath = enginePath;

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    public <T> T get(String path, Class<T> template) {

        VaultKeyValueOperations keyValueOperations =
            this.vaultTemplate.opsForKeyValue(this.enginePath,
                                              VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);

        return Objects.requireNonNull(keyValueOperations.get(path, template)).getData();
    }

}
