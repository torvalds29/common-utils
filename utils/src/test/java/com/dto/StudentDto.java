package com.dto;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author torvalds on 2018/9/16 1:53.
 * @version 1.0
 */
@Data
public class StudentDto {
    private String id;
    private String name;
    private BigDecimal money;
    private int age;
    private ClazzDto clazz;
}
