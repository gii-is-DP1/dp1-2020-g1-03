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

@Component
public class ApuntarClaseFormatter implements Formatter<Pet>{
	private final PetService peService;
	private final OwnerService owService;

	@Autowired
	public ApuntarClaseFormatter(PetService petService, OwnerService owService) {
		this.peService = petService;
		this.owService = owService;
	}
	@Override
	public String print(Pet pet, Locale locale) {
		return pet.getName();
	}

	@Override
	public Pet parse(String text, Locale locale) throws ParseException {
		Collection<Pet> findPets = this.peService.findAllPets();
		String[] split = text.split(",");
		Integer id = Integer.parseInt(split[1]);
		for (Pet pet : findPets) {
			if (pet.getName().equals(split[0]) && pet.getOwner().getId().equals(id)){
				return pet;
			}
		}
		throw new ParseException("type not found: " + text, 0);
	}
}