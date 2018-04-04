package com.mo.rocketmq.core.consumer;

import java.util.List;

import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

/**
 * 消息处理器
 *
 * @author jolly
 */
public class MessageHandler {

	public final static Logger log = LoggerFactory.getLogger(MessageHandler.class);

	@SuppressWarnings("unchecked")
	public static ConsumeConcurrentlyStatus handleMessage(final RocketMqConsumerListener listener, final List<MessageExt> msgs,
			final MessageQueue messageQueue) {
		try {
			for (MessageExt msg : msgs) {
				byte[] body = msg.getBody();
				final MessageContext messageContext = new MessageContext();
				messageContext.setMessageExt(msg);
				messageContext.setMessageQueue(messageQueue);
				if (log.isDebugEnabled()) {
					log.debug("开始消费，msg={}", msg);
				}
				listener.onMessage(JSON.parseObject(new String(body, "UTF-8"), listener.getConsumerConfig().getMessageClass()), messageContext);
				if (log.isDebugEnabled()) {
					log.debug("消费完成");
				}
			}
		} catch (Exception e) {
			log.error(e.getMessage());
			return ConsumeConcurrentlyStatus.RECONSUME_LATER;
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}

}
