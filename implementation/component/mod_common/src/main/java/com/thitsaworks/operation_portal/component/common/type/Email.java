package com.thitsaworks.operation_portal.component.common.type;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import com.thitsaworks.operation_portal.component.misc.exception.InputException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.Serializable;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Email {

    public static final String FORMAT = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @EqualsAndHashCode.Include
    private String value;

    public Email(String value) {

        assert value != null : "Value is required.";

        if (!Pattern.matches(FORMAT, value)) {
            throw new InputException(new ErrorMessage("FORMAT_ERROR", "Invalid email format."));
        }

        this.value = value;
    }

    @Override
    public String toString() {

        return this.value;
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
