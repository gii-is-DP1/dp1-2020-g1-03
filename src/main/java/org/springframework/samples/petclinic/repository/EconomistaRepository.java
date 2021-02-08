
package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Economista;

public interface EconomistaRepository extends Repository<Economista, Integer>{

	Collection<Economista> findAll() throws DataAccessException;

	Economista findById(int economistaId)throws DataAccessException;

	@Query("SELECT id FROM Economista economista WHERE economista.user.username LIKE :user%")
	int findEconIdByUsername(String user)throws DataAccessException;

}
