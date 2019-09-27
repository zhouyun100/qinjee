package com.qinjee.masterdata.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Render Excel File to Html5 table string
 *
 * @author czhouyi@gmail.com
 */
public class ExcelRender {

    private Workbook workbook;

    public ExcelRender(String path) throws Exception {
        this.workbook = WorkbookFactory.create(new FileInputStream(new File(path)));
    }

    public ExcelRender(InputStream inputStream) throws Exception {
        this.workbook = WorkbookFactory.create(inputStream);
    }

    /**
     * 获取excel转html集合
     * @return
     */
    public List<ExcelSheet> render() {
        int sheets = workbook.getNumberOfSheets();
        List<ExcelSheet> excelSheets = new ArrayList<>();
        for (int i = 0; i < sheets; ++i) {
            Sheet sheet = workbook.getSheetAt(i);
            excelSheets.add(getExcelSheet(sheet));
        }

        return excelSheets;
    }

    private ExcelSheet getExcelSheet(Sheet sheet) {
        if (sheet == null) {
            return null;
        }
        ExcelSheet excelSheet = new ExcelSheet();
        excelSheet.setTitle(sheet.getSheetName());

        // 合并单元格的位置
        Map<String, CellRangeAddress> cellRangeAddressMap = new HashMap<>();
        // 被合并单元格占用的格子
        Set<String> matrix = new HashSet<>();

        int numberMerges = sheet.getNumMergedRegions();
        for (int lastRowNum = 0; lastRowNum < numberMerges; ++lastRowNum) {
            CellRangeAddress ca = sheet.getMergedRegion(lastRowNum);
            int fc = ca.getFirstColumn();
            int lc = ca.getLastColumn();
            int fr = ca.getFirstRow();
            int lr = ca.getLastRow();

            for (int i = fc; i <= lc; i++) {
                for (int j = fr; j <= lr; j++) {
                    matrix.add(String.format("%d-%d", j, i));
                }
            }
            cellRangeAddressMap.put(String.format("%d-%d", fr, fc), ca);
        }

        for (int index = 0, lastRowNum = sheet.getLastRowNum(); index <= lastRowNum; ++index) {
            Row row = sheet.getRow(index);

            if (((XSSFRow) row).getCTRow().getHidden()) {
                continue;
            }
            int rowNum = row.getRowNum();
            ExcelRow excelRow = new ExcelRow();
            for (int i = row.getFirstCellNum(), len = row.getLastCellNum(); i < len; i++) {
                String key = String.format("%d-%d", rowNum, i);
                Cell cell = row.getCell(i);
                ExcelCell excelCell = new ExcelCell();
                style(excelCell, cell);
                excelCell.setValue(getCellValue(cell));
                if (cell.getCellComment() != null) {
                    String comment = cell.getCellComment().getString().toString();
                    excelCell.setComment(comment);
                }
                if (cellRangeAddressMap.containsKey(key)) {
                    CellRangeAddress cra = cellRangeAddressMap.get(key);
                    excelCell.setCols(cra.getLastColumn() - cra.getFirstColumn() + 1);
                    excelCell.setRows(cra.getLastRow() - cra.getFirstRow() + 1);
                    excelRow.getCells().add(excelCell);
                } else if (!matrix.contains(key)) {
                    excelRow.getCells().add(excelCell);
                }
            }
            excelSheet.getRows().add(excelRow);
        }
        return excelSheet;
    }

    private void style(ExcelCell excelCell, Cell cell) {
        Font font = workbook.getFontAt(cell.getCellStyle().getFontIndex());
        try {
            if (font instanceof XSSFFont) {
                String color = ((XSSFFont) font).getXSSFColor().getARGBHex();
                excelCell.setColor("#" + color.substring(2));
            } else {
                String color = ((HSSFFont) font).getHSSFColor((HSSFWorkbook) workbook).getHexString();
                excelCell.setColor("#" + color);
            }
        }catch (Exception e){
            System.out.println("null");
        }
        excelCell.setFontFamily(font.getFontName());
        excelCell.setBold(font.getBold());
        excelCell.setFontSize(font.getFontHeightInPoints());
        excelCell.setStrikeout(font.getStrikeout());
        excelCell.setAlignment(cell.getCellStyle().getAlignmentEnum());
        excelCell.setVerticalAlignment(cell.getCellStyle().getVerticalAlignmentEnum());

        if (cell instanceof XSSFCell) {
            XSSFColor bColor = ((XSSFCell) cell).getCellStyle().getFillForegroundXSSFColor();
            if (bColor != null) {
                String bg = bColor.getARGBHex();
                excelCell.setBackground("#" + bg.substring(2));
            }
        } else {
            HSSFColor bColor = ((HSSFCell) cell).getCellStyle().getFillForegroundColorColor();
            if (bColor != null) {
                String bg = bColor.getHexString();
                excelCell.setBackground("#" + bg);
            }
        }
    }
    public static Object getCellValue(Cell cell) {
        String cellValue = "";
        if (cell == null) {
            return cellValue;
        }
        CellType cellType = cell.getCellTypeEnum();
        // 判断数据的类型
        switch (cellType) {
            case NUMERIC: // 数字
                //short s = cell.getCellStyle().getDataFormat();
                if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date = cell.getDateCellValue();
                    cellValue = sdf.format(date);
                } else if (cell.getCellStyle().getDataFormat() == 0) {//处理数值格式
                    cell.setCellType(Cell.CELL_TYPE_STRING);
                    cellValue = String.valueOf(cell.getRichStringCellValue().getString());
                }
                break;
            case STRING: // 字符串
                cellValue = String.valueOf(cell.getStringCellValue());
                break;
            case BOOLEAN: // Boolean
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA: // 公式
                cellValue = String.valueOf(cell.getCellFormula());
                break;
            case BLANK: // 空值
                cellValue = null;
                break;
            case  ERROR: // 故障
                cellValue = "非法字符";
                break;
            default:
                cellValue = "未知类型";
                break;
        }
        return cellValue;

    }

}
