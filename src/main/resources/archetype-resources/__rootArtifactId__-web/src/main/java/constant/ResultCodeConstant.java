#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.constant;

/**
 * 请求应答result定义
 *
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: youwenfeng
 * @date: 2019/4/22
 * @history:
 */
public interface ResultCodeConstant {

    /**
     * 业务逾期
     */
    int BUSINESS_OVERDUE = 10027;

    /**
     * 请求失败
     */
    int FAIL = 0;

    /**
     * 请求成功
     */
    int SUCCESS = 1;

    /**
     * 请求失败
     */
    String NO_RESERVE_VISITOR_PERMISSION = "4000";

    /**
     * 非法请求
     */
    int ILLEGAL = 10000;

    /**
     * 限流阻塞 (网关组件)
     */
    int SENTINEL_BLOCK = 100;

    /**
     * 访问令牌过期
     */
    int ACCESS_TOKEN_INVALID = 10001;

    /**
     * 刷新令牌过期
     */
    int REFRESH_TOKEN_INVALID = 10002;

    /**
     * 暂无权限
     */
    int NO_PERMISSION = 10003;

    /**
     * 账号已存在
     */
    int  ACCOUNT_EXIST = 10011;

    /**
     * 账号未注册
     */
    int ACCOUNT_NO_REGIST = 10012;

    /**
     * 账号异地登录
     */
    int ACCOUNT_DIFFERENT_EQUIPMENT = 10013;

    /**
     * 账号被锁定
     */
    int ACCOUNT_LOCK = 10014;

    /**
     * 账号已注销
     */
    int ACCOUNT_CANCEL = 10015;

    /**
     * 账号被禁用
     */
    int ACCOUNT_FORBIDDEN = 10016;

    /**
     * 账号密码错误(需要回传剩余次数)
     */
    int ACCOUNT_PWD_ERROR = 10017;

    /**
     * 账号原密码错误（修改密码）
     */
    int ACCOUNT_NEW_PWD_ERROR = 10018;

    /**
     * 账号密码被修改
     */
    int ACCOUNT_PWD_BE_RESET = 10019;

    /**
     * 登录设备未授权
     */
    int UN_AUTH_TERMINAL = 10020;

    /**
     * 无效验证码
     */
    int SMS_INVALID = 10024;

    /**
     * 验证码过期
     */
    int SMS_EXPIRE = 10025;

    /**
     * 验证码发送太频繁
     */
    int SMS_LIMIT = 10026;

    /**
     * 物业离线
     */
    int COMMUNITY_OFF_LINE = 10030;

    /**
     * 未授权（没有绑定平安云）
     */
    int NO_AUTHORITY = 10031;

    /**
     * 设备离线
     */
    Integer DEVICE_OFF_LINE = 10031;

    /**
     * 其他账户正在配置中
     */
    Integer OTHERS_IS_CONFIG = 10037;

    /**
     * 登录密码有误
     */
    Integer LOGIN_PWD_FAIL = 10038;

    /**
     * 登录密码未校验或已超时
     */
    Integer LOGIN_PWD_NOCKECK_OVERTIME = 10039;

    /**
     * 未设置密码
     */
    Integer PWD_IS_NOT_SET = 10042;

    /**
     * 密码已重复
     */
    Integer PWD_IS_REPEATED = 10047;

    /**
     * 配置状态缓存已失效
     */
    Integer CONFIG_STATUS_FAIL = 10049;


    Integer ISBUSTY = 10050;
    /**
     * 该账号无权限
     */
    Integer ACCOUNT_NO_PERMISSION = 10053;
    /**
     * 该设备已绑定住家
     */
    Integer DEVICE_BOUND = 10052;
    /**
     * 校验文件大小失败
     */
    Integer CHECK_SIZE_FAIL = 10067;


    /**
     * 处理同步属性中
     */
    int DEAL_SYNC_PIID = 10063;

    /**
     * 处理同步子设备中
     */
    int DEAL_SYNC_SUB_DEVICE = 10064;

    /**
     * 处理同步动态服务中
     */
    int DEAL_SYNC_DYNAMIC_SERVICE = 10065;

    /**
     * 子设备不同步
     */
    int UN_SYNC_SUB_DEVICE = 10066;

