package org.springframework.samples.petclinic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.TipoEnfermedad;
import org.springframework.samples.petclinic.model.Vacuna;

public interface VacunaRepository extends Repository<Vacuna, Integer> {

		
	//@Query()
	List<Vacuna> findAll() throws DataAccessException;
	
	
	@Query("SELECT vacuna FROM Vacuna vacuna WHERE vacuna.pet.owner.id LIKE ?1")
	Collection<Vacuna> findVacunasByOwnerId(int ownerId) throws DataAccessException;
	
	//@Query()
	Vacuna findById(int id) throws DataAccessException;
	
	void save(Vacuna vacuna) throws DataAccessException;
	
	@Query("SELECT tenfermedad FROM TipoEnfermedad tenfermedad ORDER BY tenfermedad.name")
	Collection<TipoEnfermedad> findTipoEnfermedades() throws DataAccessException;
	
	@Query("SELECT tenfermedad FROM TipoEnfermedad tenfermedad WHERE tenfermedad.name LIKE ?1")
	TipoEnfermedad findTipoEnfermedad(String tipoEnfermedad);
	
	@Query("SELECT DISTINCT pet FROM Pet pet WHERE pet.type.name LIKE ?1")
	public Collection<Pet> findMascotaByEspecie(String especieMascota);
	
}
