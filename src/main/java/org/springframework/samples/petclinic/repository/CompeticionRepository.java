package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.model.Competicion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.model.Vet;

public interface CompeticionRepository extends Repository<Competicion, Integer>{
	
	void save(Competicion competicion) throws DataAccessException;
	
//	@Query("SELECT id FROM Vet vet WHERE vet.user.username LIKE :user%")
//	int findVetIdByUsername(String user)throws DataAccessException;
	
	List<Competicion> findAll() throws DataAccessException;
	Collection<Competicion> findCompeticionBySecretarioId(Integer idSecretario) throws DataAccessException;
	
	@Query("SELECT competicion FROM Competicion competicion WHERE competicion.nombre LIKE :nombreCompeticion%")
	List<Competicion> findByName(String nombreCompeticion) throws DataAccessException;
	
	Competicion findById(int competicionId) throws DataAccessException;
	
	void delete(Competicion competicion) throws DataAccessException;
	
	@Query("SELECT competicion FROM Competicion competicion WHERE competicion.secretario LIKE ?1")
	List<Competicion> findCompeticionesBySecretario(Secretario secre) throws DataAccessException;
	
	
	//APUNTAR
	
	
}