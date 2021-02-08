package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.CompeticionPet;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.CompeticionPetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class CompeticionPetService {

	private CompeticionPetRepository competicionPetRepository;
	
	@Autowired
	public CompeticionPetService(CompeticionPetRepository competicionPetRepository) {
		this.competicionPetRepository = competicionPetRepository;
	}
	
	@Transactional(readOnly = true)
	public List<CompeticionPet> findCompeticionByPetId(int petId) throws DataAccessException{
		return competicionPetRepository.findCompeticionByPetId(petId);
	}
	
	@Transactional()
	public void saveCompeticionPet(CompeticionPet competicionPet) throws DataAccessException{
		this.competicionPetRepository.save(competicionPet);
	}
	
	@Transactional(readOnly = true)
	public Collection<CompeticionPet> findCompeticionPetByCompeticionId(int competicionId){
		return this.competicionPetRepository.findCompeticionPetByCompeticionId(competicionId);
	}
	
	@Transactional(readOnly = true)
	public Pet findPetByCompeticionPetId(int competicionPetId) {
		return this.competicionPetRepository.findPetByCompeticionPetId(competicionPetId);
	}
}
