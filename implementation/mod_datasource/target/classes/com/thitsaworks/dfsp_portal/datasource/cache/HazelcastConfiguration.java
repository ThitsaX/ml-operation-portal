package com.thitsaworks.dfsp_portal.datasource.cache;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import com.thitsaworks.dfsp_portal.vault.DfspVault;
import com.thitsaworks.dfsp_portal.vault.VaultConfiguration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(value = {VaultConfiguration.class})
public class HazelcastConfiguration {

    @Autowired
    private DfspVault dfspVault;

    @Bean
    public HazelcastConfiguration.Settings hazelcastConfigurationSettings() {

        HazelcastConfiguration.Settings settings =
                this.dfspVault.get("hazelcast/settings",
                        HazelcastConfiguration.Settings.class);

        return settings;
    }

    @Bean(destroyMethod = "shutdown")
    public HazelcastInstance hazelcastInstance(HazelcastConfiguration.Settings settings) {

        String clusterName = settings.clusterName;
        String hostAddress = settings.hostAddress;

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setClusterName(clusterName);
        clientConfig.getNetworkConfig().addAddress(hostAddress);

        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);

        return client;

    }

    @NoArgsConstructor
    @Getter
    public static class Settings {

        String clusterName;

        String hostAddress;

    }

}
