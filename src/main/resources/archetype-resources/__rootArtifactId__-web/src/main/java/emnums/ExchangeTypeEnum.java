#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.emnums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @Version: 1.0
 * @Description: 定时类型枚举
 * @copyright: Copyright (c) 2019 立林科技 All Rights Reserved
 * @company 厦门立林科技有限公司
 * @Author: hj
 * @date: 2022-02-12 10:14
 * @history:
 */
@Getter
@AllArgsConstructor
public enum ExchangeTypeEnum {
    DIRECT("direct", "直连"),
    TOPIC("topic", "topic"),
    FANOUT("fanout", "广播"),
    HEADERS("headers", "header"),
    X_DELAYED_MESSAGE("x-delayed-message", "x-delayed-message"),
    ;
    /**
     * code
     */
    private String code;
    /**
     * 描述
     */
    private String desc;

    /**
     * 通过code获取描述
     * @param code
     * @return
     */
    public static String getByCode(Integer code) {
        if (null == code) {
            return null;
        }
        return Arrays.stream(values()).filter(workFlowStatusEnum -> Objects.equals(workFlowStatusEnum.code, code)).findFirst().orElse(DIRECT).getDesc();
    }
}
