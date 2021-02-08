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
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.service.AdiestradorService;

@ExtendWith(MockitoExtension.class)
public class AdiestradorFormatterTests {
	@Mock
	private AdiestradorService adService;

	private AdiestradorFormatter adiestradorFormatter;
	
	@BeforeEach
	void setup() {
		adiestradorFormatter = new AdiestradorFormatter(adService);
	}
	
	@Test
	void testPrint() {
		Adiestrador adiestrador = new Adiestrador();
		adiestrador.setFirstName("Daniel");
		String firstName = adiestradorFormatter.print(adiestrador, Locale.ENGLISH);
		assertEquals("Daniel", firstName);
	}
	
	@Test
	void shouldParse() throws ParseException {
		Mockito.when(adService.findAllAdiestradores()).thenReturn(makeAdiestradores());
		Adiestrador adiestrador = adiestradorFormatter.parse("Daniel,Castroviejo", Locale.ENGLISH);
		assertEquals("Daniel", adiestrador.getFirstName());
		assertEquals("Castroviejo", adiestrador.getLastName());
	}
	
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(adService.findAllAdiestradores()).thenReturn(makeAdiestradores());
		Assertions.assertThrows(ParseException.class, () -> {
			adiestradorFormatter.parse("Manolo,ElDeLbOmBo", Locale.ENGLISH);
		});
	}

	private Collection<Adiestrador> makeAdiestradores() {
		Collection<Adiestrador> adiestradores = new ArrayList<>();
		adiestradores.add(new Adiestrador() {
			{
				setFirstName("Daniel");
				setLastName("Castroviejo");
			}
		});
		adiestradores.add(new Adiestrador() {
			{
				setFirstName("Manuel");
				setLastName("Castroviejo");
			}
		});
		return adiestradores;
	}
}
