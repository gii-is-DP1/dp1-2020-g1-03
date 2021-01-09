package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.service.AdiestradorService;
import org.springframework.stereotype.Component;

@Component
public class AdiestradorFormatter implements Formatter<Adiestrador>{
	
	private final AdiestradorService adService;

	@Autowired
	public AdiestradorFormatter(AdiestradorService adiService) {
		this.adService = adiService;
	}

	@Override
	public String print(Adiestrador adiestrador, Locale locale) {
		return adiestrador.getFirstName();
	}

	@Override
	public Adiestrador parse(String text, Locale locale) throws ParseException {
		Collection<Adiestrador> findAdiestradors = this.adService.findAllAdiestradores();
		String[] split = text.split(",");
		for (Adiestrador adiestrador : findAdiestradors) {
			if (adiestrador.getFirstName().equals(split[0]) && adiestrador.getLastName().equals(split[1])) {
				return adiestrador;
			}
		}
		throw new ParseException("adiestrador not found: " + text, 0);
	}
}
