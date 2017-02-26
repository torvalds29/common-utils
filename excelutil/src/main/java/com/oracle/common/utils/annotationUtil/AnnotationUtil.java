package com.oracle.common.utils.annotationUtil;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oracle on 2016/11/8.
 */
public class AnnotationUtil {
    /**
     * java反射获取注解
     *
     * @param className
     * @param annotationClass
     * @return
     * @throws Exception
     */
    @SuppressWarnings("all")
    public static List<Annotation> loadAnnotationFieldVlaue(String className, Class<? extends Annotation> annotationClass) throws Exception {
        List<Annotation> annotations = new ArrayList<Annotation>();
        Field[] declaredFields = Class.forName(className).getDeclaredFields();
        for (Field declaredField : declaredFields) {
            if (declaredField.isAnnotationPresent(annotationClass)) {
                Annotation annotation = declaredField.getAnnotation(annotationClass);
                annotations.add(annotation);
            }
        }
        return annotations;
    }

    @SuppressWarnings("all")
    public static Annotation loadAnnotationClassVlaue(String className, Class<? extends Annotation> annotationClass) throws Exception {
        Annotation annotation = null;
        Class<?> clazz = Class.forName(className);
        if (clazz.isAnnotationPresent(annotationClass)) {
            annotation = clazz.getAnnotation(annotationClass);
        }
        return annotation;
    }
}