    /**
     * 无该属性值
     */
    int NULL_PIIDS_VALUE = 10069;
    /**
     * 二维码过期
     */
    int QR_CODE_DISABLED = 10076;
    /**
     * 错误二维码
     */
    int QR_CODE_ERROR = 10077;
    /**
     * 住家已融合
     */
    int HOME_IS_MERGE = 10078;
    /**
     * 住家未融合
     */
    int HOME_IS_NOT_MERGE = 10092;

    /**
     * 参数错误，同步队列对于这种错误码的消息，不需要进行消息重发
     */
    int PARAM_ERROR_NO_RESEND = 10108;

    /**
     * 接口请求失败，同步队列对于这种错误码的消息，需要对消息进行重发
     */
    int NEED_RE_SEND_ERROR=10109;

    //凭证类型错误
    int CERT_TYPE_ERROR = 10110;
    /**
     * 实体卡凭证已满
     */
    int ENTITY_CARD_FULL = 10111;
    /**
     * 指纹凭证已满
     */
    int FINGERPRINT_FULL = 10112;
    /**
     * 密码凭证已满
     */
    int PWD_FULL = 10113;
    /**
     * proofId非法
     */
    int PROOF_ID_ILLEGAL = 10114;

    // 验证码过期
    int VERIFY_CODE_EXPIRE = 10115;

    // 验证码错误
    int VERIFY_CODE_INCORRECT = 10116;

    /****************************以下为云端与物业进行MQTT通信的Result code*********************************/
    /**
     * 该账单已缴清
     */
    int BILL_PAYED = 10040;
    /**
     * 外部接口调用失败
     */
    int EXTERNAL_INTERFACE_ABNORMAL = 10032;

    /**
     * 存在已缴账单
     */
    int SOME_BILLS_PAYED = 10041;    /****************************以下为云端与物业进行MQTT通信的Result code*********************************/

    /**
     * 设备密码错误，需要重新注册
     */
    int DEVICE_WRONG_PASSWORD = -10000;
    /**
     * 参数错误
     */
    int WRONG_PARAMS = -10001;
    /**
     * 命令不支持
     */
    int CMD_NOT_SUPPORT = -10002;
    /**
     * 设备不存在
     */
    int DEVICE_NOT_EXIST = -10003;
    /**
     * 设备不在线
     */
    int DEVICE_NOT_ONLINE = -10004;
    /**
     * 系统异常
     */
    int SYSTEM_EXCEPTION = -10005;
    /**
     * 业务异常
     */
    int BUSINESS_EXCEPTION = -10006;

    /**
     * 全局版本号异常
     */
    int GLOBAL_VERSION_EXCEPTION = -10007;

    /**
     * 属性版本号异常
     */
    int PROPERTY_VERSION_EXCEPTION = 10008;


    int UPGRADE_CODE_VERSION_LOW = -10011;

    int UPGRADE_CODE_VERSION_HEIGH = -10012;


    /**
     * 密码有误
     */
    int DEVICE_PWD_FAIL = -10023;
    /**
     * 设备未绑定
     */
    int DEVICE_UNBIND = 10051;

    int PHONE_NO_EXIST = -10098;

    /****************************以上为云端与物业进行MQTT通信的Result code*********************************/

    //设备呼叫相关参数
    interface DEVICE_CALL {
        int UN_CALL_NOW = 0; //设备获取可呼叫列表后不呼叫
        int CALL_NOW = 1; //设备获取可呼叫列表后马上呼叫
    }

    //设备呼叫类型相关参数
    interface DEVICE_CALL_TYPE {
        int CALLNO = 0; //呼叫号码
        int PHONE = 1; //手机号
        int DID = 2; //根据设备did
        int CALL_MANAGER = 3; //呼叫管理机
    }

    //转电话相关错误常量
    //小区号错误
    int NEIGH_NOT_EXIST = -5001;

    //住家账号已存在
    int GROUP_ACCOUT_EXIST = 10059;


    /*************   停车场缴费应答码   *************/
    /**
     * 存在已缴未生效账单
     */
    int EXIST_UN_EFFECTIVE_BILL = 10070;

    /**
     * 存在正在支付中账单
     */
    int EXIST_PAYING_BILL = 10071;

