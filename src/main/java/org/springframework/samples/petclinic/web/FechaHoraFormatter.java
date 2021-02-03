package org.springframework.samples.petclinic.web;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import org.springframework.format.Formatter;


public class FechaHoraFormatter implements Formatter<LocalDateTime>{

	@Override
	public String print(LocalDateTime object, Locale locale) {
		return LocalDateTime.class.toString();
	}

	@Override
	public LocalDateTime parse(String text, Locale locale) throws ParseException {
		LocalDateTime fechaHora=LocalDateTime.parse(text);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.US);
		fechaHora.format(formatter);
		if(fechaHora==null) {
			throw new ParseException("type not found: " + text, 0);
		}
		return fechaHora;
	}

}
