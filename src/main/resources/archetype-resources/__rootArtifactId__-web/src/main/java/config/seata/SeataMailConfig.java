#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config.seata;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @version: 1.00.00
 * @description:
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: yangyicong
 * @date: 2020-11-09 13:11
 */
@Component
@RefreshScope
@ConfigurationProperties(prefix = "mail")
public class SeataMailConfig {


    public static String host;

    public static String username;

    public static String password;

    public static String mailFrom;

    public static String mailTo;

    @Value("host")
    public void setHost(String host) {
        SeataMailConfig.host = host;
    }

    @Value("username")
    public void setUsername(String username) {
        SeataMailConfig.username = username;
    }

    @Value("password")
    public void setPassword(String password) {
        SeataMailConfig.password = password;
    }

    @Value("mailFrom")
    public void setMailFrom(String mailFrom) {
        SeataMailConfig.mailFrom = mailFrom;
    }

    @Value("mailTo")
    public void setMailTo(String mailTo) {
        SeataMailConfig.mailTo = mailTo;
    }


}
