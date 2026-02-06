package com.thitsaworks.operation_portal.component.misc;

import com.fasterxml.jackson.core.StreamReadConstraints;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.thitsaworks.operation_portal.component.misc.spring.SpringContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@ComponentScan("com.thitsaworks.operation_portal.component.misc")
public class MiscConfiguration {

    @InitBinder
    public void initBinder(WebDataBinder binder) {

        binder.registerCustomEditor(String.class, new java.beans.PropertyEditorSupport() {

            @Override
            public void setAsText(String text) {

                setValue(text == null ? null : text.trim());
            }
        });
    }

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        objectMapper.getFactory()
                    .setStreamReadConstraints(
                            StreamReadConstraints.builder()
                                                 .maxStringLength(50_000_000)
                                                 .maxNestingDepth(500)
                                                 .build());

        return objectMapper;
    }

    @Bean
    public SpringContext springContext() {

        return new SpringContext();
    }

}
