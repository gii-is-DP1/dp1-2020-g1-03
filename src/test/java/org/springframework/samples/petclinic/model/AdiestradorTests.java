package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class AdiestradorTests {
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Adiestrador adiestrador = new Adiestrador();
		User user = new User();

		adiestrador.setFirstName("PruebaAdiestrador");
		adiestrador.setId(1);
		adiestrador.setLastName("AdiestradorPrueba");
		adiestrador.setCompetencias("CompetenciaPrueba");
		adiestrador.setUser(user);


		Validator validator = this.createValidator();
		Set<ConstraintViolation<Adiestrador>> constraintViolations = validator.validate(adiestrador);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}
	
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Adiestrador adiestrador = new Adiestrador();
		User user = new User();

		adiestrador.setFirstName("PruebaAdiestrador");
		adiestrador.setId(1);
		adiestrador.setLastName("AdiestradorPrueba");
		adiestrador.setCompetencias("");
		adiestrador.setUser(user);


		Validator validator = this.createValidator();
		Set<ConstraintViolation<Adiestrador>> constraintViolations = validator.validate(adiestrador);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);

	}
}
