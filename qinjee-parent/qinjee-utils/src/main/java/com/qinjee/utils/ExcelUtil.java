package com.qinjee.utils;


import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

public class ExcelUtil {
    /**
     * @param title
     * 工作簿名称
     * @param heads
     * 表格第一行名称数组
     * @param dates
     * 要导入表格的数据
     * @param url
     * 临时存放excel文件的文件夹
     * 导出excel
     */
    private final static String xls = "xls";
    private final static String xlsx = "xlsx";
    private final static String INTEGER = "Integer";
    private final static String STRING = "String";
    private final static String BOOLEAN = "Boolean";
    private final static String NULL = "";
    private final static String DATE = "Date";
    private final static String SHORT = "Short";
    /**
     * @param path  临时存放excel文件路径
     * @param title 工作簿名称
     * @param heads 表格第一行名称数组
     * @param dates 要写入表格的数据
     *              下载excel
     */
    /**
     * @param title 主题
     * @param heads 这个参数是所有字段的内容，做为表头显示
     * @param dates 这个是存储信息的字段，
     *              示例：
     *              Map<String, String> map = new LinkedHashMap<String,String>();
     *              map.put("ID", String.valueOf(i+1));
     *              map.put("Name", "name"+(i+1));
     *              map.put("Pass", "pass"+(i+1));
     *              dates.add(map);
     * @param map   以表头信息为key，类型为value的Map集合
     */
    public static void download(HttpServletResponse response,
                                String title, List<String> heads, List<Map<String, String>> dates, Map<String, String> map) {
        // 新建excel报表
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();
        // 添加一个sheet名称
        HSSFSheet hssfSheet = hssfWorkbook.createSheet(title);
        //设置单元格样式
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        //水平居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 垂直居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        /*
         * 设定合并单元格区域范围 firstRow 0-based lastRow 0-based firstCol 0-based lastCol
         * 0-based
         */
        CellRangeAddress cra = new CellRangeAddress(0, 0, 0, heads.size() - 1);
        // 在sheet里增加合并单元格
        hssfSheet.addMergedRegion(cra);
        HSSFRow row = hssfSheet.createRow(0);
        HSSFCell cell1 = row.createCell(0);
        cell1.setCellValue(title);
        //设置样式
        cell1.setCellStyle(style);
        HSSFRow hssfRow = hssfSheet.createRow(1);
        for (int i = 0; i < heads.size(); i++) {
            HSSFCell hssfCell = hssfRow.createCell(i);
            //先设定类型，再设定值
            hssfCell.setCellType(getCellType(map.get(heads.get(i))));
            hssfCell.setCellValue(heads.get(i));
        }

        // 循环将list里面的值取出来放进excel中
        for (int i = 0; i < dates.size(); i++) {
            HSSFRow hssfRow1 = hssfSheet.createRow(i + 2);
            int j = 0;
            Iterator<Map.Entry<String, String>> it = dates.get(i).entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                HSSFCell hssfCell = hssfRow1.createCell(j);
                hssfCell.setCellValue(entry.getValue());
                j++;
            }
        }

        FileOutputStream fout = null;
        String path = "d:\\a.xls";
        try {
            // 用流将其写到指定的url
            fout = new FileOutputStream(path);
            hssfWorkbook.write(fout);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            // path是指欲下载的文件的路径。
            File file = new File(path);
            // 取得文件名。
            String filename = file.getName();
            // 以流的形式下载文件。
            InputStream fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename="
                    + new String(filename.getBytes()));
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
            File file2 = new File(path);
            file2.delete();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    /**
     * CellType 类型 值
     * CELL_TYPE_NUMERIC	数值型	0
     * CELL_TYPE_STRING	字符串型	1
     * CELL_TYPE_FORMULA	公式型	2
     * CELL_TYPE_BLANK	空值	3
     * CELL_TYPE_BOOLEAN	布尔型	4
     * CELL_TYPE_ERROR	错误	5
     *
     * @param type
     * @return
     */
    public static CellType getCellType(String type) {

        switch (type) {
            case INTEGER:
                return CellType.NUMERIC;
            case STRING:
                return CellType.STRING;
            case BOOLEAN:
                return CellType.BOOLEAN;
            case NULL:
                return CellType.BLANK;
            case DATE:
                return CellType.NUMERIC;
            case SHORT:
                return CellType.NUMERIC;
            default:
                return CellType.STRING;
        }

    }


