package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.TipoEnfermedad;
import org.springframework.samples.petclinic.service.VacunaService;
import org.springframework.stereotype.Component;

@Component
public class VacunaFormatter implements Formatter<TipoEnfermedad>{
	
	
	private final VacunaService vaService;

	@Autowired
	public VacunaFormatter(VacunaService vaService) {
		this.vaService = vaService;
	}

	@Override
	public String print(TipoEnfermedad tipoEnfermedad, Locale locale) {
		return tipoEnfermedad.getName();
	}

	@Override
	public TipoEnfermedad parse(String text, Locale locale) throws ParseException {
		Collection<TipoEnfermedad> findTipoEnfermedades = this.vaService.findTipoEnfermedades();
		for (TipoEnfermedad type : findTipoEnfermedades) {
			if (type.getName().equals(text)) {
				return type;
			}
		}
		throw new ParseException("type not found: " + text, 0);
	}
}
