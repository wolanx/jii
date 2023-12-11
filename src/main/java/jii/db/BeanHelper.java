package jii.db;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.*;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

@Slf4j
public class BeanHelper {

    public static void copyNotNull(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
    }

    /*public static void copyFromMap(Map<String, Object> source, Object target) {
        BeanUtilsBean ins = BeanUtilsBean.getInstance();
        Converter converter = new DateConverter(null);
        ins.getConvertUtils().register(false, true, 0);
        ins.getConvertUtils().register(converter, Date.class);
        try {
            Map<String, Object> source2 = convertSnakeToCamel(source);
            org.apache.commons.beanutils.BeanUtils.copyProperties(target, source2);
            System.out.println("target = " + target);
        } catch (IllegalAccessException | InvocationTargetException e) {
            log.error(e.getMessage(), e);
        }
        ins.getConvertUtils().deregister();
    }*/

    public static void copyFromMap(Map<String, Object> source, Object target) {
        ObjectMapper objectMapper = new ObjectMapper();

        Map<String, Object> camel = convertSnakeToCamel(source);
        Object source2 = objectMapper.convertValue(camel, target.getClass());

        List<String> updatedKey = camel.keySet().stream().toList();
        copyProperties(source2, target, updatedKey);
    }

    /**
     * org.springframework.beans.BeanUtils.copyProperties 魔改
     */
    private static void copyProperties(Object source, Object target, List<String> allowList) throws BeansException {
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Class<?> actualEditable = target.getClass();
        PropertyDescriptor[] targetPds = BeanUtils.getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null && allowList.contains(targetPd.getName())) {
                PropertyDescriptor sourcePd = BeanUtils.getPropertyDescriptor(source.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null) {
                        ResolvableType sourceResolvableType = ResolvableType.forMethodReturnType(readMethod);
                        ResolvableType targetResolvableType = ResolvableType.forMethodParameter(writeMethod, 0);

                        // Ignore generic types in assignable check if either ResolvableType has unresolvable generics.
                        boolean isAssignable = (sourceResolvableType.hasUnresolvableGenerics() || targetResolvableType.hasUnresolvableGenerics() ? ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType()) : targetResolvableType.isAssignableFrom(sourceResolvableType));

                        if (isAssignable) {
                            try {
                                if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                    readMethod.setAccessible(true);
                                }
                                Object value = readMethod.invoke(source);
                                if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                    writeMethod.setAccessible(true);
                                }
                                writeMethod.invoke(target, value);
                            } catch (Throwable ex) {
                                throw new FatalBeanException("Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                            }
                        }
                    }
                }
            }
        }
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
