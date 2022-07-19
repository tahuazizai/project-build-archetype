#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.constant;

/**
 * 社区相关常量类
 *
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: youwenfeng
 * @date: 2019/4/22
 * @history:
 */
public interface CommunityConstant {

    String SEATA_DATASOURCE = "seataDb";

    String DEFAULT_DATASOURCE = "defaultDataSource";
    String clientId = "1";
    /**
     * mqtt异步应答默认时间3s
     */
    int DEFAULT_SYNC_ACK_WAIT_TIME_5 = 5000;

    int DEFAULT_SYNC_ACK_WAIT_NUM = 1;

    int SYNC_ACK_WAIT_NUM_0 = 0;

    /**
     * mqtt异步应答时间20s
     */
    int SYNC_ACK_WAIT_TIME_20 = 20000;

    /**
     * mqtt异步应答时间15s
     */
    int SYNC_ACK_WAIT_TIME_15 = 15000;
    /**
     * mqtt异步应答默认时间10s
     */
    int DEFAULT_SYNC_ACK_WAIT_TIME_10 = 10000;

    /**
     * mqtt异步应答时间3s
     */
    int SYNC_ACK_WAIT_TIME_3 = 3000;

    /**
     * mqtt异步应答APP等待时间10s
     */
    int DEFAULT_SYNC_ACK_APP_WAIT_TIME = 10000;

    /**
     * mqtt异步应答APP等待时间20s
     */
    int DEFAULT_SYNC_ACK_APP_WAIT_TIME_20 = 20000;

    /**
     * mqtt异步应答APP等待时间30s
     */
    int SYNC_ACK_APP_WAIT_TIME_30 = 30000;

    /**
     * 乐比邻app mqtt clientId 过期时间
     */
    int COMMUNITY_MQTT_CLIENT_ID_EXPIRE_TIME = Token.REFRESH_EXPIRES_IN;

    /**
     * seata feign调用失败码
     */
    int SEATA_ROLLBACK = 5109;
    /**
     * 乐比邻redis的key前缀
     */
    String LC_IOT = "iot:";

    /**
     * 车辆出入记录表前缀
     */
    String CAR_ACCESS_RECORD_TABLE_PREFIX = "community_rc_parkinglot_entrance_";

    /**
     * 车辆出入记录图片表前缀
     */
    String CAR_ACCESS_RECORD_PIC_TABLE_PREFIX = "community_fs_pic_parkinglot_entrance_";

    /**
     * 住家业务常量
     */
    interface Token {

        interface Status {
            /**
             * token正常状态
             */
            int NORMAL_STATUS = 0;
            /**
             * token被踢状态
             */
            int FORCED_STATUS = 1;
            /**
             * token对应账号密码修改状态
             */
            int RESET_PWD_STATUS = 2;
            /**
             * token对应账号手机号修改状态
             */
            int MODIFY_PHONE = 3;
        }

        /**
         * 访问令牌过期时间(1h)
         */
        int ACCESS_EXPIRES_IN = 3600;
        /**
         * 刷新令牌过期时间(7d)
         */
        int REFRESH_EXPIRES_IN = 604800;
        /**
         * 访问令牌前缀【val:refreshToken】
         */
        String ACCESS_TOKEN_PREFIX = "iot:token:access:{}";
        /**
         * 刷新令牌前缀【val:tokenInfo】
         */
        String REFRESH_TOKEN_PREFIX = "iot:token:refresh:{}";
    }


    /**
     * 账号信息相关常量
     */
    interface Account {

        String VCODE_TEMPLATE = LC_IOT + "app:{}:intl_phone_and_phone:{}:verify_code_sign:{}";
        String ERROR_TEMPLATE = LC_IOT + "app:{}:error_num:account_id:{}";
        String LOCK_TEMPLATE = LC_IOT + "app:{}:lock:account_id:{}";
        String ONLINE_TEMPLATE = LC_IOT + "app:{}:online:account_id:{}";

        String AUTH_ID_TO_ACCOUNT = LC_IOT + "app:{}:auth_to_account:auth_id:{}";

        /**
         * 验证码过期时间（5min）
         */
        int VCODE_EXPIRES_IN = 300;

        /**
         * MQTT通知类型
         */
        int NOTICE_TYPE_PWD_CHANGE = 1;
        int NOTICE_TYPE_DISCONNECT = 2;

        /**
         * 操作系统定义
         */
        int OSTYPE_ANDROID_PHONE = 1;
        int OSTYPE_ANDROID_PAD = 2;
        int OSTYPE_IPHONE = 3;
        int OSTYPE_IPAD = 4;
        int OSTYPE_WECHAT = 5;
        int OSTYPE_WX_MINIPROGRAM = 6;

