#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.ConfigureRedisAction;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.annotation.PostConstruct;

/**
 *
 * @version: 1.00.00
 * @description: http session共享
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hongyiyong
 * @date: 2018-03-02 10:45:57
 * @history:
 */
@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {
    @Value("${symbol_dollar}{server.expire:1800}")
    private Integer sessionExpire;

    @Value("${symbol_dollar}{session.namespace:leelencloud}")
    private String sessionNamespace;


    @Bean
    public static ConfigureRedisAction configureRedisAction() {
        return ConfigureRedisAction.NO_OP;
    }
    @Autowired
    private RedisIndexedSessionRepository sessionRepository;

    @PostConstruct
    private void afterPropertiesSet() {
        sessionRepository.setDefaultMaxInactiveInterval(sessionExpire);
        sessionRepository.setRedisKeyNamespace(sessionNamespace);
    }
}