    /*************   乐比邻小程序应答码   *************/
    /**
     * 乐比邻账号已绑定其他微信
     */
    int LBL_HAS_BIND = 10072;

    /**
     * 15天不能解绑账号关联
     */
    int LBL_CAN_NOT_UNBIND = 10073;

    /*************   自助登记应答码   *************/
    /**
     * 小区功能建设中
     */
    int COMMUNITY_FUNCTION_DISABLE = 10093;

    /**
     * 该人员（证件号码）在当前小区下已存在，请前往物业中心处理，或删除该人员后进行新增操作重试
     */
    int CARD_INFO_EXIST = 10094;

    /**
     * 当前记录已被更新，请刷新记录
     */
    int RECORD_OUT_TIME = 10095;

    /**
     * 审核单已被审核，请刷新查看结果
     */
    int AUDIT_STATUS_CHANGE = 10096;

    /**
     * 当前房屋不存在
     */
    int REGISTER_HOUSE_NOT_EXIST = 10097;

    /**
     * 当前房屋已被认证过，请勿重复认证（房屋认证）/该成员在当前住家下已存在，请勿重复提交（家庭成员）
     */
    int PERSON_HOUSE_EXIST = 10098;

    /**
     * 该小区暂未开通此功能，请联系物业
     */
    int SELF_REGIST_SWITCH_OFF = 10099;

    /**
     * 该帐号已被使用过，请先完善个人身份信息，人员身份信息一致可正常提交，若人员身份信息不一致，请到物业中心处理。
     */
    int HOUSE_ACCOUNT_EXIST = 10100;

    /**
     * 家庭成员暂无增删改权限
     */
    int HOUSEHOLD_NO_PERMISSION = 10101;


    /**
     * 该人员已经被删除
     */
    int PERSON_DELETED = 10102;

    /**
     * 配置信息不完整
     */
    int CONFIGURATION_ILLEGAL = 10104;

    /*************   账单生效状态应答物业码   *************/
    /**
     * 上报参数错误
     */
    int REPORT_PARAM_ERROR = -10060;

    /**
     * 账单不存在
     */
    int REPORT_BILL_NOT_EXIST = -10061;

    /**
     * 生效状态处理失败
     */
    int REPORT_DEAL_FAIL = -10062;


    /*************   停车场退款应答物业码   *************/
    /**
     * 退款参数错误
     */
    int REFUND_PARAM_ERROR = -10070;

    /**
     * 退款账单不存在
     */
    int REFUND_BILL_NOT_EXIST = -10071;

    /**
     * 系统异常
     */
    int REFUND_SYSTEM_EXCEPTION = -10072;

    /**
     * 缺少支付配置
     */
    int REFUND_PAY_CONFIG_LACK = -10073;

    /**
     * 退款失败
     */
    int REFUND_FAIL = -10074;

    /**
     * 未发起退款，无法查询退款状态
     */
    int REFUND_NOT_EXIST = -10075;

    /*************   本地短信下发应答物业码   *************/
    /**
     * 短信请求参数错误
     */
    int SMS_PARAM_ERROR = -10080;

    /**
     * 短信发送失败
     */
    int SMS_SEND_FAIL = -10081;

    /**
     * 短信发送超频1条/分钟
     */
    int SMS_FREQUENT_MIN = -10082;

    /**
     * 短信发送超频5条/小时
     */
    int SMS_FREQUENT_HOUR = -10083;

    /**
     * 短信发送超频10条/天
     */
    int SMS_FREQUENT_DAY_10 = -10084;

    /**
     * 短信发送超频50条/天
     */
    int SMS_FREQUENT_DAY_50 = -10085;

    /*************   第三方退款应答物业码   *************/

    /**
     * 系统错误，请使用相同的参数再次调用
     */
    int ALI_SYSTEM_ERROR = -20000;

    /**
     * 参数无效，请求参数有错，重新检查请求后，再调用退款
     */
    int ALI_INVALID_PARAMETER = -20001;

    /**
     * 卖家余额不足，请商户支付宝账户充值后重新发起退款即可
     */
    int ALI_SELLER_BALANCE_NOT_ENOUGH = -20002;

