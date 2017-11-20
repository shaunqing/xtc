package cn.mostic.xtc.zxstats;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LIQing
 * 2017/11/19 18:47
 */
public class ReadFile {

    /**
     * 读取文件，每一行为一个值
     *
     * @param url
     * @return
     */
    public static List<String> readProvince(String url) {
        List<String> list = new ArrayList<>();

        File file = new File(url);
        try {
            //构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            //使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {
                list.add(s);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    /**
     * 获取专项，序号从0开始
     *
     * @return
     */
    public static Map<String, Integer> readZx() {
        Map<String, Integer> map = new HashMap<>();

        String zxFilePath = "zx.txt";
        File file = new File(zxFilePath);
        try {
            //构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            //使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {
                String[] values = s.split(" ");
                map.put(values[0], Integer.valueOf(values[1]));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
