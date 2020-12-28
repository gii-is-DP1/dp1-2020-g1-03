
package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.ApuntarClase;
import org.springframework.samples.petclinic.model.Clase;

public interface ApuntarClaseRepository extends Repository<ApuntarClase, Integer>{
	@Query("SELECT apuntarClase FROM ApuntarClase apuntarClase WHERE apuntarClase.pet.id LIKE ?1 ORDER BY apuntarClase.clase.fechaHoraFin")
	List<ApuntarClase> findClasesByPetId(int petId) throws DataAccessException;
	
	void save(ApuntarClase apClase) throws DataAccessException;
}
