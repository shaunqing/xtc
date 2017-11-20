package cn.mostic.xtc.zxstats;

import cn.mostic.xtc.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author LIQing
 * @create 2017-11-20 9:05
 */
@Slf4j
public class ResultExcel {

    /**
     * 导出为excel
     *
     * @param resultDataMap
     * @param excelName
     */
    public void export(Map<String, ResutlData> resultDataMap, String excelName) {
        log.info("开始生成excel");

        Workbook workbook = new XSSFWorkbook();
        writeSheet(workbook, resultDataMap);

        log.info("生成完成，开始导出");

        ExcelUtil.write(workbook, excelName);

        log.info("导出excel完成");
    }

    private void writeSheet(Workbook workbook, Map<String, ResutlData> resutlDataMap) {
        CellStyle cellStyle = ExcelUtil.getCellStyle(workbook, false);
        Sheet sheet = ExcelUtil.createSheet(workbook, null, getHeader());
        int sheetRowNum = 0;
        for (ResutlData resutlData : resutlDataMap.values()) {
            Row row = sheet.createRow(++sheetRowNum);
            setRow(cellStyle, row, resutlData);
        }
    }

    private List<String> getHeader() {
        List<String> header = new ArrayList<>();
        header.add("日期");
        header.add("省市");
        header.add("小时");

        // 各专项名称
        Map<String, Integer> zxMap = ReadFile.readZx();
        int size = zxMap.size();

        for (int i = 0; i < size; i++) {
            header.add(getZxNameByCode(zxMap, i));
        }

        header.add("共计");

        return header;
    }

    /**
     * 根据专项的序号获取名称
     *
     * @param zxMap
     * @param zxCode
     * @return
     */
    private String getZxNameByCode(Map<String, Integer> zxMap, int zxCode) {
        for (Map.Entry<String, Integer> entry : zxMap.entrySet()) {
            if (zxCode == entry.getValue()) {
                return entry.getKey();
            }
        }
        return null;
    }


    /**
     * 写入一行数据
     *
     * @param style
     * @param row
     * @param resutlData
     */
    private void setRow(CellStyle style, Row row, ResutlData resutlData) {
        Cell cell = row.createCell(0); // 日期
        cell.setCellStyle(style);
        cell.setCellValue(resutlData.getDate());

        cell = row.createCell(1); // 省市
        cell.setCellStyle(style);
        cell.setCellValue(resutlData.getProvince());

        cell = row.createCell(2); // 小时
        cell.setCellStyle(style);
        cell.setCellValue(resutlData.getHour());

        // 各省市统计
        setCountRow(style, row, resutlData.getCount());

        // 共计
        cell = row.createCell(resutlData.getCount().length + 3);
        cell.setCellStyle(style);
        cell.setCellValue(resutlData.getTotal());
    }

    /**
     * 各省市的统计情况
     *
     * @param style
     * @param row
     * @param count
     */
    private void setCountRow(CellStyle style, Row row, int[] count) {
        int size = count.length;
        for (int i = 0; i < size; i++) {
            Cell cell = row.createCell(i + 3);
            cell.setCellStyle(style);
            cell.setCellValue(count[i]);
        }
    }


}
