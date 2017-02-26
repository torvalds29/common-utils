package com.oracle.utils;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * Created by oracle on 2017/2/26.
 */
public class Copy {
    private Object from;
    private Object to;
    private boolean excludeNull = true;

    public static Copy create() {
        return new Copy();
    }

    /**
     * 生成一个是否拷贝空值的拷贝对象，true为不拷贝空值，false为拷贝空值，默认为true
     *
     * @param excludeNull
     * @return
     */
    public static Copy create(boolean excludeNull) {
        return new Copy().excludeNull(excludeNull);
    }


    public Copy from(Object from) {
        this.from = from;
        done();
        return this;
    }

    public Copy to(Object to) {
        this.to = to;
        done();
        return this;
    }

    private Copy excludeNull(boolean excludeNull) {
        this.excludeNull = excludeNull;
        return this;
    }

    public void done() {
        if (from != null && to != null) {
            //拷贝当前类属性同时拷贝父类属性
            Class<?> fromSubClass = from.getClass();
            Class<?> toSubClass = to.getClass();
            while (fromSubClass.getSuperclass() != null || fromSubClass == from) {
                Field[] fromDeclaredFields = fromSubClass.getDeclaredFields();
                Field.setAccessible(fromDeclaredFields, true);
                for (Field fromDeclaredField : fromDeclaredFields) {
                    try {
                        Field toDeclaredField = toSubClass.getDeclaredField(fromDeclaredField.getName());
                        copyField(fromDeclaredField, toDeclaredField, from, to);
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                }
                fromSubClass = fromSubClass.getSuperclass();
                toSubClass = toSubClass.getSuperclass();
            }
        }
    }

    private void copyField(Field fromDeclaredField, Field toDeclaredField, Object from, Object to) {
        try {
            Object value = fromDeclaredField.get(from);
            if (excludeNull) {
                if (value != null) {
                    copyFieldValue(toDeclaredField, value, to);
                }
            } else {
                copyFieldValue(toDeclaredField, value, to);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyFieldValue(Field toDeclaredField, Object value, Object to) throws NoSuchFieldException, IllegalAccessException {
        Field.setAccessible(new AccessibleObject[]{toDeclaredField}, true);
        toDeclaredField.set(to, value);
    }


    public Object getTo() {
        return to;
    }

    public Object getFrom() {
        return from;
    }


    public boolean isExcludeNull() {
        return excludeNull;
    }


}
