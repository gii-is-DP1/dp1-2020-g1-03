/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.CitaMascota;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.CitaMascotaRepository;
import org.springframework.samples.petclinic.repository.CitaRepository;
//import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.samples.petclinic.repository.VisitRepository;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * Mostly used as a facade for all Petclinic controllers Also a placeholder
 * for @Transactional and @Cacheable annotations
 *
 * @author Michael Isvy
 */
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

	
	public List<Cita> findCitasByVet(Vet vet) throws DataAccessException{
		return citaRepository.findCitasByVet(vet);
	}

	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	public void saveCita(Cita cita) throws DataAccessException {//, DuplicatedPetNameException
            	citaRepository.save(cita);                
	}

	public List<Cita> findAllCitas() {
		// TODO Auto-generated method stub
		return citaRepository.findAll();
	}
	
	public Cita findCitaById(int citaId) throws DataAccessException{
		return citaRepository.findById(citaId);
	}
	
	public List<CitaMascota> findCitaMascotaByCitaId (int citaId) throws DataAccessException{
		return citaMascotaRepository.findCitaMascotaByCitaId(citaId);
	}
	
	public List<Cita> findCitasByOwnerId (int ownerId) throws DataAccessException{
		return citaMascotaRepository.findCitasByOwnerId(ownerId);
	}

	public List<Cita> findCitasSinVet() throws DataAccessException{
		return citaRepository.findCitasSinVet();
	}

}
