package com.qinjee.masterdata.utils.excel;

import com.alibaba.fastjson.JSON;
import com.qinjee.masterdata.model.entity.CustomField;
import com.qinjee.masterdata.model.entity.CustomTableData;
import lombok.Data;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 高雄
 * @version 1.0.0
 * @Description TODO
 * @createTime 2019年09月29日 08:49:00
 */
@Data
public class ExcelHandler<T> {

    private List<T> entityList;

    private List<CustomTableData> customTableDataList;

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public ExcelHandler(){
        entityList = new ArrayList<>();
        customTableDataList = new ArrayList<>();
    }

    /**
     *
     * @param workbook
     * @param t
     * @param customFieldList 所有自定义表的数据 包含是否系统自定义属性
     * @return
     * @throws Exception
     */
    public ExcelHandler<T> Handler(Workbook workbook,T t, List<CustomField> customFieldList) throws Exception {
        try {
            if (workbook != null) {
                int numOfSheet = workbook.getNumberOfSheets();
               List<CustomField> columns = null;
                short cellNum = 0;
                // 解析sheet
                for (int j = 0; j < numOfSheet; j++) {
                    Sheet sheet = workbook.getSheetAt(j);
                    Row row;
                    int realRow = getExcelRealRow(sheet);
                    for (int i = 0; i < realRow; i++) {
                        row = sheet.getRow(i);
                        if (i == 0) {
                            cellNum = row.getLastCellNum();
                            columns = parseExcelRowTitle(row, cellNum,customFieldList, j);
                        }else {
                            parseExcelRow(row, cellNum, t, j, columns);
                        }
                    }
                }
            }else {
                return null;
            }
        } catch (Exception e) {
            String message = e.getMessage();
            if(StringUtils.isNotBlank(message)){
                throw new Exception(message);
            }else {
                throw new Exception("导入文件解析异常!");
            }
        }
        return null;
    }

    /**
     * 获取Excel表的真实行数
     * @param sheet
     * @return
     */
    public int getExcelRealRow(Sheet sheet) {

        CellReference cellReference = new CellReference("A4");
        boolean flag = false;
//        System.out.println("总行数：" + (sheet.getLastRowNum() + 1));
        for (int i = cellReference.getRow(); i <= sheet.getLastRowNum(); ) {
            Row r = sheet.getRow(i);
            if (r == null) {
                // 如果是空行（即没有任何数据、格式），直接把它以下的数据往上移动
                sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
                continue;
            }
            flag = false;
            for (Cell c : r) {
                if (c.getCellType() != Cell.CELL_TYPE_BLANK) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                i++;
                continue;
            } else {
                // 如果是空白行（即可能没有数据，但是有一定格式）
                if (i == sheet.getLastRowNum())// 如果到了最后一行，直接将那一行remove掉
                    sheet.removeRow(r);
                else//如果还没到最后一行，则数据往上移一行
                    sheet.shiftRows(i + 1, sheet.getLastRowNum(), -1);
            }
        }
//        System.out.println("总行数：" + (sheet.getLastRowNum() + 1));
        return sheet.getLastRowNum() + 1;
    }

    /**
     * 解析第一行row数据
     * */
    private List<CustomField> parseExcelRowTitle(Row firstRow, short cellNum,List<CustomField> customFieldList, int numOfSheet) throws Exception {
        List<CustomField> results = new ArrayList<>();
        for (int i = 0; i < cellNum; i++) {
            Cell cell = firstRow.getCell(i);
            if (cell == null) {
                throw new Exception((numOfSheet + 1) + "页,第1行的第" + (i + 1) + "格不能为空!" );
            }else {
                // 定义每一个cell的数据类型
                cell.setCellType(Cell.CELL_TYPE_STRING);
                // 取出cell中的value
                String cellValue = cell.getStringCellValue();
                List<CustomField> fields = customFieldList.stream().filter(customField -> {
                    String fieldCode = customField.getFieldCode();
                    if (fieldCode.equals(cellValue)) {
                        return true;
                    }
                    return false;
                }).collect(Collectors.toList());

                if(CollectionUtils.isEmpty(fields)){
                    throw new Exception((numOfSheet + 1) + "页,第1行的第" + (i + 1) + "格列名错误!" );
                }
                CustomField customField = fields.get(0);
                results.add(customField);
            }
        }
        return results;
    }

