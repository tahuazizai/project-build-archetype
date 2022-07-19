#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import ${package}.emnums.RedisObjectTypeMethod;
import ${package}.utils.entity.JedisMapBean;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;
import redis.clients.jedis.util.Pool;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/7/10.
 */
public class RedisUtils {

    private static Logger logger = LoggerFactory.getLogger(RedisUtils.class);
    public static ShardedJedisPool slavePool = null;
    //public static JedisPool masterPool = null;
    public static Pool<Jedis> masterPool = null;

    public static final Integer DEFAULT_BATCH_DEL_SIZE = 1000;

    //    public static String masterUrl ="119.23.26.129";
//    public static String slaveUrlA = ConfigUtil.getValue("redis_slave_url1");
//    public static String SlaveUrlB = ConfigUtil.getValue("redis_slave_url2");
 /*   public static int timeOut = 10000;
    static
    {
        try {
            JedisPoolConfig config = new JedisPoolConfig();
            // 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true
            config.setBlockWhenExhausted(true);
            // 设置的逐出策略类名, 默认DefaultEvictionPolicy(当连接超过最大空闲时间,或连接数超过最大空闲连接数)
            config.setEvictionPolicyClassName("org.apache.commons.pool2.impl.DefaultEvictionPolicy");
            // 是否启用pool的jmx管理功能, 默认true
            config.setJmxEnabled(true);
            // 最大空闲连接数, 默认8个 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例。
            config.setMaxIdle(8);
            // 最大连接数, 默认8个
            config.setMaxTotal(200);
            // 表示当borrow(引入)一个jedis实例时，最大的等待时间，如果超过等待时间，则直接抛出JedisConnectionException；
            config.setMaxWaitMillis(1000 * 100);
            // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
            config.setTestOnBorrow(true);

            *//*String hostA = ConfigUtil.getValue("redis_slave_url1");
            int portA = port;
            String authA = ConfigUtil.getValue("redis_slave_auth1");

            String hostB = ConfigUtil.getValue("redis_slave_url2");
            int portB = port;
            String authB = ConfigUtil.getValue("redis_slave_auth2");


            List<JedisShardInfo> jdsInfoList =new ArrayList<JedisShardInfo>(2);

            JedisShardInfo infoA = new JedisShardInfo(hostA, portA);
            infoA.setPassword(authA);

            JedisShardInfo infoB = new JedisShardInfo(hostB, portB);
            infoB.setPassword(authB);

            jdsInfoList.add(infoA);
            jdsInfoList.add(infoB);
            slavePool =new ShardedJedisPool(config, jdsInfoList, Hashing.MURMUR_HASH,
                    Sharded.DEFAULT_KEY_TAG_PATTERN);*//*

            int masterPost = port;
            masterPool = new JedisPool(config, masterUrl, masterPost,timeOut,auth);
            *//*masterPool = new JedisPool(config, masterUrl, masterPost,timeOut);*//*
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
*/
    public static void initialize(Pool<Jedis> jedisPool) {
        masterPool = jedisPool;
    }

    /**
     * 释放jedis资源
     *
     * @param jedis
     */
    public static void close(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static boolean clearAll() {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            jedis.flushDB();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtils.close(jedis);
        }
        return result;
    }


    public static boolean mSetHash(String key, Map<String, String> map) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            jedis.hmset(key, map);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtils.close(jedis);
        }
        return result;
    }

    //获取整个map
    public static Map<String, String> mGetHash(String key) {
        Jedis jedis = null;
        Map<String, String> map = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            map = jedis.hgetAll(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtils.close(jedis);
        }
        return map;
    }

    //获取map中的某个值
    public static String mGetSingleHash(String key, String field) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            result = jedis.hget(key, field);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtils.close(jedis);
        }
        return result;
    }

    //在map添加一个值
    public static boolean mSetSingleHash(String key, String field, String value) {
        Jedis jedis = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            jedis.hset(key, field, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            RedisUtils.close(jedis);
        }
        return true;
    }

    //在map删除某些
    public static boolean mDelSome(String key, String... field) {
        Jedis jedis = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            jedis.hdel(key, field);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            RedisUtils.close(jedis);
        }
        return true;
    }

    //设置set
   /* public synchronized static void mSet(String key,Set<String> mySet)
    {

        Jedis jedis = null;
        try{
            jedis = RedisUtils.masterPool.getResource();
            jedis.set(key,mySet);
        }catch(Exception e)
        {
            e.printStackTrace();
        }finally {
            RedisUtils.close(jedis);
        }

    }*/

