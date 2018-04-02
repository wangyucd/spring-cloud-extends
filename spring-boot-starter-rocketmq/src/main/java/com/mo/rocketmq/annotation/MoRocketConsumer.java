package com.mo.rocketmq.annotation;

@interface MoRocketConsumer {

	String topic() default "defaultTopic";
	String tag() default "";
}
