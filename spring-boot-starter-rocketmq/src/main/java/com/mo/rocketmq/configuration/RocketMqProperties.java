package com.mo.rocketmq.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * ClassName: RocketMqProperties <br/>
 * Function: 配置. <br/>
 * 
 * date: 2018年4月2日 下午2:09:39 <br/>
 * 
 * @author shuangyu
 * @version
 * @since JDK 1.8
 */
@ConfigurationProperties("rocketmq")
public final class RocketMqProperties {
	private String nameSrvAddr = "localhost:9876";
	private int consumeThreadMin = 18;
	private int consumeThreadMax = 48;
	public String getNameSrvAddr() {
		return nameSrvAddr;
	}
	public void setNameSrvAddr(String nameSrvAddr) {
		this.nameSrvAddr = nameSrvAddr;
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