    /**
     * 读入excel文件，解析后返回
     *
     * @param file
     * @throws IOException
     */
    public static List<Map<String, String>> readExcel(MultipartFile file)
            throws Exception {
        // 获得Workbook工作薄对象
        Workbook workbook = getWorkBook(file);
        // 创建返回对象，把每行中的值作为一个数组，所有行作为一个集合返回
        List<String[]> list = new ArrayList<>();
        if (workbook != null) {
            for (int sheetNum = 0; sheetNum < workbook.getNumberOfSheets(); sheetNum++) {
                // 获得当前sheet工作表
                Sheet sheet = workbook.getSheetAt(sheetNum);
                if (sheet == null) {
                    continue;
                }
                // 获得当前sheet的开始行
                int firstRowNum = sheet.getFirstRowNum();
                // 获得当前sheet的结束行
                int lastRowNum = sheet.getLastRowNum();
                // 循环除了第一行的所有行
                for (int rowNum = firstRowNum; rowNum <= lastRowNum; rowNum++) {
                    // 获得当前行
                    Row row = sheet.getRow(rowNum);
                    if (row == null) {
                        continue;
                    }
                    // 获得当前行的开始列
                    int firstCellNum = row.getFirstCellNum();
                    // 获得当前行的列数
                    int lastCellNum = row.getPhysicalNumberOfCells();
                    String[] cells = new String[row.getPhysicalNumberOfCells()];
                    // 循环当前行
                    for (int cellNum = firstCellNum; cellNum < lastCellNum; cellNum++) {
                        Cell cell = row.getCell(cellNum);
                        cells[cellNum] = getCellValue(cell);
                    }
                    list.add(cells);
                    //此时应该返回一个表属性的list集合，与一个以表头为key，value为数据的map
                }
            }
        }
        //heads存的是第一行的字段名
        List<String> heads = Arrays.asList(list.get(0));
        List<Map<String, String>> listMap = new ArrayList<>();
        Map<String, String> mapRow;
        for (int i = 1; i < list.size(); i++) {
            mapRow = new HashMap<>();
            for (int j = 0; j < heads.size(); j++) {
                mapRow.put(heads.get(j), list.get(i)[j]);
            }
            listMap.add(mapRow);
        }

        return listMap;
    }

    /**
     * @param file
     * @return 创建并返回一个workBook（兼容xls和xlsx）
     */
    public static Workbook getWorkBook(MultipartFile file) throws Exception {
        // 获得文件名
        String fileName = file.getOriginalFilename();
        // 创建Workbook工作薄对象，表示整个excel
        Workbook workbook = null;
        try {
            // 获取excel文件的io流
            InputStream is = file.getInputStream();
            // 根据文件后缀名不同(xls和xlsx)获得不同的Workbook实现类对象
            assert fileName != null;
            if (fileName.endsWith(xls)) {
                // 2003
                workbook = new HSSFWorkbook(is);
            } else if (fileName.endsWith(xlsx)) {
                // 2007
                workbook = new XSSFWorkbook(is);
            }
        } catch (IOException e) {
            throw new Exception ( "文件类型不支持！" );
        }
        return workbook;
    }
    /**
     * @param cell
     * @return 根据excel表格中的数据类型和数据值返回相对应的数据值
     */
    public static String getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        // 判断数据的类型
        switch (cell.getCellType()) {
            // 数字
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
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
                cellValue = "";
                break;
            // 故障
            case ERROR:
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;
    }


    public static MultipartFile getMultipartFile(File file) {
        MultipartFile multipartFile = null;
        try {
            FileInputStream fileInput = new FileInputStream(file);
            multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(fileInput));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return multipartFile;
    }

    public static void main(String[] args) throws IOException {
        MultipartFile multipartFile = null;
        File file = new File("E:\\import_test.xlsx");
        FileInputStream fileInput = new FileInputStream(file);
        multipartFile = new MockMultipartFile("file", file.getName(), "text/plain", IOUtils.toByteArray(fileInput));
        List<Map<String, String>> list = null;
        try {
            list = ExcelUtil.readExcel(multipartFile);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (Map<String, String> stringStringMap : list) {
            System.out.println(stringStringMap);
        }

//        for (List<String> strings : list) {
//            for (int i = 0; i < strings.length; i++) {
//                //这里可以获取excel的数据，然后将数据添加进数据库里面
//                System.out.print(strings[i] + "\t");
//            }
//            System.out.println();
//        }
    }
}
