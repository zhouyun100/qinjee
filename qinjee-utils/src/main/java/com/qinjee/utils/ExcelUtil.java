package com.qinjee.utils;

import com.alibaba.fastjson.JSONObject;
import com.qinjee.entity.CustomField;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Administrator
 * 注意：此excel的导入导出方法需要用到三个参数，即装好数据的集合List，以及每个对象所需要展示对应的以map形式存储的字段名（key为数据库中对应的字段名，value为
 * 需要展示在excel中的列名），以及需要将excel输出到指定的文件位置
 *
 * 在自定义表中，先根据自定义表的id可以查出所需要展示的字段放进map形式中的field中
 *
 *
 * excel导出为JsonObject时，需要通过自定义表的自定义字段类型来确定是否满足类型，精度以及长度的需要
 * 1，编写获得自定义字段表的类型数据
 * 2，存入到集合中，通过字段名与excel表头来筛选所需要的字段
 * 3，获取excel对应的单元格中的内容
 * 4，内容格式与自定义字段需求的规范相比较，符合则取，不符合则放弃
 */

public class ExcelUtil {
    public static int sheetsize = 5000;

    /**
     * @param data   导入到excel中的数据
     * @param out    数据写入的文件
     * @param fields 需要注意的是这个方法中的map中：每一列对应的实体类的英文名为键，excel表格中每一列名为值
     * @throws Exception
     * @author Lyy
     */
    public static <T> void ListtoExecl(List<JSONObject> data, OutputStream out,
                                       Map<String, String> fields) throws Exception {
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 如果导入数据为空，则抛出异常。
        if (data == null || data.size() == 0) {
            workbook.close();
            throw new Exception("导入的数据为空");
        }
        // 根据data计算有多少页sheet
        int pages = data.size() / sheetsize;
        if (data.size() % sheetsize > 0) {
            pages += 1;
        }
        // 提取表格的字段名（英文字段名是为了对照中文字段名的）
        String[] egtitles = new String[fields.size()];
        String[] cntitles = new String[fields.size()];
        Iterator<String> it = fields.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            String egtitle = (String) it.next();
            String cntitle = fields.get(egtitle);
            egtitles[count] = egtitle;
            cntitles[count] = cntitle;
            count++;
        }
        // 添加数据
        for (int i = 0; i < pages; i++) {
            int rownum = 0;
            // 计算每页的起始数据和结束数据
            int startIndex = i * sheetsize;
            int endIndex = (i + 1) * sheetsize - 1 > data.size() ? data.size()
                    : (i + 1) * sheetsize - 1;
            // 创建每页，并创建第一行
            HSSFSheet sheet = workbook.createSheet();
            HSSFRow row = sheet.createRow(rownum);

            // 在每页sheet的第一行中，添加字段名
            for (int f = 0; f < cntitles.length; f++) {
                HSSFCell cell = row.createCell(f);
                cell.setCellValue(cntitles[f]);
            }
            rownum++;
            // 将数据添加进表格
            for (int j = startIndex; j < endIndex; j++) {
                row = sheet.createRow(rownum);
                JSONObject item = data.get(j);
                for (int h = 0; h < cntitles.length; h++) {
                    String value = item.getString(egtitles[h]);
                    HSSFCell cell = row.createCell(h);
                    cell.setCellValue(value);
                }
                rownum++;
            }
        }
        // 将创建好的数据写入输出流
        workbook.write(out);
        // 关闭workbook
        workbook.close();
    }

    /**
     * 　　* @author hkt
     * 　　* @param entityClass excel中每一行数据的实体类
     * 　　* @param in          excel文件
     * 　　* @param fields     字段名字
     * 　　*             需要注意的是这个方法中的map中：
     * 　　*             excel表格中每一列名为键，每一列对应的实体类的英文名为值
     * 　　 * @throws Exception
     */
