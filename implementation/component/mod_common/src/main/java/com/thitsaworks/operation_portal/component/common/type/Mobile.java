package com.thitsaworks.operation_portal.component.common.type;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import com.thitsaworks.operation_portal.component.misc.exception.InputException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import lombok.Getter;

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

        if (!Pattern.matches(FORMAT, value)) {

            throw new InputException(new ErrorMessage("FORMAT_ERROR", "Invalid mobile format."));
        }

        this.value = value;

    }

    @Override
    public String toString() {

        return this.value;
    }

    public String getPlainValue(String discardingPrefix, String replacement) {

        return this.value.replaceFirst(discardingPrefix, replacement);
    }

}
