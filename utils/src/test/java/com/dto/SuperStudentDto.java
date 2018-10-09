package com.dto;

import lombok.Data;

/**
 * @author torvalds on 2018/9/16 1:53.
 * @version 1.0
 */
@Data
public class SuperStudentDto extends StudentDto {
    private String id;
    private String superName;
}
