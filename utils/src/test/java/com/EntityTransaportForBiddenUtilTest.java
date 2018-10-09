package com;

import com.po.BaseDomain;
import com.po.Clazz;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author torvalds on 2018/10/3 18:12.
 * @version 1.0
 */
public class EntityTransaportForBiddenUtilTest {
    @Test
    public void testEntity() {
        List<Clazz> clazz = new ArrayList<Clazz>();
        for (int t = 0; t < 10000; t++) {
            clazz.add(new Clazz());
        }

        Set<Class> classSet = new HashSet<>();
        classSet.add(BaseDomain.class);
        com.util.EntityValidateUtil.setCheckTargetClassSet(classSet);
        com.util.EntityValidateUtil.checkEntity(clazz);
    }

}
