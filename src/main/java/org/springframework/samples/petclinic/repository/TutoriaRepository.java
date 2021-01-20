package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Tutoria;

public interface TutoriaRepository extends Repository<Tutoria,Integer> {
	
	void save(Tutoria tutoria) throws DataAccessException;
	
	Tutoria findById(int id) throws DataAccessException;
	
	List<Tutoria> findAll() throws DataAccessException;
	

}
