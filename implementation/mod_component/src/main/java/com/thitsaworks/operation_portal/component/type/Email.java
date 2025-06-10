package com.thitsaworks.operation_portal.component.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.Serializable;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
public class Email implements Serializable {

    public static final String FORMAT = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,})+$";

    private static final Pattern PATTERN = Pattern.compile(FORMAT);

    @EqualsAndHashCode.Include
    private String value;

    public Email(String value) {

        assert value != null : "Value is required.";

        if (!PATTERN.matcher(value).matches() && !value.isEmpty()) {

            throw new IllegalArgumentException("Value is in wrong format.");
        }

        this.value = value;

    }

    @Converter
    public static class JpaConverter implements AttributeConverter<Email, String> {

        @Override
        public String convertToDatabaseColumn(Email attribute) {

            return attribute == null ? null : attribute.value;

        }

        @Override
        public Email convertToEntityAttribute(String dbData) {

            return dbData == null ? null : new Email(dbData);

        }

    }

}
