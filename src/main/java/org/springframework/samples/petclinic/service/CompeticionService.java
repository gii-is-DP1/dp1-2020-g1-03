package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
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
import org.springframework.samples.petclinic.service.exceptions.ComenzarAntesAcabarException;
import org.springframework.samples.petclinic.service.exceptions.MascotaYaApuntadaCompeticionException;
import org.springframework.samples.petclinic.service.exceptions.SolapamientoDeCompeticionesException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class CompeticionService {
	
	private CompeticionRepository competicionRepository;
	private CompeticionPetRepository competicionPetRepository;
//	public static final int limiteClases=3;
//	public static final int dias=7;

	@Autowired
	public CompeticionService(CompeticionRepository competicionRepository) {
		this.competicionRepository = competicionRepository;	
		this.competicionPetRepository = competicionPetRepository;
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
	
	public List<Competicion> findCompeticionesBySecretario(Owner owner) throws DataAccessException{
		return competicionRepository.findCompeticionesByOwner(owner);
	}
	
	
//	@Transactional()
//	public void escogerMascota(CompeticionPet compPet) throws DataAccessException, ComenzarAntesAcabarException{
//		Pet pet = compPet.getPet();
//		Competicion competicion = compPet.getCompeticion();
//		List<CompeticionPet> clasesApuntadas = this.competicionPetRepository.findCompeticionByPetId(pet.getId());
//		if(compPet.getCompeticion().getFechaHoraInicio().isBefore(LocalDate.now())) { 
//			throw new ComenzarAntesAcabarException();
//		}else {
//			competicionPetRepository.save(compPet);
//		}
//			
//		}
	
	@Transactional()
	public void escogerMascota(CompeticionPet compPet, List<CompeticionPet> competicionesApuntadas) throws DataAccessException,
	SolapamientoDeCompeticionesException, MascotaYaApuntadaCompeticionException{
		//System.out.println("Service");
		Pet pet = compPet.getPet();
		System.out.println("Pet: " +pet.getId());
		Competicion competicion = compPet.getCompeticion();
		//List<CompeticionPet> competicionesApuntadas = this.competicionPetService.findCompeticionByPetId(competicionPet.getPet().getId());
		System.out.println("Comp apuntadas: "+ competicionesApuntadas);
		//List<CompeticionPet> competicionesApuntadas = this.competicionPetRepository.findCompeticionByPetId(compPet.getPet().getId());
		
		Boolean b=true;
		int i=0;
		Boolean apuntada=false;
		if (!competicionesApuntadas.isEmpty()) {
			while (b && i < competicionesApuntadas.size() && apuntada.equals(false)) {
				if (competicionesApuntadas.get(i).getCompeticion().getFechaHoraFin()
						.isAfter(compPet.getCompeticion().getFechaHoraInicio())
						|| competicionesApuntadas.get(i).getCompeticion().getFechaHoraInicio()
						.isBefore(compPet.getCompeticion().getFechaHoraFin())
					&& (competicionesApuntadas.get(i).getCompeticion().getId() != compPet.getCompeticion()
					.getId())) {
					b = false;
				}
				if (competicionesApuntadas.get(i).getCompeticion().getId()
						.equals(compPet.getCompeticion().getId())) {
					apuntada = true;
				}
				i++;
			}
		}
		System.out.println("Importa");
		if (b == false) {
			throw new SolapamientoDeCompeticionesException();
		} else if (apuntada) {
			throw new MascotaYaApuntadaCompeticionException();
		} else {
			System.out.println("Pet: "+ compPet.getPet()+" Competición: "+ compPet.getCompeticion().getNombre());
			competicionPetRepository.save(compPet);
		}

	}
	
}