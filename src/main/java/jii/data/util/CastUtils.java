package jii.data.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CastUtils {

    public static <T> T convet(Object products, TypeReference<T> toValueTypeRef) {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.convertValue(products, toValueTypeRef);
    }

}
