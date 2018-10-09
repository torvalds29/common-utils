package com.util;

import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author torvalds on 2018/10/3 18:12.
 * @version 1.0
 */
public class EntityValidateUtil {
    private static Set<Class> checkTargetAnnotationSet = new HashSet<>();
    private static Set<Class> checkTargetClassSet = new HashSet<>();
    private static Set<String> javaPackageSet = new HashSet<>();

    /**
     * 校验对象是否为实体通过是否有实体注解或具有实体类的父类校验
     */
    static {
        checkTargetAnnotationSet.add(Entity.class);
        checkTargetAnnotationSet.add(Table.class);


        javaPackageSet.add(String.class.getPackage().getName());
        javaPackageSet.add(BigDecimal.class.getPackage().getName());
        javaPackageSet.add(Date.class.getPackage().getName());
    }

    public static void checkEntity(Object o) throws IllegalArgumentException {
        if (o instanceof Collection) {
            checkCollection((Collection) o);
        } else if (o instanceof Map) {
            checkCollection(((Map) o).keySet());
            checkCollection(((Map) o).values());
        } else {
            checkObj(o);
        }
    }

    public static void checkCollection(Collection collection) {
        for (Object o : collection) {
            checkObj(o);
        }
    }

    private static void checkObj(Object o) {
        if (o != null) {
            Class<?> targetClass = o.getClass();
            checkTarget(targetClass, new HashSet<>());
        }
    }

    private static void checkTarget(Class<?> targetClass, Set<Class> existClassSet) {

        for (Class illegalClass : checkTargetAnnotationSet) {
            if (targetClass.isAnnotationPresent(illegalClass)) {
                throw new IllegalArgumentException(" 包含非法实体 " + targetClass.getName());
            }
        }
        for (Class illegalClass : checkTargetClassSet) {
            if (illegalClass.isAssignableFrom(targetClass)) {
                throw new IllegalArgumentException(" 包含非法实体 " + targetClass.getName());
            }
        }
        if (targetClass.isPrimitive() || javaPackageSet.contains(targetClass.getPackage().getName()) || existClassSet.contains(targetClass)) {
            return;
        }
        existClassSet.add(targetClass);
        Field[] declaredFields = targetClass.getDeclaredFields();
        if (declaredFields != null && declaredFields.length > 0) {
            for (Field declaredField : declaredFields) {
                Class<?> type = declaredField.getType();
                if (Collection.class.isAssignableFrom(type) || Map.class.isAssignableFrom(type)) {
                    if (!(declaredField.getGenericType() instanceof ParameterizedTypeImpl)) {
                        throw new IllegalArgumentException("使用集合请指定泛型 " + targetClass.getName() + " [" + declaredField.getType().getName() + " " + declaredField.getName() + "]");
                    }
                    Type[] actualTypeArguments = ((ParameterizedTypeImpl) declaredField.getGenericType()).getActualTypeArguments();
                    for (Type actualTypeArgument : actualTypeArguments) {
                        checkTarget((Class<?>) actualTypeArgument, existClassSet);
                    }
                } else {
                    checkTarget(type, existClassSet);
                }
            }
        }

    }


    public static Set<Class> getCheckTargetAnnotationSet() {
        return checkTargetAnnotationSet;
    }

    public static void setCheckTargetAnnotationSet(Set<Class> checkTargetAnnotationSet) {
        EntityValidateUtil.checkTargetAnnotationSet = checkTargetAnnotationSet;
    }

    public static Set<Class> getCheckTargetClassSet() {
        return checkTargetClassSet;
    }

    public static void setCheckTargetClassSet(Set<Class> checkTargetClassSet) {
        EntityValidateUtil.checkTargetClassSet = checkTargetClassSet;
    }

    public static Set<String> getJavaPackageSet() {
        return javaPackageSet;
    }

    public static void setJavaPackageSet(Set<String> javaPackageSet) {
        EntityValidateUtil.javaPackageSet = javaPackageSet;
    }
}
