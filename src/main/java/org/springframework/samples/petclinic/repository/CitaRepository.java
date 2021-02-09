
package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Estado;
import org.springframework.samples.petclinic.model.Vet;

public interface CitaRepository extends Repository<Cita, Integer> {


	Cita findById(int citaId) throws DataAccessException;
	
	void save(Cita cita) throws DataAccessException;

	@Query("SELECT cita FROM Cita cita WHERE cita.vet LIKE ?1")
	List<Cita> findCitasByVet(Vet vet) throws DataAccessException;
	
	List<Cita> findAll() throws DataAccessException;
	
	@Query("SELECT cita FROM Cita cita WHERE cita.vet IS NULL AND cita.estado LIKE ?1")
	List<Cita> findCitasSinVet(Estado estado) throws DataAccessException;

	void delete(Cita cita)throws DataAccessException;

	
}


	
	

