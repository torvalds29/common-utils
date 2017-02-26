package com.oracle.controller;

import com.oracle.common.utils.excelUtil.CreateExcelUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oracle on 2016/11/8.
 */
@Controller
public class DownloadController {

    @RequestMapping("download.html")
    public String down() {
        return "download";
    }

    @RequestMapping("/download")
    public void download(HttpServletResponse response) {
        OutputStream out = null;
        try {
            out = response.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

        List<Student> studentList1 = new ArrayList<Student>();
        for (int i = 0; i < 200; i++) {
            Student student = new Student();
            student.setName("李明一" + i);
            student.setAge(20);
            student.setGrass(85D);
            student.setSex("男");
            studentList1.add(student);
        }
/*        List<List<String>> studentList1=new ArrayList<List<String>>();
        for (int t = 0; t < 200; t++) {
            List<String> list=new ArrayList<String>();
            for (int p = 0; p < 10; p++) {
                list.add("列"+t+p);
            }
            studentList1.add(list);
        }
        List<String> colummn=new ArrayList<String>();
        colummn.add("列名1");
        colummn.add("列名2");
        List<String> sheetNames=new ArrayList<String>();
        sheetNames.add("第一页");
        sheetNames.add("第二页");*/
        String fileName="excelCretaeTest.xls";             //excel 2003
//        String fileName = "excelCretaeTest.xlsx";        //excel 2007
        String headerValue = String.format("attachment; filename=\"%s\"",
                fileName);
        response.setHeader("Content-Disposition", headerValue);
        //设置list 实体类 页大小
        CreateExcelUtil createExcelUtil=new CreateExcelUtil(studentList1,Student.class,10);
        try {
            //设置下载文件名
            createExcelUtil.create(fileName);
            //createExcelUtils.excelAvailable()  这个函数会输出两次excel来获取长度，时间会加长一点，可以考虑不显示设置下载文件大小
            response.setContentLength(createExcelUtil.availableLength());
            //下载文件输出
            createExcelUtil.write(out);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
