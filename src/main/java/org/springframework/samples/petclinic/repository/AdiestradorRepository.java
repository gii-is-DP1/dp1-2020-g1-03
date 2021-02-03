package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Adiestrador;

public interface AdiestradorRepository extends Repository<Adiestrador, Integer>{
	void save(Adiestrador adiestrador) throws DataAccessException;
	
	
	@Query("SELECT adiestrador.id FROM Adiestrador adiestrador WHERE adiestrador.user.username LIKE :username%")
	int findAdiestradorIdByUsername(String username) throws DataAccessException;
	
	@Query("SELECT adiestrador.firstName, adiestrador.lastName FROM Adiestrador adiestrador")
	List<String> findNameAndLastnameAdiestrador()throws DataAccessException;
	
	
	
	Collection<Adiestrador> findAll() throws DataAccessException;
}
