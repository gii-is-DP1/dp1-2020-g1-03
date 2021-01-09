package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.CompeticionPet;
import org.springframework.samples.petclinic.model.Pet;

public interface CompeticionPetRepository extends Repository<CompeticionPet, Integer>{
	
	@Query("SELECT competicionPet FROM CompeticionPet competicionPet WHERE competicionPet.pet.id LIKE ?1")
	List<CompeticionPet> findCompeticionByPetId(int petId) throws DataAccessException;
	
	void save(CompeticionPet competicionPet) throws DataAccessException;
	
	@Query("SELECT competicionPet FROM CompeticionPet competicionPet WHERE competicionPet.competicion.id =:competicionId")
	Collection<CompeticionPet> findCompeticionPetByCompeticionId(int competicionId);
	
	@Query("SELECT competicionPet FROM CompeticionPet competicionPet WHERE competicionPet.competicion.id =:competicionId")
	Collection<Pet> findPetByCompeticionId(int competicionId);
	
	@Query("SELECT pet FROM CompeticionPet competicionPet WHERE competicionPet.id =:competicionPetId")
	Pet findPetByCompeticionPetId(int competicionPetId);
	
}