        /**
         * 设备类型
         */
        int TERMINAL_TYPE_PHONE = 1;
        int TERMINAL_TYPE_PAD = 2;
        int TERMINAL_TYPE_WECHAT = 3;
        int TERMINAL_TYPE_MINIPROGRAM = 4;

        /**
         * 验证方式
         */
        int VERIFYMODEL_SMS = 1;
        int VERIFYMODEL_EMAIL = 2;

        /**
         * 账号类型
         */
        int ACCOUNT_TYPE_PHONE = 1;
        int ACCOUNT_TYPE_EMAIL = 2;
        int ACCOUNT_TYPE_USER = 3;

        /**
         * 账号在线状态
         */
        int STATUS_ONLINE = 1;
        int STATUS_OFFLINE = 0;

        /**
         * 验证码类型：注册
         */
        int VERIFYCODETYPE_REGISTER = 1;
        /**
         * 验证码类型：修改/忘记密码
         */
        int VERIFYCODETYPE_PASSWORD = 2;
        /**
         * 验证码类型：修改手机号
         */
        int VERIFYCODETYPE_PHONE = 6;
        /**
         * 验证码类型：设备绑定
         */
        int VERIFYCODETYPE_DEVICE = 7;
        /**
         * 验证码类型：下载APP
         */
        int VERIFYCODETYPE_DOWNLOAD = 8;
        /**
         * 验证码类型：乐知安注册
         */
        int VERIFYCODETYPE_SAFE_REGISTER = 9;
        /**
         * 验证码类型：账号登录
         */
        int VERIFYCODETYPE_LOGIN = 12;

        /**
         * 语言缩写对照表
         */
        String LANGUAGE_ZH_CN = "zh-CN";
        String LANGUAGE_ZH_TW = "zh-TW";
        String LANGUAGE_ZH_HK = "zh-HK";
        String LANGUAGE_EN_US = "en-US";

        /**
         * 第三方推送平台定义
         */
        int JIGUANG_PUSH = 1;
        int MI_PUSH = 2;
        int HUAWEI_PUSH = 3;
        int MEIZU_PUSH = 4;
        int IOS_PUSH = 5;

        /**
         * 需要加锁上限次数4
         */
        int ERR_TIME_LIMIT = 4;

        /**
         * 免费时长：永久时间
         */
        int PERMANENT_TIME = -1;

        /**
         * 字符串常量1
         */
        String STRING_1 = "1";

        /**
         * 字符串常量4
         */
        String STRING_4 = "4";

        /**
         * 字符串常量5
         */
        String STRING_5 = "5";

        /**
         * 字符串常量10
         */
        String STRING_10 = "10";

        /**
         * 字符串常量50
         */
        String STRING_50 = "50";

        /**
         * 常量5
         */
        Integer INTEGER_5 = 5;

        /**
         * 常量6
         */
        Integer INTEGER_6 = 6;

        /**
         * 默认区号开头
         */
        String CHINA_AREA_CODE = "86";

        /**
         * redis存储的验证码key
         */
        String REDIS_VERIFY_CODE = "code";

        /**
         * redis存储的验证码类型key
         */
        String REDIS_VERIFY_TYPE = "type";

        /**
         * 返回给用户的次数num
         */
        String RESPONSE_DATA_NUM = "num";

        /**
         * 返回给用户的时间time
         */
        String RESPONSE_DATA_TIME = "time";

        /**
         * 返回给用户的单位unit
         */
        String RESPONSE_DATA_UNIT = "unit";

        String UNIT_MIN = "min";

        String UNIT_HOUR = "hour";

        String UNIT_DAY = "day";

        String STRING_COLON = ":";

        /**
         * 登记方式
         */
        // APP
        Integer REGISTER_TYPE_APP = 3;
        // 小程序
        Integer REGISTER_TYPE_MINIPROGRAM = 7;

        interface ForceMqttParam {

            String CODE = "code";

            String OSTYPE = "osType";
        }

        /**
         * 互踢模式: false：手机平板不互踢 true：手机平板互踢
         */
        interface DisconnectType {

            Boolean LEELEN_COMMUNITY = true;

            Boolean LEELEN_SAFETY = false;
        }
    }

    /**
     * 推送个性化设置常量
     */
    interface PushConfigType {
        /**
         * 巡更提醒自定义铃声开关
         */
        int REMINDER = 2;

        /**
         * 云对讲开关
         */
        int CLOUD_TALK = 1;
        /**
         * 住家管理
         */
        int HOUSE_MANAGE = -1;

        /**
         * 消息来源
         */
        interface Status {
            /**
             * 关闭
             */
            int OFF = 0;
            /**
             * 开启
             */
            int ON = 1;

        }
    }

