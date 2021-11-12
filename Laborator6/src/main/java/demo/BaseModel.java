package demo;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public interface BaseModel {

    default Map<String, Object> sparseFields(String[] fields) {
        return Arrays.stream(getClass().getDeclaredFields())
                .filter(field -> Arrays.asList(fields).contains(field.getName()))
                .map(field -> Map.entry(field.getName(), getFieldValue(field, this) ))
                .filter(entry -> entry.getValue() != null)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private static Object getFieldValue(Field field, Object object) {
        field.setAccessible(true);
        try {
            return field.get(object);
        } catch (IllegalAccessException e) {
           return null;
        }
    }
    static Comparator<Object> sorter(String fieldName){
        return new Comparator<>() {
            @Override
            public int compare(Object first, Object second) {
                try {
                    Object firstValue = getFieldValue(first.getClass().getDeclaredField(fieldName), first);
                    Object secondValue = getFieldValue(second.getClass().getDeclaredField(fieldName), second);
                    return firstValue.toString().toLowerCase().compareTo(secondValue.toString().toLowerCase());
                } catch (NoSuchFieldException e) {
                    return 0;
                }
            }
        };
    }
}
