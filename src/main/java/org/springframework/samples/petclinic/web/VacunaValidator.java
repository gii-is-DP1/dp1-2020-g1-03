package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Vacuna;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class VacunaValidator implements Validator{
	
	private static final String REQUIRED = "required";

	@Override
	public void validate(Object obj, Errors errors) {
		Vacuna vacuna = (Vacuna) obj;

		// type validation
		if (vacuna.isNew() && vacuna.getTipoEnfermedad() == null) {
			errors.rejectValue("tipoEnfermedad", REQUIRED, REQUIRED);
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
