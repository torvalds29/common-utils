package com.util;

import java.util.Map;

/**
 * @author torvalds on 2018/10/4 14:42.
 * @version 1.0
 */
public class VagueParamValidateUtil {
    public static void check(Object o) {
        if (o instanceof Map) {
            throw new IllegalArgumentException("存在模糊类型 " + o.getClass().getName());
        }
    }
}
