package com;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.*;

/**
 * @author torvalds on 2018/9/16 1:33.
 * @version 1.0
 */
public class CopyUtil {
    private static List<String> copyPackageList = Arrays.asList(
            String.class.getPackage().getName(),
            BigDecimal.class.getPackage().getName(),
            Date.class.getPackage().getName());

    private static String idCopyProperty = "id";

    /**
     * 对象拷贝，如果有关联对象只拷贝id属性
     *
     * @param source
     * @param destinationClass
     * @param <S>
     * @param <D>
     * @return
     */
    public static <S, D> D map(S source, Class<D> destinationClass) {

        Class<?> sourceClass = source.getClass();
        Field[] destinationDeclaredFields = destinationClass.getDeclaredFields();
        D destinationObject = null;
        try {
            destinationObject = destinationClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Class destinationHolderClass = destinationClass;
        Class sourceHolderClass = sourceClass;
        while (destinationDeclaredFields != null && destinationDeclaredFields.length > 0) {
            Field.setAccessible(destinationDeclaredFields, true);
            for (Field destinationDeclaredField : destinationDeclaredFields) {
                try {
                    if (Collection.class.isAssignableFrom(destinationDeclaredField.getType())) {
                        continue;
                    }
                    Field sourcedDclaredField = sourceHolderClass.getDeclaredField(destinationDeclaredField.getName());
                    sourcedDclaredField.setAccessible(true);
                    Object sourceFieldValue = sourcedDclaredField.get(source);
                    if (sourceFieldValue == null) {
                        continue;
                    }
                    if (destinationDeclaredField.getType().isPrimitive() || copyPackageList.contains(destinationDeclaredField.getType().getPackage().getName())) {
                        destinationDeclaredField.set(destinationObject, sourceFieldValue);
                    } else {
                        Class<?> idClass = destinationDeclaredField.getType();
                        Field desidField = idClass.getDeclaredField(idCopyProperty);

                        Field idField = sourceFieldValue.getClass().getDeclaredField(idCopyProperty);
                        idField.setAccessible(true);
                        Object sourceIdFieldValue = idField.get(sourceFieldValue);
                        if (sourceFieldValue == null) {
                            continue;
                        }

                        /**
                         * desidField存在则实例化属性拷贝对象
                         */
                        Object idObject = idClass.newInstance();
                        desidField.setAccessible(true);
                        desidField.set(idObject, sourceIdFieldValue);
                        destinationDeclaredField.set(destinationObject, idObject);
                    }

                } catch (Exception e) {
                    //没有对应的属性不处理
//                e.printStackTrace();
                }
            }

            destinationHolderClass = destinationHolderClass.getSuperclass();
            sourceHolderClass = sourceHolderClass.getSuperclass();
            destinationDeclaredFields = destinationHolderClass.getDeclaredFields();
        }
        return destinationObject;
    }

    public static <S, D> List<D> mapList(Collection<S> sourceList, Class<D> destinationHolderClass) {
        List<D> destinationList = new ArrayList<>();
        if (sourceList != null && !sourceList.isEmpty()) {
            sourceList.forEach(source -> {
                destinationList.add(map(source, destinationHolderClass));
            });
        }
        return destinationList;
    }

    public static <S, D> List<D> parallelMapList(Collection<S> sourceList, Class<D> destinationHolderClass) {
        List<D> destinationList = new ArrayList<>();
        if (sourceList != null && !sourceList.isEmpty()) {
            sourceList.stream().parallel().forEach(source -> {
                destinationList.add(map(source, destinationHolderClass));
            });
        }
        return destinationList;
    }
}
