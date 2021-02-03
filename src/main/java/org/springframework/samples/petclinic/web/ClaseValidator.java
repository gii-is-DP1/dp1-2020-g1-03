package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ClaseValidator implements Validator{
	private static final String REQUIRED = "required";
	private static final String POSITIVE = "must be positive";
	private static final String POSITIVEORZERO = "must be positive or zero";

	@Override
	public void validate(Object obj, Errors errors) {
		Clase clase = (Clase) obj;
		

		if (clase.isNew() && clase.getType() == null) {
			errors.rejectValue("type", REQUIRED, REQUIRED);
		}

		if (clase.getAdiestrador() == null) {
			errors.rejectValue("adiestrador", REQUIRED, REQUIRED);
		}
		
		if (clase.getName() == null) {
			errors.rejectValue("name", REQUIRED, REQUIRED);
		}
		
		if (clase.getNumeroPlazasDisponibles() <0) {
			errors.rejectValue("disponibles", POSITIVEORZERO, POSITIVEORZERO);
		}
		
		if (clase.getNumeroPlazasTotal() <=0) {
			errors.rejectValue("plazas", POSITIVE, POSITIVE);
		}
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return Pet.class.isAssignableFrom(clazz);
	}
}
