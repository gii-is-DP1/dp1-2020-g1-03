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
	
	Tutoria findById(int tutoriaId) throws DataAccessException;
	
	List<Tutoria> findAll() throws DataAccessException;
	
	@Query("SELECT DISTINCT pet FROM Pet pet WHERE pet.name LIKE ?1")
	Collection<Pet> findMascotaByName(String name) throws DataAccessException;
	
	@Query("SELECT COUNT (tutoria.id) FROM Tutoria tutoria WHERE tutoria.fechaHora LIKE :fechaHora AND tutoria.adiestrador.id LIKE :adiestradorId")
	int findTutoriasByAdiestradorId(LocalDateTime fechaHora, int adiestradorId) throws DataAccessException;
	
	
	Collection<Tutoria> findTutoriasByOwnerId(Integer idOwner) throws DataAccessException;
	
	@Query("SELECT COUNT (tutoria.id) FROM Tutoria tutoria WHERE DAY(tutoria.fechaHora) LIKE :day AND MONTH(tutoria.fechaHora) LIKE :month "
			+ "AND YEAR(tutoria.fechaHora) LIKE :year AND tutoria.adiestrador.id LIKE :adiestradorId")
	int numeroTutoriasEnUnDiaAdiestrador(int day, int month, int year, int adiestradorId) throws DataAccessException;
	
	@Query("SELECT COUNT (tutoria.id) FROM Tutoria tutoria WHERE tutoria.fechaHora LIKE :fechaHora AND tutoria.pet.owner.id LIKE :ownerId")
	int numeroTutoriasEnUnDiaOwner(LocalDateTime fechaHora, int ownerId) throws DataAccessException;
	
	@Query("SELECT COUNT (tutoria.id) FROM Tutoria tutoria WHERE tutoria.fechaHora LIKE :fechaHora AND tutoria.pet.id LIKE :petId")
	int numeroTutoriasEnUnDiaPet(LocalDateTime fechaHora, int petId) throws DataAccessException;

	Tutoria findTutoriaById(int tutoriaId) throws DataAccessException;
	
}
