
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Gasto;
import org.springframework.samples.petclinic.repository.GastoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GastoService {

	private GastoRepository gastoRepository;
	
	

	@Autowired
	public GastoService(GastoRepository gastoRepository) {
		this.gastoRepository = gastoRepository;
	}

	
	@Transactional(readOnly = true)
	public Gasto findGastoById(int id) throws DataAccessException {
		return gastoRepository.findById(id);
	}

	@Transactional()
	public void saveGasto(Gasto gasto) throws DataAccessException {
		gastoRepository.save(gasto);                
	}

	@Transactional(readOnly = true)
	public List<Gasto> findAllGastosS() {
		return gastoRepository.findAll();
	}

}
