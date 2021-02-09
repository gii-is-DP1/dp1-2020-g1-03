package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Tutoria;
import org.springframework.samples.petclinic.service.exceptions.MismaHoraTutoriaException;
import org.springframework.samples.petclinic.service.exceptions.MismaHoraTutoriaPetException;
import org.springframework.samples.petclinic.service.exceptions.NumeroTutoriasMaximoPorDiaException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TutoriaServiceTests {
	@Autowired
	private TutoriaService tutoriaService;
	@Autowired
	private AdiestradorService adiestradorService;
	@Autowired
	private PetService petService;
	
	@Test
	void shouldFindTutoriaById() {
		Tutoria tutoria1 = this.tutoriaService.findTutoriaById(1);
		assertThat(tutoria1.getTitulo().equals("TutoriaPrueba"));
	}
	
	@Test
	void shouldFindAllTutorias() {
		List<Tutoria> tutorias = this.tutoriaService.findAllTutorias();
		assertThat(tutorias.size()==1);
	}
	
	@Test
	void shouldFindTutoriaByOwnerId() {
		Collection<Tutoria> tutorias = this.tutoriaService.findTutoriaByOwnerId(1);
		assertThat(tutorias.size()==1);
	}
	
	@Test
	@Transactional
	public void shouldSaveTutoria() {
		List<Tutoria> tutorias = this.tutoriaService.findAllTutorias();
		int found = tutorias.size();
		
		Tutoria tutoria = new Tutoria();
		tutoria.setTitulo("Titulo");
		tutoria.setRazon("Razon");
		tutoria.setPet(this.petService.findPetById(1));
		tutoria.setId(14);
		tutoria.setFechaHora(LocalDateTime.of(2021, 03, 04, 13, 00));
		tutoria.setAdiestrador(this.adiestradorService.findAdiestradorById(1));
        
		try {
            this.tutoriaService.saveTutoria(tutoria, false);
        } catch (MismaHoraTutoriaException ex1) {
            Logger.getLogger(TutoriaServiceTests.class.getName()).log(Level.SEVERE, null, ex1);
        }catch (NumeroTutoriasMaximoPorDiaException ex2) {
            Logger.getLogger(TutoriaServiceTests.class.getName()).log(Level.SEVERE, null, ex2);
        }catch (MismaHoraTutoriaPetException ex3) {
            Logger.getLogger(TutoriaServiceTests.class.getName()).log(Level.SEVERE, null, ex3);
        }

		tutorias = this.tutoriaService.findAllTutorias();
		assertThat(tutorias.size()).isEqualTo(found + 1);
	}
	
	@Test
	@Transactional
	public void shouldThrowExceptionMismaHoraTutoriaException() {
		Tutoria tutoria = new Tutoria();
		tutoria.setTitulo("Titulo");
		tutoria.setRazon("Razon");
		tutoria.setPet(this.petService.findPetById(1));
		tutoria.setId(14);
		tutoria.setFechaHora(LocalDateTime.of(2021, 03, 04, 13, 00));
		tutoria.setAdiestrador(this.adiestradorService.findAdiestradorById(1));
        
		try {
            this.tutoriaService.saveTutoria(tutoria, false);
        } catch (MismaHoraTutoriaException ex1) {
            ex1.printStackTrace();
        }catch (NumeroTutoriasMaximoPorDiaException ex2) {
        	ex2.printStackTrace();
        }catch (MismaHoraTutoriaPetException ex3) {
        	ex3.printStackTrace();
        }

		Tutoria tutoria2 = new Tutoria();
		tutoria2.setTitulo("Titulo");
		tutoria2.setRazon("Razon");
		tutoria2.setPet(this.petService.findPetById(2));
		tutoria2.setId(15);
		tutoria2.setFechaHora(LocalDateTime.of(2021, 03, 04, 13, 00));
		tutoria2.setAdiestrador(this.adiestradorService.findAdiestradorById(1));
		Assertions.assertThrows(MismaHoraTutoriaException.class, () ->{
			tutoriaService.saveTutoria(tutoria2, false);
		});
	}
	
	@Test
	@Transactional
	public void shouldThrowExceptionNumeroTutoriasMaximoPorDiaException() {
		Tutoria tutoria = new Tutoria();
		tutoria.setTitulo("Titulo");
		tutoria.setRazon("Razon");
		tutoria.setPet(this.petService.findPetById(1));
		tutoria.setId(14);
		tutoria.setFechaHora(LocalDateTime.of(2021, 03, 04, 13, 00));
		tutoria.setAdiestrador(this.adiestradorService.findAdiestradorById(1));
        
		try {
            this.tutoriaService.saveTutoria(tutoria, false);
        } catch (MismaHoraTutoriaException ex1) {
            ex1.printStackTrace();
        }catch (NumeroTutoriasMaximoPorDiaException ex2) {
        	ex2.printStackTrace();
        }catch (MismaHoraTutoriaPetException ex3) {
        	ex3.printStackTrace();
        }

		Tutoria tutoria2 = new Tutoria();
		tutoria2.setTitulo("Titulo");
		tutoria2.setRazon("Razon");
		tutoria2.setPet(this.petService.findPetById(2));
		tutoria2.setId(15);
		tutoria2.setFechaHora(LocalDateTime.of(2021, 03, 04, 14, 00));
		tutoria2.setAdiestrador(this.adiestradorService.findAdiestradorById(1));
		try {
            this.tutoriaService.saveTutoria(tutoria2, false);
        } catch (MismaHoraTutoriaException ex1) {
            ex1.printStackTrace();
        }catch (NumeroTutoriasMaximoPorDiaException ex2) {
        	ex2.printStackTrace();
        }catch (MismaHoraTutoriaPetException ex3) {
        	ex3.printStackTrace();
        }
		Tutoria tutoria3 = new Tutoria();
		tutoria3.setTitulo("Titulo");
		tutoria3.setRazon("Razon");
		tutoria3.setPet(this.petService.findPetById(3));
		tutoria3.setId(16);
		tutoria3.setFechaHora(LocalDateTime.of(2021, 03, 04, 15, 00));
		tutoria3.setAdiestrador(this.adiestradorService.findAdiestradorById(1));
		try {
            this.tutoriaService.saveTutoria(tutoria3, false);
        } catch (MismaHoraTutoriaException ex1) {
            ex1.printStackTrace();
        }catch (NumeroTutoriasMaximoPorDiaException ex2) {
        	ex2.printStackTrace();
        }catch (MismaHoraTutoriaPetException ex3) {
        	ex3.printStackTrace();
        }
		Tutoria tutoria4 = new Tutoria();
		tutoria4.setTitulo("Titulo");
		tutoria4.setRazon("Razon");
		tutoria4.setPet(this.petService.findPetById(4));
		tutoria4.setId(17);
		tutoria4.setFechaHora(LocalDateTime.of(2021, 03, 04, 18, 00));
		tutoria4.setAdiestrador(this.adiestradorService.findAdiestradorById(1));
		
		
		Assertions.assertThrows(NumeroTutoriasMaximoPorDiaException.class, () ->{
			tutoriaService.saveTutoria(tutoria4, false);
		});
	}
	
	@Test
	@Transactional
	public void shouldThrowExceptionMismaHoraTutoriaPetException() {
		Tutoria tutoria = new Tutoria();
		tutoria.setTitulo("Titulo");
		tutoria.setRazon("Razon");
		tutoria.setPet(this.petService.findPetById(1));
		tutoria.setId(14);
		tutoria.setFechaHora(LocalDateTime.of(2021, 03, 04, 13, 00));
		tutoria.setAdiestrador(this.adiestradorService.findAdiestradorById(1));
        
		try {
            this.tutoriaService.saveTutoria(tutoria, false);
        } catch (MismaHoraTutoriaException ex1) {
            ex1.printStackTrace();
        }catch (NumeroTutoriasMaximoPorDiaException ex2) {
        	ex2.printStackTrace();
        }catch (MismaHoraTutoriaPetException ex3) {
        	ex3.printStackTrace();
        }

		Tutoria tutoria2 = new Tutoria();
		tutoria2.setTitulo("Titulo");
		tutoria2.setRazon("Razon");
		tutoria2.setPet(this.petService.findPetById(1));
		tutoria2.setId(15);
		tutoria2.setFechaHora(LocalDateTime.of(2021, 03, 04, 13, 00));
		tutoria2.setAdiestrador(this.adiestradorService.findAdiestradorById(2));
		Assertions.assertThrows(MismaHoraTutoriaPetException.class, () ->{
			tutoriaService.saveTutoria(tutoria2, false);
		});
	}
	
	@Test
	@Transactional
	public void shouldUpdateTutoria() {
		Tutoria tutoria2 = new Tutoria();
		tutoria2.setTitulo("Titulo");
		tutoria2.setRazon("Razon");
		tutoria2.setPet(this.petService.findPetById(1));
		tutoria2.setId(15);
		tutoria2.setFechaHora(LocalDateTime.of(2021, 03, 04, 14, 00));
		tutoria2.setAdiestrador(this.adiestradorService.findAdiestradorById(2));
		try {
            this.tutoriaService.saveTutoria(tutoria2, false);
        } catch (MismaHoraTutoriaException ex1) {
            Logger.getLogger(TutoriaServiceTests.class.getName()).log(Level.SEVERE, null, ex1);
        }catch (NumeroTutoriasMaximoPorDiaException ex2) {
            Logger.getLogger(TutoriaServiceTests.class.getName()).log(Level.SEVERE, null, ex2);
        }catch (MismaHoraTutoriaPetException ex3) {
            Logger.getLogger(TutoriaServiceTests.class.getName()).log(Level.SEVERE, null, ex3);
        }
		List<Tutoria> tutorias = this.tutoriaService.findAllTutorias();
		int tam = tutorias.size();
		Tutoria tutoria = this.tutoriaService.findTutoriaById(1);
		tutoria.setRazon("Cambiando la razon");
		tutoria.setTitulo("Cambiando el titulo");
		tutoria.setFechaHora(LocalDateTime.of(2021, 02, 22, 12, 00));
		
		try {
            this.tutoriaService.saveTutoria(tutoria, true);
        } catch (MismaHoraTutoriaException ex1) {
            Logger.getLogger(TutoriaServiceTests.class.getName()).log(Level.SEVERE, null, ex1);
        }catch (NumeroTutoriasMaximoPorDiaException ex2) {
            Logger.getLogger(TutoriaServiceTests.class.getName()).log(Level.SEVERE, null, ex2);
        }catch (MismaHoraTutoriaPetException ex3) {
            Logger.getLogger(TutoriaServiceTests.class.getName()).log(Level.SEVERE, null, ex3);
        }
		tutorias = this.tutoriaService.findAllTutorias();
		assertThat(tutorias.size()).isEqualTo(tam);
	}
	
	@Test
	@Transactional
	public void shouldNotUpdateTutoriaThrowExceptionMismaHoraTutoriaException() {
		Tutoria tutoria2 = new Tutoria();
		tutoria2.setTitulo("Titulo");
		tutoria2.setRazon("Razon");
		tutoria2.setPet(this.petService.findPetById(1));
		tutoria2.setId(15);
		tutoria2.setFechaHora(LocalDateTime.of(2021, 03, 04, 14, 00));
		tutoria2.setAdiestrador(this.adiestradorService.findAdiestradorById(1));
		try {
            this.tutoriaService.saveTutoria(tutoria2, false);
        } catch (MismaHoraTutoriaException ex1) {
        	ex1.printStackTrace();
        }catch (NumeroTutoriasMaximoPorDiaException ex2) {
        	ex2.printStackTrace();
        }catch (MismaHoraTutoriaPetException ex3) {
        	ex3.printStackTrace();
        }
		tutoria2.setRazon("Cambiando la razon");
		tutoria2.setTitulo("Cambiando el titulo");
		tutoria2.setFechaHora(LocalDateTime.of(2021, 01, 14, 15, 30));
		
		Assertions.assertThrows(MismaHoraTutoriaException.class, () ->{
			tutoriaService.saveTutoria(tutoria2, true);
		});
	}
	
	@Test
	@Transactional
	public void shouldNotUpdateTutoriaThrowExceptionMismaHoraTutoriaPetException() {
		Tutoria tutoria2 = new Tutoria();
		tutoria2.setTitulo("Titulo");
		tutoria2.setRazon("Razon");
		tutoria2.setPet(this.petService.findPetById(3));
		tutoria2.setId(15);
		tutoria2.setFechaHora(LocalDateTime.of(2021, 03, 04, 14, 00));
		tutoria2.setAdiestrador(this.adiestradorService.findAdiestradorById(2));
		try {
            this.tutoriaService.saveTutoria(tutoria2, false);
        } catch (MismaHoraTutoriaException ex1) {
        	ex1.printStackTrace();
        }catch (NumeroTutoriasMaximoPorDiaException ex2) {
        	ex2.printStackTrace();
        }catch (MismaHoraTutoriaPetException ex3) {
        	ex3.printStackTrace();
        }
		tutoria2.setRazon("Cambiando la razon");
		tutoria2.setTitulo("Cambiando el titulo");
		tutoria2.setFechaHora(LocalDateTime.of(2021, 01, 14, 15, 30));
		
		Assertions.assertThrows(MismaHoraTutoriaPetException.class, () ->{
			tutoriaService.saveTutoria(tutoria2, true);
		});
	}
}
