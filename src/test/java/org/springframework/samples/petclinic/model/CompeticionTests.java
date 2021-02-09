
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

class CompeticionTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Competicion competicion = new Competicion();
		Secretario secretario=new Secretario();
		
		competicion.setNombre("Prueba todo correcto");
		competicion.setCantidad(1200);
		competicion.setFechaHoraInicio(LocalDate.of(2021, 11, 11));
		competicion.setFechaHoraFin(LocalDate.of(2021, 11, 11));;
		competicion.setSecretario(secretario);
		competicion.setPremios("Premio pruebas");
		
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Competicion>> constraintViolations = validator.validate(competicion);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Competicion competicion = new Competicion();
		Secretario secretario=new Secretario();
		
		competicion.setNombre("Prueba no todo correcto");
		competicion.setCantidad(1200);
		competicion.setFechaHoraInicio(LocalDate.of(2021, 11, 11));
		competicion.setFechaHoraFin(LocalDate.of(2021, 11, 11));;
		competicion.setSecretario(secretario);
		competicion.setPremios(null);
		
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Competicion>> constraintViolations = validator.validate(competicion);
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);

	}



}
