package com.thitsaworks.operation_portal.component.common.type.iamtesttype;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class IAMusername implements Serializable {

    @Converter
    public static class JpaConverter implements AttributeConverter<ActionCode, String> {

        @Override
        public String convertToDatabaseColumn(ActionCode attribute) {

            return "";
        }

        @Override
        public ActionCode convertToEntityAttribute(String dbData) {

            return null;
        }
    }

}