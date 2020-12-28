package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Adiestrador;
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
	
}

