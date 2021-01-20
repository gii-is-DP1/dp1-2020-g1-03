package org.springframework.samples.petclinic.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Tutoria;
import org.springframework.samples.petclinic.repository.TutoriaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class TutoriaService {
	
	private TutoriaRepository tutoriaRepository;
	
	
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
	

}
