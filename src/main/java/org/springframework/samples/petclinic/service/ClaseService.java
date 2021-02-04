package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.model.ApuntarClase;
import org.springframework.samples.petclinic.model.CategoriaClase;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.ApuntarClaseRepository;
import org.springframework.samples.petclinic.repository.ClaseRepository;
import org.springframework.samples.petclinic.service.exceptions.ClasePisadaDelAdiestradorException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaClasesDiasException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaTipoMascotaException;
import org.springframework.samples.petclinic.service.exceptions.LimiteAforoClaseException;
import org.springframework.samples.petclinic.service.exceptions.MacostaYaApuntadaException;
import org.springframework.samples.petclinic.service.exceptions.SolapamientoDeClasesException;
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
	
	@Transactional(rollbackFor={DataAccessException.class,ClasePisadaDelAdiestradorException.class}) 
	public void saveClase(Clase clase) throws DataAccessException, ClasePisadaDelAdiestradorException{
		List<Clase>clases=findClasesAdiestrador(clase.getAdiestrador());
		boolean b=true;
		int i=0;
		if(!clases.isEmpty()) {
			while(b && i<clases.size()) {
				if(clases.get(i).getFechaHoraFin().isAfter(clase.getFechaHoraInicio()) 
						&& clases.get(i).getFechaHoraInicio().isBefore(clase.getFechaHoraFin())
						&& clases.get(i).getId()!=clase.getId()) {
					b=false;		
				}
				i++;
			}
		}if(b==false) {
			throw new ClasePisadaDelAdiestradorException();
		}
		claseRepository.save(clase);                
	}
	
	@Transactional()
	public void deleteClase(Clase clase) throws DataAccessException {
		List<ApuntarClase> clases = findMascotasApuntadasEnClaseByClaseId(clase.getId());
		if(clases.isEmpty() || clases==null) {
			claseRepository.delete(clase);
		}else {
			for(int i=0; i<clases.size(); i++) {
				this.deleteApuntarClase(clases.get(i));
			}
		claseRepository.delete(clase);
		}
	}

	@Transactional(readOnly = true)
	public Collection<Clase> findClaseByAdiestradorId(Integer idAdiestrador) throws DataAccessException{
		return claseRepository.findClaseByAdiestradorId(idAdiestrador);
	}
	
	@Transactional(readOnly = true)
	public List<Clase> findByName(String nombreClase) throws DataAccessException{
		return claseRepository.findByName(nombreClase);
	}
	
	@Transactional(readOnly = true)
	public Collection<Clase> findAllClases() throws DataAccessException{
		return claseRepository.findAll();

	}
	
	@Transactional(readOnly = true)
	public Clase findClaseById(int claseId) throws DataAccessException{
		return claseRepository.findById(claseId);
	}
	
	@Transactional(readOnly = true)
	public List<ApuntarClase> findClasesByPetId(int petId) throws DataAccessException{
		return apuntarClaseRepository.findClasesByPetId(petId);
	}
	
	@Transactional(readOnly = true)
	public List<Clase> findClasesAdiestrador(Adiestrador adie) throws DataAccessException{
		return claseRepository.findClasesAdiestrador(adie);
	}
	
	@Transactional(rollbackFor={DataAccessException.class,DiferenciaTipoMascotaException.class,LimiteAforoClaseException.class,
			DiferenciaClasesDiasException.class,SolapamientoDeClasesException.class,MacostaYaApuntadaException.class,ClasePisadaDelAdiestradorException.class})
	public void apuntarMascota(ApuntarClase apClase) throws DataAccessException, DiferenciaTipoMascotaException, LimiteAforoClaseException, 
	DiferenciaClasesDiasException, SolapamientoDeClasesException, MacostaYaApuntadaException, ClasePisadaDelAdiestradorException{
		Pet pet = apClase.getPet();
		Clase clase = apClase.getClase();
		List<ApuntarClase> clasesApuntadas = this.apuntarClaseRepository.findClasesByPetId(pet.getId());

		 
		Boolean b=true;
		int i=0;
		Boolean apuntada=false;
		if(!clasesApuntadas.isEmpty()) {
			while(b && i<clasesApuntadas.size() && apuntada.equals(false)) {
				if(clasesApuntadas.get(i).getClase().getFechaHoraFin().isAfter(apClase.getClase().getFechaHoraInicio()) 
						&& clasesApuntadas.get(i).getClase().getFechaHoraInicio().isBefore(apClase.getClase().getFechaHoraFin())
						&& clasesApuntadas.get(i).getClase().getId()!=apClase.getClase().getId()) {
					b=false;		
				}
				if(clasesApuntadas.get(i).getClase().getId().equals(apClase.getClase().getId())){
					apuntada=true;
				}
				i++;
			}
		
		if(pet.getType()!=clase.getType()) {

			throw new DiferenciaTipoMascotaException();
		}else if(clase.getNumeroPlazasDisponibles()<=0){
			throw new LimiteAforoClaseException();
		}else if(clasesApuntadas.size()+1>limiteClases && clasesApuntadas.get(clasesApuntadas.size()-1)
				.getClase().numeroDiasEntreDosFechas(clase.getFechaHoraFin())<dias && clasesApuntadas!=null){
			
			throw new DiferenciaClasesDiasException();
		}else if(b==false){
			throw new SolapamientoDeClasesException();
		}else if(apuntada){
			throw new MacostaYaApuntadaException();
		}else {
			apClase.getClase().setNumeroPlazasDisponibles(apClase.getClase().getNumeroPlazasDisponibles()-1);
			saveClase(apClase.getClase());
			this.apuntarClaseRepository.save(apClase);
			
			
			}
		}	
	}
	
	@Transactional(readOnly = true)
	public List<ApuntarClase> findMascotasApuntadasEnClaseByClaseId(int claseId) throws DataAccessException{
		return apuntarClaseRepository.findMascotasApuntadasEnClaseByClaseId(claseId);
	}
	
	@Transactional()
	public void deleteApuntarClase(ApuntarClase apClase) throws DataAccessException{
		 this.apuntarClaseRepository.delete(apClase);
	}
	
	@Transactional(readOnly = true)
	public List<CategoriaClase> findAllCategoriasClase() throws DataAccessException{
		return this.claseRepository.findAllCategoriasClases();
	}
	}