package cn.mostic.xtc.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Excel辅助类
 *
 * @author LIQing
 * @version 2017-6-23
 */
public class ExcelUtil {

    private static Logger logger = LoggerFactory.getLogger(ExcelUtil.class);

    public static void main(String[] args) {
        ExcelUtil.getWorkbook("D:/tmp/~$数据工作任务.xlsx");
    }

    /**
     * 获取Excel的工作簿
     *
     * @param absoluteFilePath 文件绝对路径
     * @return
     */
    public static Workbook getWorkbook(String absoluteFilePath) {
        InputStream is = null;
        try {
            is = new FileInputStream(absoluteFilePath);

            if (!isTempExcel(absoluteFilePath)) { // ignore temp excel
                if (absoluteFilePath.endsWith("xls")) {
                    return new HSSFWorkbook(is);
                } else if (absoluteFilePath.endsWith("xlsx")) {
                    return new XSSFWorkbook(is);
                } else {
                    throw new RuntimeException("不是Excel文件!");
                }
            } else {
                throw new RuntimeException("不能使Excel缓存文件!");
            }
        } catch (IOException e) {
            logger.error("获取工作簿异常", e);
            throw new RuntimeException("文件不存在!");
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
            }
        }
    }

    /**
     * 判断是否为临时Excel
     *
     * @param filePath
     * @return
     */
    private static boolean isTempExcel(String filePath) {
        // 获取Excel文件名称
        String excelName = "";

        if (filePath.contains("/")) {
            excelName = filePath.substring(filePath.lastIndexOf("/") + 1, filePath.length());
        } else if (filePath.contains("\\")) {
            excelName = filePath.substring(filePath.lastIndexOf("\\") + 1, filePath.length());
        } else {
            excelName = filePath;
        }

        // 忽略Excel缓存文件
        return excelName.startsWith("~$") ? true : false;
    }

    /**
     * 保存Excel文件
     *
     * @param workbook  需保存的工作簿
     * @param excelName Excel文件名（程序自动添加时间戳）
     * @return 文件名称
     */
    public static String write(Workbook workbook, String excelName) {
        // 添加时间戳
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        excelName = excelName + "_" + sf.format(new Date());

        // 设置扩展名
        String workbookClass = workbook.getClass().toString();
        if (workbookClass.endsWith("XSSFWorkbook")) {
            excelName += ".xlsx";
        } else if (workbookClass.endsWith("HSSFWorkbook")) {
            excelName += ".xls";
        }

        FileOutputStream fileOut = null;

        try {
            // makeRootFolder(savePath);
            fileOut = new FileOutputStream(excelName);
            workbook.write(fileOut);
            return excelName;
        } catch (IOException e) {
            logger.error("写入Excel异常", e);
            throw new RuntimeException("生成Excel失败，请联系管理员！");
        } finally {
            try {
                if (fileOut != null) {
                    fileOut.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static int countRows(List<String> floderPath) {
        int countRows = 0;
        for (String filePath : floderPath) {
            Workbook wb = getWorkbook(filePath);
            int sheetNumber = wb.getNumberOfSheets();
            for (int i = 0; i < sheetNumber; i++) {
                Sheet sheet = wb.getSheetAt(i);
                countRows += sheet.getLastRowNum();
            }
        }
        return countRows;
    }

    private static void makeRootFolder(String root) {
        File rootFolder = new File(root);
        if (!rootFolder.exists() || !rootFolder.isDirectory()) {
            rootFolder.mkdirs();
        }
    }

    /**
     * 创建sheet页
     *
     * @param workbook
     * @param sheetName
     * @param header
     * @return
     */
    public static Sheet createSheet(Workbook workbook, String sheetName, List<String> header) {
        Sheet sheet = null;
        if (sheetName == null || "".equals(sheetName)) {
            sheet = workbook.createSheet();
        } else {
            sheet = workbook.createSheet(sheetName);
        }
        generateHeader(workbook, sheet, header);
        return sheet;
    }

    /**
     * 生成Excel表头
     *
     * @param workbook
     * @param sheet
     * @param header
     */
    public static void generateHeader(Workbook workbook, Sheet sheet, List<String> header) {
        CellStyle style = getCellStyle(workbook, true);
        Row row = sheet.createRow(0);
        row.setHeight((short) 500); // 设置行高
        int size = header.size();
        for (int i = 0; i < size; i++) {
            Cell cell = row.createCell(i);
            cell.setCellValue(header.get(i));
            cell.setCellStyle(style);
        }
    }

    /**
     * 设置单元格格式
     *
     * @param workbook
     * @param isHeader
     * @return
     */
    public static CellStyle getCellStyle(Workbook workbook, boolean isHeader) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setLocked(true);
        style.setAlignment(HorizontalAlignment.CENTER); // 水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直居中
        if (isHeader) {
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
            Font font = workbook.createFont();
            font.setColor(IndexedColors.BLACK.index);
            font.setFontHeightInPoints((short) 12);
            style.setFont(font);
        }
        return style;
    }
}
