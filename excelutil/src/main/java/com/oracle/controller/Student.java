package com.oracle.controller;

//import com.org.excel.utils.annotation.ColumnName;
//import com.org.excel.utils.annotation.Excel;

/**
 * Created by oracle on 2016/11/7.
 */

import com.oracle.common.annotation.excelAnnotation.ColumnName;
import com.oracle.common.annotation.excelAnnotation.Excel;

/**
 * sheetName  sheet页名
 */
@Excel(sheetNames = {"sheet one", "sheet two"})
public class Student {
    @ColumnName(column = "姓名")
    private String name;
    @ColumnName(column = "年龄")
    private Integer age;
    @ColumnName(column = "分数")
    private Double grass;
    @ColumnName(column = "性别")
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Double getGrass() {
        return grass;
    }

    public void setGrass(Double grass) {
        this.grass = grass;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
