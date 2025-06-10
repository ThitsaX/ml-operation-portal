package com.thitsaworks.operation_portal.datasource.cache;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Host;
import com.aerospike.client.policy.ClientPolicy;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@PropertySource(value = {"classpath:/aerospike_configuration.properties"})
public class AerospikeConfiguration {

    @Autowired
    private Environment env;

    @Autowired
    private Settings settings;

    @Bean(destroyMethod = "close")
    public AerospikeClient aerospikeClient() {

        String hosts = this.env.getRequiredProperty("aerospike_configuration.hosts");

        String[] hostNames = hosts.split("\\|", -1);

        List<Host> hostList = new ArrayList<>();

        for (String hostName : hostNames) {

            Host host = new Host(hostName, this.settings.port);

            hostList.add(host);

        }

        ClientPolicy policy = new ClientPolicy();

        policy.threadPool = Executors.newFixedThreadPool(this.settings.threadPoolSize);

        return new AerospikeClient(policy, hostList.toArray(new Host[hostList.size()]));

    }

//    @Bean
//    public AerospikeTemplate aerospikeTemplate(AerospikeClient aerospikeClient) {
//
//        String namespace = this.env.getRequiredProperty("aerospike_configuration.namespace");
//
//        return new AerospikeTemplate(aerospikeClient, namespace);
//
//    }

    @Value
    public static class Settings {

        int port;

        int threadPoolSize;

    }

}
