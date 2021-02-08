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
		Vet vet = new Vet();
		Pet pet1= new Pet();
		Pet pet2= new Pet();
		List<Pet> petls=new ArrayList<>(); petls.add(pet1); petls.add(pet2);
		cita.setName("CitaPrueba");
		cita.setFechaHora(LocalDateTime.of(2022, 1, 4, 12, 30));
		cita.setEstado(Estado.PENDIENTE);
		cita.setRazon("RazonCitaPrueba");
		cita.setTitulo("TituloCitaPrueba");
		cita.setVet(vet);
		cita.setPets(petls);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Cita>> constraintViolations = validator.validate(cita);


		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}
	
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Cita cita = new Cita();

		Vet vet = new Vet();
		Pet pet1= new Pet();
		Pet pet2= new Pet();
		List<Pet> petls=new ArrayList<>(); petls.add(pet1); petls.add(pet2);
		cita.setName("CitaPrueba");
		cita.setFechaHora(LocalDateTime.of(2022, 1, 4, 12, 30));
		cita.setEstado(Estado.PENDIENTE);
		cita.setRazon("");
		cita.setTitulo("TituloCitaPrueba");
		cita.setVet(vet);
		cita.setPets(petls);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Cita>> constraintViolations = validator.validate(cita);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
}
