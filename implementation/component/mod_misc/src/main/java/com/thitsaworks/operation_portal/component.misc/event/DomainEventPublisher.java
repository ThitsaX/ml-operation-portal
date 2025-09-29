package com.thitsaworks.operation_portal.component.misc.event;

public interface DomainEventPublisher {

    <E extends DomainEvent> void publish(E event);

}
