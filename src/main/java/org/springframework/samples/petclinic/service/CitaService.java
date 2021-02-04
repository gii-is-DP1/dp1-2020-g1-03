
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.CitaMascota;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.CitaMascotaRepository;
import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CitaService {

	private CitaRepository citaRepository;
	private CitaMascotaRepository citaMascotaRepository;

	

	@Autowired
	public CitaService(CitaRepository citaRepository,
			CitaMascotaRepository citaMascotaRepository) {
		this.citaRepository = citaRepository;
		this.citaMascotaRepository=citaMascotaRepository;
	}

	@Transactional(readOnly = true)
	public List<Cita> findCitasByVet(Vet vet) throws DataAccessException{
		return citaRepository.findCitasByVet(vet);
	}


	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	public void saveCita(Cita cita) throws DataAccessException {
            	citaRepository.save(cita);                
	}
	
	@Transactional()
	public void saveCitaMascota(CitaMascota citaMascota) throws DataAccessException {//, DuplicatedPetNameException
            	citaMascotaRepository.save(citaMascota);                
	}

	@Transactional(readOnly = true)
	public List<Cita> findAllCitas() {
		return citaRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Cita findCitaById(int citaId) throws DataAccessException{
		return citaRepository.findById(citaId);
	}
	
	@Transactional(readOnly = true)
	public List<CitaMascota> findCitaMascotaByCitaId (int citaId) throws DataAccessException{
		return citaMascotaRepository.findCitaMascotaByCitaId(citaId);
	}
	
	@Transactional(readOnly = true)
	public List<Cita> findCitasByOwner (Owner owner) throws DataAccessException{
		return citaRepository.findCitasByOwner(owner);
	}

	@Transactional(readOnly = true)
	public List<Cita> findCitasSinVet() throws DataAccessException{
		return citaRepository.findCitasSinVet();
	}

}
