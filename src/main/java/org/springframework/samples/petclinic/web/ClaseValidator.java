package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ClaseValidator implements Validator{
	private static final String REQUIRED = "required";

	@Override
	public void validate(Object obj, Errors errors) {
		Clase clase = (Clase) obj;
		

		// type validation
		if (clase.isNew() && clase.getType() == null) {
			errors.rejectValue("type", REQUIRED, REQUIRED);
		}

		// birth date validation
		if (clase.getAdiestrador() == null) {
			errors.rejectValue("adiestrador", REQUIRED, REQUIRED);
		}
	}

	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return Pet.class.isAssignableFrom(clazz);
	}
}