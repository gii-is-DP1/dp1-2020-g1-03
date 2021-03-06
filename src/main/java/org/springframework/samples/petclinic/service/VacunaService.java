package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.TipoEnfermedad;
import org.springframework.samples.petclinic.model.Vacuna;
import org.springframework.samples.petclinic.repository.VacunaRepository;
import org.springframework.samples.petclinic.service.exceptions.DistanciaEntreDiasException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VacunaService {
	
	private VacunaRepository vacunaRepository;
	
	public static final Integer DÍAS_ENTRE_VACUNAS=7;

	@Autowired
	public VacunaService(VacunaRepository vacunaRepository) {
		this.vacunaRepository = vacunaRepository;
	}

	@Transactional(readOnly = true)
	public Vacuna findVacunaById(int id) throws DataAccessException {
		return this.vacunaRepository.findById(id);
	}
	
	@Transactional(rollbackFor={DistanciaEntreDiasException.class})
	public void saveVacuna(Vacuna vacuna, int id) throws DataAccessException, DistanciaEntreDiasException {
		List<Vacuna> ultVacunas=vacunaRepository.findVacunasByPetId(id);
		if(ultVacunas.isEmpty() || ultVacunas==null) {
		vacunaRepository.save(vacuna);
		}else {
			Vacuna ultVacuna=ultVacunas.get(ultVacunas.size()-1);
			if(vacuna.getFecha().isBefore(ultVacuna.getFecha()) || ultVacuna.numeroDiasEntreDosFechas(vacuna.getFecha())<DÍAS_ENTRE_VACUNAS) {
				throw new DistanciaEntreDiasException();
			}else {
				vacunaRepository.save(vacuna);
			}
			
		}               
	}
	
	@Transactional(readOnly = true)
	public List<Vacuna> findAllVacunas() throws DataAccessException {
		return this.vacunaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Collection<Vacuna> findAllVacunasByOwnerId(int ownerId) throws DataAccessException{
		return vacunaRepository.findVacunasByOwnerId(ownerId);
	}
	
	@Transactional(readOnly = true)
	public Collection<TipoEnfermedad> findTipoEnfermedades() throws DataAccessException {
		return vacunaRepository.findTipoEnfermedades();
	}
	
	@Transactional(readOnly = true)
	public Collection<Pet> findMascotaByEspecie(String especie) throws DataAccessException {
		return vacunaRepository.findMascotaByEspecie(especie);
	}

}
