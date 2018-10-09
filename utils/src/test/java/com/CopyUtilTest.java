package com;

import com.dto.SuperStudentDto;
import com.po.Clazz;
import com.po.Student;
import com.po.SuperStudent;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author torvalds on 2018/9/16 1:59.
 * @version 1.0
 */
public class CopyUtilTest {
    @Test
    public void testCopy() {
        List<Student> students = new ArrayList<>();
        Clazz clazz = new Clazz();
        clazz.setId(UUID.randomUUID().toString());
        for (int t = 0; t < 10; t++) {
            SuperStudent student = new SuperStudent();
            student.setId(UUID.randomUUID().toString());
            student.setAge(25);
            student.setMoney(new BigDecimal(100));
            student.setName("张明");
            student.setSuperName("superName");
            student.setClazz(clazz);
            students.add(student);
        }

//        clazz.setStudents(students);

        long startTime = System.currentTimeMillis();
        List<SuperStudentDto> studentDtos = CopyUtil.mapList(students, SuperStudentDto.class);
//        List<StudentDto> studentDtos = BeanMapper.mapList(students, Student.class, StudentDto.class);
//        ClazzDto clazzDto = CopyUtil.map(clazz, ClazzDto.class);
        System.out.println("(System.currentTimeMillis()-startTime) = " + (System.currentTimeMillis() - startTime));
//        System.out.println("studentDto " + studentDtos);
    }
}
