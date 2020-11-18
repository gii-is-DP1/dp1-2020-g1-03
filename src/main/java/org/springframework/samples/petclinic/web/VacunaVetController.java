package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Vacuna;
import org.springframework.samples.petclinic.service.VacunaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vets/{vetId}/vacunas")
public class VacunaVetController {
	
	private VacunaService vacunaService;

	@Autowired
	public VacunaVetController(VacunaService vacunaService) {
		this.vacunaService = vacunaService;
	}

	@GetMapping()
	public String listadoVacunas(Map<String, Object> model) {
		List<Vacuna> vacunas = vacunaService.findAll();
		model.put("vacunas", vacunas);
		return "vacunas/vacunasListVet";
	}
	
	@GetMapping(value = "{vacunaId}")
	public String mostarVacuna(@PathVariable("vacunaId") int Id,Map<String, Object> model) {
		Vacuna vacuna= vacunaService.findById(Id);
		model.put("vacuna", vacuna);
		return "vacunas/vacunasShow";
		}
}