    /**
     * 退款金额超限，请检查退款金额是否正确，重新修改请求后，重新发起退款
     */
    int ALI_REFUND_AMT_NOT_EQUAL_TOTAL = -20003;

    /**
     * 请求退款的交易被冻结，请联系支付宝小二，确认该笔交易的具体情况
     */
    int ALI_REASON_TRADE_BEEN_FREEZEN = -20004;

    /**
     * 交易不存在，请检查请求中的交易号和商户订单号是否正确，确认后重新发起
     */
    int ALI_TRADE_NOT_EXIST = -20005;

    /**
     * 交易已完结，该交易已完结，不允许进行退款，确认请求的退款的交易信息是否正确
     */
    int ALI_TRADE_HAS_FINISHED = -20006;

    /**
     * 交易状态非法，请查询交易，确认交易是否已经付款
     */
    int ALI_TRADE_STATUS_ERROR = -20007;

    /**
     * 不一致的请求，检查该退款号是否已退过款或更换退款号重新发起请求
     */
    int ALI_DISCORDANT_REPEAT_REQUEST = -20008;

    /**
     * 退款金额无效，请检查退款请求的金额是否正确
     */
    int ALI_REASON_TRADE_REFUND_FEE_ERR = -20009;

    /**
     * 当前交易不允许退款，检查当前交易的状态是否为交易成功状态以及签约的退款属性是否允许退款，确认后，重新发起请求（超过退款期限）
     */
    int ALI_TRADE_NOT_ALLOW_REFUND = -20010;

    /**
     * 交易退款金额有误，请检查传入的退款金额是否正确
     */
    int ALI_REFUND_FEE_ERROR = -20011;

    /**
     * 接口返回错误，请不要更换商户退款单号，请使用相同参数再次调用API
     */
    int WX_SYSTEMERROR = -20012;

    /**
     * 退款业务流程错误，需要商户触发重试来解决，请不要更换商户退款单号，请使用相同参数再次调用API
     */
    int WX_BIZERR_NEED_RETRY = -20013;

    /**
     * 订单已经超过退款期限，请选择其他方式自行退款
     */
    int WX_TRADE_OVERDUE = -20014;

    /**
     * 业务错误
     */
    int WX_ERROR = -20015;

    /**
     * 退款请求失败，商户可自行处理退款
     */
    int WX_USER_ACCOUNT_ABNORMAL = -20016;

    /**
     * 无效请求过多，请检查业务是否正常，确认业务正常后请在1分钟后再来重试
     */
    int WX_INVALID_REQ_TOO_MUCH = -20017;

    /**
     * 商户可用退款余额不足
     */
    int WX_NOTENOUGH = -20018;

    /**
     * 请求参数错误，检查原交易号是否存在或发起支付交易接口返回失败
     */
    int WX_INVALID_TRANSACTIONID = -20019;

    /**
     * 请求参数错误，请重新检查再调用退款申请
     */
    int WX_PARAM_ERROR = -20020;

    /**
     * 请检查APPID是否正确
     */
    int WX_APPID_NOT_EXIST = -20021;

    /**
     * 请检查MCHID是否正确
     */
    int WX_MCHID_NOT_EXIST = -20022;

    /**
     * 订单号不存在，请检查你的订单号是否正确且是否已支付，未支付的订单不能发起退款
     */
    int WX_ORDER_NOT_EXIST = -20023;

    /**
     * 请检查请求参数是否通过post方法提交
     */
    int WX_REQUIRE_POST_METHOD = -20024;

    /**
     * 请检查签名参数和方法是否都符合签名算法要求
     */
    int WX_SIGN_ERROR = -20025;

    /**
     * 请检查XML参数格式是否正确
     */
    int WX_XML_FORMAT_ERROR = -20026;

    /**
     * 2个月之前的订单申请退款有频率限制
     */
    int WX_FREQUENCY_LIMITED = -20027;

    /**
     * 异常IP请求不予受理
     */
    int WX_NO_AUTH = -20028;

    /**
     * 证书校验错误，请检查证书是否正确，证书是否过期或作废
     */
    int WX_CERT_ERROR = -20029;

    /**
     * 订单金额或退款金额与之前请求不一致，请核实后再试
     */
    int WX_REFUND_FEE_MISMATCH = -20030;

