package com.thitsaworks.operation_portal.component.common.type;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ActionCode implements Serializable {

    @EqualsAndHashCode.Include
    private String value;

    public ActionCode(String value) {

        this.value = value;
    }

    @Converter
    public static class JpaConverter implements AttributeConverter<ActionCode, String> {

        @Override
        public String convertToDatabaseColumn(ActionCode attribute) {

            return attribute.value;
        }

        @Override
        public ActionCode convertToEntityAttribute(String dbData) {

            return new ActionCode(dbData);
        }

    }

    @Override
    public String toString() {

        return this.value;
    }

}