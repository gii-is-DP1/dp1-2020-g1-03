
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Gasto;
import org.springframework.samples.petclinic.model.Ingreso;
import org.springframework.samples.petclinic.repository.EconomistaRepository;
import org.springframework.samples.petclinic.repository.IngresoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class IngresoService {
	
	private IngresoRepository ingresoRepository;
	

	@Autowired
	public IngresoService(IngresoRepository ingresoRepository) {
		this.ingresoRepository = ingresoRepository;
	}

	
	@Transactional(readOnly = true)
	public Ingreso findIngresoById(int id) throws DataAccessException {
		return ingresoRepository.findById(id);
	}

	@Transactional()
	public void saveIngreso(Ingreso ingreso) throws DataAccessException {
		ingresoRepository.save(ingreso);                
	}


	public List<Ingreso> findAllIngresos() {
		return ingresoRepository.findAll();
	}
	

}