    /**
     * 请求参数符合参数格式，但不符合业务规则
     */
    int WX_INVALID_REQUEST = -20031;

    /**
     * 订单处理中，暂时无法退款，请稍后再试
     */
    int WX_ORDER_NOT_READY = -20032;
    /**
     * 邀请函不存在
     */
    int INVITATION_NOT_EXIST = 10068;
    /*************   工程APP业务应答码   *************/

    /**
     * 设备信息匹配失败
     */
    int DEVICE_NOT_MATCH = 12001;

    /**
     * 设备已绑定
     */
    int DEVICE_BINDED = 12002;

    /**
     * 授权已过期，请重新扫码
     */
    int AUTH_OVERTIME = 12003;

    /**
     * 存在未完成的点位
     */
    int EXIST_POSITION = 12002;

    /**
     * 任务已超时，系统自动提交
     */
    int TASK_TIMEOUT = 12003;

    /**
     * 任务已被删除
     */
    int TASK_IS_DELETE = 12006;

    /**
     * 点位已删除
     */
    int POSITION_IS_DELETE = 12007;

    /**
     * 不在有效授权时间内
     */
    int GRANTED_TIME_ILLEGAL = 12008;


    /**
     * 设备sn非法
     */
    Integer DEVICE_SN_ILLEGAL = 12009;

    /**
     * 临时人员角色不存在
     */
    Integer TEMP_ROLE_NO_EXIST = 12010;

    /**
     * 添加失败
     */
    Integer ADD_FAIL = 12011;

    /**
     * 编辑失败
     */
    Integer EDIT_FAIL = 12012;

    /**
     * 删除失败
     */
    Integer DELETE_FAIL = 12013;

    /**
     * 手机号已经存在
     */
    Integer PHONE_EXIST = 12014;

    /**
     * 人员已被授权
     */
    Integer GRANTED_ALREADY = 12015;

    /**
     * 账号手机号被修改
     */
    int ACCOUNT_PHONE_RESET = 12016;

    /**
     * IC卡已存在
     */
    int IC_CARD_EXIST = 12017;

    /**
     * 门禁卡长度超过上限
     */
    int CARD_NUMBER_LIMIT = 12018;

    /**
     * 人员卡数量超过上限
     */
    int PERSON_CARD_LIMIT = 12019;

    /**
     * 解绑失败
     */
    int DEVICE_UNBIND_FAIL = 12020;

    /**
     * 绑定失败
     */
    int DEVICE_BIND_FAIL = 12024;

    /**
     * 人员未绑定设备
     */
    int PERSON_DEVICE_RELATIONSHIP_LOST = 12021;

    /**
     * 该设备已被他人解绑，请刷新页面
     */
    int UNBIND_BY_OTHER = 12022;

    /**
     * 设备接入失败，SN号在系统中不存在，请联系技术支持处理
     */
    int SN_UN_IMPORT = 12023;

    /**
     * 当前住家下存在设备未绑定,无法生成配置文件
     */
    int HOUSE_HAS_UNBIND_DEVICE = 12025;

    /*************   设备应答码   *************/
    /**
     * 无权限
     */
    int NOT_PERMISSION = -10086;
    /**
     * 本地物业不在线
     */
    int COMMUNITY_OFFLINE = -10087;

    /**
     * 存在正在使用的套餐，请选择激活方式
     */
    int EXIST_USE_SPECIFICATION_SERVICE = 10075;

    /**
     * 套餐不能重复激活
     */
    int SPECIFICATION_CAN_NOT_REPEAT_ACTIVATION = 10076;

    int CAN_NOT_WRITE_PIID_CLOUD_AND_DEV = 4033;
    /**
     * 小区业务未开通
     */
    Integer NEIGH_BUSINESS_FORBIDDEN=14101;

    /**
     * feign调用超时
     */
    Integer FEIGN_TIME_OUT=14444;


    /**
     * DeviceDataDTO参数不合法
     */
    String DEVICE_DATA_DTO_ILLEGAL = "10000";

    /**
     *  UserInfoPo不存在
     */
    String USER_INFO_NO_EXIT = "10000";
    /**
     * 配网token过期
     */
    int DEVICE_TOKEN_EXPIRED = -10094;
}
