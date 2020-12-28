package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Clase;

public interface ClaseRepository extends Repository<Clase, Integer>{
	
	void save(Clase clase) throws DataAccessException;
	
//	@Query("SELECT id FROM Vet vet WHERE vet.user.username LIKE :user%")
//	int findVetIdByUsername(String user)throws DataAccessException;
	
	List<Clase> findAll() throws DataAccessException;
	Collection<Clase> findClaseByAdiestradorId(Integer idAdiestrador) throws DataAccessException;
	
	@Query("SELECT clase FROM Clase clase WHERE clase.name LIKE :nombreClase%")
	List<Clase> findByName(String nombreClase) throws DataAccessException;
	
	Clase findById(int claseId) throws DataAccessException;
	
	void delete(Clase clase) throws DataAccessException;
}
