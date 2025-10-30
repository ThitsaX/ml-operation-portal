package com.thitsaworks.operation_portal.component.common.type;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import com.thitsaworks.operation_portal.component.misc.exception.InputException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Getter
public class Password {

    public static final String FORMAT = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_\\-+={}|:;<>,.?/\\[\\]]).{6,}$";

    private static final Pattern PATTERN = Pattern.compile(FORMAT);

    @EqualsAndHashCode.Include
    private String value;

    public Password(String value) {

        assert value != null : "Password is required.";

        if (!PATTERN.matcher(value)
                    .matches()) {

            throw new InputException(new ErrorMessage(
                "PASSWORD_INVALID_FORMAT",
                "The password format is invalid. Please ensure it meets the required security criteria."));
        }

        this.value = value;

    }

    @Converter
    public static class JpaConverter implements AttributeConverter<Password, String> {

        @Override
        public String convertToDatabaseColumn(Password attribute) {

            return attribute == null ? null : attribute.value;

        }

        @Override
        public Password convertToEntityAttribute(String dbData) {

            return dbData == null ? null : new Password(dbData);

        }

    }

}
