package com.mo.rocketmq.core.consumer;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.mo.rocketmq.ConsumeException;

public interface RocketMqConsumerListener<M> {

	public void onMessage(M message, MessageExt messageExt) throws ConsumeException;

	// ConsumerConfig getConsumerConfig();

}
