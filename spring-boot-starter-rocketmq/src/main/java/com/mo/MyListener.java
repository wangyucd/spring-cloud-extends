package com.mo;

import com.mo.rocketmq.annotation.ConsumeProcessor;
import com.mo.rocketmq.annotation.RocketMqListener;

@RocketMqListener(topic = "MY_TOPIC")
public class MyListener {

	@ConsumeProcessor(msgClazz = String.class, tag = "TAG_1")
	public void method1(String message) {
		System.out.println(message);
	}

	@ConsumeProcessor(msgClazz = Object.class, tag = "TAG_2")
	public void method2(Object message) {
		System.out.println(message.toString());
	}

}