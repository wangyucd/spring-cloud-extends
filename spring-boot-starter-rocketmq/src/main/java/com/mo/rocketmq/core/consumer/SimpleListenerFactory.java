package com.mo.rocketmq.core.consumer;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.ws.handler.MessageContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.CollectionUtils;

import com.mo.rocketmq.annotation.RocketMqListener;

public class SimpleListenerFactory implements ApplicationContextAware {

	private static final Logger logger = LoggerFactory.getLogger(SimpleListenerFactory.class);

	private Map<String, RocketMqConsumerListener> listeners = new ConcurrentHashMap<>();

	private ConsumeProcessResolver resolver = new ConsumeProcessResolver();

	private ApplicationContext context;

	public Map<String, RocketMqConsumerListener> getListeners() {
		return listeners;
	}

	public void initSubscriptionGroups() {
		Map<String, SubscriptionGroup> subscriptionGroups = this.resolver.getSubscriptionGroups();
		subscriptionGroups.forEach((topic, subscriptionGroup) -> allListeners.put(topic, createRocketMqConsumerListener(subscriptionGroup)));

	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.context = applicationContext;
		this.resolver.setApplicationContext(applicationContext);
		initSubscriptionGroups();
	}

	private class ConsumeProcessResolver implements ApplicationContextAware {

		private ApplicationContext context;

		private Map<String, SubscriptionGroup> subscriptionGroups = new HashMap<>();

		private boolean initSubscription = false;

		Map<String, SubscriptionGroup> getSubscriptionGroups() {
			if (!initSubscription) {
				resolveListenerMethod();
			}
			return subscriptionGroups;
		}

		void resolveListenerMethod() {
			context.getBeansWithAnnotation(RocketMqListener.class).forEach((beanName, beanInstance) -> {
				Map<Method, RocketMqListener> annotatedMethods = MethodIntrospector.selectMethods(beanInstance.getClass(),

						(MethodIntrospector.MetadataLookup<RocketMqListener>) method -> AnnotatedElementUtils.findMergedAnnotation(method,
								RocketMqListener.class));

				initSubscriptionGroup(annotatedMethods, beanInstance);
			});
			this.initSubscription = true;
		}

		private void initSubscriptionGroup(Map<Method, RocketMqListener> annotatedMethod, Object target) {
			if (!CollectionUtils.isEmpty(annotatedMethod)) {
				annotatedMethod.forEach((method, listener) -> {
					validateMethod(method);
					RocketMqListener rocketListeners = method.getDeclaringClass().getAnnotation(RocketMqListener.class);
					String topic = rocketListeners.topic();
					String tag = listener.tag();
					if (subscriptionGroups.containsKey(topic)) {
						subscriptionGroups.get(topic).putTagToGroup(tag, method);
					} else {
						SubscriptionGroup subscriptionGroup = new SubscriptionGroup(topic);
						subscriptionGroup.putTagToGroup(tag, method);
						subscriptionGroup.setTarget(target);
						subscriptionGroups.put(topic, subscriptionGroup);
					}

				});
			}

		}

		private void validateMethod(Method method) {
			if (method.getParameterCount() > 2) {
				throw new MethodNotSupportException("method: " + method + " 参数列表不被支持");
			}
			boolean typeSupport = Arrays.stream(method.getParameterTypes()).allMatch(
					parmType -> parmType.equals(method.getAnnotation(RocketMQListener.class).messageClass()) || parmType.equals(MessageContext.class));
			if (!typeSupport) {
				throw new MethodNotSupportException("方法参数中含有不被支持的类型");
			}
		}

		@Override
		public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
			this.context = applicationContext;
		}
	}

}
