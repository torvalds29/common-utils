package com.po;

import lombok.Data;

import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * @author torvalds on 2018/9/16 1:53.
 * @version 1.0
 */
@Entity
@Data
public class Student {
    private String id;
    private String name;
    private BigDecimal money;
    private int age;
    private Clazz clazz;
}
