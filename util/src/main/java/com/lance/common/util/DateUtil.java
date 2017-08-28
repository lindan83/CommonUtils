package com.lance.common.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author lindan
 *         日期工具
 */
public class DateUtil {
    private DateUtil() {
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 日期时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String PATTERN_DATETIME = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式：yyyy-MM-dd
     */
    public static final String PATTERN_DATE = "yyyy-MM-dd";

    /**
     * 时间格式：HH:mm:ss
     */
    public static final String PATTERN_TIME = "HH:mm:ss";

    /**
     * 日期时间格式：yyyy-MM-dd HH:mm
     */
    public static final String PATTERN_DATETIME_HH_MM = "yyyy-MM-dd HH:mm";

    /**
     * 时间格式：HH:mm
     */
    public static final String PATTERN_HH_MM = "HH:mm";

    /**
     * 日期格式：yyyy-MM
     */
    public static final String PATTERN_YEAR_MONTH = "yyyy-MM";

    /**
     * 年份
     */
    public static final String YEAR = "yyyy";

    /**
     * 月份
     */
    public static final String MONTH = "MM";

    /**
     * 日期
     */
    public static final String DATE = "dd";

    /**
     * 时
     */
    public static final String HOUR = "HH";

    /**
     * 分
     */
    public static final String MINUTE = "mm";

    /**
     * 秒
     */
    public static final String SECOND = "ss";

    private static final Map<String, ThreadLocal<SimpleDateFormat>> formatterMap = new HashMap<>();

    /**
     * 根据pattern获取对应线程的SimpleDateFormat实例
     *
     * @param pattern Pattern Map中的key
     * @return SimpleDateFormat实例
     */
    private static SimpleDateFormat getSimpleDateFormat(final String pattern) {
        ThreadLocal<SimpleDateFormat> threadLocal = formatterMap.get(pattern);
        if (threadLocal == null) {
            synchronized (DateUtil.class) {
                threadLocal = formatterMap.get(pattern);
                if (threadLocal == null) {
                    threadLocal = new ThreadLocal<SimpleDateFormat>() {
                        @Override
                        protected SimpleDateFormat initialValue() {
                            return new SimpleDateFormat(pattern, Locale.getDefault());
                        }
                    };
                    formatterMap.put(pattern, threadLocal);
                }
            }
        }
        return threadLocal.get();
    }

    /**
     * 将日期转换为时间戳
     *
     * @param date DateString
     * @return Long
     */
    public static long getTime(Date date) {
        return date.getTime();
    }

    /**
     * 将日期字符串转换为时间戳
     *
     * @param dateString DateString
     * @param pattern    Pattern
     * @return Long
     */
    public static long getTime(String dateString, String pattern) {
        try {
            return getSimpleDateFormat(pattern).parse(dateString).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 将日期字符串转换为日期对象
     *
     * @param dateString DateString
     * @param pattern    Pattern
     * @return Date
     */
    public static Date parseDate(String dateString, String pattern) {
        try {
            return getSimpleDateFormat(pattern).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将指定日期转换为某种格式的字符串
     *
     * @param date    Date
     * @param pattern Pattern
     * @return String
     */
    public static String getDate(Date date, String pattern) {
        try {
            return getSimpleDateFormat(pattern).format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 处理yyyy-MM-ddT11:09:30Z格式的时间日期字符串，并生成yyyy-MM-dd HH:mm:ss
     *
     * @param dateString DateString
     * @return String
     */
    public static String changeDateFormat(String dateString) {
        if (TextUtils.isEmpty(dateString)) {
            return "";
        }
        return dateString.substring(0, 19).replaceAll("[A-Z|a-z]", " ");
    }

    /**
     * 处理yyyy-MM-ddT11:09:30Z格式的时间日期字符串，并生成yyyy-MM-dd HH:mm HH:mm的字符串
     *
     * @param dateString DateString
     * @return String
     */
    public static String getDateTimeRangeString(String dateString) {
        if (TextUtils.isEmpty(dateString)) {
            return "";
        }
        String beginDateString = changeDateFormat(dateString);
        Date beginDate = parseDate(beginDateString, PATTERN_DATETIME_HH_MM);
        Date endDate = null;
        if (beginDate != null) {
            endDate = new Date(beginDate.getTime() + 1800000);
        }

        return getDate(beginDate, PATTERN_DATETIME_HH_MM) + " - " + getDate(endDate, PATTERN_HH_MM);
    }

    /**
     * 根据指定日期获取对应周的第一天，一周的开始从周日开始
     *
     * @param today 指定日期
     * @return 所在周的第一天
     */
    public static Date getWeekBeginDateOfToday(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        return calendar.getTime();
    }

    /**
     * 根据指定日期获取对应周的最后一天，一周的结束为周六
     *
     * @param today 指定日期
     * @return 所在周的最后一天
     */
    public static Date getWeekEndDateOfToday(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        return calendar.getTime();
    }

    /**
     * 根据指定日期获取对应月的第一天，即该月一号
     *
     * @param today 指定日期
     * @return 对应月的第一天
     */
    public static Date getMonthBeginDateOfToday(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 根据指定日期获取对应月的最后一天
     *
     * @param today 指定日期
     * @return 对应月的最后一天
     */
    public static Date getMonthEndDateOfToday(Date today) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calendar.getTime();
    }

    /**
     * 根据指定日期获取对应周的范围字符串
     *
     * @param today        指定日期
     * @param concatString 连接字符串
     * @return 结果
     */
    public static String getWeekRangeOfToday(Date today, String concatString) {
        Date beginDate = getWeekBeginDateOfToday(today);
        Date endDate = getWeekEndDateOfToday(today);
        String beginDateString = getDate(beginDate, PATTERN_DATE);
        String endDateString = getDate(endDate, PATTERN_DATE);
        return beginDateString + concatString + endDateString;
    }

    /**
     * 获取日期值
     *
     * @param dateString DateString
     * @return String
     */
    public static String getDateValue(String dateString) {
        if (TextUtils.isEmpty(dateString)) {
            return "";
        }
        dateString = changeDateFormat(dateString);
        Date date = parseDate(dateString, PATTERN_DATETIME);
        return getDate(date, DATE);
    }

    /**
     * 获取时间值
     *
     * @param dateString DateString
     * @return String
     */
    public static String getHourAndMinute(String dateString) {
        if (TextUtils.isEmpty(dateString)) {
            return "";
        }
        dateString = changeDateFormat(dateString);
        Date date = parseDate(dateString, PATTERN_DATETIME);
        return getDate(date, PATTERN_HH_MM);
    }

    /**
     * 获取一个月的最后一天
     *
     * @param date Date
     * @return int
     */
    public static int getLastDateInMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 改变日期字符串格式
     *
     * @param dateString 原始日期
     * @param oldPattern 原格式
     * @param newPattern 新格式
     * @return 结果
     */
    public static String changeDateFormat(String dateString, String oldPattern, String newPattern) {
        try {
            SimpleDateFormat formatter = getSimpleDateFormat(oldPattern);
            Date date = formatter.parse(dateString);
            formatter.applyLocalizedPattern(newPattern);
            return formatter.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取年份
     *
     * @param date Date
     * @return 年份
     */
    public static int getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取月份
     *
     * @param date Date
     * @return 月份
     */
    public static int getMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取日期
     *
     * @param date Date
     * @return 日期
     */
    public static int getDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取小时
     *
     * @param date Date
     * @return 小时
     */
    public static int getHour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取分钟
     *
     * @param date Date
     * @return 分钟
     */
    public static int getMinute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR);
    }

    /**
     * 获取秒
     *
     * @param date Date
     * @return 秒
     */
    public static int getSecond(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }
}
