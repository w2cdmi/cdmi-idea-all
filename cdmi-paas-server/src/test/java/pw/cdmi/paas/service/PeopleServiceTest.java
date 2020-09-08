package pw.cdmi.paas.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import pw.cdmi.open.model.Nation;
import pw.cdmi.open.model.PeopleIDCard;
import pw.cdmi.open.model.Sex;
import pw.cdmi.open.service.PeopleService;
import pw.cdmi.paas.Application;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes=Application.class)// 指定spring-boot的启动类
//public class PeopleServiceTest {
//	@Autowired
//    private PeopleService service;
//	
//	private PeopleIDCard people;
//	
//	@Before
//	public void before() {
//        //创建对象
//		people = new PeopleIDCard();
//		people.setTrueName("张三丰");
//		people.setIdCard("51012219872323432x");
//		people.setNation(Nation.Han);
//		people.setSex(Sex.FEMALE);
//    }
//	
//	@Test
//    public void addPeople()  {
//		service.createPeople(people);
//    }
//	
//}
