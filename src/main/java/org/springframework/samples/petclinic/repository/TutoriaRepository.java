package org.springframework.samples.petclinic.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Tutoria;

public interface TutoriaRepository extends Repository<Tutoria,Integer> {
	
	void save(Tutoria tutoria) throws DataAccessException;
	
	Tutoria findById(int id) throws DataAccessException;
	
	List<Tutoria> findAll() throws DataAccessException;
	
	@Query("SELECT DISTINCT pet FROM Pet pet WHERE pet.name LIKE ?1")
	Collection<Pet> findMascotaByName(String name) throws DataAccessException;
	
	@Query("SELECT COUNT (tutoria.id) FROM Tutoria tutoria WHERE tutoria.fechaHora LIKE ?1")
	int findTutoriasByAdiestradorId(LocalDateTime fechaHora) throws DataAccessException;
	
	@Query("SELECT COUNT (tutoria.id) FROM Tutoria tutoria WHERE DATE(tutoria.fechaHora) LIKE ?1 AND tutoria.adiestrador.id LIKE ?1")
	int numeroTutoriasEnUnDia(LocalDate fecha, int adiestradorId) throws DataAccessException;
}
