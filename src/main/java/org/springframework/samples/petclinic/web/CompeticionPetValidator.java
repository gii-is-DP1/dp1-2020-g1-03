package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.CompeticionPet;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CompeticionPetValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Pet.class.isAssignableFrom(clazz);
	}

	private static final String REQUIRED = "required";

	@Override
	public void validate(Object obj, Errors errors) {
		CompeticionPet comPet = (CompeticionPet) obj;
		

		// type validation
		if (comPet.getPet().isNew() && comPet.getPet() == null) {
			errors.rejectValue("pet", REQUIRED, REQUIRED);
		}

		
	}

}