package pw.cdmi.paas.manager.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pw.cdmi.paas.manager.model.entities.Manager;
import pw.cdmi.paas.manager.repositories.jpa.JpaManagerRepository;
import pw.cdmi.paas.manager.service.ManagerService;
import pw.cdmi.wishlist.model.entities.WxUser;

@Service
public class ManagerServiceImpl implements ManagerService {

    @Autowired
    private JpaManagerRepository jpaManagerRepository;
    
	@Override
	public String createManager(Manager manager) {
		manager = jpaManagerRepository.save(manager);
		return manager.getId();
	}

	@Override
	public void switchManager(String oldmanager_id, WxUser wxUser) {
		Manager manager = jpaManagerRepository.findById(oldmanager_id).get();
		manager.setWxUnionId(wxUser.getWxUnionId());
		jpaManagerRepository.save(manager);
	}

	@Override
	public Manager getManagerByUserId(String userId) {
		return jpaManagerRepository.findOneByUserId(userId);
	}

	@Override
	public long countManager() {
		return jpaManagerRepository.count();
	}

}
