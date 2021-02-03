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

public class ClaseTests {
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Clase clase = new Clase();
		Adiestrador adiestrador = new Adiestrador();
		Secretario secretario = new Secretario();
		clase.setName("ClasePrueba");
		clase.setFechaHoraInicio(LocalDateTime.of(2020, 1, 4, 12, 30));
		clase.setFechaHoraFin(LocalDateTime.of(2020, 1, 4, 13, 00));
		clase.setNumeroPlazasTotal(20);
		clase.setNumeroPlazasDisponibles(15);
		clase.setCategoriaClase(new CategoriaClase());
		clase.setAdiestrador(adiestrador);
		clase.setSecretario(secretario);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Clase>> constraintViolations = validator.validate(clase);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}
	
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Clase clase = new Clase();

		clase.setName("");
		clase.setFechaHoraInicio(LocalDateTime.of(2020, 1, 4, 12, 30));
		clase.setFechaHoraFin(LocalDateTime.of(2020, 1, 4, 13, 00));
		clase.setNumeroPlazasTotal(20);
		clase.setNumeroPlazasDisponibles(15);
		clase.setCategoriaClase(new CategoriaClase());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Clase>> constraintViolations = validator.validate(clase);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);

	}
}
