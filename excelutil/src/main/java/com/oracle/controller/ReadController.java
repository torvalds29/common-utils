package com.oracle.controller;

import com.oracle.common.utils.excelUtil.ReadExcelUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;

/**
 * Created by oracle on 2017/2/25.
 */
@Controller
public class ReadController {
    @RequestMapping("/readExcel")
    @ResponseBody
    public void readExcel(){
        try {
            List list = ReadExcelUtil.readExcel(new File("D:/excelCretae - 副本.xlsx"));
            System.out.println(list.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
