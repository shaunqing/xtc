package cn.mostic.xtc.zxstats;

import cn.mostic.xtc.util.ExcelUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.*;

/**
 * Created by LIQing
 * 2017/11/19 18:25
 */
public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.execute(0, 0, 0);
    }

    public void execute(int dateNo, int hourNo, int provinceNo) {
        Workbook workbook = ExcelUtil.getWorkbook("D:\\test.xlsx");
        Sheet sheet = workbook.getSheetAt(0);
        int rowNum = sheet.getLastRowNum();

        Set<String> dateSet = getDateSet(sheet, dateNo);

        Map<String, Integer> zxMap = getZx();
        int zxAmount = zxMap.size();
        Map<String, ResutlData> resutlDataMap = InitArray.getArray(dateSet, zxAmount);


        for (int i = 0; i <= rowNum; i++) {
            Row row = sheet.getRow(i);

            // 获取日期
            Cell cell = row.getCell(dateNo);
            String date = cell.getStringCellValue();

            // 获取小时数
            cell = row.getCell(hourNo);
            String hour = cell.getStringCellValue();

            // 获取省市
            cell = row.getCell(provinceNo);
            String province = cell.getStringCellValue();

            String indexMD5 = DigestUtils.md5Hex(date + hour + province);

            ResutlData resutlData = resutlDataMap.get(indexMD5);

            if (resutlData == null) {
                continue;
            }

            // 获取专项名称
            cell = row.getCell(9);
            String zx = cell.getStringCellValue();

            // 根据专项名称获得index，计数自增
            int zxCode = zxMap.get(zx);
            resutlData.getCount()[zxCode]++;

            // 总计自增
            int total = resutlData.getTotal();
            resutlData.setTotal(++total);


        }

        // TODO 写入excel
        // 写excel的表头，需要按一定顺序写专项名
        for (int i = 0; i < zxAmount; i++) {
            String zxName = getNameByZxCode(zxMap, i);
        }



        // 只读取value
        for (ResutlData resutlData : resutlDataMap.values()) {
            // 设置日期、小时、地区

            // 获取每个专项的数量
            for (int i = 0; i < zxAmount; i++) {

            }
        }


    }

    private Map<String, Integer> getZx() {
        return ReadFile.readZx("zx.txt");
    }

    private String getNameByZxCode(Map<String, Integer> zxMap, int zxCode) {
        for (Map.Entry<String, Integer> entry : zxMap.entrySet()) {
            int value = entry.getValue().intValue();
            if (value == zxCode) {
                return entry.getKey();
            }
        }
        return null;
    }

    private Set<String> getDateSet(Sheet sheet, int cellNo) {
        Set<String> dateSet = new HashSet<>();
        int rowNum = sheet.getLastRowNum();
        for (int i = 0; i <= rowNum; i++) {
            Row row = sheet.getRow(i);
            // 获取日期
            Cell cell = row.getCell(cellNo);
            dateSet.add(cell.getStringCellValue());
        }
        return dateSet;
    }
}
