package com.qinjee.masterdata.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

import java.util.Map;
import java.util.Set;

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
public class RedisClusterService {

	@Autowired
	private JedisCluster jedisCluster;

	/**
	 * 设置key值，无过期时间，永久有效
	 * @param key
	 * @param value
	 */
	public void set(String key, String value) {
		try {
			jedisCluster.set(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 设置key-value，并设置有效时间，单位秒
	 * @param key
	 * @param seconds 秒
	 * @param value
	 */
	public void setex(String key, Integer seconds, String value) {
		try {
			jedisCluster.setex(key, seconds, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 移除给定的一个key,如果key不存在,则忽略该命令.
	 * @param key
	 */
	public void del(String key) {
		try {
			jedisCluster.del(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 指定缓存失效时间
	 * @param key
	 * @param time
	 * @return
	 */
	public boolean expire(String key, Integer time) {
        try{
        	if (time > 0) {
				jedisCluster.expire(key, time);
        	}
        	return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
	}
	
	/**
	 * 获取key的过期时间
	 * 
	 * @param key
	 * @return
	 */
	public Long getExpire(String key) {
        try{
        	return jedisCluster.ttl(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
	}
	
	/**
	 * 获取key的value值
	 * @param key
	 * @return
	 */
	public String get(String key) {
		String result = null;
        try{
            result = jedisCluster.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
	}

	/**
	 * 判断key是否存在
	 * @param key
	 * @return
	 */
	public boolean exists(String key) {
		try {
			return jedisCluster.exists(key);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 新增哈希key-value
	 * @param key
	 * @param map
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void hmset(String key, Map map) {
		try {
			jedisCluster.hmset(key, map);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 返回集合key的元素数量
	 * @param key
	 * @return
	 */
	public Long scard(String key) {
		Long count = null;
		try {
			count = jedisCluster.scard(key);
		} catch (Exception e) {
			e.printStackTrace();
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
		try {
			strSet = jedisCluster.smembers(key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strSet;
	}
	
	/**
	 * 移除集合中的value元素
	 * @param key
	 * @param value
	 */
	public void srem(String key, String value) {
		try {
			jedisCluster.srem(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 将value元素加入到集合key当中
	 * @param key
	 * @param value
	 */
	public void sadd(String key, String value) {
		try {
			jedisCluster.sadd(key, value);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 判断元素value是否是集合key的成员
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean exists(String key,String value) {
		try {
			return jedisCluster.sismember(key, value);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
