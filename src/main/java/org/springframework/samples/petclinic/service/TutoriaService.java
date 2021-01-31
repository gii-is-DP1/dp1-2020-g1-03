package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Tutoria;
import org.springframework.samples.petclinic.repository.TutoriaRepository;
import org.springframework.samples.petclinic.service.exceptions.MismaHoraTutoriaException;
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
	
	
	@Transactional()
	public Tutoria findTutoriaById(int id) throws DataAccessException {
		return tutoriaRepository.findById(id);
	}
	
	
	public List<Tutoria> findAllTutorias() {
		return tutoriaRepository.findAll();
	}
	
	@Transactional()
	public void saveTutoria(Tutoria tutoria, boolean b) throws DataAccessException, MismaHoraTutoriaException, NumeroTutoriasMaximoPorDiaException{
		int tutorias = tutoriaRepository.findTutoriasByAdiestradorId(tutoria.getFechaHora());
		int numeroTutoriasPorDia = tutoriaRepository.numeroTutoriasEnUnDia(tutoria.getFechaHora().toLocalDate(), tutoria.getAdiestrador().getId());
		if(tutorias>=1&&b==false) {
			throw new MismaHoraTutoriaException();
		}else if(tutoriasMaximoPorDia<numeroTutoriasPorDia){
			throw new NumeroTutoriasMaximoPorDiaException();
		}else {
			this.tutoriaRepository.save(tutoria);
		}
	}

	public Collection<Pet> findMascotaByName(String name) throws DataAccessException{
		return tutoriaRepository.findMascotaByName(name);
	}
}
