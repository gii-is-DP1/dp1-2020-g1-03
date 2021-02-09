package org.springframework.samples.petclinic.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.PetService;

@ExtendWith(MockitoExtension.class)
public class ApuntarClaseFormatterTests {
	@Mock
	private PetService petService;

	private ApuntarClaseFormatter apuntarClaseFormatter;
	
	@BeforeEach
	void setup() {
		apuntarClaseFormatter = new ApuntarClaseFormatter(petService);
	}
	
	@Test
	void testPrint() {
		Pet pet = new Pet();
		Owner owner = new Owner();
		owner.setId(3);
		pet.setName("Rosy");
		pet.setOwner(owner);
		String name = apuntarClaseFormatter.print(pet, Locale.ENGLISH);
		assertEquals("Rosy", name);
	}
	
	@Test
	void shouldParse() throws ParseException {
		Mockito.when(petService.findPetsByOwnerId(3)).thenReturn(makePets());
		Pet pet = apuntarClaseFormatter.parse("Rosy,3", Locale.ENGLISH);
		assertEquals("Rosy", pet.getName());
	}
	
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(petService.findPetsByOwnerId(3)).thenReturn(makePets());
		Assertions.assertThrows(ParseException.class, () -> {
			apuntarClaseFormatter.parse("Jesulin,3", Locale.ENGLISH);
		});
	}

	private List<Pet> makePets() {
		List<Pet> pets = new ArrayList<>();
		Owner owner = new Owner();
		owner.setId(3);
		pets.add(new Pet() {
			{
				setName("Rosy");
				setOwner(owner);
			}
		});
		return pets;
	}
}
