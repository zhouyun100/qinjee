/**
 * 文件名：RabbitQueueBinding
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
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 周赟
 * @date 2019/10/30
 *
 */
@Configuration
public class RabbitQueueBinding extends RabbitExchange{

    /**
     * durable="true" 持久化 rabbitmq重启的时候不需要创建新的队列
     * auto-delete 表示消息队列没有在使用时将被自动删除 默认是false
     * exclusive  表示该消息队列是否只在当前connection生效,默认是false
     * @return
     */
    @Bean
    protected Queue fanoutQueue() {
        return QueueBuilder.durable(MQConsts.FANOUT_QUEUE_QINJEE).build();
    }

    @Bean
    protected Queue directQueue() {
        return QueueBuilder.durable(MQConsts.DIRECT_QUEUE_QINJEE).build();
    }

    @Bean
    protected Queue topicQueue() {
        return QueueBuilder.durable(MQConsts.TOPIC_QUEUE_QINJEE).build();
    }

    /**
     * DirectExchange绑定Queue：处理路由键。需要将一个队列绑定到交换机上，要求该消息与一个特定的路由键完全匹配。这是一个完整的匹配。
     * 如果一个队列绑定到该交换机上要求路由键 “dog”，则只有被标记为“dog”的消息才被转发，不会转发dog.puppy，也不会转发dog.guard，只会转发dog。
     * 任何发送到Direct Exchange的消息都会被转发到RouteKey中指定的Queue。
     *
     * 1.一般情况可以使用rabbitMQ自带的Exchange：”"(该Exchange的名字为空字符串，下文称其为default Exchange)。
     *
     * 2.这种模式下不需要将Exchange进行任何绑定(binding)操作
     *
     * 3.消息传递时需要一个“RouteKey”，可以简单的理解为要发送到的队列名字。
     *
     * 4.如果vhost中不存在RouteKey中指定的队列名，则该消息会被抛弃。
     * @return
     */
    @Bean
    public Binding bingingDirect() {
        return BindingBuilder.bind(directQueue()).to(directExchange()).with(MQConsts.DIRECT_ROUTINGKEY_QINJEE);
    }

    /**
     * FanoutExchange绑定Queue：不处理路由键。你只需要简单的将队列绑定到交换机上。一个发送到交换机的消息都会被转发到与该交换机绑定的所有队列上。
     * 很像子网广播，每台子网内的主机都获得了一份复制的消息。Fanout交换机转发消息是最快的。
     * 任何发送到Fanout Exchange的消息都会被转发到与该Exchange绑定(Binding)的所有Queue上。
     *
     * 1.可以理解为路由表的模式
     *
     * 2.这种模式不需要RouteKey
     *
     * 3.这种模式需要提前将Exchange与Queue进行绑定，一个Exchange可以绑定多个Queue，一个Queue可以同多个Exchange进行绑定。
     *
     * 4.如果接受到消息的Exchange没有与任何Queue绑定，则消息会被抛弃。
     * @return
     */
    @Bean
    public Binding bingingFanout() {
        return BindingBuilder.bind(fanoutQueue()).to(fanoutExchange());
    }

    /**
     * TopicExchange绑定Queue：将路由键和某模式进行匹配。此时队列需要绑定要一个模式上。符号“#”匹配一个或多个词，符号“*”匹配不多不少一个词。
     * 因此“audit.#”能够匹配到“audit.irs.corporate”，但是“audit.*” 只会匹配到“audit.irs”任何发送到Topic Exchange的消息都会被转发到所有关心RouteKey中指定话题的Queue上
     *
     * 1.这种模式较为复杂，简单来说，就是每个队列都有其关心的主题，所有的消息都带有一个“标题”(RouteKey)，Exchange会将消息转发到所有关注主题能与RouteKey模糊匹配的队列。
     *
     * 2.这种模式需要RouteKey，也许要提前绑定Exchange与Queue。
     *
     * 3.在进行绑定时，要提供一个该队列关心的主题，如“#.log.#”表示该队列关心所有涉及log的消息(一个RouteKey为”MQ.log.error”的消息会被转发到该队列)。
     *
     * 4.“#”表示0个或若干个关键字，“*”表示一个关键字。如“log.*”能与“log.warn”匹配，无法与“log.warn.timeout”匹配；但是“log.#”能与上述两者匹配。
     *
     * 5.同样，如果Exchange没有发现能够与RouteKey匹配的Queue，则会抛弃此消息。
     * @return
     */
    @Bean
    public Binding bingingTopic(){
        return BindingBuilder.bind(topicQueue()).to(topicExchange()).with(MQConsts.TOPIC_ROUTINGKEY_QINJEE);
    }
}
