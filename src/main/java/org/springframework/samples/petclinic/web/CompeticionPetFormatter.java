package org.springframework.samples.petclinic.web;
import java.security.Principal;
import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Component;

public class CompeticionPetFormatter implements Formatter<Pet>{

	private final PetService peService;
	private final OwnerService owService;

	@Autowired
	public CompeticionPetFormatter(PetService petService, OwnerService owService) {
		this.peService = petService;
		this.owService = owService;
	}
	@Override
	public String print(Pet pet, Locale locale) {
		return pet.getName();
	}

	public Pet parse(String text, Locale locale) throws ParseException {
		Collection<Pet> findPets = this.peService.findAllPets();
		for (Pet pet : findPets) {
			if (pet.getName().equals(text)) {
				return pet;
			}
		}
		throw new ParseException("type not found: " + text, 0);
	}
}