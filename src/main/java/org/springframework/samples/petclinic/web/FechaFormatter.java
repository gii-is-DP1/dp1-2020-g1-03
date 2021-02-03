package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;

public class FechaFormatter implements Formatter<Cita>{
	private final CitaService citaService;
	//private final OwnerService owService;

	@Autowired
	public FechaFormatter(CitaService citaService/*, OwnerService owService*/) {
		this.citaService = citaService;
		//this.owService = owService;
	}
	public String print(Cita cita, Locale locale) {
		String fecha=cita.getFechaHora().toString();
		String fechaMod=fecha.replace("T", " ");
		return fechaMod;
	}

	//@Override
	public Cita parse(String text, Locale locale) throws ParseException {
		Collection<Cita> findCitas = this.citaService.findAllCitas();
		for (Cita cita : findCitas) {
			if (cita.getFechaHora().toString().equals(text)) {
				return cita;
			}
		}
		throw new ParseException("type not found: " + text, 0);
	}
}

