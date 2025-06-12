package com.thitsaworks.operation_portal.component.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Nric {

    public static final String FORMAT = "^\\d{1,2}\\\\[a-z|A-Z]+\\([a-z|A-Z]+\\)\\d{6}$";

    private static final Pattern PATTERN = Pattern.compile(FORMAT);

    @EqualsAndHashCode.Include
    private String value;

    public Nric(String value) {

        assert value != null : "Value is required.";

        if (!PATTERN.matcher(value).matches()) {

            throw new IllegalArgumentException("Value is in wrong format.");
        }

        this.value = value;

    }

    @Converter
    public static class JpaConverter implements AttributeConverter<Nric, String> {

        @Override
        public String convertToDatabaseColumn(Nric attribute) {

            return attribute == null ? null : attribute.value;

        }

        @Override
        public Nric convertToEntityAttribute(String dbData) {

            return dbData == null ? null : new Nric(dbData);

        }

    }

}
