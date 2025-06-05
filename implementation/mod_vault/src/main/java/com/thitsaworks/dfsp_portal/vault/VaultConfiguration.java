package com.thitsaworks.dfsp_portal.vault;

import com.thitsaworks.dfsp_portal.component.security.JasyptCrypto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.vault.authentication.TokenAuthentication;
import org.springframework.vault.client.VaultEndpoint;
import org.springframework.vault.core.VaultTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@ComponentScan("com.thitsaworks.dfsp_portal.vault")
@PropertySource(value = {"classpath:/dfsp_portal_vault.properties"})
public class VaultConfiguration {

    @Autowired
    private Environment env;

    @Bean
    public VaultTemplate vaultTemplate(JasyptCrypto jasyptCrypto) throws URISyntaxException {

        VaultEndpoint vaultEndpoint =
                VaultEndpoint.from(new URI(this.env.getRequiredProperty("mod_vault.vaultAddress")));

        VaultTemplate vaultTemplate = new VaultTemplate(vaultEndpoint,
                new TokenAuthentication(
                        jasyptCrypto.decrypt(this.env.getRequiredProperty("mod_vault.vaultKey"))));

        return vaultTemplate;
    }

    @Bean
    public DfspVault dfspVault(VaultTemplate vaultTemplate) {

        return new DfspVault(vaultTemplate);

    }

}
