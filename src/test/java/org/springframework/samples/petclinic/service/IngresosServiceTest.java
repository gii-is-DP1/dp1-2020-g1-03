package org.springframework.samples.petclinic.service;

import static org.junit.Assert.assertThat;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Ingreso;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class IngresosServiceTest {
	
	@Autowired
	protected IngresoService ingresoService;
	
	@Test
	void shouldFindIngresos() {
		Collection<Ingreso> ingresos = this.ingresoService.findAllIngresos();
		
		Ingreso ingreso = EntityUtils.getById(ingresos, Ingreso.class, 2);
		//assertThat(ingreso.getCantidad()).isEqualTo(1250);
		assertThat(ingreso.getCantidad()).isEqualTo(800);
		assertThat(ingreso.getDescription()).isNotNull();
	}
	
	 @Test
	 @Transactional
	 public void shouldInsertIngreso() {
		 List<Ingreso> ingresos = this.ingresoService.findAllIngresos();
		 int found = ingresos.size();
		 Ingreso ingreso = new Ingreso();
		 ingreso.setTitulo("Vacunas");
		 ingreso.setCantidad(30);
		 LocalDate fecha= LocalDate.now();
		 ingreso.setFecha(fecha);
		 ingreso.setDescription("Ingresos obtenidos por las vacunas en el mes de Noviembre");
		 this.ingresoService.saveIngreso(ingreso);
		 assertThat(ingreso.getId().longValue()).isNotEqualTo(0);
		 
		 assertThat(this.ingresoService.findAllIngresos().size()).isEqualTo(found + 1);
	 }
	 
//	 @Test
//	 public void NoInsertarIngresoConTituloVacio() {
//		 Ingreso ingreso = new Ingreso();
//		 ingreso.setTitulo("");
//		 ingreso.setCantidad(30);
//		 LocalDate fecha= LocalDate.now();
//		 ingreso.setFecha(fecha);
//		 ingreso.setDescription("Ingresos obtenidos por las vacunas en el mes de Noviembre");
//		 
//		 Validator validator = createValidator();
//		 Set<ConstraintViolation<Ingreso>> constraintViolations = validator.validate(ingreso);
//
//		assertThat(constraintViolations.size()).isEqualTo(1);
//		ConstraintViolation<Ingreso> violation = constraintViolations.iterator().next();
//		assertThat(violation.getPropertyPath().toString()).isEqualTo("titulo");
//		assertThat(violation.getMessage()).isEqualTo("no puede estar vacío");
//	 }
	
	@Test
	void shouldUpdateIngresos() {
		Ingreso ingreso = this.ingresoService.findIngresoById(1);
		Integer oldCantidad = ingreso.getCantidad();
		Integer newCantidad = oldCantidad + 200;
		
		ingreso.setCantidad(newCantidad);
		this.ingresoService.saveIngreso(ingreso);
		
		// retrieving new cantidad from database
		ingreso = this.ingresoService.findIngresoById(1);
		assertThat(ingreso.getCantidad()).isEqualTo(newCantidad);
		
	}
}
