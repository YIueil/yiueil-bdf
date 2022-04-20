package cn.yiueil.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

/**
 * Author:YIueil
 * Date:2022/4/15 15:46
 * Description: 旧日期API工具类
 */
public class DateUtils {
    private static final List<SimpleDateFormat> textFormatList;

    static {
        textFormatList = new ArrayList<>();
        textFormatList.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));// ISO 8601格式
        textFormatList.add(new SimpleDateFormat("yyyy-MM-dd hh:mm:sss"));// 12小时制 含毫秒
        textFormatList.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));// 24小时制 不含毫秒
        textFormatList.add(new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"));// 12小时制 不含毫秒
        textFormatList.add(new SimpleDateFormat("yyyy-MM-dd"));// 不含时分秒
    }

    //region 当前日期相关
    /**
     * 获取当前年份
     * @return 年
     */
    public static int getYear() {
        return getYear(new Date());
    }

    /**
     * 获取当前月份
     * @return 月
     */
    public static int getMouth() {
        return getMouth(new Date());
    }

    /**
     * 获取当前是本月的第几天
     * @return 日
     */
    public static int getDayOfMouth() {
        return getDayOfMouth(new Date());
    }

    /**
     * 获取当前的学年
     * <p>如果在9、10、11、12、1月，为此学年第 2 学期，
     * 其中在9、10、11、12月为 year 学年，1月为 year-1 学年。
     * 如果在2、3、4、5、6、7、8月，为此学年第 2 学期，
     * year-1学年。</p>
     * @return 当前学年
     * <p>e.g. : 2021-2022-2</p>
     */
    public static String getCurrentTerm() {
        return getCurrentTerm(new Date());
    }
    //endregion

    //region 指定日期相关
    /**
     * 获取当前年份
     * @return 年
     */
    public static int getYear(Date date) {
        return date.getYear() + 1900; // 必须加上1900
    }

    /**
     * 获取当前月份
     * @return 月
     */
    public static int getMouth(Date date) {
        return date.getMonth() + 1; // 0~11，必须加上1
    }

    /**
     * 获取当前是本月的第几天
     * @return 日
     */
    public static int getDayOfMouth(Date date) {
        return date.getDate(); // 1~31，不能加1
    }

    /**
     * 获取当前的学年
     * <p>如果在9、10、11、12、1月，为此学年第 2 学期，
     * 其中在9、10、11、12月为 year 学年，1月为 year-1 学年。
     * 如果在2、3、4、5、6、7、8月，为此学年第 2 学期，
     * year-1学年。</p>
     * @return 当前学年
     * <p>e.g. : 2021-2022-2</p>
     */
    public static String getCurrentTerm(Date date) {
        int year = getYear(date);
        int month = getMouth(date);
        int term = 1;
        if (month < 9)
            year = year - 1;
        if (month > 2 && month < 9)
            term = 2;
        return year + "-" + (year + 1) + "-" + term;
    }
    //endregion

    //region 新老API的转换
    public static Date localDate2Date(LocalDate localDate) {
        LocalDateTime localDateTime = localDate.atTime(0, 0);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static Date str2Date(String dateStr) {
        for (SimpleDateFormat simpleDateFormat : textFormatList) {
            try {
                return simpleDateFormat.parse(dateStr);
            } catch (ParseException ignored) {}
        }
        throw new RuntimeException("G! 字符串转换失败");
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }
    //endregion

    /**
     * 通过时间戳，地区，时区等参数，展示用户容易接受的时间字符串
     * <p>
     *     参考： 廖雪峰java教程
     *     @link https://www.liaoxuefeng.com/wiki/1252599548343744/1303978948165666
     * </p>
     * <p>e.g. :
     *         LocalDateTime ldt = LocalDateTime.of(2019, 9, 15, 15, 16, 17);
     *         ZonedDateTime zbj = ldt.atZone(ZoneId.systemDefault());
     *         ZonedDateTime zny = ldt.atZone(ZoneId.of("America/New_York"));
     *         System.out.println(zbj);
     *         System.out.println(zny);
     *
     * </p>
     * <p>
     *     2019-09-15T15:16:17+08:00[Asia/Shanghai]
     *     2019-09-15T15:16:17-04:00[America/New_York]
     * </p>
     * @param epochMilli 时间戳
     * @param lo 地区
     * @param zoneId 时区
     * @return 各地区容易接受的日期字符串
     */
    static String timestampToString(long epochMilli, Locale lo, String zoneId) {
        Instant ins = Instant.ofEpochMilli(epochMilli);
        DateTimeFormatter f = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.SHORT);
        return f.withLocale(lo).format(ZonedDateTime.ofInstant(ins, ZoneId.of(zoneId)));
    }
}
