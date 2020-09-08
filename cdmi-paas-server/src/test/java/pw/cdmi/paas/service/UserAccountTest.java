package pw.cdmi.paas.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pw.cdmi.paas.Application;
import pw.cdmi.paas.account.model.NewUser;
import pw.cdmi.paas.account.model.RegisterChannel;
import pw.cdmi.paas.account.service.UserService;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes=Application.class)// 指定spring-boot的启动类
//public class UserAccountTest {
//
//	@Autowired
//    private UserService service;
//	
//	private NewUser user;
//	@Before
//	public void before() {
//        //创建对象
//		user = new NewUser();
//		user.setChannel(RegisterChannel.MOBILE);
//		NewUser.Info info = user.new Info();
//		info.setMobile("18615703273");
//		user.setInfo(info);;
//    }
//	
//	@Test
//    public void addSelfApplication()  {
////		String newid = service.createUserAccount(user);
//    }
//}
