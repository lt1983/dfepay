package com.dooffe.common.utils;

/**
 * DateUtils.java
 * 描述：日期工具类 默认使用 "yyyy-MM-dd HH:mm:ss" 格式化日期
 *
 * @author tarsliu
 * @version v 0.1
 * @since 2017/12/12 PM1:27
 */

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public final class DateUtils {

    public final static String DATE_CHINESE_PATTERN = "yyyy年MM月dd日";
    public final static String DATE_MONTH_DAY_CHINESE_PATTERN = "MM月dd日";
    /**
     * 标准的中文小时分钟格式
     */
    public final static String HOUR_MINUTE_CHINESE_PATTERN = "HH点mm分";
    /**
     * 标准日期格式
     */
    public final static String DATE_PATTERN = "yyyy-MM-dd";
    /**
     * 标准年月格式
     */
    public final static String MONTH_PATTERN = "yyyy-MM";
    public final static String DATE_SHORT_PATTERN = "yyyyMMdd";
    public final static String DATE_SLASH_PATTERN = "yyyy/MM/dd";
    /**
     * 标准日期时分秒毫秒格式
     */
    public final static String DATETIME_MILL_SECOND = "yyyy-MM-dd HH:mm:ss.SSS";
    /**
     * 标准时间格式
     */
    public final static String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 标准时间格式，不含秒
     */
    public final static String DATETIME_PATTERN_SHORT = "yyyy-MM-dd HH:mm";

    /**
     * 特殊的格式 针对创建订单，拼凑的最晚支付时间
     */
    public final static String DATETIME_PATTERN_CREAT_ORDER = "yyyy-MM-ddHH:mm";

    public final static String DATETIME_SHORT_PATTERN = "yyyyMMddHHmmss";

    public final static String DATETIME_MILL_SECOND_SHORT_PATTERN = "yyyyMMddHHmmssSSS";

    /**
     * 标准年小时分钟格式
     */
    public final static String HOUR_MINUTE = "HH:mm";

    /**
     * 标准年小时分钟秒格式
     */
    public final static String HOUR_MINUTE_SECOND = "HH:mm:ss";

    private final static Logger logger = LoggerFactory.getLogger(DateUtils.class);

    /**
     * Number of milliseconds in a standard day.
     */
    public static final long MILLIS_PER_DAY = 24 * DateUtils.MILLIS_PER_HOUR;

    /**
     * Number of milliseconds in a standard hour.
     */
    public static final long MILLIS_PER_HOUR = 60* DateUtils.MILLIS_PER_MINUTE;

    /**
     * Number of milliseconds in a standard minute.
     */
    public static final long MILLIS_PER_MINUTE = 60* DateUtils.MILLIS_PER_SECOND;

    /**
     * Number of milliseconds in a standard second.
     */
    public static final long MILLIS_PER_SECOND = 1000;
    private final static String[] WEEK_NAMES = {"星期一", "星期二", "星期三", "星期四", "星期五", "星期六", "星期天"};

    /**
     * 在指定日期增加指定月数
     *
     * @param date 指定日期
     * @param months 指定月数
     * @return 结果日期
     */
    public static Date addMonth(Date date, int months) {
        if (months == 0) {
            return date;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, months);
        return c.getTime();
    }

    /**
     * 在指定日期增加指定天数
     *
     * @param date 指定日期
     * @param days 指定天数
     * @return 结果日期
     */
    public static Date addDay(Date date, int days) {
        if (days == 0) {
            return date;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DAY_OF_YEAR, days);
        return c.getTime();
    }

    /**
     * 在指定日期增加指定天数
     *
     * @param date 指定日期
     * @param days 指定天数
     * @return 结果日期
     */
    public static Date addDay(String date, int days) {
        return DateUtils.addDay(DateUtils.parseToDate(date), days);
    }

    public static Date addSecond(Date date, int second) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.SECOND, second);
        return c.getTime();
    }

    public static Date addMinute(Date date, int minute) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MINUTE, minute);
        return c.getTime();
    }

    public static Date addHour(Date date, int hour) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR, hour);
        return c.getTime();
    }

    /**
     * 当前日期之后
     *
     * @param date 目标日期
     * @return 比较结果
     */
    public static boolean isAfterToday(Object date) {
        Date currentDate = new Date();
        return DateUtils.compareDate(date, currentDate) == 1;
    }

    /**
     * 当前时间之后
     *
     * @param date 目标时间
     * @return 比较结果
     */
    public static boolean isAfterNow(Date date) {
        Date currentDate = new Date();
        return currentDate.compareTo(date) < 0;
    }

    /**
     * 当前日期之前
     *
     * @param date 目标日期
     * @return 比较结果
     */
    public static boolean isBeforeToday(Object date) {
        Date currentDate = new Date();
        return DateUtils.compareDate(date, currentDate) == -1;
    }

    /**
     * 当前时间之前
     *
     * @param date 目标时间
     * @return 比较结果
     */
    public static boolean isBeforeNow(Date date) {
        Date currentDate = new Date();
        return currentDate.compareTo(date) > 0;
    }

    /**
     * 前者时间早于后者时间
     *
     * @param former 起始日期
     * @param latter 终止日期
     * @return 比较结果
     */
    public static boolean isFormerBeforeLatter(Date former, Date latter) {
        return latter.compareTo(former) > 0;
    }

    /**
     * 比较两个日期date1大于date2 返回1 等于返回0 小于返回-1
     *
     * @param date1 目标日期1
     * @param date2 目标日期2
     * @return date1大于date2 返回1 等于返回0 小于返回-1
     */
    public static int compareDate(Object date1, Object date2) {
        if (date1 == null || date2 == null) {
            String msg = "illegal arguments,date1 and date2 must be not null.";
            throw new IllegalArgumentException(msg);
        }
        Date d1 = (Date) (date1 instanceof String ? DateUtils.parseToDate((String) date1) : date1);
        Date d2 = (Date) (date2 instanceof String ? DateUtils.parseToDate((String) date2) : date2);
        return DateUtils.round(d1, Calendar.DATE).compareTo(DateUtils.round(d2, Calendar.DATE));
    }

    /**
     * 比较两个时间
     * @param date1 目标时间1
     * @param date2 目标时间2
     * @return date1大于date2 返回1 等于返回0 小于返回-1
     */
    public static long compareDateTime(Object date1, Object date2) {
        if (date1 == null || date2 == null) {
            String msg = "illegal arguments,date1 and date2 must be not null.";
            throw new IllegalArgumentException(msg);
        }
        Date d1 = (Date) (date1 instanceof String ? DateUtils.parseToDate((String) date1) : date1);
        Date d2 = (Date) (date2 instanceof String ? DateUtils.parseToDate((String) date2) : date2);
        assert d1 != null;
        assert d2 != null;
        return d1.getTime() - d2.getTime();
    }

    /**
     * 按照日期格式解析成Date对象
     *
     * @param date 目标日期
     * @param pattern 格式
     * @return 结果日期
     */
    public static Date parseToDate(Date date, String pattern) {
        if (StringUtils.isBlank(pattern)) {
            String msg = "the date or pattern is empty.";
            throw new IllegalArgumentException(msg);
        }
        String dateForPattern = DateUtils.formatDate(date, pattern);
        return DateUtils.parseToDate(dateForPattern, pattern);
    }

    /**
     * 将long型整数转化为Date对象。
     *
     * @param date 时间对应的long值
     * @return 时间对象
     */
    public static Date parseToDate(Long date) {
        return new Date(date);
    }

    /**
     * 将日期或者时间戳转化为日期对象
     *
     * @param date yyyy-MM-dd or yyyy-MM-dd HH:mm:ss or yyyy-MM-dd HH:mm:ss.SSS
     * @return 结果日期
     */
    public static Date parseToDate(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        if (StringUtils.isNumeric(date)) {
            long timestamp = Long.parseLong(date);
            if (timestamp > 0 && timestamp < Long.MAX_VALUE) {
                return new Date(timestamp);
            } else {
                return null;
            }
        }
        if (date.indexOf(":") > 0) {
            return DateUtils.parseToDate(date, DateUtils.DATETIME_PATTERN);
        } else if (date.indexOf(".") > 0) {
            return DateUtils.parseToDate(date, DateUtils.DATETIME_MILL_SECOND);
        } else {
            return DateUtils.parseToDate(date, DateUtils.DATE_PATTERN);
        }
    }

    /**
     * 将日期或者时间字符串转化为日期对象
     *
     * @param date 日期字符串
     * @param pattern 格式字符串</br> yyyy-MM-DD, yyyy/MM/DD, yyyyMMdd</br> yyyy-MM-dd-HH:mm:ss, yyyy-MM-dd HH:mm:ss
     *            格式字符串可选字符："GyMdkHmsSEDFwWahKzZ"
     * @return Date
     */
    public static Date parseToDate(String date, String pattern) {
        try {
            if (StringUtils.isEmpty(pattern) || StringUtils.isEmpty(date)) {
                String msg = "the date or pattern is empty.";
                throw new IllegalArgumentException(msg);
            }
            SimpleDateFormat df = new SimpleDateFormat(pattern.trim());
            return df.parse(date.trim());
        } catch (Exception e) {
            DateUtils.logger.error("Method===DateUtils.convertDate error!", e);
            return null;
        }
    }

    /**
     * 将时间字符串转化为时间对象Time
     *
     * @param time 时间字符串
     * @param pattern 格式字符串 yyyy-MM-dd HH:mm:ss or yyyy-MM-dd HH:mm:ss.SSS
     * @return
     */
    public static Time parseToTime(String time, String pattern) {
        if ("24:00:00".equals(time)) {
            time = "23:59:59";
        }
        Date d = DateUtils.parseToDate(time, pattern);
        return new Time(d.getTime());
    }

    /**
     * 获得日期相差天数
     *
     * @param date1 日期
     * @param date2 日期
     * @return
     */
    public static int diffDate(Date date1, Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / DateUtils.MILLIS_PER_DAY);
    }

    /**
     * 获取两个日期相差的分钟数
     *
     * @param date1
     * @param date2
     * @return
     */
    public static int diffMinute(Date date1, Date date2) {
        return (int) ((date1.getTime() - date2.getTime()) / DateUtils.MILLIS_PER_MINUTE);
    }

    public static long diffSeconds(Date date1, Date date2) {
        return ((date1.getTime() - date2.getTime()) / DateUtils.MILLIS_PER_SECOND);
    }

    /**
     * 格式为时间字符串
     *
     * @param date 日期
     * @return yyyy-MM-dd Date
     */
    public static String formatDate(Date date) {
        try {
            return DateUtils.formatDate(date, DateUtils.DATE_PATTERN);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 按指定格式字符串格式时间
     *
     * @param date 日期或者时间
     * @param pattern 格式化字符串 yyyy-MM-dd， yyyy-MM-dd HH:mm:ss, yyyy年MM月dd日 etc.</br>
     * @return
     */
    public static String formatDate(Date date, String pattern) {
        if (null == date || StringUtils.isBlank(pattern)) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(pattern.trim());
        return format.format(date);
    }

    /**
     * 格式为时间戳字符串
     *
     * @param date 时间
     * @return yyyy-MM-dd HH:mm:ss Date
     */
    public static String formatDateTime(Date date) {
        try {
            return DateUtils.formatDate(date, DateUtils.DATETIME_PATTERN);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 将指定时间格式为字符串'yyyyMMddHHmmss'.
     *
     * @return
     */
    public static String formatDateToYMDHMS(Date date) {
        return DateUtils.formatDate(date, DateUtils.DATETIME_SHORT_PATTERN);
    }

    /**
     * 将指定时间格式为字符串'yyyy-MM'.
     *
     * @return
     */
    public static String formatMonth(Date date) {
        return DateUtils.formatDate(date, DateUtils.MONTH_PATTERN);
    }

    /**
     * 将当前时间格式为字符串'yyyyMMddHHmmss'.
     *
     * @return
     */
    public static String formatNowToYMDHMS() {
        return DateUtils.formatDateToYMDHMS(new Date());
    }

    public static Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获得本周第一天
     *
     * @param date
     * @return
     */
    public static Date getFirstDayOfThisWeek(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return cal.getTime();
    }

    /**
     * 获得本月第一天
     * @param date
     * @return
     */
    public static Date getFirstDateOfCurrentMonth(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 获得小时
     *
     * @param date
     * @return
     */
    public static int getHourOfDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.HOUR_OF_DAY);
    }

    public static Date getLastMonth() {
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        c.set(Calendar.MONTH, month - 1);
        return c.getTime();
    }

    /**
     * 获得分钟数
     *
     * @param date
     * @return
     */
    public static int getMinute(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MINUTE);
    }

    /**
     * 获取后续第n天日期
     *
     * @param date
     * @param n 第n天
     * @return
     */
    public static Date getNextNDay(Date date, int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, n);
        return cal.getTime();
    }

    /**
     * 获得星期数
     *
     * @param date 日期
     * @return
     */
    public static int getWeekNumber(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int number = c.get(Calendar.DAY_OF_WEEK) - 1;
        if (number == 0) {
            number = 7;
        }
        return number;
    }

    /**
     * 获得星期名称
     *
     * @param date
     * @return
     */
    public static String getWeekNumberString(Date date) {
        int dayNum = DateUtils.getWeekNumber(date);
        return DateUtils.WEEK_NAMES[dayNum - 1];
    }

    /**
     * 是否同一天
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameDay(Object date1, Object date2) {
        return DateUtils.compareDate(date1, date2) == 0;
    }

    /**
     * 检查时间或者字符串是否合法
     *
     * @param date 时间
     * @param pattern 格式串
     * @return
     */
    public static boolean isValidDate(String date, String pattern) {
        try {
            DateUtils.parseToDate(pattern, date);
            return true;
        } catch (Exception e) {
            DateUtils.logger.error("Method===DateUtils.isValidDate error!", e);
            return false;
        }
    }

    /**
     * 获得当前时间戳
     *
     * @return Timestamp
     */
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * 获得当前时间字符串,格式为：yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String nowDateTime() {
        return DateUtils.formatDate(new Date(), DateUtils.DATETIME_PATTERN);
    }

    /**
     * 按指定roundType格式化日期。
     *
     * @param date 日期
     * @param roundType Calendarl
     * @return Date
     */
    public static Date round(Date date, int roundType) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        switch (roundType) {
            case Calendar.MONTH:
                c.set(Calendar.DAY_OF_MONTH, 1);
                break;
            case Calendar.DATE:
                c.set(Calendar.HOUR_OF_DAY, 0);
                break;
            case Calendar.HOUR:
                c.set(Calendar.MINUTE, 0);
                break;
            case Calendar.MINUTE:
                c.set(Calendar.SECOND, 0);
                break;
            case Calendar.SECOND:
                c.set(Calendar.MILLISECOND, 0);
                break;
            default:
                throw new IllegalArgumentException("invalid round roundType.");
        }
        return c.getTime();
    }

    /**
     * 获得当前日期对象
     *
     * @return
     */
    public static Date today() {
        return DateUtils.parseToDate(DateUtils.formatDate(new Date()), DateUtils.DATE_PATTERN);
    }

    /**
     * 获得当前日期字符串,格式为：yyyy-MM-dd
     *
     * @return
     */
    public static String todayString() {
        return DateUtils.formatDate(new Date());
    }

    /**
     *
     * 将日期或者时间字符串转化为Timestamp对象
     *
     * @param date 日期字符串
     * @param pattern 格式字符串</br> yyyy-MM-DD, yyyy/MM/DD, yyyyMMdd</br> yyyy-MM-dd-HH:mm:ss, yyyy-MM-dd HH:mm:ss
     * @return Timestamp
     * @author reeboo
     */
    public static Timestamp toTimestamp(String date, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern.trim());
        try {
            return new Timestamp(format.parse(date).getTime());
        } catch (ParseException e) {
        }
        return null;
    }

    /**
     * 得到指定date的起始时间
     *
     * @param date
     * @return
     */
    public static Date getBeginOfTheDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * 得到指定date的结束时间
     * @param date
     * @return
     */
    public static Date getEndOfTheDate(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 59);
        return cal.getTime();
    }

    /**
     * 判断当前时间是否在某个区间内
     * @param startDate
     * @param endDate
     * @return
     */
    public static boolean todayInClose(Date startDate, Date endDate) {
        Date currentDate = parseToDate(new Date(), DATE_PATTERN);
        return currentDate.getTime() >= startDate.getTime()
                && currentDate.getTime() <= endDate.getTime();
    }

    /**
     * 判断某个时间是否在对应的时间段内
     * @param startDate
     * @param endDate
     * @param time
     * @return
     */
    public static boolean timeInClose(Date startDate, Date endDate, Date time) {
        return time.getTime() >= startDate.getTime() && time.getTime() <= endDate.getTime();
    }

    /**
     * 时间转换成毫秒,空返回-1
     * @param Date格式时间
     * @return 毫秒
     */
    public static Long getTime(Date date) {
        if (null == date) {
            return null;
        }
        return date.getTime();
    }

    /**
     * 判断两个时间区间是否有交集
     * @param closeOneStartDate
     * @param closeOneEndDate
     * @param closeTwoStartDate
     * @param closeTwoEndDate
     * @return
     */
    public static boolean timeIntersection(Date closeOneStartDate, Date closeOneEndDate,
                                           Date closeTwoStartDate, Date closeTwoEndDate) {
        long start = Math.max(closeOneStartDate.getTime(), closeTwoStartDate.getTime());
        long end = Math.min(closeOneEndDate.getTime(), closeTwoEndDate.getTime());
        return start <= end;
    }

    /**
     * 按默认格式的字符串距离今天的天数
     *
     * @param date
     *            日期字符串
     * @return
     */
    public static int countDays(String date) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parseToDate(date));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }

    /**
     * 按用户格式字符串距离今天的天数
     *
     * @param date
     *            日期字符串
     * @param format
     *            日期格式
     * @return
     */
    public static int countDays(String date, String format) {
        long t = Calendar.getInstance().getTime().getTime();
        Calendar c = Calendar.getInstance();
        c.setTime(parseToDate(date, format));
        long t1 = c.getTime().getTime();
        return (int) (t / 1000 - t1 / 1000) / 3600 / 24;
    }
}