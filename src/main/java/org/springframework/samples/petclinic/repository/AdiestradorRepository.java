package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Adiestrador;

public interface AdiestradorRepository extends Repository<Adiestrador, Integer>{
	void save(Adiestrador adiestrador) throws DataAccessException;
	
	@Query("SELECT adiestrador FROM Adiestrador adiestrador WHERE adiestrador.id =:id")
	public Adiestrador findById(@Param("id") int id);
	
	@Query("SELECT adiestrador FROM Adiestrador adiestrador WHERE adiestrador.user.username LIKE :username%")
	Adiestrador findAdiestradorByUsername(String username) throws DataAccessException;

	
	@Query("SELECT adiestrador.firstName, adiestrador.lastName FROM Adiestrador adiestrador")
	List<String> findNameAndLastnameAdiestrador()throws DataAccessException;
	
	@Query("SELECT adiestrador FROM Adiestrador adiestrador WHERE adiestrador.user.username LIKE :username%")
	Adiestrador findAdiestradorByUsername(String username) throws DataAccessException;
	
	
	Collection<Adiestrador> findAll() throws DataAccessException;
}
