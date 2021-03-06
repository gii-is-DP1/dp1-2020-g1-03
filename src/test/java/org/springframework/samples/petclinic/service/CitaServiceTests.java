package org.springframework.samples.petclinic.service;

import java.util.Collection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.junit.Assert;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class CitaServiceTests {
	@Autowired
	protected CitaService citaService;
	protected VetService vetService;

	@Test
	@Transactional
	public void shouldUpdateCita() throws Exception {
		Cita cita = this.citaService.findCitaById(1);
		String nombreCita = "Cita";
		
		cita.setName(nombreCita);
		
		this.citaService.saveCita(cita);

		Cita citaRev = this.citaService.findCitaById(1);
		Assert.assertTrue(citaRev.getName().equals(nombreCita));
	}

	@Test
	void shouldFindAllCitas() {
		Collection<Cita> citas = this.citaService.findAllCitas();
		Assert.assertEquals(citas.size(), 3);
	}


	@Test
	void shouldFindCitaWithCorrectId() {
		Cita cita = this.citaService.findCitaById(1);
		Assert.assertEquals(cita.getTitulo(), "Cita1");
	}

	@Test
	void shouldNotFindCitaWithCorrectId() {
		Assert.assertNull(this.citaService.findCitaById(100));
	}

	@Test
	void shouldFindCitasByVetId() {
		Assert.assertNotNull(this.citaService.findCitaById(1));
	}
	

	@Test
	void shouldFindCitaById() throws Exception {
		Cita cita = this.citaService.findCitaById(1);
		Assert.assertTrue(cita.getId() == 1);
	}

}
