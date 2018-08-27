package cn.itcast.demo.ws.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientTest {

	public static void main(String[] args) {
		ApplicationContext ac = new ClassPathXmlApplicationContext("classpath:applicationContext_cxf.xml");
		IWeatherService ws = (IWeatherService)ac.getBean("weatherClient");
		System.out.println(ws.info("北京"));
		System.out.println(ws.info("深圳"));
	}

}
