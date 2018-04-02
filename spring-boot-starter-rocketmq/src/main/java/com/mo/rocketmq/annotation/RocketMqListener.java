package com.mo.rocketmq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.stereotype.Component;

import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;

/**
 * ClassName: RocketMqListener <br/>
 * Function: TODO 功能说明. <br/>
 * 
 * date: 2018年4月2日 下午3:08:25 <br/>
 * 
 * @author shuangyu
 * @version
 * @since JDK 1.8
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RocketMqListener {

	/**
	 * 是否为顺序消息
	 *
	 * @return true：顺序消息 false：非顺序消息
	 */
	boolean orderly() default false;

	/**
	 * 转换为DefaultMqPushConsumer后订阅的topic 默认为“DEFAULT_TOPIC”
	 */
	String topic() default "DEFAULT_TOPIC";

	/**
	 * 消息模式，默认为集群模式
	 *
	 * @see MessageModel
	 */
	MessageModel messageModel() default MessageModel.CLUSTERING;

	/**
	 * 消费者组
	 */
	String consumerGroup() default "DEFAULT_GROUP";

	/**
	 * 此消费者在消费时的最大线程数，如果在此处设置则使用此处设置的值 否则统一使用配置文件中的值
	 */
	int consumeThreadMax() default 0;

	/**
	 * 此消费者在消费时的最小线程数，如果在此处设置则使用此处设置的值 否则统一使用配置文件中的值
	 */
	int consumeThreadMin() default 0;

}
