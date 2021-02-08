package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.stereotype.Component;

@Component
public class ApuntarClaseFormatter implements Formatter<Pet>{
	private final PetService peService;
	@Autowired
	public ApuntarClaseFormatter(PetService petService) {
		this.peService = petService;
	} 


@Override
public String print(Pet pet, Locale locale) {
	return pet.getName();
}

@Override
public Pet parse(String text, Locale locale) throws ParseException {
	String[] split = text.split(",");
	String nombre = split[0].trim();
	Integer idOwner = Integer.parseInt(split[1]);
	Collection<Pet> findPets = this.peService.findPetsByOwnerId(idOwner);
	for (Pet pet : findPets) {
		if (pet.getName().equals(nombre)) {
			return pet;
		}
	}
	throw new ParseException("type not found: " + text, 0);
}
}
