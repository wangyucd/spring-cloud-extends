package com.mo;
@RocketMqListener(topic = "MY_TOPIC")
public class MyListener {

	@ConsumerProcessor(messageClass = String.class, tag = "TAG_1")
	public void method1(String message) {
		System.out.println(message);
	}

	@ConsumerProcessor(messageClass = Object.class, tag = "TAG_2")
	public void method2(Object message) {
		System.out.println(message.toString());
	}

}