//    /**
//     * 设置自定义类型 list
//     * @param <T>
//     * @param key
//     * @param list
//     */
//    public <T> void setList(String key,List<T> list){
//        Jedis jedis = null;
//        try {
//            jedis = RedisUtils.masterPool.getResource();
//            jedis.set(key,SerializeUtil.objectSerialiable(list));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    /**
//     * 获取自定义类型list
//     * @param <T>
//     * @param key
//     * @return list
//     */
//    public <T> List<T> getList(String key){
//        Jedis jedis = null;
//        List<T> list = null;
//        try {
//            jedis = RedisUtils.masterPool.getResource();
//            String objectBytes = jedis.get(key);
//            list = (List<T>) SerializeUtil.objectDeserialization(objectBytes);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return list;
//    }

    //删除集合成员
    public static boolean delSetMember(String key, String... field) {
        Jedis jedis = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            jedis.srem(key, field);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            RedisUtils.close(jedis);
        }
        return true;
    }


    //删除缓存
    public static boolean del(String... key) {
        boolean result = false;
        Jedis jedis = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            jedis.del(key);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtils.close(jedis);
        }
        return result;

    }

    //根据正则式获取满足条件的key
    public static Set<String> getKeys(String pattern) {
        Set<String> keys = new HashSet<>();
        Jedis jedis = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            keys = jedis.keys(pattern);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtils.close(jedis);
        }
        return keys;
    }


    //生成字节数组形式普通的key（标识头 ：唯一识别）
    public static byte[] setKeyNormal(String keyHead, String keyFlag) {
        String key = null;
        key = keyHead + ":" + keyFlag;
        return key.getBytes();
    }

    //生成字符串形式的key
    public static String setKey(String keyHead, String keyFlag) {
        String key = null;
        key = keyHead + ":" + keyFlag;


        return key;
    }


    /**
     * @param keyFlag  表字段名
     * @param keyValue 字段值
     * @param isIndex  是否为索引key
     * @Description : 生成字符串形式的key
     * @Author : youwenfeng
     * @Date : 2018/4/4
     * @params :  * @param keyHead 表名
     */
    public static String setKey(String keyHead, String keyFlag, String keyValue, boolean isIndex) {
        String colon = ":";
        StringBuffer sb = new StringBuffer(keyHead);
        if (isIndex) {
            sb.append(colon).append("index");
        }
        sb.append(colon).append(keyFlag).append(colon).append(keyValue);
        return sb.toString();
    }


    //批量插入数据到redis
    public static void batchInsert(List<JedisMapBean> mapList, Map<String, Set<String>> indexMap) {
        Jedis jedis = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            Pipeline pipeline = jedis.pipelined();
            if (mapList != null && !mapList.isEmpty()) {
                for (JedisMapBean jedisMapBean : mapList) {

                    if (!jedisMapBean.getMap().isEmpty()) {
                        String key = jedisMapBean.getKey();
                        pipeline.hmset(key, jedisMapBean.getMap());
                    }
                }
            }

            if (indexMap != null && !indexMap.isEmpty()) {
                for (Map.Entry<String, Set<String>> entry : indexMap.entrySet()) {
                    String key = entry.getKey();
                    Set<String> set = entry.getValue();
                    if (!set.isEmpty()) {
                        for (String str : set) {
                            pipeline.sadd(key, str);
                        }
                    }
                }
            }


            pipeline.sync();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            //释放对象
            RedisUtils.close(jedis);
        }
    }

    /*public static <T> void batchInsert(Class<T> t)
    {
        Jedis jedis = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            Pipeline pipeline = jedis.pipelined();
            pipeline.hmset()


            pipeline.sync();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            //释放对象
            RedisUtils.close(jedis);
        }
    }
*/

    public static <T> T getObjectCache(String key, Class<T> clazz) {
        Jedis jedis = null;
        T t = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            String objectString = jedis.get(key);
            if (StringUtils.isBlank(objectString)) {
                return null;
            }
            t = JSON.parseObject(objectString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            //释放对象
            RedisUtils.close(jedis);
        }
        return t;
    }

    public static boolean setObjectCache(String key, Object object) {
        Jedis jedis = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            jedis.set(key, JSONObject.toJSONString(object));
        } catch (Exception e) {
            logger.error("=========setObjectCache error", e);
            e.printStackTrace();
            return false;
        } finally {
            //释放对象
            RedisUtils.close(jedis);
        }
        return true;
    }

    //获取一个缓存单位
    public static <T> T getCache(String key, RedisObjectTypeMethod type) {
        Jedis jedis = null;
        Object value = null;
        T t = null;
        try {
            System.currentTimeMillis();
            jedis = RedisUtils.masterPool.getResource();
            switch (type) {
                case REDIS_OBJECT_STRING:
                    value = jedis.get(key);
                    break;
                case REDIS_OBJECT_MAP:
                    value = jedis.hgetAll(key);
                    break;
                case REDIS_OBJECT_LIST:
                    value = jedis.lrange(key, 0, -1);
                    break;
                case REDIS_OBJECT_SET:
                    value = jedis.smembers(key);
                    break;
                default:
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(jedis);
        }
        //缓存中存在对象
        if (value != null)
            t = (T) value;
        return t;
    }

    //设置一个缓存对象
    public static <T> boolean setCache(String key, RedisObjectTypeMethod type, T valueBean) {
        boolean result = false;
        Jedis jedis = null;
        try {
            System.currentTimeMillis();
            jedis = RedisUtils.masterPool.getResource();

            switch (type) {
                case REDIS_OBJECT_STRING:
                    jedis.set(key, valueBean.toString());
                    break;
                case REDIS_OBJECT_MAP:
                    jedis.hmset(key, (Map<String, String>) valueBean);
                    break;
                case REDIS_OBJECT_LIST:
                    jedis.lpush(key, valueBean.toString());
                    break;
                case REDIS_OBJECT_SET:
                    jedis.sadd(key, valueBean.toString());
                    break;
                default:
                    break;
            }
            result = true;

        } catch (Exception e) {
            e.printStackTrace();
            return result;
        } finally {
            close(jedis);
        }
        return result;
    }

    public static void setExpire(String key, int seconds) {
        Jedis jedis = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            jedis.expire(key, seconds);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtils.close(jedis);
        }
    }

    //获取过期时间
    public static Long getExpire(String key) {
        Jedis jedis = null;
        Long ttl = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            ttl = jedis.ttl(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtils.close(jedis);
        }
        return ttl;
    }

    public static void mSetex(String key, int seconds, String value) {
        Jedis jedis = null;
        try {
            jedis = RedisUtils.masterPool.getResource();
            jedis.setex(key, seconds, value);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            RedisUtils.close(jedis);
        }
    }

    //demo
    public static void main(String[] args) {

        String value = null;
        Jedis jedis = null;

//        BraceletBean braceletBean = new BraceletBean();
        try {
//            jedis = RedisUtils.masterPool.getResource();
//            String key = "sosNumberCache:90011208481972FF";

//            Date date = new Date(System.currentTimeMillis());
//            Date expiredTime = DateUtil.add(date, Calendar.HOUR,2);
//            System.out.println(expiredTime);
//            String myValue = "I am super man!";

//            jedis.set(setKeyNormal(key,"1"),"123".getBytes());

//            int saveTime = 3600*2;
//            jedis.expire(key,saveTime);

//            braceletBean = (BraceletBean)SerializeUtil.unserialize(jedis.get(key.getBytes()));

            jedis = RedisUtils.masterPool.getResource();

            String key = "outdoor_binding:index:18300023010";
            Set<String> result = getCache(key, RedisObjectTypeMethod.REDIS_OBJECT_SET);
            System.out.println("OK");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //释放对象
            RedisUtils.close(jedis);
        }




    }

    /**
     * 锁持有超时，防止线程在入锁以后，无限的执行下去，让锁无法释放
     */
    private static int expireMsecs = 60 * 1000;

    /**
     * 锁等待超时，防止线程饥饿，永远没有入锁执行代码的机会
     */
    private static int timeoutMsecs = 60 * 1000;

    private static final int DEFAULT_ACQUIRY_RESOLUTION_MILLIS = 1000;

    public static boolean lock(String key) {
        return lock(key, null);
    }

    public static boolean lock(String key, String Scope) {
        StringBuilder sb = new StringBuilder("lock:");
        String lockKey;
        if (StringUtils.isNotEmpty(Scope)) {
            lockKey = sb.append(Scope).append(":").append(key).toString();
        } else {
            lockKey = sb.append(key).toString();
        }


        while (timeoutMsecs >= 0) {
            Jedis jedis = null;
            try {
                jedis = masterPool.getResource();
                long expires = System.currentTimeMillis() + expireMsecs + 1;
                String expiresStr = String.valueOf(expires); //锁到期时间
                if (jedis.setnx(lockKey, expiresStr) == 1) {
                    jedis.expire(lockKey, (int) expires / 1000);
                    return true;
                }

                if (jedis.ttl(lockKey) == -1) {
                    jedis.expire(lockKey, (int) expires / 1000);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            } finally {
                RedisUtils.close(jedis);
            }

            try {

                timeoutMsecs -= DEFAULT_ACQUIRY_RESOLUTION_MILLIS;
                Thread.sleep(DEFAULT_ACQUIRY_RESOLUTION_MILLIS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;

    }

    public static boolean unlock(String key) {
        return unlock(key, null);
    }

    public static boolean unlock(String key, String scope) {

        StringBuilder sb = new StringBuilder("lock:");
        String lockKey;
        if (StringUtils.isNotEmpty(scope)) {
            lockKey = sb.append(scope).append(":").append(key).toString();
        } else {
            lockKey = sb.append(key).toString();
        }
        Jedis jedis = null;
        try {
            jedis = masterPool.getResource();
            jedis.del(lockKey);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return true;
    }


    /**
     * 为哈希表中的字段赋值
     *
     * @param key   哈希表的key
     * @param filed 字段名称
     * @param value 字段的value
     */
    public static void hset(String key, String filed, String value) {
        Jedis jedis = null;
        try {
            jedis = masterPool.getResource();
            jedis.hset(key, filed, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
    }

    /**
     * 返回哈希表中指定字段的值
     *
     * @param key
     * @param filed
     * @return
     */
    public static String hget(String key, String filed) {
        Jedis jedis = null;
        String value = "";
        try {
            jedis = masterPool.getResource();
            value = jedis.hget(key, filed);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return value;
    }

    /**
     * 返回哈希表中指定字段的值
     *
     * @param key
     * @param fields
     * @return
     */
    public static List<String> hmget(String key, String... fields) {
        Jedis jedis = null;
        List<String> vallues = Collections.EMPTY_LIST;
        try {
            jedis = masterPool.getResource();
            vallues = jedis.hmget(key, fields);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return vallues;
    }

    /**
     * 返回Set集合中的所有成员
     *
     * @param key
     * @return
     */
    public static Set<String> smembers(String key) {
        Jedis jedis = null;
        Set<String> value = new HashSet<>();
        try {
            jedis = masterPool.getResource();
            value = jedis.smembers(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return value;
    }


    /**
     * 查看哈希表的指定字段是否存在
     *
     * @param key   key的名称
     * @param filed 字段的名称
     * @return boolean
     */
    public static boolean hexists(String key, String filed) {
        Jedis jedis = null;
        boolean value = false;
        try {
            jedis = masterPool.getResource();
            value = jedis.hexists(key, filed);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return value;
    }

    /**
     * 返回哈希表中所有的字段和值
     *
     * @param key
     * @return Map<String, String>
     */
    public static Map<String, String> hgetAll(String key) {
        Jedis jedis = null;
        Map<String, String> map = new HashMap<String, String>();
        try {
            jedis = masterPool.getResource();
            map = jedis.hgetAll(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }

        return map;
    }

    /**
     * 获取所有的哈希表的数据并清除key
     *
     * @param key 哈希表的key
     * @return Map<String, String>
     */
    public static Map<String, String> hgetAllAndClear(String key) {
        Jedis jedis = null;
        Map<String, String> resultMap = new HashMap<String, String>();
        try {
            jedis = masterPool.getResource();
            jedis.watch(key);
            Transaction multi = jedis.multi();
            multi.hgetAll(key);
            multi.del(key);
            List<Object> exec = multi.exec();
            jedis.unwatch();
            resultMap = (Map<String, String>) exec.get(0);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return resultMap;

    }

    /**
     * 用于同时将多个 field-value (字段-值)对设置到哈希表中。 此命令会覆盖哈希表中已存在的字段
     *
     * @param key 哈希表的key名称
     * @param map 哈希表中的map对象
     * @return
     */
    public static String hmset(String key, Map<String, String> map) {
        Jedis jedis = null;
        String result = "";
        try {
            jedis = masterPool.getResource();
            result = jedis.hmset(key, map);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return result;
    }

    /**
     * 用于删除哈希表 key 中的一个或多个指定字段，不存在的字段将被忽略
     *
     * @param key   哈希表的key名称
     * @param filed 哈希表中的字段名称
     * @return
     */
    public static Long hdel(String key, String... filed) {
        Jedis jedis = null;
        Long result = 0L;
        try {
            jedis = masterPool.getResource();
            result = jedis.hdel(key, filed);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return result;
    }

    /**
     * 用于获取哈希表 中的所有字段集合，没有字段返回null；
     *
     * @param key 哈希表的key名称
     * @return
     */
    public static Set<String> hkeys(String key) {
        Jedis jedis = null;
        Set<String> result = null;
        try {
            jedis = masterPool.getResource();
            result = jedis.hkeys(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return result;
    }

    /**
     * 获取key的value
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        Jedis jedis = null;
        String value = "";
        try {
            jedis = masterPool.getResource();
            value = jedis.get(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return value;
    }


    /**
     * 设置key的value
     *
     * @param key
     * @param value
     * @return
     */
    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = "";
        try {
            jedis = masterPool.getResource();
            result = jedis.set(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return result;
    }

    /**
     * @param var1
     * @return
     */
    @Deprecated
    public static Set<String> keys(String var1) {
        Jedis jedis = null;
        Set<String> result = new HashSet<>();
        try {
            jedis = masterPool.getResource();
            result = jedis.keys(var1);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return result;
    }

    /**
     * 设置key的value，并设置key过期时间
     *
     * @param key
     * @param value
     * @param seconds
     * @return String
     */
    public static String setex(String key, String value, int seconds) {
        Jedis jedis = null;
        String result = "";
        try {
            jedis = masterPool.getResource();
            result = jedis.setex(key, seconds, value);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return result;
    }

    /**
     * 返回列表的长度
     *
     * @param key 列表的key
     * @return
     */
    public static Long llen(String key) {
        Jedis jedis = null;
        Long llen = 0L;
        try {
            jedis = masterPool.getResource();
            llen = jedis.llen(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return llen;
    }

    /**
     * 获取队列数据
     *
     * @param key 键名
     * @return String
     */
    public static String rpop(String key) {
        Jedis jedis = null;
        String rpop = "";
        try {
            jedis = masterPool.getResource();
            rpop = jedis.rpop(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return rpop;
    }

    /**
     * 判断key存不存在
     *
     * @param key 键名
     * @return Boolean
     */
    public static Boolean exists(String key) {
        Jedis jedis = null;
        Boolean rpop = false;
        try {
            jedis = masterPool.getResource();
            rpop = jedis.exists(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return rpop;
    }

    /**
     * @param match  匹配的key
     * @param cursor 游标 默认0开始
     * @param count  查找数量
     * @param db     redis第几个数据库
     * @return ScanResult<String>
     */
    public static ScanResult<String> scan(String match, String cursor, int count, int db) {
        Jedis jedis = null;
        ScanResult<String> result = null;
        try {
            jedis = masterPool.getResource();
            jedis.select(db);
            ScanParams scanParams = new ScanParams();
            result = jedis.scan(cursor, scanParams.count(count).match(match));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return result;
    }

    /**
     * 通过索引获取列表中的元素
     *
     * @param key 键名
     * @return String
     */
    public static String lindex(String key, Long index) {
        Jedis jedis = null;
        String rpop = "";
        try {
            jedis = masterPool.getResource();
            rpop = jedis.get(key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
        return rpop;
    }

    /**
     * 移出并获取列表的第一个元素， 如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     *
     * @param key 键名
     * @return String
     */
    public static void blpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = masterPool.getResource();
            jedis.blpop(timeout, key);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            RedisUtils.close(jedis);
        }
    }

    public static void batchDel(String patternKey) {
        batchDel(patternKey, null);
    }

    public static void batchDel(String patternKey, Integer db) {
        Instant startTime = Instant.now();
        logger.debug("开始删除【{}】", patternKey);
        //init context
        String cursor = ScanParams.SCAN_POINTER_START;
        long total = 0L;
        int batchIndex = 1;
        Jedis jedis = null;
        ScanParams scanParams = new ScanParams();
        scanParams.count(DEFAULT_BATCH_DEL_SIZE).match(patternKey);
        try {
            jedis = masterPool.getResource();
            if (db != null) {
                jedis.select(db);
            }
            //handle delete
            do {
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                List<String> elements = scanResult.getResult();
                cursor = scanResult.getCursor();
                if (CollectionUtils.isEmpty(elements)) {
                    continue;
                }
                jedis.del(elements.toArray(new String[0]));
                total += elements.size();
                logger.debug("正在删除【{}】，第{}批，该批数量：{}，当前已删除数量：{}", patternKey, batchIndex++, elements.size(), total);
            } while (!ScanParams.SCAN_POINTER_START.equals(cursor));
            db = jedis.getDB();
        } catch (Exception e) {
            logger.error("删除缓存失败【{}】（delete from db{} }）", patternKey, db, e);
        } finally {
            RedisUtils.close(jedis);
        }
        Instant endime = Instant.now();
        logger.debug("删除db{}【{}】结束；总共清理key数量：{}，耗时：{}秒", db, patternKey, total, Duration.between(startTime, endime));
    }
}
