package cn.mostic.xtc.zxstats;

import cn.mostic.xtc.util.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.*;

import java.util.*;

/**
 * Created by LIQing
 * 2017/11/19 18:25
 */
@Slf4j
public class StatsMain {

    public static void main(String[] args) {
        StatsMain statsMain = new StatsMain();

        String excelPath = "D:\\123.xlsx";
        ResultExcel resultExcel = new ResultExcel();

        Map<String, ResutlData> result = statsMain.execute(excelPath, 0);
        resultExcel.export(result, "按提交时间统计");

        result.clear();

        result = statsMain.execute(excelPath, 1);
        resultExcel.export(result, "按创建时间统计");
    }

    public Map<String, ResutlData> execute(String excelPath, int type) {
        log.info("开始执行分析");

        Workbook workbook = ExcelUtil.getWorkbook(excelPath);
        Sheet sheet = workbook.getSheetAt(0);

        if (0 == type) {
            // 按提交时间统计
            return executeStats(sheet, 9, 10);
        } else {
            // 按创建时间统计
            return executeStats(sheet, 12, 13);
        }
    }

    /**
     * 执行专项数量统计
     *
     * @param sheet  需要处理的sheet页
     * @param dateNo 日期列的序号（从0开始）
     * @param hourNo 小时列的序号
     * @return
     */
    private Map<String, ResutlData> executeStats(Sheet sheet, int dateNo, int hourNo) {

        int rowNum = sheet.getLastRowNum();
        Set<String> dateSet = getDateSet(sheet, dateNo);

        Map<String, Integer> zxMap = ReadFile.readZx(); // 获取专项列表
        int zxAmount = zxMap.size();
        Map<String, ResutlData> resutlDataMap = InitArray.getArray(dateSet, zxAmount); // 初始化结果数组

        for (int i = 1; i <= rowNum; i++) {
            Row row = sheet.getRow(i);

            // 获取日期
            Cell cell = row.getCell(dateNo);
            cell.setCellType(CellType.STRING);
            String date = cell.getStringCellValue();

            // 获取小时数
            cell = row.getCell(hourNo);
            cell.setCellType(CellType.STRING);
            String hour = cell.getStringCellValue();

            // 获取省市
            cell = row.getCell(7);
            cell.setCellType(CellType.STRING);
            String province = cell.getStringCellValue();

            // 计算唯一值
            String indexMD5 = DigestUtils.md5Hex(date + hour + province);

            ResutlData resutlData = resutlDataMap.get(indexMD5);
            if (resutlData == null) {
                continue;
            }

            // 获取专项名称
            cell = row.getCell(1);
            String zx = cell.getStringCellValue();

            // 根据专项名称获得index，计数自增
            int zxCode = zxMap.get(zx);
            resutlData.getCount()[zxCode]++;

            // 总计自增
            int total = resutlData.getTotal();
            resutlData.setTotal(++total);
        }

        return resutlDataMap;
    }

    /**
     * 根据专项序号获得名称
     *
     * @param zxMap
     * @param zxCode
     * @return
     */
    private String getNameByZxCode(Map<String, Integer> zxMap, int zxCode) {
        for (Map.Entry<String, Integer> entry : zxMap.entrySet()) {
            int value = entry.getValue().intValue();
            if (value == zxCode) {
                return entry.getKey();
            }
        }
        return null;
    }

    /**
     * 获得日期（不重复）
     *
     * @param sheet
     * @param cellNo
     * @return
     */
    private Set<String> getDateSet(Sheet sheet, int cellNo) {
        Set<String> dateSet = new HashSet<>();
        int rowNum = sheet.getLastRowNum();
        // 不统计表头
        for (int i = 1; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            // 获取日期
            Cell cell = row.getCell(cellNo);
            dateSet.add(cell.getStringCellValue());
        }
        return dateSet;
    }
}
