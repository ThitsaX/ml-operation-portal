package com.thitsaworks.dfsp_portal.component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thitsaworks.dfsp_portal.component.event.DomainEventPublisher;
import com.thitsaworks.dfsp_portal.component.event.publisher.SpringDomainEventPublisher;
import com.thitsaworks.dfsp_portal.component.security.JasyptCrypto;
import com.thitsaworks.dfsp_portal.component.spring.SpringContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.event.ApplicationEventMulticaster;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.support.TaskUtils;

@ComponentScan("com.thitsaworks.dfsp_portal.component")
public class ComponentConfiguration {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Bean
    public JasyptCrypto jasyptCrypto() {

        return new JasyptCrypto("JASYPT_PASSWORD");
    }

    @Bean
    public SpringContext springContext() {

        return new SpringContext();

    }

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.findAndRegisterModules();

        return objectMapper;

    }

    @Bean
    public ApplicationEventMulticaster applicationEventMulticaster() {

        SimpleApplicationEventMulticaster eventMulticaster = new SimpleApplicationEventMulticaster();

        eventMulticaster.setTaskExecutor(new SimpleAsyncTaskExecutor("DFSPPortal - EventThread"));
        eventMulticaster.setErrorHandler(TaskUtils.LOG_AND_SUPPRESS_ERROR_HANDLER);

        return eventMulticaster;

    }

    @Bean
    public DomainEventPublisher domainEventPublisher() {

        DomainEventPublisher domainEventPublisher = new SpringDomainEventPublisher(this.applicationEventPublisher);

        return domainEventPublisher;

    }

}
