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
	private UserService userService;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public AdiestradorService(AdiestradorRepository adiestradorRepository) {
		this.adiestradorRepository = adiestradorRepository;
	}	

	@Transactional(readOnly = true)
	public Adiestrador findAdiestradorById(int id) throws DataAccessException {
		return adiestradorRepository.findById(id);
	}
	@Transactional(readOnly = true)
	public int findAdiestradorIdByUsername(String user) throws DataAccessException {
		return adiestradorRepository.findAdiestradorIdByUsername(user);
	}
	
	@Transactional(readOnly = true)
	public Adiestrador findAdiestradorByUsername(String user) throws DataAccessException {
		return adiestradorRepository.findAdiestradorByUsername(user);
	}

	@Transactional
	public void saveAdiestrador(Adiestrador adiestrador) throws DataAccessException {
		//creating owner
		adiestradorRepository.save(adiestrador);		
		//creating user
	}		
	
	public Collection<Adiestrador> findAllAdiestradores() {
		return adiestradorRepository.findAll();
	}
	
	public Collection<String> findNameAndLastnameAdiestrador() throws DataAccessException{
		return adiestradorRepository.findNameAndLastnameAdiestrador();
	}
}