//    public static <T> List<T> ExecltoList(InputStream in, Class<T> entityClass,Map<String, String> fields) throws Exception {
    public static List<JSONObject> ExecltoList(InputStream in, Map<String, String> fields) throws Exception {


//        List<T> resultList = new ArrayList<T>();
        List<JSONObject> resultList = new ArrayList<JSONObject>();

        HSSFWorkbook workbook = new HSSFWorkbook(in);

        // excel中字段的中英文名字数组
        String[] egtitles = new String[fields.size()];
        String[] cntitles = new String[fields.size()];
        Iterator<String> it = fields.keySet().iterator();
        int count = 0;
        while (it.hasNext()) {
            String cntitle = (String) it.next();
            String egtitle = fields.get(cntitle);
            egtitles[count] = egtitle;
            cntitles[count] = cntitle;
            count++;
        }

        // 得到excel中sheet总数
        int sheetcount = workbook.getNumberOfSheets();

        if (sheetcount == 0) {
            workbook.close();
            throw new Exception("Excel文件中没有任何数据");
        }

        // 数据的导出
        for (int i = 0; i < sheetcount; i++) {
            HSSFSheet sheet = workbook.getSheetAt(i);
            if (sheet == null) {
                continue;
            }
            // 每页中的第一行为标题行，对标题行的特殊处理
            HSSFRow firstRow = sheet.getRow(0);
            int celllength = firstRow.getLastCellNum();

            String[] excelFieldNames = new String[celllength];
            LinkedHashMap<String, Integer> colMap = new LinkedHashMap<String, Integer>();

            // 获取Excel中的列名
            for (int f = 0; f < celllength; f++) {
                HSSFCell cell = firstRow.getCell(f);
                excelFieldNames[f] = cell.getStringCellValue().trim();
                // 将列名和列号放入Map中,这样通过列名就可以拿到列号
                for (int g = 0; g < excelFieldNames.length; g++) {
                    colMap.put(excelFieldNames[g], g);
                }
            }
            // 由于数组是根据长度创建的，所以值是空值，这里对列名map做了去空键的处理
            colMap.remove(null);
            // 判断需要的字段在Excel中是否都存在
            // 需要注意的是这个方法中的map中：中文名为键，英文名为值
            boolean isExist = true;
            List<String> excelFieldList = Arrays.asList(excelFieldNames);
            for (String cnName : fields.keySet()) {
                if (!excelFieldList.contains(cnName)) {
                    isExist = false;
                    break;
                }
            }
            // 如果有列名不存在，则抛出异常，提示错误
            if (!isExist) {
                workbook.close();
                throw new Exception("Excel中缺少必要的字段，或字段名称有误");
            }
            // 将sheet转换为list
            for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                HSSFRow row = sheet.getRow(j);
                JSONObject posJson = new JSONObject();
                // 给对象中的字段赋值
                for (Map.Entry<String, String> entry : fields.entrySet()) {
                    // 获取中文字段名
                    String cnNormalName = entry.getKey();
                    // 获取英文字段名
                    String enNormalName = entry.getValue();
                    // 根据中文字段名获取列号
                    int col = colMap.get(cnNormalName);
                    // 获取当前单元格中的内容
//                    String content = row.getCell(col).toString().trim();

                    //TODO 此处为了防止报错，将自定义字段集合写死了，后期连接数据库查询

                    String content = getCellValue(getCustomFieldList(1),row,colMap).trim();

                    // 给对象赋值
//                    setFieldValueByName(enNormalName, content, entity);
                    posJson.put(enNormalName, content);
                }
                resultList.add(posJson);
            }
        }
        workbook.close();
        return resultList;

    }


    /**
     * @param list
     * @param row
     * @param map
     * @return
     * @throws ParseException
     */
    private static String getCellValue(List<CustomField> list, HSSFRow row, LinkedHashMap<String, Integer> map) throws ParseException {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        String cellValue = "";
        for (int i = 0; i < list.size(); i++) {
            String codeName = list.get(i).getCodeName();
            HSSFCell cell = row.getCell(map.get(list.get(i).getCodeName()));
            if (cell == null) {
                return cellValue;
            }
            switch (codeName) {
                case "数字类型":
                    cell.setCellType(CellType.NUMERIC);
                    break;
                case "字符串类型":
                    cell.setCellType(CellType.STRING);
                    break;
                case "日期类型":
                    cell.setCellType(CellType.NUMERIC);
                    break;
                default:
            }
            //TODO 在此需要获取到自定义字段的自定义类型，而且还要考虑精度，位数，一次性查询出来存储。根据Switch case取值
            CellType cellType = cell.getCellTypeEnum();
            // 判断数据的类型
            switch (cellType) {
                // 数字、日期
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        // 日期型
                        cellValue = fmt.format(cell.getDateCellValue());
                    } else {
                        // 数字
                        cellValue = String.valueOf(cell.getNumericCellValue());
                        if (cellValue.contains("E")) {
                            // 数字
                            cellValue = String.valueOf(new Double(cell.getNumericCellValue()).longValue());
                        }
                    }
                    break;
                // 字符串
                case STRING:
                    cellValue = String.valueOf(cell.getStringCellValue());
                    break;
                // Boolean
                case BOOLEAN:
                    cellValue = String.valueOf(cell.getBooleanCellValue());
                    break;
                // 公式
                case FORMULA:
                    cellValue = String.valueOf(cell.getCellFormula());
                    break;
                // 空值
                case BLANK:
                    cellValue = cell.getStringCellValue();
                    break;
                // 故障
                case ERROR:
                    cellValue = "非法字符";
                    break;
                default:
                    cellValue = "未知类型";
                    break;
            }

        }
        return cellValue;
    }
