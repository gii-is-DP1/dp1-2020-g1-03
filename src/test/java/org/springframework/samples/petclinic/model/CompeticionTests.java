
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

/**
 * @author Michael Isvy Simple test to make sure that Bean Validation is working (useful
 *         when upgrading to a new version of Hibernate Validator/ Bean Validation)
 */
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
		competicion.setFechaHoraInicio(LocalDateTime.of(2021, 11, 11, 10, 00));
		competicion.setFechaHoraFin(LocalDateTime.of(2021, 11, 11, 13, 00));;
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
		competicion.setFechaHoraInicio(LocalDateTime.of(2021, 11, 11, 10, 00));
		competicion.setFechaHoraFin(LocalDateTime.of(2021, 11, 11, 13, 00));;
		competicion.setSecretario(secretario);
		competicion.setPremios(null);
		
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Competicion>> constraintViolations = validator.validate(competicion);
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);

	}

	@Test
	void shouldNotValidateWhenHisFechaCompeIsPast() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Competicion competicion = new Competicion();
		Secretario secretario=new Secretario();
		
		competicion.setNombre("Prueba no todo correcto");
		competicion.setCantidad(1200);
		competicion.setFechaHoraInicio(LocalDateTime.of(2019, 11, 11, 10, 00));
		competicion.setFechaHoraFin(LocalDateTime.of(2019, 11, 11, 13, 00));;
		competicion.setSecretario(secretario);
		competicion.setPremios("Premio pruebas");
		
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Competicion>> constraintViolations = validator.validate(competicion);


		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);
	}

}
