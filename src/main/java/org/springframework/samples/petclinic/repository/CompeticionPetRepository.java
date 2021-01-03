package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.model.CompeticionPet;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;

public interface CompeticionPetRepository extends Repository<CompeticionPet, Integer>{
	
	@Query("SELECT competicionPet FROM CompeticionPet competicionPet WHERE competicionPet.pet.id LIKE ?1")
	List<CompeticionPet> findCompeticionByPetId(int petId) throws DataAccessException;
	
	void save(CompeticionPet competicionPet) throws DataAccessException;
}
