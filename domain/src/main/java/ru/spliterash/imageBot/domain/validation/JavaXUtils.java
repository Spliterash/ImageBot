package ru.spliterash.imageBot.domain.validation;

import lombok.experimental.UtilityClass;
import ru.spliterash.imageBot.domain.def.annotation.VariableName;

import javax.validation.ConstraintViolation;
import javax.validation.ElementKind;
import javax.validation.Path;
import java.lang.reflect.Field;

@UtilityClass
public class JavaXUtils {
    public String getFieldName(ConstraintViolation<?> violation) {
        Object leafBean = violation.getLeafBean();
        if (leafBean == null)
            return defaultName(violation);

        Path path = violation.getPropertyPath();
        Path.Node last = null;

        for (Path.Node node : path) {
            last = node;
        }
        if (last == null)
            return defaultName(violation);
        if (!last.getKind().equals(ElementKind.PROPERTY))
            return null;
        String fieldName = last.getName();

        try {
            Field field = leafBean.getClass().getDeclaredField(fieldName);

            VariableName annotation = field.getAnnotation(VariableName.class);
            if (annotation == null)
                return defaultName(violation);

            return annotation.value();
        } catch (NoSuchFieldException e) {
            return defaultName(violation);
        }
    }

    private String defaultName(ConstraintViolation<?> violation) {
        return violation.getPropertyPath().toString();
    }

    public static Object getFieldValue(Object object, String fieldName) {
        try {
            Field declaredField = object.getClass().getDeclaredField(fieldName);

            if (!declaredField.trySetAccessible())
                return null;

            return declaredField.get(object);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null;
        }
    }
}
