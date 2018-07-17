package cn.itcast.erp.test.dao;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import cn.itcast.erp.dao.IDepDao;

public class DepDaoTest {

	@Test
	public void testDep(){
		ApplicationContext ac=new ClassPathXmlApplicationContext("classpath*:applicationContext_*.xml");
		IDepDao depDao=(IDepDao) ac.getBean("depDao"); 
	}
}