    /**
     * 权限标识常量
     */
    interface PermissionSign {

        /**
         * 云门禁
         */
        int CLOUD_DOOR = 1;
        /**
         * 云对讲
         */
        int CLOUD_TALK = 2;
        /**
         * 云电话
         */
        int CLOUD_PHONE = 3;

        int CARED_PERSON = 14;

    }

    /**
     * 权限状态常量
     */
    interface PermissionState {

        /**
         * 开启
         */
        int ON = 1;
        /**
         * 关闭
         */
        int OFF = 2;

    }

    /**
     * 设备状态常量
     */
    interface DeviceStatus {
        /**
         * 在线
         */
        int ON_LINE = 1;
        /**
         * 离线
         */
        int OFF_LINE = 0;

    }

    /**
     * 转电话常量
     */
    interface Transfer {
        /**
         * 电信默认超时回收时间
         */
        int OUT_TIME = 2;

        String DEFAULT_NEIGH_NO = "9999";

        //第三方转电话前缀 key   thirdApi:transferPhone:{ neighNo}:{ neighStructure}
        String THIRD_TRANSFER_PREFIX = "thirdApi:transferPhone:{}:{}";

        interface neighTransferRule {
            /**
             * 动态分配
             */
            int DYNAMIC = 0;
            /**
             * 静态分配
             */
            int STATIC = 1;

        }

    }

    interface Talk {

        //key为 iot : groupDeviceTalkAccount : {appCode} : {groupId}
        //value为jsonArray住家对讲账号信息内容字符串
        // 包含accountId,authorizedId和permissionType住家账号权限类型（0,创建者，1，管理员，2，使用者）
        String DEVICE_TALKACCOUNT_PREFIX = "iot:groupDeviceTalkAccount:{}:{}";

    }

    /**
     * 云对讲
     */
    interface CloudTalk {
        /**
         * key为:    cb:4:{neighNo}:{callNo}:{did}
         * 呼叫列表
         */
        String CLOUD_TALK_CALL_LIST_PREFIX = "cb:4:{}:{}";
        /**
         * 云对讲呼叫列表缓存有效时间:7天
         */
        int CLOUD_TALK_CALL_LIST_CACHE_EXPIRE = 604800;
        /**
         * APP账号
         */
        int ACCOUNT_TYPE_APP =0 ;
        /**
         * 分机账号
         */
        int ACCOUNT_TYPE_DEVICE =1 ;
        /**
         * 管理机账号
         */
        int ACCOUNT_TYPE_MANAGER_DEVICE =2 ;
        /**
         * 云端向设备发起呼叫method
         */
        String TALK_CALLDEVICE ="talk.callDevice";
        /**
         * 设备发起挂断method
         */
        String TALK_HANG_UP = "talk.hangUp";

        /**
         * 云对讲账号权限配置
         */
        String TALK_ACCOUNT_PERMISSION_PREFIX = "cb:9:{neighNo}:{houseCallNo}";

        /**
         * 云对讲账号权限迁移标识
         */
        String TALK_ACCOUNT_PERMISSION_MOVE_PREFIX = "cb:10:permission_move_sign";

    }

    interface File {

        //key为 iot : fileToken : {did}
        //value为token(用于直连设备图片上传鉴权，值为UUID)
        //全局缓存
        String DEVICE_FILE_TOKEN_PREFIX = "iot:fileToken:{}";

    }

    /**
     * 图片类型
     */
    interface PicType {
        /**
         * 1 对讲记录 2门禁记录
         */
        int TALK_PIC = 1;

        int DOOR_PIC = 2;

        int CAR_ACCESS_PIC = 3;
    }

    interface HouseManage {
        /**
         * 解绑方式: 0:主动解绑  1:被动解绑
         */
        interface UnbindType {
            int INITIATIVE = 0;

            int UNACTIVE = 1;
        }
    }

    interface DisBusinessKey {

        String HOUSE_DISABLE_BUSINESS_CONFIG = LC_IOT + "app:house_disable_business_config";
    }

    interface ClientMethod {

        String APP = "APP";

        String WEB = "WEB";
    }

    interface YwCode {

        /**
         * 云门禁
         */
        Integer CLOUD_DOOR = 1;

        /**
         * 云对讲
         */
        Integer CLOUD_TALK = 2;

        /**
         * 转电话
         */
        Integer TRANSFER_PHONE = 3;

        /**
         * 停车场
         */
        Integer PARKING = 4;

        /**
         * 呼梯
         */
        Integer CALL_ELEVATOR = 5;

        /**
         * 物业缴费
         */
        Integer PROPER_BILL = 6;

        /**
         * 物业报修
         */
        Integer PROPER_REPAIR = 7;

