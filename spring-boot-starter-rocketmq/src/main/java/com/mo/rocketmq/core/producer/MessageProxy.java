package com.mo.rocketmq.core.producer;

import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendCallback;
import com.alibaba.rocketmq.common.message.Message;

public class MessageProxy<M> {

	// 生产者顺序发送
	private MessageQueueSelector messageQueueSelector;

	private Object selectorArg;

	private SendCallback sendCallback;

	private Message message;

	public Object getSelectorArg() {
		return selectorArg;
	}

	public void setSelectorArg(Object selectorArg) {
		this.selectorArg = selectorArg;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public MessageQueueSelector getMessageQueueSelector() {
		return messageQueueSelector;
	}

	public void setMessageQueueSelector(MessageQueueSelector messageQueueSelector) {
		this.messageQueueSelector = messageQueueSelector;
	}

	public SendCallback getSendCallback() {
		return sendCallback;
	}

	public void setSendCallback(SendCallback sendCallback) {
		this.sendCallback = sendCallback;
	}
}
