package jii.db.jpa;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JpaConverterJson implements AttributeConverter<Object, String> {

    private final static ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Object meta) {
        try {
            return objectMapper.writeValueAsString(meta);
        } catch (JsonProcessingException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

    @Override
    public Object convertToEntityAttribute(String text) {
        try {
            return objectMapper.readValue(text, Object.class);
        } catch (IOException ex) {
            log.error(ex.getMessage());
            return null;
        }
    }

}
