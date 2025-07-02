package com.thitsaworks.operation_portal.component.infra.hazelcast;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;

public class HazelcastConfiguration {

    public static final String HAZELCAST_SETTINGS_PATH = "hazelcast/settings";

//    @Bean(destroyMethod = "shutdown")
//    public HazelcastInstance hazelcastInstance(Settings settings) {
//
//        String clusterName = settings.clusterName;
//        String hostAddress = settings.hostAddress;
//
//        ClientConfig clientConfig = new ClientConfig();
//        clientConfig.setClusterName(clusterName);
//        clientConfig.getNetworkConfig().addAddress(hostAddress);
//
//        return HazelcastClient.newHazelcastClient(clientConfig);
//
//    }

    public record Settings (String clusterName, String hostAddress) {

    }

}
