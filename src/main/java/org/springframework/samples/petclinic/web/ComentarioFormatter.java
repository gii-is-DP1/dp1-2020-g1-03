package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Component;
@Component
public class ComentarioFormatter implements Formatter<Vet>{
	private final VetService vetService;

	@Autowired
	public ComentarioFormatter(VetService vetService) {
		this.vetService = vetService;
	}
	@Override
	public String print(Vet vet, Locale locale) {
		return vet.getLastName();
	}

	public Vet parse(String text, Locale locale) throws ParseException {
		Vet findVet = this.vetService.findVetsByLastName(text);
		if(findVet==null) {
			throw new ParseException("vet not found: " + text, 0);
		}
		return findVet;
	}
}
