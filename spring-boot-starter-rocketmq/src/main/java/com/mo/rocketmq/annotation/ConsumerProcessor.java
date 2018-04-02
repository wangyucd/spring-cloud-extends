package com.mo.rocketmq.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * ClassName: ConsumerProcessor <br/>
 * Function: 改注解运用在消费端的方法上，用来处理同一topic中不同的tag类型的消息 . <br/>
 * 
 * date: 2018年4月2日 下午3:11:01 <br/>
 * 
 * @author shuangyu
 * @version
 * @since JDK 1.8
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ConsumerProcessor {

	/**
	 * 订阅的tag
	 */
	String tag() default "*";

	/**
	 * 请求方消息类型
	 */
	Class<?> msgClz() default Object.class;
}
