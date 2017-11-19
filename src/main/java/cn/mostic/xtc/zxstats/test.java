package cn.mostic.xtc.zxstats;


/**
 * Created by LIQing
 * 2017/11/19 17:31
 */
public class test {

    public static void main(String[] args) {
        ResutlData resutlData = new ResutlData("2017-11-15", 23, "北京",42);
        resutlData.getCount()[1]++;
        int x = resutlData.getTotal();
        resutlData.setTotal(++x);


        System.out.println(resutlData.getCount()[1]);
        System.out.println(resutlData.getTotal());

    }
}
