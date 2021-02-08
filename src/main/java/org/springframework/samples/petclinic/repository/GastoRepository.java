
package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Gasto;

public interface GastoRepository extends Repository<Gasto, Integer> {

	
	Gasto findById(int id) throws DataAccessException;

	
	void save(Gasto gasto) throws DataAccessException;
	
	
	List<Gasto> findAll() throws DataAccessException;

}
