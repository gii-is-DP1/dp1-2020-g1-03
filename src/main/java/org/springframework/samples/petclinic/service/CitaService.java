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
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.repository.CitaRepository;
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

//	private CitaRepository citaRepository;
//	
//	//private VisitRepository visitRepository;
//	
//
//	@Autowired
//	public CitaService(PetRepository petRepository,
//			VisitRepository visitRepository) {
//		this.citaRepository = citaRepository;
//	}
//
////	@Transactional(readOnly = true)
////	public Collection<PetType> findPetTypes() throws DataAccessException {
////		return citaRepository.findPetTypes();
////	}
//	
//
//	@Transactional(readOnly = true)
//	public Cita findPetById(int id) throws DataAccessException {
//		return citaRepository.findById(id);
//	}
//
//	@Transactional(rollbackFor = DuplicatedPetNameException.class)
//	public void saveCita(Cita cita) throws DataAccessException {//, DuplicatedPetNameException
////		Cita otherCita=cita.getOwner().getPetwithIdDifferent(cita.getTitulo(), cita.getId());
////            if (StringUtils.hasLength(cita.getTitulo()) &&  (otherCita!= null && otherCita.getId()!=cita.getId())) {            	
////            	throw new DuplicatedPetNameException();
////            }else
//            	citaRepository.save(cita);                
//	}
//
//	public List<Cita> findAllCitas() {
//		// TODO Auto-generated method stub
//		return citaRepository.findAllCitas();
//	}
//
//
////	public Collection<Visit> findVisitsByPetId(int petId) {
////		return visitRepository.findByPetId(petId);
////	}

}