    /**
     * 解析每一个row数据
     * */
    private void parseExcelRow(Row row, int cellNum, T t, int numOfSheet, List<CustomField> columns) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Class<?> aClass = t.getClass();
        Boolean isMust = false;
        CustomTableData customTableData = new CustomTableData();
        T obj = (T) aClass.newInstance();
        for (int i = 0; i < cellNum; i++) {
            CustomField customField = columns.get(i);
            //字段类型
            String fieldType = customField.getFieldType();
            //是否是系统自定义
            Short isSystemDefine = customField.getIsSystemDefine();
            String fieldName = fieldToProperty(customField.getFieldName());
            Field declaredField = null;
            if(isSystemDefine == 1){
                declaredField = aClass.getDeclaredField(fieldName);
                declaredField.setAccessible(true);
            }

            Cell cell = row.getCell(i);
            CellStyle cellStyle = cell.getCellStyle();
            short foregroundColor = cellStyle.getFillForegroundColor();
            if (foregroundColor == HSSFColor.YELLOW.index) {
                //必填
                isMust = true;
            }
            if (cell == null && isMust) {
                throw new Exception((numOfSheet + 1) + "页,第1行的第" + (i + 1) + "格不能为空!");
            } else {
                if (cell != null) {
                    int cellType = cell.getCellType();
                    //判断是否为数字类型
                    if (cellType == Cell.CELL_TYPE_NUMERIC) {
                        //如果是判断是否为日期类型
                        if (HSSFDateUtil.isCellDateFormatted(cell)) {
                            //获取日期的时间
                            Date date = cell.getDateCellValue();
                            //判断excel中日期格式的字符串样式
                            String dataFormat = cell.getCellStyle().getDataFormatString();
                            if (!"yyyy-MM-dd HH:mm:ss".equals(dataFormat)) {
                                throw new Exception((numOfSheet + 1) + "页,第1行的第" + (i + 1) + "时间格式错误!");
                            }
                            if(isSystemDefine == 1){
                                map.put(fieldName,date);
                            }else {
                                declaredField.set(obj, date);
                            }
                        }
                        //数值类型的值
                        else {
                            cell.setCellType(Cell.CELL_TYPE_STRING);
                            String value = cell.getStringCellValue();
                            if(isSystemDefine == 1){
                                map.put(fieldName,value);
                            }else {
                                declaredField.set(obj, value);
                            }
                        }
                    } else if (cellType == Cell.CELL_TYPE_STRING) {
                        //返回字符类型的值
                        if(isSystemDefine == 1){
                            map.put(fieldName,cell.getStringCellValue());
                        }else {
                            declaredField.set(obj, cell.getStringCellValue());
                        }
                    } else if (cellType == Cell.CELL_TYPE_BLANK) {
                        //返回null值
                        throw new Exception((numOfSheet + 1) + "页,第1行的第" + (i + 1) + "格不能为空!");
                    } else if (cellType == Cell.CELL_TYPE_BOOLEAN) {
                        if(isSystemDefine == 1){
                            map.put(fieldName,cell.getStringCellValue());
                        }else {
                            declaredField.set(obj, cell.getStringCellValue());
                        }
                    }
                }
            }
        }
        if(!CollectionUtils.isEmpty(map)){
            customTableData.setBigData(JSON.toJSONString(map));
            customTableDataList.add(customTableData);
        }
        entityList.add(obj);
    }

    /**
     * 字段转换成对象属性 例如：user_name to userName
     * @param field
     * @return
     */
    public static String fieldToProperty(String field) {
        if (null == field) {
            return "";
        }
        char[] chars = field.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (c == '_') {
                int j = i + 1;
                if (j < chars.length) {
                    sb.append(StringUtils.upperCase(CharUtils.toString(chars[j])));
                    i++;
                }
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
