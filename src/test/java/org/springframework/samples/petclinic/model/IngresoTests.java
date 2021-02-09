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

class IngresoTests {
	
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Ingreso ingreso = new Ingreso();

		ingreso.setTitulo("Prueba todo correcto");

		ingreso.setCantidad(1200);
		ingreso.setFecha(LocalDate.of(2020, 11, 11));
		ingreso.setDescription("Descripcion de prueba todo correcto");;

		ingreso.setEconomista(new Economista());
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Ingreso>> constraintViolations = validator.validate(ingreso);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}

	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Ingreso ingreso = new Ingreso();

		ingreso.setTitulo("");

		ingreso.setCantidad(100);
		ingreso.setFecha(LocalDate.of(2020, 11, 11));
		ingreso.setDescription("");;

		ingreso.setEconomista(new Economista());
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Ingreso>> constraintViolations = validator.validate(ingreso);

		System.out.println(constraintViolations.toString());
		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);

	}
	
	@Test
	void shouldNotValidateWhenHisFechaIngresoIsFuture() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Ingreso ingreso = new Ingreso();

		ingreso.setTitulo("Prueba todo correcto");

		ingreso.setCantidad(1200);
		ingreso.setFecha(LocalDate.of(2021, 11, 11));
		ingreso.setDescription("Descripcion de prueba todo correcto");;

		ingreso.setEconomista(new Economista());
		Validator validator = this.createValidator();
		Set<ConstraintViolation<Ingreso>> constraintViolations = validator.validate(ingreso);


		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
	}

}
