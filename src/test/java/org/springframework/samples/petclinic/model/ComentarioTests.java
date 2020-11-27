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

public class ComentarioTests {
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}
	
	@Test
	void shouldValidateWhenFieldsAreCorrect() {

		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Comentario comentario = new Comentario();

		comentario.setTitulo("Prueba todo correcto");

		comentario.setCuerpo("Este comentario se ha realizado correctamente");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Comentario>> constraintViolations = validator.validate(comentario);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);

	}
	
	@Test
	void shouldNotValidateWhenHisFieldsBlankOrNull() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
		Comentario comentario = new Comentario();

		comentario.setTitulo("Título de prueba");

		comentario.setCuerpo("");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Comentario>> constraintViolations = validator.validate(comentario);

		System.out.println(constraintViolations.toString());
		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);

	}
}
