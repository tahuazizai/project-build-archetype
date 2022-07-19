#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.utils.entity;

import java.util.Map;

/**
 * description:批量插入redis的bean
 * Created by cuihuang on 2017/11/29.
 */
public class JedisMapBean {

    private String key;
    private Map<String,String> map;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

}
