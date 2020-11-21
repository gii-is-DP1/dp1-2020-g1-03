package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.model.Vet;

public interface ComentarioRepository extends Repository<Comentario, Integer>{
	Vet findByVetId(int vetId) throws DataAccessException;

	void save(Comentario comentario) throws DataAccessException;
	
	List<Comentario> findAll() throws DataAccessException;
	Collection<Comentario> findComentariosByVetId(Integer idVet) throws DataAccessException;
}
