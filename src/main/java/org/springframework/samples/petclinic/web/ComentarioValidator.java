package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class ComentarioValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Vet.class.isAssignableFrom(clazz);
	}
	private static final String REQUIRED = "required";
	
	@Override
	public void validate(Object obj, Errors errors) {
		Comentario com = (Comentario) obj;
		

		if (com.getVet().isNew() && com.getVet() == null) {
			errors.rejectValue("vet", REQUIRED, REQUIRED);
		}

}
}
