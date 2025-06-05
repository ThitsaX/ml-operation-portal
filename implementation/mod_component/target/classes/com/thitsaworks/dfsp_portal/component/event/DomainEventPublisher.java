package com.thitsaworks.dfsp_portal.component.event;

public interface DomainEventPublisher {

    <E extends DomainEvent> void publish(E event);

}
