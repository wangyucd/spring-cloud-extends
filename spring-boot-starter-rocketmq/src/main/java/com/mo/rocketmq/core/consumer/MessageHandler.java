package com.mo.rocketmq.core.consumer;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.xml.ws.handler.MessageContext;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.common.protocol.body.ConsumeStatus;
import com.alibaba.rocketmq.shade.com.alibaba.fastjson.JSON;

import me.jollyfly.rocketmq.starter.RocketMqConsumerListener;
import me.jollyfly.rocketmq.starter.exception.ConsumeException;

/**
 * 消息处理器
 *
 * @author jolly
 */
public class MessageHandler {

	@SuppressWarnings("unchecked")
	public static ConsumeStatus handleMessage(final RocketMqConsumerListener listener, final List<MessageExt> msgs, final MessageQueue messageQueue) {
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
			return handleException(e);
		}
		return ConsumeStatus.SUCCESS;
	}

	/**
	 * 异常处理
	 *
	 * @param e
	 *            捕获的异常
	 * @return 消息消费结果
	 */
	private static ConsumeStatus handleException(final Exception e) {
		Class exceptionClass = e.getClass();
		if (exceptionClass.equals(UnsupportedEncodingException.class)) {
			log.error(e.getMessage());
		} else if (exceptionClass.equals(ConsumeException.class)) {
			log.error(e.getMessage());
		}
		return ConsumeStatus.RETRY;
	}

}
