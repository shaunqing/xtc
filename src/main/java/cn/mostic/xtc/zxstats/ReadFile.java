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
     * 读取专项文件
     *
     * @param url
     * @return
     */
    public static Map<String, Integer> readZx(String url) {
        Map<String, Integer> map = new HashMap<>();

        File file = new File(url);
        try {
            //构造一个BufferedReader类来读取文件
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s = null;
            //使用readLine方法，一次读一行
            while ((s = br.readLine()) != null) {
                System.out.println(s);
                String[] values = s.split(" ");
                map.put(values[0], Integer.valueOf(values[1]));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    public static void main(String[] args) {
        Map<String, Integer> map = ReadFile.readZx("zx.txt");
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
        }


        System.out.println(map.containsValue(Integer.valueOf(1)));

    }

}
