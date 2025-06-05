package com.thitsaworks.dfsp_portal.vault;

import lombok.AllArgsConstructor;
import org.springframework.vault.core.VaultKeyValueOperations;
import org.springframework.vault.core.VaultKeyValueOperationsSupport;
import org.springframework.vault.core.VaultTemplate;

@AllArgsConstructor
public class DfspVault {

    private VaultTemplate vaultTemplate;

    public <T> T get(String path, Class<T> template) {

        VaultKeyValueOperations keyValueOperations =
                vaultTemplate.opsForKeyValue("dfsp_portal", VaultKeyValueOperationsSupport.KeyValueBackend.KV_2);

        return keyValueOperations.get(path, template).getData();
    }

}
