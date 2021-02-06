package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Competicion;
import org.springframework.samples.petclinic.model.CompeticionPet;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.CompeticionPetRepository;
import org.springframework.samples.petclinic.repository.CompeticionRepository;
import org.springframework.samples.petclinic.service.exceptions.MascotaYaApuntadaCompeticionException;
import org.springframework.samples.petclinic.service.exceptions.SolapamientoDeCompeticionesException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class CompeticionService {
	
	private CompeticionRepository competicionRepository;
	private CompeticionPetRepository competicionPetRepository;


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

	@Transactional(readOnly = true)
	public List<Competicion> findByName(String nombreCompeticion) throws DataAccessException{
		return competicionRepository.findByName(nombreCompeticion);
	}
	
	@Transactional(readOnly = true)
	public Collection<Competicion> findAllCompeticiones() throws DataAccessException{
		return competicionRepository.findAll();
	}
	
	@Transactional(readOnly = true)
	public Competicion findCompeticionById(int competicionId) throws DataAccessException{
		return competicionRepository.findById(competicionId);
	}
	
	

	
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
//			competicionRepository.save(competicion);
//			competicionPetRepository.save(compPet);
		}

	}
	
}

