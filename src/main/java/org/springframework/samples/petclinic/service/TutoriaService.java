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
	
	@Transactional(rollbackFor= {MismaHoraTutoriaException.class,NumeroTutoriasMaximoPorDiaException.class,MismaHoraTutoriaPetException.class})
	public void saveTutoria(Tutoria tutoria, boolean estaEditando) throws DataAccessException, MismaHoraTutoriaException, NumeroTutoriasMaximoPorDiaException, MismaHoraTutoriaPetException{
		List<Tutoria> tutorias = tutoriaRepository.findAllTutoriasByAdiestradorId(tutoria.getFechaHora(),tutoria.getAdiestrador().getId());
		List<Tutoria> todasTutoriasPet = tutoriaRepository.findAllTutoriasByPetId(tutoria.getFechaHora(),tutoria.getPet().getId());
		int tutoriasAdiestrador = tutorias.size();
		int numeroTutoriasPorDia = tutoriaRepository.numeroTutoriasEnUnDiaAdiestrador(tutoria.getFechaHora().getDayOfMonth(),
				tutoria.getFechaHora().getMonthValue(),tutoria.getFechaHora().getYear(), tutoria.getAdiestrador().getId());
		int tutoriasPet = todasTutoriasPet.size();
		if((tutoriasAdiestrador>=1&&estaEditando==false)) {
			throw new MismaHoraTutoriaException();
		}else if(tutorias.size()!=0&&!tutorias.get(0).getId().equals(tutoria.getId())){
			if((estaEditando==true&&tutorias.get(0).getFechaHora().equals(tutoria.getFechaHora()))){
				throw new MismaHoraTutoriaException();
			}
		}else if(tutoriasMaximoPorDia<=numeroTutoriasPorDia){
			throw new NumeroTutoriasMaximoPorDiaException();
		}else if(tutoriasPet>=1&&estaEditando==false) {
			throw new MismaHoraTutoriaPetException();
			
		}else if(todasTutoriasPet.size()!=0&&!todasTutoriasPet.get(0).getId().equals(tutoria.getId())){
			if((estaEditando==true&&todasTutoriasPet.get(0).getFechaHora().equals(tutoria.getFechaHora()))){
				throw new MismaHoraTutoriaPetException();
			}
		}else {
			this.tutoriaRepository.save(tutoria);
		}
	}


	public Collection<Pet> findMascotaByName(String name) throws DataAccessException{
		return tutoriaRepository.findMascotaByName(name);
	}
}
