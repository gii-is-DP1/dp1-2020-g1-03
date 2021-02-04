package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Comentario;

public interface ComentarioRepository extends Repository<Comentario, Integer>{
	
	void save(Comentario comentario) throws DataAccessException;
	
	
	List<Comentario> findAll() throws DataAccessException;
	
	Collection<Comentario> findComentariosByVetId(Integer idVet) throws DataAccessException;
	
	List<Comentario> findComentariosByOwnerId(Integer idOwner) throws DataAccessException;
	
	Comentario findById(int comentarioId) throws DataAccessException;
	
	@Query("SELECT COUNT (comentario.id) FROM Comentario comentario WHERE comentario.vet.id LIKE ?1 AND comentario.owner.id LIKE ?1")
	int findComentariosOwnerConVet(int idVet, int idOwner) throws DataAccessException;
}
