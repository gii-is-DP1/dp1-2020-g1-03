package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.samples.petclinic.model.Vacuna;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VacunaValidator implements Validator{
	
	private static final String REQUIRED = "required";
	private static final String PAST = "date must be a past date";

	@Override
	public void validate(Object obj, Errors errors) {
		Vacuna vacuna = (Vacuna) obj;

		// type validation
		if (vacuna.isNew() && vacuna.getTipoEnfermedad() == null) {
			errors.rejectValue("tipoEnfermedad", REQUIRED, REQUIRED);
		}
		if (vacuna.getFecha() == null) {
			errors.rejectValue("fecha", REQUIRED, REQUIRED);
		}
		
		if (vacuna.getFecha().isAfter(LocalDate.now())) {
			errors.rejectValue("fecha", PAST, PAST);
		}
	}

	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Vacuna.class.isAssignableFrom(clazz);
	}

}
