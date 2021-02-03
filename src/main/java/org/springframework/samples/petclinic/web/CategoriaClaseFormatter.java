package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.CategoriaClase;
import org.springframework.samples.petclinic.service.ClaseService;
import org.springframework.stereotype.Component;
@Component
public class CategoriaClaseFormatter implements Formatter<CategoriaClase>{

	private ClaseService claseService;
	
	@Autowired
	public CategoriaClaseFormatter(ClaseService claseService) {
		this.claseService = claseService;
	}
	
	@Override
	public String print(CategoriaClase categoriaClase, Locale locale) {
		return categoriaClase.getName();
	}

	@Override
	public CategoriaClase parse(String text, Locale locale) throws ParseException {
		List<CategoriaClase> categorias = this.claseService.findAllCategoriasClase();
		for(CategoriaClase categoria: categorias) {
			if(categoria.getName().equals(text)) {
				return categoria;
			}
		}
		throw new ParseException("type not found: " + text, 0);
	}

}
