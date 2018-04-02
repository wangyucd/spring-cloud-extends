package com.mo.rocketmq.core.consumer;

import com.alibaba.rocketmq.common.message.MessageExt;
import com.mo.rocketmq.ConsumeException;

/**
 * ClassName: RocketMqConsumerListener <br/>
 * Function: 一个topic对应一个入口. <br/>
 * 
 * date: 2018年4月2日 下午5:11:02 <br/>
 * 
 * @author shuangyu
 * @version @param <M>
 * @since JDK 1.8
 */
public interface RocketMqConsumerListener<M> {

	public void onMessage(M message, MessageExt messageExt) throws ConsumeException;

	// ConsumerConfig getConsumerConfig();

}
