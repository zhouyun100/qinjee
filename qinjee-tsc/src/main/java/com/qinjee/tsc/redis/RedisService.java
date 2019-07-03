package com.qinjee.tsc.redis;

import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * redis常用方法工具类
 * 
 *
 * @author 周赟
 *
 * @version 
 *
 * @since 2019年5月15日
 */
@Component
public class RedisService{

	@Autowired
	private JedisPool jedisPool;

	public Jedis getResource() {
		return jedisPool.getResource();
	}

	public void returnResource(Jedis jedis) {
		if(jedis != null){
            jedis.close();
        }
	}

	/**
	 * 设置key值，无过期时间，永久有效
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 设置key-value，并设置有效时间，单位秒
	 * @param key
	 * @param seconds 秒
	 * @param value
	 */
	public void setex(String key, Integer seconds, String value) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.setex(key, seconds, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 移除给定的一个或多个key,如果key不存在,则忽略该命令.
	 * @param keys
	 */
	public void del(String... keys) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.del(keys);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
	}

	/**
	 * 指定缓存失效时间
	 * @param key
	 * @param time
	 * @return
	 */
	public boolean expire(String key, Integer time) {
        Jedis jedis=null;
        try{
        	if (time > 0) {
        		jedis = getResource();
        		jedis.expire(key, time);
        	}
        	return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            returnResource(jedis);
        }
	}
	
	/**
	 * 获取key的过期时间
	 * 
	 * @param key
	 * @return
	 */
	public Long getExpire(String key) {
		Jedis jedis=null;
        try{
        	jedis = getResource();
        	return jedis.ttl(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally{
            returnResource(jedis);
        }
	}
	
	/**
	 * 获取key的value值
	 * @param key
	 * @return
	 */
	public String get(String key) {
		String result = null;
        Jedis jedis=null;
        try{
            jedis = getResource();
            result = jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            returnResource(jedis);
        }
        return result;
	}

	/**
	 * 判断key是否存在
	 * @param key
	 * @return
	 */
	public boolean exists(String key) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 新增哈希key-value
	 * @param key
	 * @param map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void hmset(String key, Map map) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.hmset(key, map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 返回集合key的元素数量
	 * @param key
	 * @return
	 */
	public Long scard(String key) {
		Long count = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			count = jedis.scard(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return count;
	}
	
	/**
	 * 返回集合key中的所有成员
	 * @param key
	 * @return
	 */
	public Set<String> smembers(String key) {
		Set<String> strSet = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			strSet = jedis.smembers(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return strSet;
	}
	
	/**
	 * 移除集合中的value元素
	 * @param key
	 * @param value
	 */
	public void srem(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.srem(key, value); 
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 将value元素加入到集合key当中
	 * @param key
	 * @param value
	 */
	public void sadd(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			jedis.sadd(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
	}
	
	/**
	 * 判断元素value是否是集合key的成员
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean exists(String key,String value) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			return jedis.sismember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			returnResource(jedis);
		}
	}
}
