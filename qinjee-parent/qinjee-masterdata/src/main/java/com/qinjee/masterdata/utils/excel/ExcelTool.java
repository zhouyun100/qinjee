package com.qinjee.masterdata.utils.excel;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月29日 09:29:00
 */
public class ExcelTool {

    public static Workbook initWorkBook(MultipartFile file) throws IOException {
        if (file == null || "".equals(file.getOriginalFilename().trim())) {
            return null;
        }
        String postfix = getPostfix(file.getOriginalFilename());
        if (!"".equals(postfix)) {
            InputStream input = file.getInputStream();
            Workbook workbook = null;
            //如果不是xls、xlsx，返回null
            if ("xls".equals(postfix)) {
                workbook = new HSSFWorkbook(input);
            } else if ("xlsx".equals(postfix)) {
                workbook = new XSSFWorkbook(input);

            }
            return workbook;
        } else {
            return null;
        }
    }

    /**
     * 获得path的后缀名
     *
     * @param path
     * @return
     */
    private static String getPostfix(String path) {
        if (StringUtils.isEmpty(path)) {
            return "";
        }
        if (path.contains(".")) {
            return path.substring(path.lastIndexOf(".") + 1);
        }
        return "";
    }
}
