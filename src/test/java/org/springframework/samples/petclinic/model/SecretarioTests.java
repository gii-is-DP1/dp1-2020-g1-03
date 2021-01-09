

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
class SecretarioTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Secretario secretario = new Secretario();
		User user=new User();

		
		secretario.setFirstName("PruebaNombre");
		secretario.setLastName("PruebaApellidos");
		secretario.setProgramasDominados("Prueba programas dominados");
		secretario.setUser(user);
		
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Secretario>> constraintViolations = validator.validate(secretario);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Secretario secretario = new Secretario();
		User user=new User();

		
		secretario.setFirstName(null);
		secretario.setLastName("PruebaApellidos");
		secretario.setProgramasDominados("Prueba programas dominados");
		secretario.setUser(user);
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Secretario>> constraintViolations = validator.validate(secretario);

		System.out.println(constraintViolations.toString());
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);

	}


}
