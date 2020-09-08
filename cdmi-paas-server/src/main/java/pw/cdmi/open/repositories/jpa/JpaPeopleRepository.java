package pw.cdmi.open.repositories.jpa;

import org.springframework.data.jpa.repository.Query;

import pw.cdmi.open.model.entities.People;
import pw.cdmi.open.repositories.PeopleRepository;

public interface JpaPeopleRepository extends PeopleRepository {

	@Override
	@Query("select p from People p where p.idCard = ?1 or p.passportNumber = ?2 or p.socialSecurityCode = ?3 or p.driverLicenseNumber= ?4")
	public People findPeopleByKeyFields(String idCard, String passportNumber, String socialSecurityCode,
			String driverLicenseNumber);
	
}
