package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class SecretarioTests {
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Secretario secretario = new Secretario();
		User user = new User();

		secretario.setFirstName("PruebaSecretario");
		secretario.setId(1);
		secretario.setLastName("SecretarioPrueba");
		secretario.setProgramasDominados("ProgramaPrueba");
		secretario.setUser(user);


		Validator validator = this.createValidator();
		Set<ConstraintViolation<Secretario>> constraintViolations = validator.validate(secretario);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}
	
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Secretario secretario = new Secretario();
		User user = new User();

		secretario.setFirstName("PruebaSecretario");
		secretario.setId(1);
		secretario.setLastName("SecretarioPrueba");
		secretario.setProgramasDominados("");
		secretario.setUser(user);


		Validator validator = this.createValidator();
		Set<ConstraintViolation<Secretario>> constraintViolations = validator.validate(secretario);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);

	}
}
