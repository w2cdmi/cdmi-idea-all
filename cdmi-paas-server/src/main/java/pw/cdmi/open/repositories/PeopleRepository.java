package pw.cdmi.open.repositories;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

import pw.cdmi.open.model.entities.People;

@NoRepositoryBean
public interface PeopleRepository extends PagingAndSortingRepository<People, String>, QueryByExampleExecutor<People> {

	public People findPeopleByKeyFields(String idCard, String passportNumber, String socialSecurityCode,
			String driverLicenseNumber);
	
	public People findPeopleByIdCard(String idCard);
}
