package org.springframework.samples.petclinic.service;

import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.repository.AdiestradorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class AdiestradorService {
	private AdiestradorRepository adiestradorRepository;	
	
	@Autowired
	public AdiestradorService(AdiestradorRepository adiestradorRepository) {
		this.adiestradorRepository = adiestradorRepository;
	}	

	@Transactional(readOnly = true)
	public int findAdiestradorIdByUsername(String user) throws DataAccessException {
		return adiestradorRepository.findAdiestradorIdByUsername(user);
	}

	@Transactional
	public void saveAdiestrador(Adiestrador adiestrador) throws DataAccessException {
		adiestradorRepository.save(adiestrador);			
	}		
	
	@Transactional(readOnly = true)
	public Collection<Adiestrador> findAllAdiestradores() {
		return adiestradorRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Collection<String> findNameAndLastnameAdiestrador() throws DataAccessException{
		return adiestradorRepository.findNameAndLastnameAdiestrador();
	}
}
