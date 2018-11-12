package com.ixinhoo.config.redis;

import com.google.common.base.Strings;
import org.apache.ibatis.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class RedisCache implements Cache {
    private static final Logger logger = LoggerFactory.getLogger(RedisCache.class);

    private static JedisConnectionFactory jedisConnectionFactory;

    private final String id;

    /**
     * The {@code ReadWriteLock}.
     */
    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    public RedisCache(final String id) {
        if (id == null) {
            throw new IllegalArgumentException("Cache instances require an ID");
        }
        logger.debug(">>>>>>>>>>>>>>>>>>>>>MybatisRedisCache:id={}", id);
        this.id = id;
    }

    @Override
    public void clear() {
        RedisConnection connection = null;
        try {
            connection = jedisConnectionFactory.getConnection();
//            connection.flushDb();
//            connection.flushAll();
            logger.debug("出现CUD操作，清空对应Mapper缓存======>");
        } catch (JedisConnectionException e) {
            logger.error("JedisConnectionException:{}",e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Object getObject(Object key) {
        Object result = null;
        RedisConnection connection = null;
        try {
            // MD5加密key值,暂时先不加密 			key = Digests.md5()
//			key =  MD5.getMD5Str(key.toString());
            connection = jedisConnectionFactory.getConnection();
            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
            result = serializer.deserialize(connection.get(serializer.serialize(key)));
            if (result == null) {
                removeObject(key);
                return null;
            }
            logger.debug("从缓存中获取-----key=:{}", key);
        } catch (JedisConnectionException e) {
            logger.error("JedisConnectionException:{}",e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return result;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return this.readWriteLock;
    }

    @Override
    public int getSize() {
        int result = 0;
        RedisConnection connection = null;
        try {
            connection = jedisConnectionFactory.getConnection();
            result = Integer.valueOf(connection.dbSize().toString());
            logger.debug("{}---->>>>总缓存数:{}", this.id, result);
        } catch (JedisConnectionException e) {
            logger.error("JedisConnectionException:{}",e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return result;
    }

    @Override
    public void putObject(Object key, Object value) {
        if (key == null || value == null) {
            return;
        }
        if (Strings.isNullOrEmpty(value.toString()) || value.toString().equals("[]")) {
            logger.info("key:={}--- value 为空 :{}", key, value);
            return;
        }
        RedisConnection connection = null;
        try {
            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
//			key = MD5.getMD5Str(key.toString());
            connection = jedisConnectionFactory.getConnection();
            connection.set(serializer.serialize(key), serializer.serialize(value));
            logger.debug("添加缓存--------key:={} value:=", key, value);
        } catch (JedisConnectionException e) {
            logger.error("JedisConnectionException:{}",e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    @Override
    public Object removeObject(Object key) {
        RedisConnection connection = null;
        Object result = null;
        try {
//			key = MD5.getMD5Str(key.toString());
            connection = jedisConnectionFactory.getConnection();
            RedisSerializer<Object> serializer = new JdkSerializationRedisSerializer();
            result = connection.expire(serializer.serialize(key), 0);
            logger.debug("LRU算法从缓存中移除-----:{} key:{}", this.id, key);
        } catch (JedisConnectionException e) {
            logger.error("JedisConnectionException:{}",e);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
        return result;
    }

    public static void setJedisConnectionFactory(JedisConnectionFactory jedisConnectionFactory) {
        RedisCache.jedisConnectionFactory = jedisConnectionFactory;
    }

}
