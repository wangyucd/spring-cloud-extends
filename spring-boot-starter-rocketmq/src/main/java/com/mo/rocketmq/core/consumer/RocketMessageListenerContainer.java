package com.mo.rocketmq.core.consumer;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.SmartLifecycle;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.exception.MQClientException;

/**
 * @author jolly
 */
public class RocketMessageListenerContainer implements InitializingBean, DisposableBean, BeanNameAware, SmartLifecycle, ApplicationContextAware {

	private String nameSrvAddr;

	private int consumeThreadMin;

	private int consumeThreadMax;

	private final Object monitor = new Object();

	private final Object mapMonitor = new Object();

	private volatile boolean running = false;

	private volatile boolean initialized = false;

	private List<DefaultMQPushConsumer> pushConsumers = new CopyOnWriteArrayList<>();

	private Map<String, DefaultMQPushConsumer> pushConsumerMap = new ConcurrentHashMap<>();

	private Map<String, DefaultMQPushConsumer> removedMap = new ConcurrentHashMap<>();

	private Map<String, DefaultMQPushConsumer> runningMap = new ConcurrentHashMap<>();

	private Map<String, Map.Entry<DefaultMQPushConsumer, String>> startErrMap = new ConcurrentHashMap<>();

	private MqPushConsumerFactory consumerFactory;

	private ApplicationContext applicationContext;

	private String beanName;

	@Override
	public void start() {
		if (!isRunning()) {
			running = true;
			synchronized (monitor) {
				registMessageListener();
				startAllListener();
			}
		}
	}

	private void startAllListener() {
		pushConsumerMap.forEach((topic, consumer) -> {
			try {
				consumer.start();
				runningMap.put(topic, consumer);
			} catch (MQClientException e) {
				Map.Entry<DefaultMQPushConsumer, String> errEntry = new AbstractMap.SimpleEntry<>(consumer, e.getErrorMessage());
				startErrMap.put(topic, errEntry);
			}
		});
	}

	@Override
	public void stop() {
		if (isRunning()) {
			running = false;
			pushConsumers.forEach(DefaultMQPushConsumer::shutdown);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Stopped RocketMessageListenerContainer");
		}
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void setBeanName(String name) {
		this.beanName = name;
	}

	@Override
	public void destroy() {
		this.initialized = false;
		stop();
	}

	@Override
	public void afterPropertiesSet() {
		initMqPushConsumerFactory();
		this.initialized = true;

	}

	private void initMqPushConsumerFactory() {
		this.consumerFactory = new MqPushConsumerFactory(this.nameSrvAddr);
		this.consumerFactory.setApplicationContext(applicationContext);
		this.consumerFactory.setConsumeThreadMax(this.consumeThreadMax);
		this.consumerFactory.setConsumeThreadMin(this.consumeThreadMin);
		this.consumerFactory.afterPropertiesSet();

	}

	@Override
	public boolean isAutoStartup() {
		return true;
	}

	@Override
	public void stop(Runnable callback) {
		stop();
		callback.run();
	}

	@Override
	public int getPhase() {
		return Integer.MAX_VALUE;
	}

	private void registMessageListener() {
		SimpleListenerFactory listenerFactory = consumerFactory.getListenerFactory();
		pushConsumers.addAll(consumerFactory.getAllMQPushConsumer());
		pushConsumerMap.putAll(consumerFactory.getPushConsumerMap());
		Map<String, RocketMqConsumerListener> listenerMap = listenerFactory.getAllListeners();
		pushConsumerMap.forEach((topic, consumer) -> {
			RocketMqConsumerListener listener = listenerMap.get(topic);
			if (listener.getConsumerConfig().isOrderlyMessage()) {
				consumer.registerMessageListener(new MessageListenerOrderlyImpl(listener));
			} else {
				consumer.registerMessageListener(new MessageListenerConcurrentlyImpl(listener));
			}
		});
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	private FindResult findInMap(String topic) {
		if (!pushConsumerMap.containsKey(topic)) {
			return FindResult.NONE;
		} else {
			if (startErrMap.containsKey(topic)) {
				return FindResult.START_ERROR;
			}
			if (runningMap.containsKey(topic)) {
				return FindResult.RUNNING;
			}
			if (removedMap.containsKey(topic)) {
				return FindResult.SUSPEND;
			}
		}
		return FindResult.ERROR;
	}

	private enum FindResult {
		// 未找到
		NONE,
		// 启动异常
		START_ERROR,
		// 运行中
		RUNNING,
		// 停止
		SUSPEND,
		// 其他异常
		ERROR
	}

	private enum OperatinType {
		// 恢复
		RESUME,
		// 暂停
		SUSPEND
	}

	public String getNameSrvAddr() {
		return nameSrvAddr;
	}

	public void setNameSrvAddr(String nameSrvAddr) {
		this.nameSrvAddr = nameSrvAddr;
	}

	public MqPushConsumerFactory getConsumerFactory() {
		return consumerFactory;
	}

	public void setConsumerFactory(MqPushConsumerFactory consumerFactory) {
		this.consumerFactory = consumerFactory;
	}

	public int getConsumeThreadMin() {
		return consumeThreadMin;
	}

	public void setConsumeThreadMin(int consumeThreadMin) {
		this.consumeThreadMin = consumeThreadMin;
	}

	public int getConsumeThreadMax() {
		return consumeThreadMax;
	}

	public void setConsumeThreadMax(int consumeThreadMax) {
		this.consumeThreadMax = consumeThreadMax;
	}
}
