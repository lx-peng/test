package cn.itcast.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.itcast.demo.TestSend;

public class TestJavaMail {
	@Test
	public void testMail() throws Exception{
		ApplicationContext ac=new ClassPathXmlApplicationContext("classpath:applicationContext-mail.xml");
		TestSend ts = (TestSend)ac.getBean("testSend");
		ts.sendMail();
	}

}
