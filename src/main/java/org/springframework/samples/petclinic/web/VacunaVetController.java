package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Ingreso;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.TipoEnfermedad;
import org.springframework.samples.petclinic.model.Vacuna;
import org.springframework.samples.petclinic.service.VacunaService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/vets/vacuna")
public class VacunaVetController {
	
	private VacunaService vacunaService;

	@Autowired
	public VacunaVetController(VacunaService vacunaService) {
		this.vacunaService = vacunaService;
	}
	
	@ModelAttribute("tipoenfermedades")
	public Collection<TipoEnfermedad> populateTipoEnfermedad() {
		return this.vacunaService.findTipoEnfermedad();
	}

	@GetMapping()
	public String listadoVacunas(Map<String, Object> model) {
		List<Vacuna> vacunas = vacunaService.findAllVacunas();
		model.put("vacunas", vacunas);
		return "vacunas/vacunasListVet";
	}
	
	@GetMapping(value = "{vacunaId}")
	public String mostarVacuna(@PathVariable("vacunaId") int Id,Map<String, Object> model) {
		Vacuna vacuna= vacunaService.findVacunaById(Id);
		model.put("vacuna", vacuna);
		return "vacunas/vacunasShow";
		}
	
	@GetMapping(value = "/create")
	public String initCreateVacuna(Map<String, Object> model) {
		Vacuna vacuna = new Vacuna();
		model.put("vacuna", vacuna);
		return "vacunas/crearVacuna";
	}

	@PostMapping(value = "/create")
	public String processCreateVacuna(@Valid Vacuna vacuna, BindingResult result) {
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "vacunas/crearVacuna";
		} else {
			this.vacunaService.saveVacuna(vacuna);
			return "redirect:/vets/vacuna" + vacuna.getId();
		}
	}
}
