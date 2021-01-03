package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.model.Competicion;
import org.springframework.samples.petclinic.model.CompeticionPet;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.repository.ComentarioRepository;
import org.springframework.samples.petclinic.repository.CompeticionPetRepository;
import org.springframework.samples.petclinic.repository.CompeticionRepository;
import org.springframework.samples.petclinic.repository.OwnerRepository;
import org.springframework.samples.petclinic.repository.VetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class CompeticionService {
	
	private CompeticionRepository competicionRepository;
	private CompeticionPetRepository competicionPetRepository;
//	public static final int limiteClases=3;
//	public static final int dias=7;

	@Autowired
	public CompeticionService(CompeticionRepository competicionRepository, CompeticionPetRepository competicionPetRepository) {
		this.competicionRepository = competicionRepository;
		this.competicionPetRepository=competicionPetRepository;
		
	}
	@Transactional()
	public void saveCompeticion(Competicion competicion) throws DataAccessException {
		competicionRepository.save(competicion);                
	}
	
	@Transactional()
	public void deleteCompeticion(Competicion competicion) throws DataAccessException {
		competicionRepository.delete(competicion);
	}


//	public Collection<Competicion> findCompeticionByOwnerId(Integer idOwner) throws DataAccessException{
//		return competicionRepository.findCompeticionByOwnerId(idOwner);
//	}
	public List<Competicion> findByName(String nombreCompeticion) throws DataAccessException{
		return competicionRepository.findByName(nombreCompeticion);
	}
	public Collection<Competicion> findAllCompeticiones() throws DataAccessException{

		return competicionRepository.findAll();

	}
	
	public Competicion findCompeticionById(int competicionId) throws DataAccessException{
		return competicionRepository.findById(competicionId);
	}
	
	public List<CompeticionPet> findCompeticionByPetId(int petId) throws DataAccessException{
		return competicionPetRepository.findCompeticionByPetId(petId);
	}
	
	public List<Competicion> findCompeticionesBySecretario(Owner owner) throws DataAccessException{
		return competicionRepository.findCompeticionesByOwner(owner);
	}
	
//	@Transactional()
//	public void escogerMascota(CompeticionPet compPet) throws DataAccessException, DiferenciaTipoMascotaException, LimiteAforoClaseException, DiferenciaClasesDiasException{
//		Pet pet = compPet.getPet();
//		Clase clase = compPet.getClase();
//		List<ApuntarClase> clasesApuntadas = this.apuntarClaseRepository.findClasesByPetId(pet.getId());
//		if(pet.getType()!=clase.getType()) {
//			throw new DiferenciaTipoMascotaException();
//		}else if(clase.getNumeroPlazasDisponibles()<=0){
//			throw new LimiteAforoClaseException();
//		}else if(clasesApuntadas.size()>limiteClases && clasesApuntadas.get(clasesApuntadas.size()-1)
//				.getClase().numeroDiasEntreDosFechas(clase.getFechaHoraFin())>dias && clasesApuntadas!=null){
//			throw new DiferenciaClasesDiasException();
//		}else {
//			apuntarClaseRepository.save(apClase);
//		}
//			
//		}
	}