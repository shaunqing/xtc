package cn.mostic.xtc.zxstats;

import java.util.*;

/**
 * 初始化结果数组
 * Created by LIQing
 * 2017/11/19 17:19
 */
public class InitArray {

    public static Map<String, ResutlData> getArray(Set<String> dateSet, int zxAmount) {
        // 1、初始化结果数组
        Map<String, ResutlData> resutlDataMap = new HashMap<>();

        // 2、读取省市的配置文件（含有省市名称）
        List<String> provinces = getProvinces();

        // 3、遍历日期数组
        for (String data : dateSet) {
            // 3.1、遍历省市
            for (String province : provinces) {
                // 3.2、遍历24次（代表24小时）
                for (int hour = 0; hour < 24; hour++) {
                    // 3.3、初始化ResultData对象
                    ResutlData resutlData = new ResutlData(data, hour, province, zxAmount);

                    // 将indexMD5作为key值
                    resutlDataMap.put(resutlData.getIndexMD5(), resutlData);
                }
            }
        }

        return resutlDataMap;
    }

    /**
     * 读取省市文件
     *
     * @return
     */
    private static List<String> getProvinces() {
        return ReadFile.readProvince("provinces.txt");
    }
}
