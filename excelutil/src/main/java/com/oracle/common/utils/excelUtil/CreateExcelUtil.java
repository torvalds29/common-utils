package com.oracle.common.utils.excelUtil;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Created by oracle on 2016/11/7.
 */
public class CreateExcelUtil<T> {
    private List<String> sheetNames = new ArrayList<String>();
    private List<List<String>> cells = null;
    private List<T> objRows = null;
    private List<String> columnNames = null;
    private Workbook wb = null;
    private Integer pageSize = null;
    private Short colorIndex;
    private Integer pageNum;
/*    public CreateExcelUtil() {

    }*/

    /**
     * 多页sheet实例
     *
     * @param sheetNames
     * @param columnNames
     * @param cells
     * @param pageSize
     */
    public CreateExcelUtil(List<String> sheetNames, List<String> columnNames, List<List<String>> cells, Integer pageSize) {
        autoSheetNames((List<T>) cells,sheetNames,pageSize);
        initExcelData(sheetNames, columnNames, cells, null, pageSize, IndexedColors.GREEN.getIndex());
    }

    /**
     * 多页sheet实例
     *
     * @param columnNames
     * @param cells
     * @param pageSize
     */
    public CreateExcelUtil(List<String> columnNames, List<List<String>> cells, Integer pageSize) {
        initExcelData(null, columnNames, cells, null, pageSize, IndexedColors.GREEN.getIndex());
    }

    /**
     * 单页sheet实例
     *
     * @param columnNames
     * @param cells
     */
    public CreateExcelUtil(List<String> columnNames, List<List<String>> cells) {
        initExcelData(null, columnNames, cells, null, Integer.MAX_VALUE, IndexedColors.GREEN.getIndex());
    }

    /**
     * 单页sheet实例
     *
     * @param columnNames
     * @param cells
     */
    public CreateExcelUtil(String sheetNames, List<String> columnNames, List<List<String>> cells) {
        this.sheetNames.add(sheetNames);
        initExcelData(this.sheetNames, columnNames, cells, null, Integer.MAX_VALUE, IndexedColors.GREEN.getIndex());
    }

    /**
     * 多页sheet实例
     *
     * @param datas
     * @param pageSize
     * @param classType
     */
    public CreateExcelUtil(List<T> datas, Class<?> classType, Integer pageSize) {
        initExcelData(initsheetNames(datas, classType, pageSize), initColumnName(classType), null, datas, pageSize, IndexedColors.GREEN.getIndex());
    }

    /**
     * 单页sheet实例
     *
     * @param datas
     * @param classType
     */
    public CreateExcelUtil(List<T> datas, Class<?> classType) {
        this(datas, classType, Integer.MAX_VALUE);
    }


    /**
     * 初始化列名
     *
     * @param classType
     * @return
     */
    private List<String> initColumnName(Class<?> classType) {
        List<String> columnNames = LoadExcelAnnotationValueUtil.loadColumnNames(classType);
        return columnNames;
    }


    /**
     * 初始化sheet标题
     *
     * @param datas
     * @param classType
     * @param pageSize
     * @return
     */
    private List<String> initsheetNames(List<T> datas, Class<?> classType, Integer pageSize) {
        List<String> sheetNames = LoadExcelAnnotationValueUtil.loadSheetNames(classType);
        return autoSheetNames(datas, sheetNames, pageSize);
    }

    /**
     * 自动生成页标题
     *
     * @param datas
     * @param sheetNames
     * @param pageSize
     * @return
     */
    private List<String> autoSheetNames(List<T> datas, List<String> sheetNames, Integer pageSize) {
        initPageNum(datas, pageSize);
        if (sheetNames.size() < pageNum) {
            int sheetIndex = sheetNames.size();
            for (int t = sheetIndex; t < pageNum; t++) {
                sheetNames.add("sheet" + (t + 1));
            }
        }
        return sheetNames;
    }

    /**
     * 计算页数
     *
     * @param datas
     * @param pageSize
     */
    private void initPageNum(List<T> datas, Integer pageSize) {
        int dSize = datas.size();
        if (dSize % pageSize == 0) {
            this.pageNum = dSize / pageSize;
        } else {
            this.pageNum = dSize / pageSize + 1;
        }
    }

    /**
     * 初始化全部创建excel所需数据
     *
     * @param sheetNames
     * @param columnNames
     * @param cells
     * @param objRows
     * @param pageSize
     * @param colorIndex
     */
    public void initExcelData(List<String> sheetNames, List<String> columnNames, List<List<String>> cells, List<T> objRows, Integer pageSize, Short colorIndex) {
        this.sheetNames = sheetNames;
        this.columnNames = columnNames;
        this.cells = cells;
        this.pageSize = pageSize;
        this.colorIndex = colorIndex;
        this.objRows = objRows;
    }

    public void create(String fileName) {
        this.wb = getWorkbook(fileName);
        if (objRows != null) {
            createObjExcel();
        } else {
            createDataExcel();
        }

    }

