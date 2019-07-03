/*
 * 文件名： RabbitConfig.java
 * 
 * 工程名称: qinjee-tsc
 *
 * Shang De
 *
 * 创建日期： 2019年5月15日
 *
 * Copyright(C) 2019, by zhouyun
 *
 * 原始作者: 周赟
 *
 */
package com.qinjee.tsc.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistrar;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

import com.qinjee.consts.MQConsts;

/**
 * 
 *
 * @author 周赟
 *
 * @version 
 *
 * @since 2019年5月15日
 */
@Configuration
public class RabbitConfig implements RabbitListenerConfigurer{
    
    /**
     *   1.定义direct exchange，绑定queueTest
     *   2.durable="true" rabbitmq重启的时候不需要创建新的交换机
     *   3.direct交换器相对来说比较简单，匹配规则为：如果路由键匹配，消息就被投送到相关的队列
     *     fanout交换器中没有路由键的概念，他会把消息发送到所有绑定在此交换器上面的队列中。
     *     topic交换器你采用模糊匹配路由键的原则进行转发消息到队列中
     *   key: queue在该direct-exchange中的key值，当消息发送给direct-exchange中指定key为设置值时，
     *   消息将会转发给queue参数指定的消息队列
     */
    @Bean
    public DirectExchange directExchange(){
        DirectExchange directExchange = new DirectExchange(MQConsts.EXCHANGE_QINJEE_1, true, false);
        return directExchange;
    }
    
    /**
     * durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
     * auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
     * exclusive  表示该消息队列是否只在当前connection生效,默认是false
     * @return
     */
    @Bean
    public Queue queueOne() {
    	return QueueBuilder.durable(MQConsts.QUEUE_1).build();
    }
    
    @Bean
    public Binding bingingOne() {
        return BindingBuilder.bind(queueOne()).to(directExchange()).with(MQConsts.ROUTINGKEY_QINJEE_1);
    }
    
	@Bean
    public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        //开启mq事务支持，spring会跟数据库事务进行集成，在事务结束后再提交到消息队列
        rabbitTemplate.setChannelTransacted(true);
        return rabbitTemplate;
    }
	
	@Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
	
	@Override
	public void configureRabbitListeners(RabbitListenerEndpointRegistrar registrar) {
		registrar.setMessageHandlerMethodFactory(messageHandlerMethodFactory());
	}

	@Bean
    MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory messageHandlerMethodFactory = new DefaultMessageHandlerMethodFactory();
        messageHandlerMethodFactory.setMessageConverter(consumerJackson2MessageConverter());
        return messageHandlerMethodFactory;
    }
	
	@Bean
    public MappingJackson2MessageConverter consumerJackson2MessageConverter() {
        return new MappingJackson2MessageConverter();
    }
	
}
