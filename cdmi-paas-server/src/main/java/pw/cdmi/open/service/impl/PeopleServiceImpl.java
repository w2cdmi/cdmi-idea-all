package pw.cdmi.open.service.impl;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;




import pw.cdmi.collection.PageView;
import pw.cdmi.open.model.PeopleIDCard;
import pw.cdmi.open.model.entities.People;
import pw.cdmi.open.repositories.PeopleRepository;
import pw.cdmi.open.service.PeopleService;

@Service
public class PeopleServiceImpl implements PeopleService {

	private static final Logger logger = LoggerFactory.getLogger(PeopleServiceImpl.class);

	@Autowired
	private PeopleRepository peopleRepository;

	@Override
	public String createPeople(PeopleIDCard idcard) {
		// 判断新添加的公民信息是否已存在
		People people = getPeopleByIdCode(idcard.getIdCard());
		String newid = null;
		// 如果公民信息已存在，则更新其他字段信息
		if (people != null) {
			//新增的公民信息已经存在，该公民信息为：
			logger.warn("公民信息已存在:" + idcard.toString());
			//更新原有公民信息
			people = transform(people, idcard);
			peopleRepository.save(people);
			newid = people.getId();
		}else {
			//将idcard对象转换为people对象
			people = new People();
			people.setIdCard(idcard.getIdCard());
			people = transform(people, idcard);
			try {
				People newPeople = peopleRepository.save(people);
				newid = newPeople.getId();
			} catch (ConstraintViolationException ex) {
				// 公民信息的唯一识别字段内容已存在。放弃添加
				logger.error("ex:" + ex.getMessage());
			}
		}

		return newid;
	}

	@Override
	public boolean existPeople(People people) {
		People findPeople = peopleRepository.findOne(Example.of(people)).get();
		if(findPeople==null){
			return false;		
		}
		return true;
	}

	@Override
	public void updatePeople(People people) {
		if(StringUtils.isBlank(people.getId())){
			throw new SecurityException("更新失败");
		}
		peopleRepository.save(people);

	}

	@Override
	public void deletePeopleById(String id) {
		peopleRepository.deleteById(id);

	}

	@Override
	public People getPeopleById(String id) {
		return peopleRepository.findById(id).get();
	}

	@Override
	public People getPeopleByIdCode(String idCard) {
		return peopleRepository.findPeopleByIdCard(idCard);
	}

	@Override
	public People getPeopleBySocialCode(String socialCode) {
		People people = new People();
		people.setSocialSecurityCode(socialCode);
		return peopleRepository.findOne(Example.of(people)).get();
	}

	@Override
	public People getPeopleByDriverLicenseNumber(String driverNumber) {
		People people = new People();
		people.setDriverLicenseNumber(driverNumber);
		return peopleRepository.findOne(Example.of(people)).get();
	}

	@Override
	public People getPeopleByPassportNumber(String passportNumber) {
		People people = new People();
		people.setPassportNumber(passportNumber);
		return peopleRepository.findOne(Example.of(people)).get();
	}

	@Override
	public People getPeopleByKeyFields(String idCard, String passportNumber, String socialSecurityCode,
			String driverLicenseNumber) {
		People people = peopleRepository.findPeopleByKeyFields(idCard, passportNumber, socialSecurityCode,
				driverLicenseNumber);
		return people;
	}

	@Override
	public People getPeopleByUserId(long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public People getPeopleByAccountId(long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<People> findAllPeople() {
		// TODO Auto-generated method stub
		return peopleRepository.findAll();
	}

	@Override
	public PageView findPeopleByConditionAndPage(int pageNo, int pageSize, People queryObject) {
		// TODO Auto-generated method stub
		return null;
	}

	private People transform(People people, PeopleIDCard idcard) {
		if(idcard.getTrueName() != null) {
			people.setTrueName(idcard.getTrueName());
		}
		if(idcard.getSex() != null) {
			people.setSex(idcard.getSex());
		}
		if(idcard.getNation() != null) {
			people.setNation(idcard.getNation());
		}
		if(idcard.getLiveResidence() != null) {
			people.setLiveResidence(idcard.getLiveResidence());
		}
		if(idcard.getBirthday() != null) {
			people.setBirthday(idcard.getBirthday());
		}
		if(idcard.getPoliceStation() != null) {
			people.setPoliceStation(idcard.getPoliceStation());
		}
		if(idcard.getIdCardUrl() !=null) {
			people.setIdCardUrl(idcard.getIdCardUrl());
		}
		//TODO 未更新身份证生效和失效时间
		return people;
	}
}
