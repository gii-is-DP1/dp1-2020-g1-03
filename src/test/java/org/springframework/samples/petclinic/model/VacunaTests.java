package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class VacunaTests {
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Vacuna vacuna = new Vacuna();
		vacuna.setFecha(LocalDate.of(2020, 11, 11));
		vacuna.setDescripcion("Prueba de que se ha añadido correactamente la vacuna");
		vacuna.setTipoEnfermedad(new TipoEnfermedad());
		vacuna.setVet(new Vet());
		vacuna.setPet(new Pet());
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Vacuna>> constraintViolations = validator.validate(vacuna);
		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}
	
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Vacuna vacuna = new Vacuna();
		vacuna.setFecha(LocalDate.of(2020, 11, 11));
		vacuna.setDescripcion("");
		vacuna.setVet(new Vet());
		vacuna.setPet(new Pet());
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Vacuna>> constraintViolations = validator.validate(vacuna);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);

	}
	
	@Test
	void shouldNotValidateWhenHisFechaVacunaIsFuture() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Vacuna vacuna = new Vacuna();
		vacuna.setFecha(LocalDate.of(2021, 11, 11));
		vacuna.setDescripcion("Prueba correcta");
		vacuna.setVet(new Vet());
		vacuna.setPet(new Pet());
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Vacuna>> constraintViolations = validator.validate(vacuna);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);

	}

}
