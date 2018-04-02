/** 
* Project Name:spring-boot-starter-rocketmq 
* Package Name:com.mo.rocketmq.core.consumer 
* File Name:MessageListenerConcurrentlyImpl.java 
* Date:2018年4月2日下午3:34:35 
* 
* Copyright (c) 2016-2018, Maike Tech
*
* Licensed under the Maike License, Version 1.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.maike51.com/licenses/LICENSE-1.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package com.mo.rocketmq.core.consumer;

import java.util.List;

import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.common.message.MessageExt;

import me.jollyfly.rocketmq.starter.RocketMqConsumerListener;

/**
 * ClassName: MessageListenerConcurrentlyImpl <br/>
 * Function: 并发消息处理实现类. <br/>
 * 
 * date: 2018年4月2日 下午3:34:35 <br/>
 * 
 * @author shuangyu
 * @version
 * @since JDK 1.8
 */
public class MessageListenerConcurrentlyImpl implements MessageListenerConcurrently {

	private final RocketMqConsumerListener listener;

	MessageListenerConcurrentlyImpl(RocketMqConsumerListener listener) {
		this.listener = listener;
	}

	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
		return MessageHandler.handleMessage(listener, msgs, context.getMessageQueue());

	}

}
