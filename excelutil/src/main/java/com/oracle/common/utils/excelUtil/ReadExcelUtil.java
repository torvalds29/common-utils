package com.oracle.common.utils.excelUtil;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by oracle on 2017/2/25.
 */
public class ReadExcelUtil {
    
    /**
     * 读取exxcel 返回list
     * @param file
     * @return
     * @throws Exception
     */
    public static List readExcel(File file) throws Exception {
        Workbook wb = null;
        List result = new ArrayList();
        try {
            wb = getWorkbook(file);
            int numberOfSheets = wb.getNumberOfSheets();
            if (numberOfSheets <= 0) {
                return null;
            } else if (numberOfSheets < 2) {
                handleSingleSheetExcel(wb, result);
            } else {
                handleSheetsExcel(wb, result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return result;

    }

    /**
     * 获取Workbook
     * @param file
     * @return
     * @throws IOException
     */
    private static Workbook getWorkbook(File file) throws IOException {
        Workbook wb;InputStream in = new FileInputStream(file);
        String fileName = file.getName();
        String stuff = fileName.substring(fileName.indexOf(".") + 1);
        if (stuff.endsWith("xlx")) {
            wb = new HSSFWorkbook(new BufferedInputStream(in));
        } else {
            wb = new XSSFWorkbook(new BufferedInputStream(in));
        }
        return wb;
    }

    /**
     * 处理多sheet excel
     *
     * @param wb
     * @param result
     */
    private static void handleSheetsExcel(Workbook wb, List result) {
        for (Sheet rows : wb) {
            List<List> sheetList = new ArrayList<List>();
            readRows(rows, sheetList);
            result.add(sheetList);
        }

    }

    /**
     * 读取一个 sheet
     * @param rows
     * @param rowsList
     */
    private static void readRows(Sheet rows, List<List> rowsList) {
        for (Row cells : rows) {
            List<String> cellList = new ArrayList<String>();
            for (Cell cell : cells) {
                cellList.add(cell.getStringCellValue());
            }
            rowsList.add(cellList);
        }
    }

    /**
     * 处理单sheet excel
     *
     * @param wb
     * @param result
     */
    private static void handleSingleSheetExcel(Workbook wb, List result) {
        readRows(wb.getSheetAt(0), result);
    }
}
