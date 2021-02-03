package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Competicion;
import org.springframework.samples.petclinic.repository.CompeticionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class CompeticionService {
	
	private CompeticionRepository competicionRepository;

	@Autowired
	public CompeticionService(CompeticionRepository competicionRepository) {
		this.competicionRepository = competicionRepository;		
	}
	@Transactional()
	public void saveCompeticion(Competicion competicion) throws DataAccessException {
		competicionRepository.save(competicion);                
	}
	
	@Transactional()
	public void deleteCompeticion(Competicion competicion) throws DataAccessException {
		competicionRepository.delete(competicion);
	}

	@Transactional(readOnly = true)
	public List<Competicion> findByName(String nombreCompeticion) throws DataAccessException{
		return competicionRepository.findByName(nombreCompeticion);
	}
	
	@Transactional(readOnly = true)
	public Collection<Competicion> findAllCompeticiones() throws DataAccessException{
		return competicionRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Competicion findCompeticionById(int competicionId) throws DataAccessException{
		return competicionRepository.findById(competicionId);
	}
	
	
	}