package com.thitsaworks.operation_portal.component.event;

public interface DomainEventPublisher {

    <E extends DomainEvent> void publish(E event);

}
