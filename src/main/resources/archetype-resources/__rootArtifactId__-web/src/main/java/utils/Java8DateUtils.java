#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.utils;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.Date;

/**
 * @version: 1.00.00
 * @description: 日期工具类
 * @copyright: Copyright (c) 2021 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2021-11-20 10:48
 */
public class Java8DateUtils {

    private static final ZoneId DEFAULT_ZONE_ID = ZoneId.systemDefault();

    // 根据指定格式显示日期和时间
    /**
     * yyyy-MM
     */
    public static final DateTimeFormatter YYYYMM = DateTimeFormatter.ofPattern("yyyy-MM");
    /**
     * yyyy-MM
     */
    public static final DateTimeFormatter YYYY = DateTimeFormatter.ofPattern("yyyy");
    /**
     * yyyy-MM-dd
     */
    public static final DateTimeFormatter YYYYMMDD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * yyyyMMdd
     */
    public static final DateTimeFormatter YYYYMMDD_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    /**
     * yyyyMMdd
     */
    public static final DateTimeFormatter HHMMSS_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");
    /**
     * yyyy-MM-dd HH
     */
    public static final DateTimeFormatter YYYYMMDDHH = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final DateTimeFormatter YYYYMMDDHHMM = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final DateTimeFormatter YYYYMMDDHHMMSS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 获取指定日期后n周日期
     *
     * @param localDate
     * @param day
     * @return
     */
    public static LocalDate getAppointDateAfterWeek(LocalDate localDate, long day) {
        return localDate.plusWeeks(day);
    }


    /**
     * 获取指定日期前n天日期
     *
     * @param localDate
     * @param day
     * @return
     */
    public static LocalDate getAppointDateBeforeDay(LocalDate localDate, long day) {
        return localDate.minusDays(day);
    }

    /**
     * 获取当前日期前n天日期
     *
     * @param day
     * @return
     */
    public static LocalDate getCurrentDateBeforeDay(long day) {
        return LocalDate.now().minusDays(day);
    }

    /**
     * 获取指定日期前n周日期
     *
     * @param localDate
     * @param day
     * @return
     */
    public static LocalDate getAppointDateBeforeWeek(LocalDate localDate, long day) {
        return localDate.minusWeeks(day);
    }

    /**
     * 获取指定日期后n月日期
     *
     * @param localDate
     * @param day
     * @return
     */
    public static LocalDate getAppointDateAfterMonth(LocalDate localDate, long day) {
        return localDate.plusMonths(day);
    }


    /**
     * 获取指定日期前n月日期
     *
     * @param localDate
     * @param day
     * @return
     */
    public static LocalDate getAppointDateBeforeMonth(LocalDate localDate, long day) {
        return localDate.minusMonths(day);
    }

    /**
     * 获取指定日期后n年日期
     *
     * @param localDate
     * @param day
     * @return
     */
    public static LocalDate getAppointDateAfterYear(LocalDate localDate, long day) {
        return localDate.plusYears(day);
    }


    /**
     * 获取指定日期前n年日期
     *
     * @param localDate
     * @param day
     * @return
     */
    public static LocalDate getAppointDateBeforeYear(LocalDate localDate, long day) {
        return localDate.minusYears(day);
    }

    /**
     * 获取一周中的某一天日期
     *
     * @param today
     * @param day
     * @return
     */
    public static LocalDate getOneDayOfWeek(TemporalAccessor today, int day) {
        TemporalField field = WeekFields.of(DayOfWeek.MONDAY, 1).dayOfWeek();
        return LocalDate.from(today).with(field, day);
    }

    /**
     * 获取指定日期所属星期
     *
     * @param localDate
     * @return
     */
    public static int getWeekOfDay(LocalDate localDate) {
        return localDate.get(ChronoField.DAY_OF_WEEK);
    }

    /**
     * 获取指定日期所在月的天数
     *
     * @param localDate
     * @return
     */
    public static int getLengthOfMonth(LocalDate localDate) {
        return localDate.lengthOfMonth();
    }

    /**
     * 构造LocalDate
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public static LocalDate constrctLocalDate(int year, int month, int day) {
        return LocalDate.of(year, month, day);
    }

    /**
     * 获取当前时间
     *
     * @param formatter
     * @return
     */
    public static String getCurrentTime(DateTimeFormatter formatter) {
        return LocalDateTime.now().format(formatter);
    }

