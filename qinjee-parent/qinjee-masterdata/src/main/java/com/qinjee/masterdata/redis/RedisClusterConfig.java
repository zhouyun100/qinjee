/**
 * 文件名：RedisClusterConfig
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/7/5
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.util.HashSet;
import java.util.Set;

/**
 * redis集群配置类
 *
 * @author 周赟
 *
 * @version
 *
 * @since 2019年5月15日
 */
@Configuration
@Profile({"dev", "test", "prod"})
public class RedisClusterConfig {

    @Value("${spring.redis.cluster.nodes}")
    private String clusterNodes;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.password}")
    private String password;

    private int maxAttempts = 5;

    @Bean
    public JedisCluster jedisCluster() {
        //分割集群节点
        String[] cNodes = clusterNodes.split(",");

        //创建set集合对象
        Set<HostAndPort> nodes = new HashSet<>();
        for (String node : cNodes) {
            String[] hp = node.split(":");
            nodes.add(new HostAndPort(hp[0], Integer.parseInt(hp[1])));
        }

        //创建连接池配置对象
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        //连接耗尽时是否阻塞, false报异常,true阻塞直到超时, 默认true
        poolConfig.setBlockWhenExhausted(false);
        //最大连接数
        poolConfig.setMaxTotal(1000);
        //最大空闲数
        poolConfig.setMaxIdle(200);
        //最大空闲数
        poolConfig.setMinIdle(50);
        //最大建立连接等待时间
        poolConfig.setMaxWaitMillis(3000);
        //是否在从池中取出连接前进行检验,如果检验失败,则从池中去除连接并尝试取出另一个
        poolConfig.setTestOnBorrow(false);

        //创建Redis集群对象，timeout默认2000
        JedisCluster jedisCluster = new JedisCluster(nodes,timeout, timeout, maxAttempts, password, poolConfig);

        return jedisCluster;
    }

}
