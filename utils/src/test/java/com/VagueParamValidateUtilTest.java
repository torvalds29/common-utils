package com;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author torvalds on 2018/10/4 14:42.
 * @version 1.0
 */
public class VagueParamValidateUtilTest {
    @Test
    public void testVagueParam() {
        Map<String,Object> testMap=new HashMap<>();
        com.util.VagueParamValidateUtil.check(testMap);
    }
}
