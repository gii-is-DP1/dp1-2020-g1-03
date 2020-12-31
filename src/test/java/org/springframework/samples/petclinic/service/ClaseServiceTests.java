package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.model.ApuntarClase;
import org.springframework.samples.petclinic.model.CategoriaClase;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ClaseServiceTests {
	@Autowired
	protected ClaseService claseService;
	protected AdiestradorService adiestradorService;
	
	
	@Test
	@Transactional
	public void shouldUpdateClase() throws Exception {
		Clase clase = this.claseService.findClaseById(1);
		Secretario secretario = new Secretario();
		Adiestrador adiestrador = new Adiestrador();
		PetType type = new PetType();
		String nombreClase = "Clase";

		clase.setId(2);
		clase.setCategoriaClase(CategoriaClase.TRUCOS_BASICOS);
		clase.setFechaHoraInicio(LocalDateTime.of(2020, 12, 27, 15, 00));
		clase.setFechaHoraFin(LocalDateTime.of(2020, 12, 27, 16, 00));
		clase.setNumeroPlazasTotal(20);
		clase.setNumeroPlazasDisponibles(15);
		clase.setType(type);
		clase.setName(nombreClase);
		clase.setAdiestrador(adiestrador);
		clase.setSecretario(secretario);
		this.claseService.saveClase(clase);

		Clase claseRev = this.claseService.findClaseById(1);
		Assert.assertTrue(claseRev.getName().equals(nombreClase));
	}
//	@Test
//	@Transactional
//	public void shouldDeleteClase() throws Exception {
//		Clase clase = this.claseService.findClaseById(1);
//		this.claseService.deleteClase(clase);
//		Assert.assertTrue(clase.equals(null));
//	}
	@Test
	void shouldFindClaseAdiestradorById() throws Exception{
		Collection<Clase> clase = this.claseService.findClaseByAdiestradorId(1);
		Assert.assertTrue(clase.size()==(5));
	}
	
	@Test
	void shouldFindClaseByName() throws Exception{
		List<Clase> clase = this.claseService.findByName("Clase1");
		Assert.assertTrue(clase.size()==1);
	}
	
	@Test
	void shouldFindClaseById() throws Exception{
		Clase clase = this.claseService.findClaseById(1);
		Assert.assertTrue(clase.getId()==1);
	}
	
	@Test
	void shouldFindClaseByPetId() throws Exception{
		List<ApuntarClase> clase = this.claseService.findClasesByPetId(4);
		Assert.assertTrue(clase.size()==1);
	}
	//ERROR
	@Test
	void shouldFindClaseByAdiestrador() throws Exception{
		Adiestrador ad = this.adiestradorService.findAdiestradorById(87);
		List<Clase> clases = this.claseService.findClasesAdiestrador(ad);
		Assert.assertTrue(clases.size()==5);
	}
	
	@Test
	@Transactional
	void shouldApuntarMascota() throws Exception{
		Clase clase = this.claseService.findClaseById(5);
		clase.setNumeroPlazasDisponibles(clase.getNumeroPlazasDisponibles()-1);
		this.claseService.saveClase(clase);
		Assert.assertTrue(clase.getNumeroPlazasDisponibles()==6);
	}
}

