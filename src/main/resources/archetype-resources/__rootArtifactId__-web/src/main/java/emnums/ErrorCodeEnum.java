#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.emnums;

import java.util.Arrays;

/**
 * @Version: 1.0
 * @Description: 错误编码枚举
 * @copyright: Copyright (c) 2019 立林科技 All Rights Reserved
 * @company 厦门立林科技有限公司
 * @Author: hj
 * @date: 2021-05-06 16:17
 * @history:
 */
public enum ErrorCodeEnum {
    DATABASE_NOT_RECORD(10010, "数据库没有对应记录"),
    OBJ_TO_MAP_FAIL(10011, "对象转换map失败"),
    JSON_STR_NOT_OBJ(10013, "json字符串中params不是对象"),
    BINARY_LENGTH_EXCEPTION(10014, "二进制长度小于协议头长度"),
    SEQ_NOT_NULL(10015, "SEQ不能为null"),
    METHOD_NOT_NULL(10016, "METHOD不能为null"),
    RESULT_NOT_NULL(10017, "RESULT不能为null"),
    PACKET_TOTAL_NOT_NULL(10018, "packetTotal不能为null"),
    PACKET_NUM_NOT_NULL(10019, "packetNum不能为null"),
    TIMEOUT_NOT_NULL(10020, "timeout不能为null"),
    PACKAGE_ID_NOT_NULL(10021, "packageId不能为null"),
    REDIS_RULE_NOT_NULL(10022, "缓存转换规则不能为null"),
    DIRECT_SRC_NOT_NULL(10023, "directId和src不能同时为空"),
    DST_NOT_NULL(10024, "dst不能为空"),
    BINARY_BYTE_ARR_EXCEPTION(10025, "二进制数组转换java基本数字类型异常,数组长度超过8个字节无法解析"),
    DISTRIBUTE_LOCK_NAME_NOT_NULL_EXCEPTION(10026, "分布式锁名称不能为空"),
    DISTRIBUTE_LOCK_BUSINESS_EXCEPTION(10027, "业务操作异常"),
    DISTRIBUTE_LOCK_FAIL_EXCEPTION(10028, "加锁失败"),
    SERVICE_DEGRADE_EXCEPTION(10029, "服务熔断异常"),
    BUSINESS_DATA_EXCEPTION(10030, "业务数据异常"),
    ;
    /**
     * 编码
     */
    private Integer code;
    /**
     * 描述
     */
    private String desc;

    ErrorCodeEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    /**
     * @Description:根据编码获取描述信息
     * @Author: huangjin@2021/5/6 16:26
     * @Param: [code]
     * @return: java.lang.String
     * @throws:
     **/
    public static String getByCode(Integer code) {
        if (code == null) {
            return null;
        }
        return Arrays.stream(ErrorCodeEnum.values()).filter(sendModeEnum -> code.equals(sendModeEnum.getCode())).findFirst().orElse(null).getDesc();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
