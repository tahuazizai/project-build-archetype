#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.constant;

/**
 * @version: 1.00.00
 * @description: 通用常量类
 * @copyright: Copyright (c) 2022 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hj
 * @date: 2022-06-27 8:42
 */
public final class CommonConstant {

    public static final String COMMA = ",";
    public static final String SEMICOLON = ";";
    public static final String DASHES = "-";
    public static final String UNDERLINE = "_";
    public static final String COLON = ":";
    public static final String SLASH = "/";
    public static final String BACKSLASH = "${symbol_escape}${symbol_escape}";
    public static final String EQUAL = "=";
    public static final String SPACE = " ";
    public static final String SINGLEQUOTA = "'";
    public static final String SPOT = ".";
    public static final String BACK_QUOTE = ")";
    public static final String FRONT_QUOTE = "(";
    public static final String WAVE_QUOTE = "~";
    public static final String ASTERISK = "*";
    public static final String SEPARATOR = "|";
    public static final String ESCAPE_SEPARATOR = "${symbol_escape}${symbol_escape}|";
    public static final String PRE_BRACKET = "[";
    public static final String POST_BRACKET = "]";

    public static final Integer ZERO_INT = 0;
    public static final Integer ONE_INT = 1;
    public static final Integer NEGATIVE_ONE_INT = -1;
    public static final Integer TWO_INT = 2;
    public static final Integer THREE_INT = 3;
    public static final Integer FOUR_INT = 4;
    public static final Integer FIVE_INT = 5;
    public static final String ZERO_STR = "0";
    public static final String ONE_STR = "1";
    public static final String TWO_STR = "2";
    public static final String THREE_STR = "3";
    public static final String FOUR_STR = "4";
    public static final String FIVE_STR = "5";
    public static final char ONE_CHAR = '1';
    public static final char ZERO_CHAR = '0';

    public static final String FANOUT = "fanout";
    public static final String DIRECT = "direct";
    public static final String TOPIC = "topic";
    public static final String HEADERS = "headers";
    public static final String X_DELAY = "x-delay";

    public static final String ID = "id";

    /**
     * 修改时间字段
     */
    public static final String UPDATE_TIME = "updateTime";

    /**
     * 创建时间字段
     */
    public static final String CREATE_TIME = "createTime";

    /**
     * 创建人名称字段
     */
    public static final String CREATE_BY = "creatorName";

    /**
     * 修改人名称字段
     */
    public static final String UPDATE_BY = "updaterName";

    /**
     * 版本号字段
     */
    public static final String VERSION = "version";


    /**
     * 默认用户名
     */
    public static final Long DEFAULT_USER_ID = 10000L;

    /**
     * 默认用户名
     */
    public static final String DEFAULT_USER_NAME = "admin";
}
