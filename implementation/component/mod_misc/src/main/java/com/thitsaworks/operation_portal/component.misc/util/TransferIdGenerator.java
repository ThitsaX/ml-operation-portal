package com.thitsaworks.operation_portal.component.misc.util;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class TransferIdGenerator {

    public static String generateTransferId() {

        UUID uuid = UUID.randomUUID(); // Generate a random UUID
        return uuid.toString(); // Combine the UUID with the prefix

    }

}
