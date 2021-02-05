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
public class CitaPetFormatter implements Formatter<List<Pet>>{
	private final PetService peService;
	@Autowired
	public CitaPetFormatter(PetService petService) {
		this.peService = petService;
	} 
	@Override
	public String print(List<Pet> pets, Locale locale) {
		String res="";
		for(int i=0; i<pets.size();i++) {
			Pet pet=pets.get(i);
			res+= pet.getName()+ ", ";
		}
		return res;
	}

	@Override
	public List<Pet> parse(String text, Locale locale) throws ParseException {
		String[] split = text.split(",");
		String nombre = split[0].trim();
		Integer idOwner = Integer.parseInt(split[1]);
		
		Collection<Pet> findPets = this.peService.findPetsByOwnerId(idOwner);
		List<Pet> pets= new ArrayList<>(findPets);
		
		return pets;

		//throw new ParseException("type not found: " + text, 0);
	}
}
