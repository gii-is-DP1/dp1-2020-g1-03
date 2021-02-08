package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Estado;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.junit.Assert;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CitaServiceTests {
	
	@Autowired
	protected CitaService citaService;
	
	@Test
	@Transactional
	public void shouldUpdateCita() throws Exception {
		Cita cita = this.citaService.findCitaById(1);
		Vet vet = new Vet();
		List<Pet> pets = new ArrayList<>();
		
		cita.setId(3);
		cita.setEstado(Estado.PENDIENTE);
		cita.setFechaHora(LocalDateTime.of(2020, 12, 27, 15, 00));
		cita.setRazon("Testeando citas");
		cita.setTitulo("Cita 1");
		cita.setPets(pets);
		cita.setVet(vet);
		
		Cita citaRev = this.citaService.findCitaById(1);
		Assert.assertTrue(citaRev.getTitulo().equals(cita.getTitulo()));
	}
	
	@Test
	void shouldFindCitasByVet() {
		Vet vet = new Vet();
		vet.setId(1);
		Assert.assertNotNull(this.citaService.findCitasByVet(vet));
	}
	
	@Test
	void shouldFindAllCitas() {
		List<Cita> citas = this.citaService.findAllCitas();
		Assert.assertEquals(citas.size(), 4);
	}
	
	@Test
	void shouldFindCitaWithCorrectId() {
		Cita cita = this.citaService.findCitaById(1);
		Assert.assertEquals(cita.getTitulo(), "Cita1");
		Assert.assertEquals(cita.getEstado(), Estado.RECHAZADA);
	}
	
	@Test
	void shouldNotFindCitaWithCorrectId() {
		Cita cita = this.citaService.findCitaById(100);
		Assert.assertNull(cita);
	}
	
	@Test
	void shouldFindCitasSinVet() {
		List<Cita> citas = this.citaService.findCitasSinVet();
		Assert.assertNotNull(citas);
	}
	
	

}
