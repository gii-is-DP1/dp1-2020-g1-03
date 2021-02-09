package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Competicion;
import org.springframework.samples.petclinic.model.CompeticionPet;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.repository.CompeticionRepository;
import org.springframework.samples.petclinic.service.exceptions.MascotaYaApuntadaCompeticionException;
import org.springframework.samples.petclinic.service.exceptions.SolapamientoDeCompeticionesException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class CompeticionService {
	
	private CompeticionRepository competicionRepository;
	@Autowired
	public CompeticionService(CompeticionRepository competicionRepository) {
		this.competicionRepository = competicionRepository;
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
	
	

	
	@Transactional(rollbackFor= {SolapamientoDeCompeticionesException.class,MascotaYaApuntadaCompeticionException.class})
	public void escogerMascota(CompeticionPet compPet, List<CompeticionPet> competicionesApuntadas) throws DataAccessException,
	SolapamientoDeCompeticionesException, MascotaYaApuntadaCompeticionException{
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
		if (b == false) {
			throw new SolapamientoDeCompeticionesException();
		} else if (apuntada) {
			throw new MascotaYaApuntadaCompeticionException();
		} 
	}
	
}

