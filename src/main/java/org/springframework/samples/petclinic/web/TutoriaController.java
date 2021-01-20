package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Tutoria;
import org.springframework.samples.petclinic.service.TutoriaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TutoriaController {

	private TutoriaService tutoriaService;
	
	@Autowired
	public TutoriaController(TutoriaService tutoriaService) {
		this.tutoriaService = tutoriaService;
	}
	@GetMapping(value = "/adiestradores/tutorias")
	public String listadoTutorias(Map<String, Object> model) {
		List<Tutoria> tutorias = tutoriaService.findAllTutorias();
		model.put("tutorias", tutorias);
		return "tutorias/tutoriasList";
	}
}
