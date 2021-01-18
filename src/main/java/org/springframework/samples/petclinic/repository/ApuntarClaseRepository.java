
package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.ApuntarClase;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Pet;

public interface ApuntarClaseRepository extends Repository<ApuntarClase, Integer>{
	
	@Query("SELECT apuntarClase FROM ApuntarClase apuntarClase WHERE apuntarClase.pet.id LIKE ?1 ORDER BY apuntarClase.clase.fechaHoraFin")
	List<ApuntarClase> findClasesByPetId(int petId) throws DataAccessException;
	
	void save(ApuntarClase apClase) throws DataAccessException;
	
	@Query("SELECT apuntarClase FROM ApuntarClase apuntarClase WHERE apuntarClase.clase.id LIKE ?1")
	List<ApuntarClase> findMascotasApuntadasEnClaseByClaseId(int claseId) throws DataAccessException;
	
	void delete(ApuntarClase apuntarClase) throws DataAccessException;
	
	@Query("SELECT pet FROM ApuntarClase apuntarClase WHERE apuntarClase.clase.id =:claseId")
	Pet findPetByClasePetId(int claseId) throws DataAccessException;
}
