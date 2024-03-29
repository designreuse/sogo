package com.yihexinda.core.utils;
/**
 * Copyright &copy; 2017-2020  All rights reserved.
 * <p>
 * Licensed under the 深圳市领居科技 License, Version 1.0 (the "License");
 */

import org.joda.time.DateTimeUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.yihexinda.core.utils.StringUtil.utf8Togb2312;

/**
 * 类说明：
 *
 *
 * <p>
 * 详细描述：.
 *
 * @author 罗书明 CreateDate: 2016年7月21日
 */
public class DateUtils {

    public static final String YYYY年MM月DD日 = "yyyy年MM月dd日";

    /**
     * MMdd.
     */
    public static final String PATTERN_MMdd = "MMdd";

    /**
     * YYYYMMDD.
     */
    public static final String PATTERN_yyyyMMdd = "yyyyMMdd";

    /**
     * YYYYMM.
     */
    public static final String PATTERN_yyyyMM = "yyyyMM";

    /**
     * YYYYMMDDHHMMSS.
     */
    public static final String PATTERN_yyyyMMddHHmmss = "yyyyMMddHHmmss";

    /**
     * YYYY-MM-DD.
     */
    public static final String PATTERN_yyyy_MM_dd = "yyyy-MM-dd";

    /**
     * YYYY-M-D.
     */
    public static final String PATTERN_yyyy_M_d = "yyyy-M-d";

    /**
     * yyyy-MM-dd HH:mm:ss.
     */
    public static final String PATTERN_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * yyyy-MM-dd HH:mm.
     */
    public static final String PATTERN_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";

    /**
     * HH:mm:ss.
     */
    public static final String PATTERN_HH_mm_ss = "HH:mm:ss";

    /**
     * The Constant mapChDate.
     */
    public static final Map<String, String> mapChDate = new HashMap<String, String>();

    public static final Map<Integer, String> mapChDate_week = new HashMap<Integer, String>();

    static {
        mapChDate.put("01", "一月");
        mapChDate.put("02", "二月");
        mapChDate.put("03", "三月");
        mapChDate.put("04", "四月");
        mapChDate.put("05", "五月");
        mapChDate.put("06", "六月");
        mapChDate.put("07", "七月");
        mapChDate.put("08", "八月");
        mapChDate.put("09", "九月");
        mapChDate.put("10", "十月");
        mapChDate.put("11", "十一月");
        mapChDate.put("12", "十二月");

        mapChDate_week.put(Calendar.MONDAY, "星期一");
        mapChDate_week.put(Calendar.TUESDAY, "星期二");
        mapChDate_week.put(Calendar.WEDNESDAY, "星期三");
        mapChDate_week.put(Calendar.THURSDAY, "星期四");
        mapChDate_week.put(Calendar.FRIDAY, "星期五");
        mapChDate_week.put(Calendar.SATURDAY, "星期六");
        mapChDate_week.put(Calendar.SUNDAY, "星期日");
    }


    /**
     * 方法说明：计算当前日期和上传日期的时间差，返回 一周前/X天前/X小时前/X分钟前/刚刚
     *
     * @param endDate
     * @return
     */
    public static String getTimeDisStr(Date endDate) {
        Date startDate = new Date();
        return getTimeDisStr(startDate, endDate);
    }

    /**
     * 方法说明：计算两个日期的时间差，返回 一周前/X天前/X小时前/X分钟前/刚刚
     *
     * @param startDate
     * @param endDate
     * @return
     */
    public static String getTimeDisStr(Date startDate, Date endDate) {
        if (endDate == null)
            return "";
        String str = "";
        long minDis = (startDate.getTime() - endDate.getTime()) / (60000);
        int min_week = 10080;
        int min_day = 1440;
        int min_hour = 60;
        if (minDis >= min_week) {
            str = "一周前";
        } else if (minDis >= min_day) {
            str = minDis / min_day + "天前";
        } else if (minDis >= min_hour) {
            str = minDis / min_hour + "小时前";
        } else if (minDis > 0) {
            str = minDis + "分钟前";
        } else {
            str = "刚刚";
        }
        return str;
    }


    /**
     * 方法说明：获取指定日期的下一天.
     *
     * @param now the now
     * @return the nextday
     */
    public static Date getNextday(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        return calendar.getTime();
    }

    /**
     * 方法说明：获取结束时间
     *
     * @return the day
     */
    public static Date getDayEnd() {
        Calendar cal = new GregorianCalendar();
                 cal.set(Calendar.HOUR_OF_DAY, 23);
                 cal.set(Calendar.MINUTE, 59);
                 cal.set(Calendar.SECOND, 59);
                return cal.getTime();
    }


