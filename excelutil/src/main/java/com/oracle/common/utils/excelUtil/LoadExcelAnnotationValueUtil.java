package com.oracle.common.utils.excelUtil;

import com.oracle.common.annotation.excelAnnotation.ColumnName;
import com.oracle.common.annotation.excelAnnotation.Excel;
import com.oracle.common.utils.annotationUtil.AnnotationUtil;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by oracle on 2017/2/25.
 */
public class LoadExcelAnnotationValueUtil {
    /**
     * 获取column注解的值
     *
     * @param classType
     * @return
     */
    public static List<String> loadColumnNames(Class<?> classType) {
        List<String> columns = new ArrayList<String>();
        try {
            List<Annotation> annotations = AnnotationUtil.loadAnnotationFieldVlaue(classType.getName(), ColumnName.class);
            for (Annotation annotation : annotations) {
                if (annotation != null) {
                    String column = ((ColumnName) annotation).column();
                    columns.add(column);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return columns;
    }

    /**
     * 获取excel注解中定义的sheet名
     *
     * @param classType
     * @return
     */
    public static List<String> loadSheetNames(Class<?> classType) {
        List<String> sheetNameList =new ArrayList<String>();
        try {
            Annotation annotation = AnnotationUtil.loadAnnotationClassVlaue(classType.getName(), Excel.class);
            if (annotation != null) {
                String[] sheetNames = ((Excel) annotation).sheetNames();
                sheetNameList.addAll(Arrays.asList(sheetNames));
//                Collections.addAll(sheetNameList,sheetNames);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sheetNameList;
    }
}
