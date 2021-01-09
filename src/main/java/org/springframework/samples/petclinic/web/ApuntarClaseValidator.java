package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.ApuntarClase;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ApuntarClaseValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Pet.class.isAssignableFrom(clazz);
	}

	private static final String REQUIRED = "required";

	@Override
	public void validate(Object obj, Errors errors) {
		ApuntarClase apClase = (ApuntarClase) obj;
		

		// type validation
		if (apClase.getPet().isNew() && apClase.getPet() == null) {
			errors.rejectValue("pet", REQUIRED, REQUIRED);
		}

		
	}

}
