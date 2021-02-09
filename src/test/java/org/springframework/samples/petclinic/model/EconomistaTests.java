
package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class EconomistaTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Economista economista = new Economista();
		User user=new User();

		
		economista.setFirstName("PruebaNombre");
		economista.setLastName("PruebaApellidos");
		economista.setEstudios("PruebaEstudios");
		economista.setUser(user);
		
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Economista>> constraintViolations = validator.validate(economista);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Economista economista = new Economista();
		User user=new User();

		
		economista.setFirstName(null);
		economista.setLastName("PruebaApellidos");
		economista.setEstudios("PruebaEstudios");
		economista.setUser(user);
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Economista>> constraintViolations = validator.validate(economista);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);

	}


}
