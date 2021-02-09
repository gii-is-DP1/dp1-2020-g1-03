package org.springframework.samples.petclinic.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.VetService;

@ExtendWith(MockitoExtension.class)
public class ComentarioFormatterTests {

		@Mock
		private VetService vetService;

		private ComentarioFormatter comentarioFormatter;
		
		@BeforeEach
		void setup() {
			comentarioFormatter = new ComentarioFormatter(vetService);
		}
		

		@Test
		void testPrint() {
			Vet vet = new Vet();
			vet.setLastName("Carter");
			String lastName = comentarioFormatter.print(vet, Locale.ENGLISH);
			assertEquals("Carter", lastName);
		}
		
		@Test
		void shouldParse() throws ParseException {
			Mockito.when(vetService.findVetsByLastName("Carter")).thenReturn(makeVets());
			Vet vet = comentarioFormatter.parse("Carter", Locale.ENGLISH);
			assertEquals("Carter", vet.getLastName());
		}
		
		
		@Test
		void shouldThrowParseException() throws ParseException {
			Mockito.when(vetService.findVetsByLastName("Juan")).thenReturn(null);
			Assertions.assertThrows(ParseException.class, () -> {
				comentarioFormatter.parse("Juan", Locale.ENGLISH);
			});
		}

		private Vet makeVets() {
			Vet vet = new Vet() {
				{
					setLastName("Carter");
				}
			};
			return vet;
		}
}

