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
import org.springframework.samples.petclinic.model.CategoriaClase;
import org.springframework.samples.petclinic.service.ClaseService;

@ExtendWith(MockitoExtension.class)
public class CategoriaClaseFormatterTests {
	@Mock
	private ClaseService claseService;

	private CategoriaClaseFormatter categoriaClaseFormatter;
	
	@BeforeEach
	void setup() {
		categoriaClaseFormatter = new CategoriaClaseFormatter(claseService);
	}
	
	@Test
	void testPrint() {
		CategoriaClase categoriaClase = new CategoriaClase();
		categoriaClase.setName("Adiestrar");
		String name = categoriaClaseFormatter.print(categoriaClase, Locale.ENGLISH);
		assertEquals("Adiestrar", name);
	}
	
	@Test
	void shouldParse() throws ParseException {
		Mockito.when(claseService.findAllCategoriasClase()).thenReturn(makeCategoriasClase());
		CategoriaClase categoriaClase = categoriaClaseFormatter.parse("Adiestrar", Locale.ENGLISH);
		assertEquals("Adiestrar", categoriaClase.getName());
	}
	
	
	@Test
	void shouldThrowParseException() throws ParseException {
		Mockito.when(claseService.findAllCategoriasClase()).thenReturn(makeCategoriasClase());
		Assertions.assertThrows(ParseException.class, () -> {
			categoriaClaseFormatter.parse("EstoNoEsUnaCategoria", Locale.ENGLISH);
		});
	}

	private List<CategoriaClase> makeCategoriasClase() {
		List<CategoriaClase> categoriasClase = new ArrayList<>();
		categoriasClase.add(new CategoriaClase() {
			{
				setName("Adiestrar");
			}
		});
		categoriasClase.add(new CategoriaClase() {
			{
				setName("Trucos básicos");
			}
		});
		return categoriasClase;
	}
}