    /**
     * 获取指定日期字符串
     *
     * @param localDate
     * @return
     */
    public static String getAppointDay(LocalDate localDate) {
        return localDate.format(YYYYMMDD);
    }

    /**
     * 转换LocalDate为LocalDateTime
     *
     * @param localDate
     * @return
     */
    public static LocalDateTime convertLocalDateTime(LocalDate localDate) {
        return localDate.atStartOfDay();
    }

    /**
     * 转换LocalDateTime为LocalDate
     *
     * @param localDateTime
     * @return
     */
    public static LocalDate convertLocalDate(LocalDateTime localDateTime) {
        return localDateTime.toLocalDate();
    }

    /**
     * LocalDate 转成 Date
     *
     * @param localDate LocalDate 类型日期
     * @return Date 类型的日期
     */
    public static Date toDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * LocalDateTime 转成 Date
     *
     * @param localDateTime LocalDateTime 类型时间
     * @return Date 类型的时间
     */
    public static Date toDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(DEFAULT_ZONE_ID).toInstant());
    }

    /**
     * 从 Date 获取特定时区的时间
     *
     * @param date Date 类型的时间
     * @return DateUtils.DEFAULT_ZONE_ID 标定的时区时间
     */
    public static ZonedDateTime toZonedDateTime(Date date) {
        return date.toInstant().atZone(DEFAULT_ZONE_ID);
    }

    /**
     * Date 类型日期转成 LocalDate 类型的日期
     *
     * @param date Date 类型的日期
     * @return LocalDate 类型的日期
     */
    public static LocalDate toLocalDate(Date date) {
        return toZonedDateTime(date).toLocalDate();
    }

    /**
     * Date 类型的时间转成 LocalDateTime 类型的时间
     *
     * @param date Date 类型的时间
     * @return LocalDateTime 类型的时间
     */
    public static LocalDateTime toLocalDateTime(Date date) {
        return toZonedDateTime(date).toLocalDateTime();
    }

    /**
     * 字符串类型的时间转成 LocalDateTime 类型的时间
     *
     * @param str       字符串时间
     * @param formatter 字符串时间格式
     * @return LocalDateTime 类型的时间
     */
    public static LocalDateTime toLocalDateTime(String str, DateTimeFormatter formatter) {
        return LocalDateTime.parse(str, formatter);
    }

    /**
     * 字符串转成 LocalDate 类型的日期
     *
     * @param str       字符串日期
     * @param formatter 字符串格式，如 DateUtils.DATE_PATTERN
     * @return LocalDate 类型的日期
     */
    public static LocalDate toLocalDate(String str, DateTimeFormatter formatter) {
        if(formatter.equals(YYYYMM)){
            return YearMonth.parse(str,formatter).atDay(1);
        }
        if(formatter.equals(YYYY)){
            return Year.parse(str, formatter).atDay(1);
        }
        return LocalDate.parse(str, formatter);
    }

    /**
     * 从全局缓存中拿 pattern 对应的 formatter 或者新建
     *
     * @param pattern pattern
     * @return pattern 对应的 formatter
     */
    private static DateTimeFormatter getFormatter(String pattern) {
        return DateTimeFormatter.ofPattern(pattern);
    }

    /**
     * LocalDate 类型的日期格式化为指定格式的字符串
     *
     * @param localDate LocalDate 类型的日期
     * @param formatter 格式，如：DateUtils.DATE_PATTERN
     * @return 返回指定格式字符串时间
     */
    public static String formartLocalDate(LocalDate localDate, DateTimeFormatter formatter) {
        return formatLocalDateTime(convertLocalDateTime(localDate), formatter);
    }

    /**
     * LocalDateTime 类型的时间格式化为指定格式的字符串
     *
     * @param localDateTime LocalDateTime 类型的时间
     * @param formatter     格式，如 DateUtils.DATE_PATTERN
     * @return 指定格式字符串时间
     */
    public static String formatLocalDateTime(LocalDateTime localDateTime, DateTimeFormatter formatter) {
        return localDateTime.format(formatter);
    }
}
