#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 公共Redission
 * @author admin
 */
@Configuration
public class ComRedissionConfig {

    @Value("${symbol_dollar}{spring.redis.host}")
    private String host;
    @Value("${symbol_dollar}{spring.redis.port}")
    private String port;
    @Value("${symbol_dollar}{spring.redis.password}")
    private String password;


    @Bean("comRedissonClient")
    public RedissonClient getRedisson() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://" + host + ":" + port).setPassword(password)
                .setReconnectionTimeout(10000)
                .setRetryInterval(5000)
                .setTimeout(10000)
                .setConnectTimeout(10000);
        return Redisson.create(config);
    }


}
