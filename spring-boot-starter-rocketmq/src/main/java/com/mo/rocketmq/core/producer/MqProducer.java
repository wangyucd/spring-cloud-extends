package com.mo.rocketmq.core.producer;

import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

public interface MqProducer<M> {

	void send(MessageProxy<M> messageProxy) throws MQClientException, InterruptedException, RemotingException;

	void start() throws MQClientException;

	void shutdown();

}
