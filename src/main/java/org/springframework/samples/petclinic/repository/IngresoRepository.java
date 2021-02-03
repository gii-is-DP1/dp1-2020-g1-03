
package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Ingreso;

public interface IngresoRepository extends Repository<Ingreso, Integer>{

	Ingreso findById(int id) throws DataAccessException;

	void save(Ingreso ingreso) throws DataAccessException;
	
	List<Ingreso> findAll() throws DataAccessException;
	
}
