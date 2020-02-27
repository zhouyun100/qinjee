package com.qinjee.masterdata.rabbitmq;

import com.rabbitmq.client.Channel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.qinjee.consts.MQConsts;

import java.io.IOException;

/**
 * @author 周赟
 * @date 2019/10/30
 */
@Component
public class Consumer{

	private static Logger logger = LogManager.getLogger(Consumer.class);

	@RabbitListener(queues = { MQConsts.DIRECT_QUEUE_QINJEE})
	 public void process(Message message, Channel channel) throws IOException {
		try {
			// 采用手动应答模式, 手动确认应答更为安全稳定
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), true);
			logger.info("send currentime: {}", new String(message.getBody()));
			logger.info("receive currenttime: {}", System.currentTimeMillis());

		} catch (Exception e) {
			logger.error("消费失败：", new String(message.getBody()), e);
			//消费失败添加至死信队列
			channel.basicReject(message.getMessageProperties().getDeliveryTag(), true);

		}
	}
}
