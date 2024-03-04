package com.med.notificationservice.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ClickActionConverter implements AttributeConverter<Object, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Object clickAction) {
        try {
            return objectMapper.writeValueAsString(clickAction);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error converting ClickAction to JSON", e);
        }
    }

    @Override
    public Object convertToEntityAttribute(String dbData) {
        try {
            // You may need to cast the result to the specific type expected in your entity
            return objectMapper.readValue(dbData, Object.class);
        } catch (Exception e) {
            throw new RuntimeException("Error converting JSON to ClickAction", e);
        }
    }
}
