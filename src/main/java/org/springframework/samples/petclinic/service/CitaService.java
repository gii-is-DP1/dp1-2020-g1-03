
package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.samples.petclinic.service.exceptions.CitaPisadaDelOwnerException;
import org.springframework.samples.petclinic.service.exceptions.CitaPisadaDelVetException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.LimiteDeCitasAlDiaDelVet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CitaService {

	private CitaRepository citaRepository;
	public static final int limiteCitas = 5;

	@Autowired
	public CitaService(CitaRepository citaRepository) {
		this.citaRepository = citaRepository;
	}

	@Transactional(readOnly = true)
	public List<Cita> findCitasByVet(Vet vet) throws DataAccessException {
		return citaRepository.findCitasByVet(vet);
	}

	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	public void saveCita(Cita cita) throws DataAccessException, CitaPisadaDelVetException, LimiteDeCitasAlDiaDelVet, CitaPisadaDelOwnerException {
		List<Cita> citas = findCitasByVet(cita.getVet());
		List<Cita> allCitas = findAllCitas();
		boolean b = true;
		boolean c = true;
		for (int j = 0; j < allCitas.size(); j++) {
			if (allCitas.get(j).getPets().get(0).getOwner().equals(cita.getPets().get(0).getOwner())&&!allCitas.get(j).getId().equals(cita.getId())
					&& allCitas.get(j).getFechaHora().isEqual(cita.getFechaHora())) {
				c = false;
				break;
			}
		}
		int i = 0;
		if (!citas.isEmpty()) {
			while (b && i < citas.size()) {
				if (citas.get(i).getFechaHora().isEqual(cita.getFechaHora()) && !citas.get(i).getId().equals(cita.getId())) {
					b = false;
					break;
				}
				i++;
			}
		}
		if (b == false) {
			throw new CitaPisadaDelVetException();
		}else if(!c) {
			throw new CitaPisadaDelOwnerException();
		}
		else if (citas.size() + 1 > limiteCitas) {
			throw new LimiteDeCitasAlDiaDelVet();
		} else {
			this.citaRepository.save(cita);
		}

	}

	@Transactional()
	public void deleteCita(Cita cita) throws DataAccessException {
		this.citaRepository.delete(cita);
	}

	@Transactional(readOnly = true)
	public List<Cita> findAllCitas() {
		return citaRepository.findAll();
	}

	@Transactional(readOnly = true)
	public Cita findCitaById(int citaId) throws DataAccessException {
		return citaRepository.findById(citaId);
	}


	@Transactional(readOnly = true)
	public List<Cita> findCitasSinVet() throws DataAccessException {
		return citaRepository.findCitasSinVet();
	}


}
