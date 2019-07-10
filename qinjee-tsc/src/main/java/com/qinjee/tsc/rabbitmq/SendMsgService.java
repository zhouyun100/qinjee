package com.qinjee.tsc.rabbitmq;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qinjee.consts.MQConsts;

import java.util.UUID;

@Service("sendMsgService")
public class SendMsgService {

	private static Logger logger = LogManager.getLogger(SendMsgService.class);

	@Autowired
	private RabbitTemplate rabbitTemplate;

	/**
	 * 定制化amqp模版
	 *
	 * ConfirmCallback接口用于实现消息发送到RabbitMQ交换器后接收ack回调   即消息发送到exchange  ack
	 * ReturnCallback接口用于实现消息发送到RabbitMQ 交换器，但无相应队列与交换器绑定时的回调  即消息发送不到任何一个队列中  ack
	 */
	public void send(Object object) {

		// 消息发送失败返回到队列中, yml需要配置 publisher-returns: true
		rabbitTemplate.setMandatory(true);

		// 消息返回, yml需要配置 publisher-returns: true
		rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
			String correlationId = new String(message.getBody());
			logger.info("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
		});

		// 消息确认, yml需要配置 publisher-confirms: true
		rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
			if (ack) {
				logger.info("消息发送到exchange成功,id: {}", correlationData.getId());
			} else {
				logger.info("消息发送到exchange失败,原因: {}", cause);
			}
		});
		CorrelationData correlationData = new CorrelationData( UUID.randomUUID().toString());
		rabbitTemplate.convertAndSend(MQConsts.DIRECT_EXCHANGE_QINJEE, MQConsts.DIRECT_ROUTINGKEY_QINJEE, object, correlationData);
	}

}