    /**
     * 方法说明：获取指定日期的前一天.
     *
     * @param now the now
     * @return the preday
     */
    public static Date getPreday(Date now) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        return calendar.getTime();
    }

    /**
     * 方法说明：中文格式化日期，如:三月05.
     *
     * @param date the date
     * @return the string
     */
    public static String formatDate_ch(Date date) {
        AssertUtils.notNullAndEmpty(date);
        String str = formatDate(date, PATTERN_MMdd);
        str = mapChDate.get(str.substring(0, 2)) + str.substring(2);
        return str;
    }

    /**
     * 方法说明：中文格式化日期，如:2016年06月01日(星期一).
     *
     * @param date
     * @return
     */
    public static String formatDate_ch_all(Date date) {
        AssertUtils.notNullAndEmpty(date);
        String str = formatDate(date, YYYY年MM月DD日);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        str = str + "(" + mapChDate_week.get(calendar.get(Calendar.DAY_OF_WEEK)) + ")";
        return str;
    }


    /**
     * Format date.
     *
     * @param date    the date
     * @param pattern the pattern
     * @return the string
     */
    public static String formatDate(Date date, String pattern) {
        SimpleDateFormat sdf = null;
        if (pattern == null) {
            sdf = new SimpleDateFormat();
        } else {
            sdf = new SimpleDateFormat(pattern);
        }
        return sdf.format(date);
    }

    /**
     * Format date.
     *
     * @param date the date
     * @return the string
     */
    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat();
        return sdf.format(date);
    }

    /**
     * Parses the date.
     *
     * @param source  the source
     * @param pattern the pattern
     * @return the date
     * @throws ParseException the parse exception
     */
    public static Date parseDate(String source, String pattern)
            throws ParseException {
        SimpleDateFormat sdf = null;
        if (pattern == null) {
            sdf = new SimpleDateFormat();
        } else {
            sdf = new SimpleDateFormat(pattern);
        }
        return sdf.parse(source);
    }

    /**
     * Parses the date.
     *
     * @param source the source
     * @return the date
     * @throws ParseException the parse exception
     */
    public static Date parseDate(String source) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat();
        return sdf.parse(source);
    }

    /**
     * Parses the date.
     *
     * @param source      the source
     * @param defaultDate the default date
     * @return the date
     */
    public static Date parseDate(String source, Date defaultDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat();
            Date date = sdf.parse(source);
            return date;
        } catch (ParseException e) {
            // todo
        }
        return defaultDate;
    }

    /**
     * Parses the date.
     *
     * @param source      the source
     * @param pattern     the pattern
     * @param defaultDate the default date
     * @return the date
     */
    public static Date parseDate(String source, String pattern, Date defaultDate) {
        try {
            SimpleDateFormat sdf = null;
            if (pattern == null) {
                sdf = new SimpleDateFormat();
            } else {
                sdf = new SimpleDateFormat(pattern);
            }
            return sdf.parse(source);
        } catch (ParseException e) {
            // todo
        }
        return defaultDate;
    }

    /**
     * Try parse.
     *
     * @param source the source
     * @return true, if try parse
     * @throws ParseException the parse exception
     */
    public static boolean tryParse(String source) throws ParseException {
        try {
            Integer.parseInt(source);
            return true;
        } catch (NumberFormatException e) {
        }
        return false;

    }

    /**
     * 方法说明：日期操作，对日期date进行timeType类型的延长
     * amount单位.
     *
     * @param date     原日期
     * @param timeType 时间类型
     * @param amount   操作时间值
     * @return the date
     */
    public static Date changeDate(Date date, TimeType timeType, int amount) {
        if (date == null || timeType == null || amount == 0) {
            return date;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(timeType.getValue(), amount);
        return c.getTime();

    }

    /**
     * 方法说明：比较两个日期是否是同一天.
     *
     * @param date1 the date1
     * @param date2 the date2
     * @return true, if checks if is same day
     */
    public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }

    /**
     * 方法说明：比较两个日期是否是同一天.
     *
     * @param cal1 the cal1
     * @param cal2 the cal2
     * @return true, if checks if is same day
     */
    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
    }

    /**
     * 方法说明：比较两个日期是否是同一月份
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMonth(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameMonth(cal1, cal2);
    }

    /**
     * 方法说明：比较两个日期是否是同一月份
     *
     * @param cal1
     * @param cal2
     * @return
     */
    public static boolean isSameMonth(Calendar cal1, Calendar cal2) {
        if (cal1 == null || cal2 == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        return (cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH));
    }


    //获取0点
    public static Date getStartTime() {
        Calendar todayStart = Calendar.getInstance();
        todayStart.set(Calendar.HOUR_OF_DAY, 0);
        todayStart.set(Calendar.MINUTE, 0);
        todayStart.set(Calendar.SECOND, 0);
        todayStart.set(Calendar.MILLISECOND, 0);
        return todayStart.getTime();
    }

    //获取23点
    public static Date getnowEndTime() {
        return getnowEndTime(23,59,59,999);
    }

    //
    public static Date getnowEndTime(int hour,int min,int second,int millisecond) {
        Calendar todayEnd = Calendar.getInstance();
        todayEnd.set(Calendar.HOUR_OF_DAY, hour);
        todayEnd.set(Calendar.MINUTE, min);
        todayEnd.set(Calendar.SECOND, second);
        todayEnd.set(Calendar.MILLISECOND, millisecond);
        return todayEnd.getTime();
    }


    /**
     * 类说明：时间类型
     *
     *
     * <p>
     * 详细描述：
     *
     * </p>.
     */
    public enum TimeType {

        /**
         * The second.
         */
        SECOND(Calendar.SECOND),
        /**
         * The minute.
         */
        MINUTE(Calendar.MINUTE),
        /**
         * The hour.
         */
        HOUR(Calendar.HOUR),
        /**
         * The day.
         */
        DAY(
                Calendar.DATE),
        /**
         * The week.
         */
        WEEK(Calendar.WEEK_OF_MONTH),
        /**
         * The month.
         */
        MONTH(
                Calendar.MONTH),
        /**
         * The year.
         */
        YEAR(Calendar.YEAR);

        /**
         * The value.
         */
        private int value;

        /**
         * The Constructor.
         *
         * @param value the value
         */
        TimeType(int value) {
            this.value = value;
        }

        /**
         * Gets the value.
         *
         * @return the value
         */
        public int getValue() {
            return value;
        }
    }

    /**
     * Adds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date          the date, not null
     * @param calendarField the calendar field to add to
     * @param amount        the amount to add, may be negative
     * @return the new date object with the amount added
     * @deprecated Will become privately scoped in 3.0
     */
    public static Date add(Date date, int calendarField, int amount) {
        if (date == null) {
            throw new IllegalArgumentException("The date must not be null");
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(calendarField, amount);
        return c.getTime();
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of years to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addYears(Date date, int amount) {
        return add(date, Calendar.YEAR, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of months to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addMonths(Date date, int amount) {
        return add(date, Calendar.MONTH, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of weeks to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addWeeks(Date date, int amount) {
        return add(date, Calendar.WEEK_OF_YEAR, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of days to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addDays(Date date, int amount) {
        return add(date, Calendar.DAY_OF_MONTH, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of hours to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addHours(Date date, int amount) {
        return add(date, Calendar.HOUR_OF_DAY, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of minutes to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addMinutes(Date date, int amount) {
        return add(date, Calendar.MINUTE, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of seconds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addSeconds(Date date, int amount) {
        return add(date, Calendar.SECOND, amount);
    }

    //-----------------------------------------------------------------------

    /**
     * Adds a number of milliseconds to a date returning a new object.
     * The original date object is unchanged.
     *
     * @param date   the date, not null
     * @param amount the amount to add, may be negative
     * @return the new date object with the amount added
     */
    public static Date addMilliseconds(Date date, int amount) {
        return add(date, Calendar.MILLISECOND, amount);
    }





    public static void main(String args[]) {
//        Calendar now = Calendar.getInstance();
//        Calendar now2 = Calendar.getInstance();
//        now2.add(Calendar.DAY_OF_MONTH, -10);
//        System.out.println(getTimeDisStr(now.getTime(), now2.getTime()));

//        long lt = new Long("1545990971786");
        long lt = new Long("1545994781555");


        Date nowtime =  new Date(lt);
        System.out.println(DateUtils.formatDate(nowtime,DateUtils.PATTERN_yyyy_MM_dd_HH_mm_ss));


        Date date =  new Date();
        Date date1 = DateUtils.addMilliseconds(date, -60);
        System.out.println(date.getTime()-date1.getTime());


        List<String> list = null;
        try {
            list = DateUtils.getIntervalTimeList("09:40:00","22:20:00",20);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for (String s : list) {
            System.out.println(s);
        }

    }

    /**
     * 将一个秒数的时长转化为:X分X秒 的字符串
     *
     * @param second
     * @return
     */
    public static String secondNum(Integer second) {
        if (second != null && second > 0) {
            return second / 60 + "分" + (second % 60) + "秒";
        }
        return "0分0秒";
    }


    /**
     * 获取固定间隔时刻集合
     * @param start 开始时间
     * @param end 结束时间
     * @param interval 时间间隔(单位：分钟)
     * @return
     */
    public static List<String> getIntervalTimeList(String start,String end,int interval) throws ParseException {
        Date startDate = DateUtils.parseDate(start,"HH:mm:ss");
        Date endDate = DateUtils.parseDate(end,"HH:mm:ss");
        List<String> list = new ArrayList<>();
        while(startDate.getTime()<=endDate.getTime()){
            list.add(DateUtils.formatDate(startDate,"HH:mm:ss"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.MINUTE,interval);
            if(calendar.getTime().getTime()>endDate.getTime()){
                if(!startDate.equals(endDate)){
                    list.add(DateUtils.formatDate(endDate,"HH:mm:ss"));
                }
                startDate = calendar.getTime();
            }else{
                startDate = calendar.getTime();
            }

        }
        return list;
    }


}
