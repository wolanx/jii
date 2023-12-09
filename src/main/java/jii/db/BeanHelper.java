package jii.db;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BeanHelper {

    public static void copyNotNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    public static void copyFromMap(Map<String, Object> source, Object target) {
        BeanUtilsBean.getInstance().getConvertUtils().register(false, true, 0);
        try {
            Map<String, Object> source2 = convertSnakeToCamel(source);
            org.apache.commons.beanutils.BeanUtils.copyProperties(target, source2);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }
        BeanUtilsBean.getInstance().getConvertUtils().deregister();
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) {
                emptyNames.add(pd.getName());
            }
        }
        return emptyNames.toArray(new String[0]);
    }

    private static Map<String, Object> convertSnakeToCamel(Map<String, Object> snakeCaseMap) {
        Map<String, Object> camelCaseMap = new HashMap<>();
        for (Map.Entry<String, Object> entry : snakeCaseMap.entrySet()) {
            String key = convertSnakeToCamel(entry.getKey());
            camelCaseMap.put(key, entry.getValue());
        }
        return camelCaseMap;
    }

    private static String convertSnakeToCamel(String snakeCase) {
        StringBuilder camelCase = new StringBuilder();
        boolean capitalizeNext = false;
        for (char c : snakeCase.toCharArray()) {
            if (c == '_') {
                capitalizeNext = true;
            } else {
                camelCase.append(capitalizeNext ? Character.toUpperCase(c) : c);
                capitalizeNext = false;
            }
        }
        return camelCase.toString();
    }

}
