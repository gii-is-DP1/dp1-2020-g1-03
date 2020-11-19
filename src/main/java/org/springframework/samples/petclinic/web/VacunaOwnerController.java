/*package org.springframework.samples.petclinic.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Gasto;
import org.springframework.samples.petclinic.model.Vacuna;
import org.springframework.samples.petclinic.service.VacunaService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/owners/{ownerId}/vacunas")
public class VacunaOwnerController {

	private VacunaService vacunaService;

	@Autowired
	public VacunaOwnerController(VacunaService vacunaService) {
		this.vacunaService = vacunaService;
	}

	@GetMapping()
	public String listadoVacunas(Map<String, Object> model, @PathVariable("ownerId") int ownerId) {
		List<Vacuna> vacunas = vacunaService.findByOwnerId(ownerId);
		model.put("vacunas", vacunas);
		return "vacunas/vacunasListOwner";
	}
	
	@GetMapping(value = "{vacunaId}")
	public String mostarVacuna(@PathVariable("vacunaId") int Id,Map<String, Object> model) {
		Vacuna vacuna= vacunaService.findVacunaById(Id);
		model.put("vacuna", vacuna);
		return "vacunas/vacunasShow";
		}
}
*/