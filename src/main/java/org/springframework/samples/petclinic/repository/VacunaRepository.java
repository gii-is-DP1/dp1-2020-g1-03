package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Vacuna;

public interface VacunaRepository extends Repository<Vacuna, Integer> {

		
	//@Query()
	List<Vacuna> findAll() throws DataAccessException;
	
	@Query("SELECT VACUNA FROM VACUNAS NATURALJOIN PETS NATURALJOIN OWNER WHERE OWNER_ID = ?1")
	List<Vacuna> findByOwnerId(int ownerId) throws DataAccessException;
	
	//@Query()
	Vacuna findById(int id) throws DataAccessException;
	
}
