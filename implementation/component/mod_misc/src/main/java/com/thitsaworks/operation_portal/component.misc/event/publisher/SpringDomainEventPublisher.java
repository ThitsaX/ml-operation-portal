package com.thitsaworks.operation_portal.component.misc.event.publisher;

import com.thitsaworks.operation_portal.component.misc.event.DomainEvent;
import com.thitsaworks.operation_portal.component.misc.event.DomainEventPublisher;
import org.springframework.context.ApplicationEventPublisher;

public class SpringDomainEventPublisher implements DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    public SpringDomainEventPublisher(ApplicationEventPublisher publisher) {

        super();
        this.publisher = publisher;

    }

    @Override
    public void publish(DomainEvent event) {

        this.publisher.publishEvent(event);

    }

}