        /**
         * 投诉建议
         */
        Integer PROPER_ADVICE = 8;

        /**
         * 社区公告
         */
        Integer COMMUNITY_ANNOUNCEMENT = 9;
    }

    /**
     * 缓存中常用key
     */
    interface RedisKey {
        /**
         *进入配置状态临时状态操作人信息key
         */
        String DEVICE_TEMP_KEY = "dc:6:{did}:{siid}";
        /**
         *存在正在发送配置状态请求的用户的key
         */
        String DEVICE_CONFIG_REQUST_USER_KEY="dc:7:{}:{}";
    }

    /**
     * 小区模式
     */
    interface NeighModel {

        /**
         *未知模式
         */
        Integer UNKNOWN_MODE = 0;
        /**
         * 云模式
         */
        Integer CLOUD =1;
        /**
         * 本地模式
         */
        Integer LOCAL =2;

        /**
         * 云网关
         */
        Integer CLOUD_GATE_WAY = 3;
        /**
         * 本地网关
         */
        Integer LOCAL_GATE_WAY = 4;
        String LOCAL_GATE_WAY_STRING = "4";
        /**
         * 智慧社区纯软模式
         */
        Integer PURE_SOFT = 5;
        String PURE_SOFT_STRING = "5";


    }

    /**
     * 小区模式状态
     */
    interface NeighModelStatus {
        /**
         * 激活
         */
        Integer ON =1;
        String ON_STRING = "1";
        /**
         * 未激活
         */
        Integer OFF =0;
        String OFF_STRING = "0";

    }

	/**

     * 小区物业管理模式
     */
    interface NeighPmsModel {
        /**
         * leelen
         */
        Integer LEELEN =1;
        /**
         * 道为
         */
        Integer DW =2;
    }

    /**
     * 门禁凭证
     */
    interface AccessAuthority {
        /**
         * 门禁卡状态: 值为1代表无效门禁卡号
         * cb:5:{neighNo}:{cardNo}:{did}
         */
        String CARD_STATUS_KEY_PREFIX ="cb:5:{}:{}:{}";
        /**
         * 门禁卡状态缓存有效期为:30秒
         */
        Integer CARD_STATUS_KEY_LIFE_TIME = 30;
        /**
         * 门禁卡号无效状态
         */
        Integer CARD_STATUS_INVALID = 1;
    }

    /**
     * 云社区
     */
    interface Community {
        //云社区测温告警模板 key为 community:4:{neighNo}
        String TEMP_ALARM_MODEL = "community:4:{}";
        //需下发至设备的云端属性 key为 community:4:{neighNo}
        String NEIGH_PROP2_DEVICE= "community:5:{}";
    }
    /**
     * 账号类型
     */
    interface AccountType {
        /**
         * 主账号
         */
        Integer MAIN = 1;
        String MAIN_STRING = "1";
        /**
         * 子账号
         */
        Integer MINOR = 2;
        String MINOR_STRING = "2";
    }

    /**
     * 请求来源
     */
    interface Source {

        /**
         * app字符串
         */
        String APP_STRING = "app";
    }
    /**
     * 是否为融合类型设备   1:是   2:否
     */
    interface IsMergeDevice {
        int IS = 1;
        int NOT_IS = 2;
    }

    /**
     * 自动开通APP账号开关 0:不开通   1:开通
     */
    interface AppOpenStatus {
        int OFF = 0;
        int ON = 1;
    }
    interface NeedJudge {
        Integer YES = 1;
        Integer NO = 0;
    }

    /**
     * 家居业务专用字段: 是否校验人脸  null或0:需要校验人脸    1:不需要校验人脸
     */
    interface NeedValidFaceForHome {
        Integer YES = 0;
        Integer NO = 1;
    }

    /**
     * 0或空:原云社区等；1:智慧社区SAAS
     */
    interface CompanyMode{
       // 本地或者云社区
       Integer LOCAL_OR_COMMUNITY = 0;
       // 智慧社区
       Integer SMART_COMMUNITY = 1;
    }
    interface NeighPlatformType {
        /**
         * 智慧社区
         */
        String SMART = "2";
        /**
         * 保障房
         */
        String SOCIAL = "3";
        /**
         * 新保障房
         */
        String NEW_SOCIAL = "5";
    }

    // 云社区新增的图片验证码的常量
    interface VerifyCode{
        // 云社区验证码缓存的key
        String CODE_CACHE_KEY = "community:verifyCode:";

        // 云社区验证码缓存的数据库
        int CODE_CACHE_DB = 3;

        // key过期时间 默认为10分钟
        int CACHE_KEY_EXPIRE_TIME = 10 * 60;
    }
}
