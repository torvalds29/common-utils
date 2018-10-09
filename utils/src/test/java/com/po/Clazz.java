package com.po;

import lombok.Data;

import java.util.Map;

/**
 * @author torvalds on 2018/9/16 1:56.
 * @version 1.0
 */
//@Entity
//@Table
@Data
public class Clazz {

    private String id;
    private String name;
    //    private List<Student> students;
    private Map<String, Student> stringStudentMap;
}