//    public static void main(String[] args) throws Exception {
//        List<JSONObject> li = new ArrayList<JSONObject>();
//        JSONObject jsonObject=new JSONObject();
//        jsonObject.put("name","zss");
//        jsonObject.put("password","123");
//        jsonObject.put("id",55);
//        jsonObject.put("data",new Date());
//        jsonObject.put("phoneNumber","17629926998");
//        jsonObject.put("myid","411111111111111111");
//        li.add(jsonObject);
//        OutputStream out = new FileOutputStream("d://版本.xls");
//        Map<String, String> fields = new HashMap<String, String>();
//        fields.put("name", "姓名");
//        fields.put("password", "密码");
//        fields.put("id", "序号");
//        fields.put("data","时间");
//        fields.put("phoneNumber","手机号");
//        fields.put("myid","身份证号");
//        ExcelUtil.ListtoExecl(li, out, fields);
//        out.close();
//    }


//    public static void main(String[] args) throws Exception {
//        InputStream in = new FileInputStream("d://版本.xls");
//        Map<String, String> fieldd = new HashMap<String, String>();
//        fieldd.put("姓名", "name");
//        fieldd.put("密码", "password");
//        fieldd.put("序号", "id");
//        fieldd.put("时间","data");
//        fieldd.put("手机号","phoneNumber");
//        fieldd.put("身份证号","myid");
//        List<JSONObject> resultList = new ArrayList<JSONObject>();
//        List<JSONObject> jsonObjects = ExecltoList(in, fieldd);
//        for (JSONObject jsonObject : jsonObjects) {
//            System.out.println(jsonObject);
//        }
//    }


        /**
         * 获取自定义表字段集合
         * @param tableId
         * @return
         */
        private static List<CustomField> getCustomFieldList (Integer tableId){
            //TODO 通过连接数据库，获得自定义表的所有自定义字段，并且存储到List集合中
            return null;
        }

        /**
         * 获取自定义表字段中精度，长度，类型等属性
         * @param fieldId
         * @return
         */
        private static String[] getCustomField(Integer fieldId){
            //TODO 通过连接数据库，获得自定义字段的字段类型，精度，长度
            return null;
        }

    }





