
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Estado;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.CitaRepository;
import org.springframework.samples.petclinic.service.exceptions.CitaPisadaDelVetException;
import org.springframework.samples.petclinic.service.exceptions.ClasePisadaDelAdiestradorException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.service.exceptions.LimiteDeCitasAlDiaDelVet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CitaService {

	private CitaRepository citaRepository;
	//private CitaMascotaRepository citaMascotaRepository;
	public static final int limiteCitas = 5;


	

	@Autowired
	public CitaService(CitaRepository citaRepository) {
		this.citaRepository = citaRepository;
		//this.citaMascotaRepository=citaMascotaRepository;
	}

	@Transactional(readOnly = true)
	public List<Cita> findCitasByVet(Vet vet) throws DataAccessException{
		return citaRepository.findCitasByVet(vet);
	}


	@Transactional(rollbackFor = DuplicatedPetNameException.class)
	public void saveCita(Cita cita) throws DataAccessException, CitaPisadaDelVetException, LimiteDeCitasAlDiaDelVet {
		List<Cita> citas = findCitasByVet(cita.getVet());
		boolean b=true;
		int i=0;
		if(!citas.isEmpty()) {
			while(b && i<citas.size()) {
				if(citas.get(i).getFechaHora().isEqual(cita.getFechaHora())) {
					b=false;		
				}
				i++;
			}
		}if(b==false) {
			throw new CitaPisadaDelVetException();
		}else if(citas.size()+1 > limiteCitas) {
			throw new LimiteDeCitasAlDiaDelVet();
		}else {
        	this.citaRepository.save(cita); 
		}
               
	}
	
	@Transactional()
	public void deleteCita(Cita cita) throws DataAccessException{
		this.citaRepository.delete(cita);
	}
	
//	
//	@Transactional()
//	public void saveCitaMascota(CitaMascota citaMascota) throws DataAccessException {//, DuplicatedPetNameException
//            	citaMascotaRepository.save(citaMascota);                
//	}
//
	@Transactional(readOnly = true)
	public List<Cita> findAllCitas() {
		return citaRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Cita findCitaById(int citaId) throws DataAccessException{
		return citaRepository.findById(citaId);
	}
	
//	
//	@Transactional(readOnly = true)
//	public List<CitaMascota> findCitaMascotaByCitaId (int citaId) throws DataAccessException{
//		return citaMascotaRepository.findCitaMascotaByCitaId(citaId);
//	}
//	
//	@Transactional(readOnly = true)
//	public List<Cita> findCitasByOwner (Owner owner) throws DataAccessException{
//		return citaRepository.findCitasByOwner(owner);
//	}

	@Transactional(readOnly = true)
	public List<Cita> findCitasSinVet() throws DataAccessException{
		return citaRepository.findCitasSinVet();
	}

//	@Transactional(readOnly = true)
//	public List<Cita> findCitasByPet(Pet pet) {
//		return citaRepository.findCitasByPet(pet);
//	}

}
