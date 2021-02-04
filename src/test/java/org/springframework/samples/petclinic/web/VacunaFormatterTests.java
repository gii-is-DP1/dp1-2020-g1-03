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
import org.springframework.samples.petclinic.model.TipoEnfermedad;
import org.springframework.samples.petclinic.service.VacunaService;

@ExtendWith(MockitoExtension.class)
public class VacunaFormatterTests {
	
	@Mock
	private VacunaService vacunaService;

	private VacunaFormatter vacunaFormatter;
	
	@BeforeEach
	void setup() {
		vacunaFormatter = new VacunaFormatter(vacunaService);
	}
	
	@Test
	void testPrint() {
		TipoEnfermedad tipoEnfermedad = new TipoEnfermedad();
		tipoEnfermedad.setName("Rabia");
		String tipoEnfermedadName = vacunaFormatter.print(tipoEnfermedad, Locale.ENGLISH);
		assertEquals("Rabia", tipoEnfermedadName);
	}
	
	@Test
	void shouldParse() throws ParseException {
		Mockito.when(vacunaService.findTipoEnfermedades()).thenReturn(makeTipoEnfermedades());
		TipoEnfermedad tipoEnfermedad = vacunaFormatter.parse("Sarna", Locale.ENGLISH);
		assertEquals("Sarna", tipoEnfermedad.getName());
	}
	
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(vacunaService.findTipoEnfermedades()).thenReturn(makeTipoEnfermedades());
		Assertions.assertThrows(ParseException.class, () -> {
			vacunaFormatter.parse("COVI-19", Locale.ENGLISH);
		});
	}

	private Collection<TipoEnfermedad> makeTipoEnfermedades() {
		Collection<TipoEnfermedad> tipoEnfermedades = new ArrayList<>();
		tipoEnfermedades.add(new TipoEnfermedad() {
			{
				setName("Rabia");
			}
		});
		tipoEnfermedades.add(new TipoEnfermedad() {
			{
				setName("Sarna");
			}
		});
		return tipoEnfermedades;
	}

}