    private void createDataExcel() {
        Sheet sheet = null;
        Integer rowIndex = 0;
        int sheetIndex=0;
        for (int t = 0; t < cells.size(); t++) {
            if (t % pageSize == 0) {
                sheet = createSheet(sheetIndex);
                sheetIndex++;
                if (columnNames == null || columnNames.isEmpty()) {
                    rowIndex = 0;
                } else {
                    rowIndex = 1;
                }
            }
            Row row = sheet.createRow(rowIndex);
            rowIndex++;
            List<String> cells = (List<String>) this.cells.get(t);
            for (int p = 0; p < cells.size(); p++) {
                Cell cell = row.createCell(p);
                String cellValue = cells.get(p);
                if (cellValue != null) {
                    cell.setCellValue(cellValue);
                } else {
                    cell.setCellValue("");
                }
            }
        }
    }


    private void createObjExcel() {
        Sheet sheet = null;
        Integer rowIndex = 0;
        int sheetIndex=0;
        for (int t = 0; t < objRows.size(); t++) {
            if (t % pageSize == 0) {
                sheet = createSheet(sheetIndex);
                sheetIndex++;
                if (columnNames == null || columnNames.isEmpty()) {
                    rowIndex = 0;
                } else {
                    rowIndex = 1;
                }
            }
            Row row = sheet.createRow(rowIndex);
            rowIndex++;
            T obj = objRows.get(t);
            try {
                List<Object> cellsValues = getCellsValues(obj);
                for (int p = 0; p < cellsValues.size(); p++) {
                    Cell cell = row.createCell(p);
                    if (cellsValues.get(p) != null) {
                        cell.setCellValue(cellsValues.get(p).toString());
                    } else {
                        cell.setCellValue("");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void write(OutputStream outputStream) {
        try {
            wb.write(outputStream);
            outputStream.close();
            wb.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建第t个sheet
     *
     * @return
     * @param sheetIndex
     */
    private Sheet createSheet(int sheetIndex) {
        Map<String, CellStyle> styles = createStyles(wb);
        Sheet sheet;
        if(sheetNames!=null&&!sheetNames.isEmpty()){
            sheet = wb.createSheet(sheetNames.get(sheetIndex));
        }else{
            sheet = wb.createSheet();
        }

        Row row = sheet.createRow(0);
        if (columnNames != null) {
            for (int l = 0; l < columnNames.size(); l++) {
                Cell cell = row.createCell(l);
                cell.setCellValue(columnNames.get(l));
                cell.setCellStyle(styles.get("title"));
            }
        }
        PrintSetup printSetup = sheet.getPrintSetup();
        printSetup.setLandscape(true);
        //the following three statements are required only for HSSF
        sheet.setAutobreaks(true);
        printSetup.setFitHeight((short) 1);
        printSetup.setFitWidth((short) 1);
        return sheet;
    }

    /**
     * 创建Workbook实例
     *
     * @param fileName
     * @return
     */
    public Workbook getWorkbook(String fileName) {
        try {
            Workbook wb;
            String stuff = fileName.substring(fileName.indexOf(".") + 1);
            if (stuff.endsWith("xls")) {
                wb = new HSSFWorkbook();
            } else {
                wb = new XSSFWorkbook();
            }
            return wb;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取excel长度
     *
     * @return
     */
    public Integer availableLength() {
        String tempFile = "tempFile" + new Date().getTime();
        String FilePath = getClass().getClassLoader().getResource("").getPath();
        File file = new File(FilePath + tempFile);
        try {
            OutputStream out = new FileOutputStream(file);
            wb.write(out);
            out.close();
            InputStream in = new FileInputStream(file);
            int available = in.available();
            in.close();
            file.delete();
            return available;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * java反射获取 list中的对象数据
     *
     * @param t
     * @param <T>
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    private <T> List<Object> getCellsValues(T t) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        List<Object> cells = new ArrayList<Object>();
        Class<?> clazz = t.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String name = field.getName();
            String upperName = name.substring(0, 1).toUpperCase()
                    + name.substring(1);
            Method method = clazz.getMethod("get" + upperName);
            Object value = method.invoke(t);
            cells.add(value);
        }
        return cells;
    }

    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();
        CellStyle style;
        Font monthFont = wb.createFont();
//        monthFont.setFontHeightInPoints((short)12);
        monthFont.setColor(IndexedColors.WHITE.getIndex());
        monthFont.setBold(true);
        style = wb.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setFillForegroundColor(colorIndex);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFont(monthFont);
        styles.put("title", style);
        return styles;
    }

    public List<String> getsheetNames() {
        return sheetNames;
    }

    public void setsheetNames(List<String> sheetNames) {
        this.sheetNames = sheetNames;
    }

    public List<List<String>> getCells() {
        return cells;
    }

    public void setCells(List<List<String>> cells) {
        this.cells = cells;
    }

    public Workbook getWb() {
        return wb;
    }

    public void setWb(Workbook wb) {
        this.wb = wb;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Short getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(Short colorIndex) {
        this.colorIndex = colorIndex;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public List<String> getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(List<String> columnNames) {
        this.columnNames = columnNames;
    }
}
