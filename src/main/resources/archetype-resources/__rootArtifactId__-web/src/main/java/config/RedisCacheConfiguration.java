#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${groupId}.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import ${groupId}.framework.redis.RedisTool;
import ${package}.utils.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.util.Pool;

/**
 * @version: 1.00.00
 * @description: Jedis 配置管理文件
 * @copyright: Copyright (c) 2018 立林科技 All Rights Reserved
 * @company: 厦门立林科技有限公司
 * @author: hongyiyong
 * @date: 2018-03-02 10:45:57
 * @history:
 */

@Configuration
@EnableCaching
public class RedisCacheConfiguration extends CachingConfigurerSupport {


    @Value("${symbol_dollar}{spring.redis.host}")
    private String host;

    @Value("${symbol_dollar}{spring.redis.port}")
    private int port;

    @Value("${symbol_dollar}{spring.redis.timeout}")
    private int timeout;

    @Value("${symbol_dollar}{spring.redis.password}")
    private String password;

    @Value("${symbol_dollar}{spring.redis.timeout}")
    private int timeOut;

    @Value("${symbol_dollar}{spring.redis.pool.maxIdle}")
    private int maxIdle;

    @Value("${symbol_dollar}{spring.redis.pool.maxActive}")
    private int maxTotal;

    @Value("${symbol_dollar}{spring.redis.pool.timeBetweenEvictionRunsMillis}")
    private int timeBetweenEvictionRunsMillis;

    @Value("${symbol_dollar}{spring.redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    @Value("${symbol_dollar}{spring.redis.pool.testWhileIdle}")
    private boolean testWhileIdle;

    @Value("${symbol_dollar}{spring.redis.pool.maxWaitMillis}")
    private int maxWaitMillis;

    @Value("${symbol_dollar}{spring.redis.database:0}")
    private int selectDb;

    @Value("${symbol_dollar}{spring.redis.dc.database:2}")
    private int devicetSelectDb;

    @Value("${symbol_dollar}{spring.redis.dc.database:8}")
    private int tuyaSelectDb;

    @Value("${symbol_dollar}{spring.redis.second.host:null}")
    private String secHost;

    @Value("${symbol_dollar}{spring.redis.second.port:6379}")
    private int secPort;

    @Value("${symbol_dollar}{spring.redis.second.password:null}")
    private String secPassword;

    @Value("${symbol_dollar}{spring.redis.second.selectDb:0}")
    private int selectSecDb;

    @Value("${symbol_dollar}{spring.redis.utils.selectInst:1}")
    private int selectInst;

    @Value("${symbol_dollar}{spring.redis.oauthDb:3}")
    private int oauthDb;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 使用Jackson2JsonRedisSerialize 替换默认序列化
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);

        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        // 设置value的序列化规则和 key的序列化规则
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        redisTemplate.setHashKeySerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);

        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setEnableDefaultSerializer(true);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }


    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(maxIdle);
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(maxTotal);
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        return jedisPoolConfig;
    }


    @Bean
    public Pool<Jedis> redisPoolFactory() {

        Pool<Jedis> jedisPool = jedisConnectionFactory.getPool();

        if (selectInst == 1) {
            RedisUtils.initialize(jedisPool);
        }
        return jedisPool;
    }

    @Bean
    @Primary
    public JedisConnectionFactory jedisConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory jedisConnectionFactory = connectionFactory(jedisPoolConfig, selectDb);
        return jedisConnectionFactory;
    }

    @Bean
    public JedisConnectionFactory redisOauth2ConnectionFactory(JedisPoolConfig jedisPoolConfig) {
        JedisConnectionFactory jedisConnectionFactory = connectionFactory(jedisPoolConfig, oauthDb);
        return jedisConnectionFactory;
    }


    private JedisConnectionFactory connectionFactory(JedisPoolConfig jedisPoolConfig, int redisDatabase) {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(jedisPoolConfig);
        jedisConnectionFactory.setUsePool(true);
        // 连接池
        jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
        // IP地址
        jedisConnectionFactory.setHostName(host);
        // 端口号
        jedisConnectionFactory.setPort(port);
        // 如果Redis设置有密码
        jedisConnectionFactory.setPassword(password);
        // 默认连接oauthDb设置数据库
        jedisConnectionFactory.setDatabase(redisDatabase);
        // 客户端超时时间单位是毫秒
        jedisConnectionFactory.setTimeout(timeout);
        return jedisConnectionFactory;
    }

    @Primary
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory);

        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);

        org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jackson2JsonRedisSerializer))
                .computePrefixWith(name -> "");

        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter, config);
        return cacheManager;
    }

    @Primary
    @Bean
    public RedisTool redisCoreDb() {
        RedisTool redisTool = new RedisTool(redisPoolFactory());
        return redisTool;
    }

    @Bean
    public RedisTool redisCoreCleanDb() {
        RedisTool redisTool = new RedisTool(redisPoolFactory(), 1);
        return redisTool;
    }

    @Bean
    public RedisTool redisComDb() {
        RedisTool redisTool = new RedisTool(redisPoolFactory(), selectDb);
        return redisTool;
    }

    @Bean
    public RedisTool deviceDb() {
        RedisTool redisTool = new RedisTool(redisPoolFactory(), devicetSelectDb);
        return redisTool;
    }

    @Bean
    public RedisTool tuyaDb() {
        RedisTool redisTool = new RedisTool(redisPoolFactory(), tuyaSelectDb);
        return redisTool;
    }

    @Bean
    @ConditionalOnProperty(name = "spring.redis.second.enabled", havingValue = "true")
    public RedisTool secRedisInst() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        // 最大空闲数
        jedisPoolConfig.setMaxIdle(maxIdle);
        // 连接池的最大数据库连接数
        jedisPoolConfig.setMaxTotal(maxTotal);
        // 最大建立连接等待时间
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        // 是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(testWhileIdle);
        Pool<Jedis> jedisPool = new JedisPool(jedisPoolConfig, secHost, secPort, timeOut, secPassword, selectSecDb);
        RedisTool redisTool = new RedisTool(jedisPool, selectSecDb);
        if (selectInst == 2) {
            RedisUtils.initialize(jedisPool);
        }
        return redisTool;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public int getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
        this.maxIdle = maxIdle;
    }

    public int getMaxTotal() {
        return maxTotal;
    }

    public void setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
    }

    public int getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(int timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public boolean isTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(boolean testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public boolean isTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(boolean testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public int getMaxWaitMillis() {
        return maxWaitMillis;
    }

    public void setMaxWaitMillis(int maxWaitMillis) {
        this.maxWaitMillis = maxWaitMillis;
    }

    public int getSelectDb() {
        return selectDb;
    }

    public void setSelectDb(int selectDb) {
        this.selectDb = selectDb;
    }

    public String getSecHost() {
        return secHost;
    }

    public void setSecHost(String secHost) {
        this.secHost = secHost;
    }

    public int getSecPort() {
        return secPort;
    }

    public void setSecPort(int secPort) {
        this.secPort = secPort;
    }

    public String getSecPassword() {
        return secPassword;
    }

    public void setSecPassword(String secPassword) {
        this.secPassword = secPassword;
    }

    public int getSelectSecDb() {
        return selectSecDb;
    }

    public void setSelectSecDb(int selectSecDb) {
        this.selectSecDb = selectSecDb;
    }

    public int getSelectInst() {
        return selectInst;
    }

    public void setSelectInst(int selectInst) {
        this.selectInst = selectInst;
    }
}
