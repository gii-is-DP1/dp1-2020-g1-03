
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

class GastoTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Gasto gasto = new Gasto();

		gasto.setTitulo("Prueba todo correcto");

		gasto.setCantidad(1200);
		gasto.setFecha(LocalDate.of(2020, 11, 11));
		gasto.setDescription("Descripcion de prueba todo correcto");;

		gasto.setEconomista(new Economista());
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Gasto>> constraintViolations = validator.validate(gasto);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Gasto gasto = new Gasto();

		gasto.setTitulo("");

		gasto.setCantidad(100);
		gasto.setFecha(LocalDate.of(2020, 11, 11));
		gasto.setDescription("");;

		gasto.setEconomista(new Economista());
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Gasto>> constraintViolations = validator.validate(gasto);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);

	}

	@Test
	void shouldNotValidateWhenHisFechaCitaIsFuture() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Gasto gasto = new Gasto();

		gasto.setTitulo("Prueba todo correcto");

		gasto.setCantidad(1200);
		gasto.setFecha(LocalDate.of(2021, 11, 11));
		gasto.setDescription("Descripcion de prueba todo correcto");;

		gasto.setEconomista(new Economista());
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Gasto>> constraintViolations = validator.validate(gasto);


		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
	}

}
