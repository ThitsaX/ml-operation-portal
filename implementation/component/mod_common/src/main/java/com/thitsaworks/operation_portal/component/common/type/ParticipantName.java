package com.thitsaworks.operation_portal.component.common.type;

import com.thitsaworks.operation_portal.component.misc.exception.ErrorMessage;
import com.thitsaworks.operation_portal.component.misc.exception.InputException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.io.Serializable;
import java.util.regex.Pattern;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ParticipantName implements Serializable {

    public static final String FORMAT = "^\\w+$";

    private static final Pattern PATTERN = Pattern.compile(FORMAT);

    @EqualsAndHashCode.Include
    private String value;

    public ParticipantName(String value) {

        assert value != null : "Value is required.";

        if (!Pattern.matches(FORMAT, value)) {

            throw new InputException(new ErrorMessage("FORMAT_ERROR", "Invalid name format."));
        }

        this.value = value;

    }

    @Converter
    public static class JpaConverter implements AttributeConverter<ParticipantName, String> {

        @Override
        public String convertToDatabaseColumn(ParticipantName attribute) {

            return attribute == null ? null : attribute.value;

        }

        @Override
        public ParticipantName convertToEntityAttribute(String dbData) {

            return dbData == null ? null : new ParticipantName(dbData);

        }

    }

}
