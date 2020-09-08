package pw.cdmi.paas.manager.repositories.jpa;

import pw.cdmi.paas.manager.model.entities.Manager;
import pw.cdmi.paas.manager.repositories.ManagerRepository;

public interface JpaManagerRepository extends ManagerRepository{

	public Manager findOneByUserId(String userId);
}
