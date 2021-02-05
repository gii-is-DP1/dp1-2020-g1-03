package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class TutoriaTests {
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Tutoria tutoria = new Tutoria();
		Adiestrador adiestrador = new Adiestrador();
		Pet pet = new Pet(); 

		tutoria.setAdiestrador(adiestrador);
		tutoria.setFechaHora(LocalDateTime.of(2021, 9, 9, 12, 00));
		tutoria.setId(45);
		tutoria.setPet(pet);
		tutoria.setRazon("razon");
		tutoria.setTitulo("titulo");


		Validator validator = this.createValidator();
		Set<ConstraintViolation<Tutoria>> constraintViolations = validator.validate(tutoria);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}
	
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Tutoria tutoria = new Tutoria();
		Adiestrador adiestrador = new Adiestrador();
		Pet pet = new Pet(); 

		tutoria.setAdiestrador(adiestrador);
		tutoria.setFechaHora(LocalDateTime.of(2021, 9, 9, 12, 00));
		tutoria.setId(45);
		tutoria.setPet(pet);
		tutoria.setRazon("razon");
		tutoria.setTitulo("");


		Validator validator = this.createValidator();
		Set<ConstraintViolation<Tutoria>> constraintViolations = validator.validate(tutoria);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);

	}
}
