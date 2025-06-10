package com.thitsaworks.operation_portal.component.retrofit;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;

public class LongToInstant extends JsonDeserializer<Instant> {

    @Override
    public Instant deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {

        Long timestamp = p.getValueAsLong();

        return Instant.ofEpochMilli(timestamp);

    }

}
