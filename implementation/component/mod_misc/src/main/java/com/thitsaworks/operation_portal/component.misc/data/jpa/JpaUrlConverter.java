package com.thitsaworks.operation_portal.component.data.jpa;

import jakarta.persistence.AttributeConverter;
import java.net.MalformedURLException;
import java.net.URL;

public class JpaUrlConverter implements AttributeConverter<URL, String> {

    @Override
    public String convertToDatabaseColumn(URL attribute) {

        if (attribute == null)
            return null;

        return attribute.toString();

    }

    @Override
    public URL convertToEntityAttribute(String dbData) {

        if (dbData == null)
            return null;

        try {

            return new URL(dbData);

        } catch (MalformedURLException e) {

            throw new IllegalArgumentException();

        }

    }

}
