package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.repository.AdiestradorRepository;
import org.springframework.samples.petclinic.repository.ClaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ClaseService {
	private ClaseRepository claseRepository;

	@Autowired
	public ClaseService(ClaseRepository claseRepository) {
		this.claseRepository = claseRepository;
	}
	@Transactional()
	public void saveClase(Clase clase) throws DataAccessException {
		claseRepository.save(clase);                
	}
	
	@Transactional()
	public void deleteClase(Clase clase) throws DataAccessException {
		claseRepository.delete(clase);
	}


	public Collection<Clase> findClaseByAdiestradorId(Integer idAdiestrador) throws DataAccessException{
		return claseRepository.findClaseByAdiestradorId(idAdiestrador);
	}
	public List<Clase> findByName(String nombreClase) throws DataAccessException{
		return claseRepository.findByName(nombreClase);
	}
	public Collection<Clase> findAllClases() throws DataAccessException{

		return claseRepository.findAll();

	}
	
	public Clase findClaseById(int claseId) throws DataAccessException{
		return claseRepository.findById(claseId);
	}
	
}