package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Vacuna;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VacunaRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VacunaService {

	private VetRepository vetRepository;
//	
	private PetRepository petRepository;
	
	private VacunaRepository vacunaRepository;

	@Autowired
	public VacunaService(VacunaRepository vacunaRepository,PetRepository petRepository,VetRepository vetRepository) {
		this.vacunaRepository = vacunaRepository;
		this.vetRepository = vetRepository;
		this.petRepository = petRepository;
	}

	@Transactional(readOnly = true)
	public Vacuna findVacunaById(int id) throws DataAccessException {
		return this.vacunaRepository.findById(id);
	}
	/*
	@Transactional(readOnly = true)
	public List<Vacuna> findByOwnerId(int ownerId) throws DataAccessException {
		return this.vacunaRepository.findByOwnerId(ownerId);
	}*/

	public List<Vacuna> findAllVacunas() throws DataAccessException {
		return this.vacunaRepository.findAll();
	}

}
