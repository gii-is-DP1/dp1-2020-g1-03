package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.ApuntarClase;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.AdiestradorRepository;
import org.springframework.samples.petclinic.repository.ApuntarClaseRepository;
import org.springframework.samples.petclinic.repository.ClaseRepository;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaClasesDiasException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaTipoMascotaException;
import org.springframework.samples.petclinic.service.exceptions.LimiteAforoClaseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class ClaseService {
	private ClaseRepository claseRepository;
	private ApuntarClaseRepository apuntarClaseRepository;
	public static final int limiteClases=3;
	public static final int dias=7;

	@Autowired
	public ClaseService(ClaseRepository claseRepository, ApuntarClaseRepository apuntarClaseRepository) {
		this.claseRepository = claseRepository;
		this.apuntarClaseRepository=apuntarClaseRepository;
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
	
	public List<ApuntarClase> findClasesByPetId(int petId) throws DataAccessException{
		return apuntarClaseRepository.findClasesByPetId(petId);
	}
	
	@Transactional()
	public void escogerMascota(ApuntarClase apClase) throws DataAccessException, DiferenciaTipoMascotaException, LimiteAforoClaseException, DiferenciaClasesDiasException{
		Pet pet = apClase.getPet();
		Clase clase = apClase.getClase();
		List<ApuntarClase> clasesApuntadas = this.apuntarClaseRepository.findClasesByPetId(pet.getId());
		if(pet.getType()!=clase.getType()) {
			throw new DiferenciaTipoMascotaException();
		}else if(clase.getNumeroPlazasDisponibles()<=0){
			throw new LimiteAforoClaseException();
		}else if(clasesApuntadas.size()>limiteClases && clasesApuntadas.get(clasesApuntadas.size()-1)
				.getClase().numeroDiasEntreDosFechas(clase.getFechaHoraFin())>dias && clasesApuntadas!=null){
			throw new DiferenciaClasesDiasException();
		}else {
			apuntarClaseRepository.save(apClase);
		}
			
		}
	}
	
