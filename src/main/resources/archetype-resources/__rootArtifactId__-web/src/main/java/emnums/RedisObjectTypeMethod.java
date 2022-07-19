#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.emnums;


/**
 * Created by cuihuang on 2017/7/11.
 */
public enum RedisObjectTypeMethod {

        REDIS_OBJECT_STRING("string",0x01),
        REDIS_OBJECT_MAP("map",0x02),
        REDIS_OBJECT_LIST("list",0x03),
        REDIS_OBJECT_SET("set",0x03);

        // 成员变量
        private String name;
        private int index;
        // 构造方法
        private RedisObjectTypeMethod(String name, int index) {
            this.name = name;
            this.index = index;
        }
        public static String getName(int index) {
            for (RedisObjectTypeMethod y : RedisObjectTypeMethod.values()) {
                if (y.getIndex() == index) {
                    return y.name;
                }
            }
            return null;
        }
        // get set 方法
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getIndex() {
            return index;
        }
        public void setIndex(int index) {
            this.index = index;
        }
}
