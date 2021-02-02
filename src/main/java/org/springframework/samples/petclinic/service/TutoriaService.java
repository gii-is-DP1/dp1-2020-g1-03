package org.springframework.samples.petclinic.service;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Tutoria;
import org.springframework.samples.petclinic.repository.TutoriaRepository;
import org.springframework.samples.petclinic.service.exceptions.MismaHoraTutoriaException;
import org.springframework.samples.petclinic.service.exceptions.MismaHoraTutoriaPetException;
import org.springframework.samples.petclinic.service.exceptions.NumeroTutoriasMaximoPorDiaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TutoriaService {
	
	private TutoriaRepository tutoriaRepository;
	public final static int tutoriasMaximoPorDia=3;
	
	@Autowired
	public TutoriaService(TutoriaRepository tutoriaRepository) {
		this.tutoriaRepository = tutoriaRepository;
	}
	
	
	public Tutoria findTutoriaById(int tutoriaId) throws DataAccessException {
		return tutoriaRepository.findTutoriaById(tutoriaId);
	}
	
	
	public List<Tutoria> findAllTutorias() {
		return tutoriaRepository.findAll();
	}
	
	@Transactional()
	public void saveTutoria(Tutoria tutoria, boolean b) throws DataAccessException, MismaHoraTutoriaException, NumeroTutoriasMaximoPorDiaException, MismaHoraTutoriaPetException{
		int tutoriasAdiestrador = tutoriaRepository.findTutoriasByAdiestradorId(tutoria.getFechaHora(),tutoria.getAdiestrador().getId());
		int numeroTutoriasPorDia = tutoriaRepository.numeroTutoriasEnUnDiaAdiestrador(tutoria.getFechaHora().getDayOfMonth(),
				tutoria.getFechaHora().getMonthValue(),tutoria.getFechaHora().getYear(), tutoria.getAdiestrador().getId());
		int tutoriasPet = tutoriaRepository.numeroTutoriasEnUnDiaPet(tutoria.getFechaHora(), tutoria.getPet().getId());
		if((tutoriasAdiestrador>=1&&b==false) || (b==true&&tutoriasAdiestrador>1)) {
			throw new MismaHoraTutoriaException();
		}else if(tutoriasMaximoPorDia<=numeroTutoriasPorDia){
			throw new NumeroTutoriasMaximoPorDiaException();
		}else if(tutoriasPet>=1&&b==false) {
			throw new MismaHoraTutoriaPetException();
		}else {
			this.tutoriaRepository.save(tutoria);
		}
	}

	public Collection<Pet> findMascotaByName(String name) throws DataAccessException{
		return tutoriaRepository.findMascotaByName(name);
	}
}
