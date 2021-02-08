package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Competicion;

public interface CompeticionRepository extends Repository<Competicion, Integer>{
	
	void save(Competicion competicion) throws DataAccessException;
	
	List<Competicion> findAll() throws DataAccessException;
	
	@Query("SELECT competicion FROM Competicion competicion WHERE competicion.nombre LIKE :nombreCompeticion%")
	List<Competicion> findByName(String nombreCompeticion) throws DataAccessException;
	
	Competicion findById(int competicionId) throws DataAccessException;
	
	void delete(Competicion competicion) throws DataAccessException;

	
	
}