package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class CitaTests {
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	

	@Test
	void shouldValidateWhenFieldsAreCorrect() {
		
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Cita cita = new Cita();
		List<Pet> pets = new ArrayList<>();
		Vet vet = new Vet();
		cita.setEstado(Estado.ACEPTADA);
		cita.setFechaHora(LocalDateTime.of(2020, 1, 4, 12, 30));
		cita.setTitulo("Cita prueba");
		cita.setRazon("Probando citas");
		cita.setPets(pets);
		cita.setVet(vet);
		
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Cita>> constraintViolations = validator.validate(cita);
		
		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}
	
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Cita cita = new Cita();
		
		cita.setEstado(Estado.ACEPTADA);
		cita.setFechaHora(LocalDateTime.of(2020, 1, 4, 12, 30));
		cita.setTitulo("");
		cita.setRazon("Probando citas");
		
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Cita>> constraintViolations = validator.validate(cita);
		
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
	}
}
