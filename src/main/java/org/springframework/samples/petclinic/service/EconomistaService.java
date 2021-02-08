
package org.springframework.samples.petclinic.service;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Economista;
import org.springframework.samples.petclinic.repository.EconomistaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EconomistaService {

	private EconomistaRepository economistaRepository;


	@Autowired
	public EconomistaService(EconomistaRepository economistaRepository) {
		this.economistaRepository = economistaRepository;
	}		

	@Transactional(readOnly = true)	
	public Collection<Economista> findEconomistas() throws DataAccessException {
		return economistaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Economista findEconomistaById(int economistaId) {
		return economistaRepository.findById(economistaId);
	}

	@Transactional(readOnly = true)
	public int findEconomistaIdByUsername(String user) throws DataAccessException {
		return this.economistaRepository.findEconIdByUsername(user);
	}	

}
