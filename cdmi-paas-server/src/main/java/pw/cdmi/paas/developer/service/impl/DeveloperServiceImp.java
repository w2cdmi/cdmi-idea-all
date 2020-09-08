package pw.cdmi.paas.developer.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import pw.cdmi.paas.developer.model.entities.Developer;
import pw.cdmi.paas.developer.repositories.DeveloperRepository;
import pw.cdmi.paas.developer.service.DeveloperService;

@Service
@Transactional
public class DeveloperServiceImp implements DeveloperService {
	@Autowired
	private DeveloperRepository developerRepository;
	
	@Override
	public String createDeveloper(Developer developer) {
		developer.setCreateTime(new Date());
		Developer save = developerRepository.save(developer);
		return save.getId();
	}

	@Override
	public Developer findOneDeveloperById(String developerId) {
		Developer developer = new Developer();
		developer.setId(developerId);
		
		return developerRepository.findOne(Example.of(developer)).get();
	}

	@Override
	public Developer findDeveloperByUserId(String userId) {
		return developerRepository.findOneByUserId(userId);
	}

}
