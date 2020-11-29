
package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.model.Ingreso;

public interface IngresoRepository extends Repository<Ingreso, Integer>{

	Ingreso findById(int id) throws DataAccessException;

	/**
	 * Save a <code>Pet</code> to the data store, either inserting or updating it.
	 * @param pet the <code>Pet</code> to save
	 * @see BaseEntity#isNew
	 */
	void save(Ingreso ingreso) throws DataAccessException;
	
	//@Query("SELECT * FROM ingresos")
	List<Ingreso> findAll() throws DataAccessException;
	
}
