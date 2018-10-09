package com.dto;

import lombok.Data;

import java.util.List;

/**
 * @author torvalds on 2018/9/16 1:56.
 * @version 1.0
 */
@Data
public class ClazzDto {
    private String id;
    private String name;
    private List<StudentDto> students;
}
