
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Secretario;

public interface SecretarioRepository extends Repository<Secretario, Integer>{
	void save(Secretario secretario) throws DataAccessException;
	
	
	@Query("SELECT secretario FROM Secretario secretario WHERE secretario.user.username LIKE :username%")
	Secretario findSecretarioByUsername(String username) throws DataAccessException;


	Secretario findSecretarioById(Integer id)throws DataAccessException;


	Collection<Secretario> findAll()throws DataAccessException;
}

