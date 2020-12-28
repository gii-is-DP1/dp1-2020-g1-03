package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.repository.SecretarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SecretarioService {
	private SecretarioRepository secretarioRepository;
	
	@Autowired
	private AuthoritiesService authoritiesService;

	@Autowired
	public SecretarioService(SecretarioRepository secretarioRepository) {
		this.secretarioRepository = secretarioRepository;
	}
	@Transactional(readOnly = true)
	public Secretario findSecretarioByUsername(String user) throws DataAccessException {
		return secretarioRepository.findSecretarioByUsername(user);
	}
	

	@Transactional
	public void saveSecretario(Secretario secretario) throws DataAccessException {
		secretarioRepository.save(secretario);		
	}	
}