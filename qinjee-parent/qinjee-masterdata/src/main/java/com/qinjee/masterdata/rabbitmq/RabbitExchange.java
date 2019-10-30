/**
 * 文件名：RabbitExchange
 * 工程名称：eTalent
 * <p>
 * qinjee
 * 创建日期：2019/7/10
 * <p>
 * Copyright(C) 2019, by zhouyun
 * 原始作者：周赟
 */
package com.qinjee.masterdata.rabbitmq;

import com.qinjee.consts.MQConsts;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;

/**
 * @author 周赟
 *
 * @date 2019/10/30
 */
public class RabbitExchange {

    /**
     *   1.定义exchange
     *   2.durable="true" rabbitmq重启的时候不需要创建新的交换机
     *   3.direct交换器相对来说比较简单，匹配规则为：如果路由键匹配，消息就被投送到相关的队列
     *     fanout交换器中没有路由键的概念，他会把消息发送到所有绑定在此交换器上面的队列中。
     *     topic交换器你采用模糊匹配路由键的原则进行转发消息到队列中
     *   key: queue在该direct-exchange中的key值，当消息发送给direct-exchange中指定key为设置值时，
     *   消息将会转发给queue参数指定的消息队列
     */
    @Bean
    protected DirectExchange directExchange(){
        DirectExchange directExchange = new DirectExchange(MQConsts.DIRECT_EXCHANGE_QINJEE, true, false);
        return directExchange;
    }

    @Bean
    protected FanoutExchange fanoutExchange(){
        FanoutExchange fanoutExchange = new FanoutExchange(MQConsts.FANOUT_EXCHANGE_QINJEE, true, false);
        return fanoutExchange;
    }

    @Bean
    protected TopicExchange topicExchange(){
        TopicExchange topicExchange = new TopicExchange(MQConsts.TOPIC_EXCHANGE_QINJEE, true,false);
        return topicExchange;
    }
}
