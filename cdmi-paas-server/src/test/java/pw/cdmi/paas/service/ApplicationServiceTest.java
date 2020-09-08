package pw.cdmi.paas.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pw.cdmi.paas.Application;
import pw.cdmi.paas.app.model.NewApplication;
import pw.cdmi.paas.app.service.AuthApplicationService;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes=Application.class)// 指定spring-boot的启动类
//public class ApplicationServiceTest {
//	@Autowired
//    private AuthApplicationService service;
//	
//	private NewApplication newapp;
//	@Before
//	public void before() {
//        //创建对象
//		newapp = new NewApplication();
//		newapp.setName("聚数开放平台");
//    }
//	
//	@Test
//    public void addSelfApplication()  {
//		String newid = service.createAuthApplication(newapp);
//    }
//}
