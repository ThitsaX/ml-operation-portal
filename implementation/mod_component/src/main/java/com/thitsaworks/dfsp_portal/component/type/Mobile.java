package com.thitsaworks.dfsp_portal.component.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.Serializable;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Mobile implements Serializable {

    @Converter
    public static class JpaConverter implements AttributeConverter<Mobile, String> {

        @Override
        public String convertToDatabaseColumn(Mobile attribute) {

            return attribute == null ? null : attribute.value;

        }

        @Override
        public Mobile convertToEntityAttribute(String dbData) {

            return dbData == null ? null : new Mobile(dbData);

        }

    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public static final String FORMAT = "^\\+[^a-zA-Z]*$";
    private static final Pattern PATTERN = Pattern.compile(FORMAT);

    @EqualsAndHashCode.Include
    private String value;

    public Mobile(String value) {

        assert value != null : "Value is required.";

        if (!PATTERN.matcher(value).matches() && !value.isEmpty()) {

            throw new IllegalArgumentException("Value is in wrong format.");

        }

        this.value = value;

    }

    public String getPlainValue(String discardingPrefix, String replacement) {

        return this.value.replaceFirst(discardingPrefix, replacement);
    }

}
