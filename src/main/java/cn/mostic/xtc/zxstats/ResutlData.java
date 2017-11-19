package cn.mostic.xtc.zxstats;

import lombok.Data;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * 统计申报情况对象
 * Created by LIQing
 * 2017/11/19 17:25
 */
@Data
public class ResutlData {

    private String indexMD5; // 唯一值
    private String date; // 日期
    private int hour; // 小时数
    private String province; // 省市
    private int[] count; // 各专项申报数量
    private int total = 0; // count中所有值的叠加结果

    public ResutlData() {
    }

    /**
     * @param date
     * @param hour
     * @param province
     * @param zxAmount 专项个数
     */
    public ResutlData(String date, int hour, String province, int zxAmount) {
        this.date = date;
        this.hour = hour;
        this.province = province;
        this.count = new int[zxAmount];
        // 根据date hour province生成md5
        this.indexMD5 = DigestUtils.md5Hex(date + hour + province);
    }
}
