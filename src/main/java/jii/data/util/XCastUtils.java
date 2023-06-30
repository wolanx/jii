package jii.data.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

public class XCastUtils {

    public static <T> T convet(Object products, TypeReference<T> toValueTypeRef) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(products, toValueTypeRef);
    }

    public static <T> T map2obj(Object old, Class<T> toValueTypeRef) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);

        try {
            String content = mapper.writeValueAsString(old);
            return mapper.readValue(content, toValueTypeRef);
        } catch (JsonProcessingException e) {
            // return toValueTypeRef;
            throw new RuntimeException("error");
        }
    }

}
