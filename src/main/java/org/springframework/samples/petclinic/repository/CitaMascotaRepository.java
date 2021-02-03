package org.springframework.samples.petclinic.repository;

import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.CitaMascota;

public interface CitaMascotaRepository extends Repository<CitaMascota, Integer>{
	
	void save(CitaMascota citaMascota) throws DataAccessException;
	
	void delete(CitaMascota citaMascota) throws DataAccessException;
	
	@Query("SELECT citaMascota FROM CitaMascota citaMascota WHERE citaMascota.cita.id LIKE ?1")
	public List<CitaMascota> findCitaMascotaByCitaId (int citaId) throws DataAccessException;
	
	@Query("SELECT DISTINCT cita FROM CitaMascota citaMascota WHERE citaMascota.pet.owner.id LIKE ?1")
	public List<Cita> findCitasByOwnerId (int ownerId) throws DataAccessException;
	
	@Query("SELECT COUNT (DISTINCT citaMascota.cita) FROM CitaMascota citaMascota WHERE citaMascota.cita.vet.id LIKE ?1 AND  citaMascota.pet.owner.id LIKE ?1")
	int findCitasOwnerConVet(int idVet, int idOwner) throws DataAccessException;

}
