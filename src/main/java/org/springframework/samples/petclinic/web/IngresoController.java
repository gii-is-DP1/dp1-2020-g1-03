package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Economista;
import org.springframework.samples.petclinic.model.Ingreso;
import org.springframework.samples.petclinic.service.EconomistaService;

import org.springframework.samples.petclinic.service.IngresoService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/economistas/ingreso")
public class IngresoController {

	private final IngresoService ingresoService;
	private final EconomistaService economistaService;

	@Autowired
	public IngresoController(IngresoService ingresoService, EconomistaService economistaService) {
		this.ingresoService = ingresoService;
		this.economistaService = economistaService;
	}

	@GetMapping()
	public String listadoIngresos(Map<String, Object> model, Principal principal) {
		// String vista="owners/{ownerId}/listadoCitas";
		if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("economista")) {
			List<Ingreso> ingresos = ingresoService.findAllIngresos();
			model.put("ingresos", ingresos);
			return "ingresos/ingresosList";
		}else {
			//ModelAndView exception = new ModelAndView("exception");
			return "exception";			
		}

	}

	@GetMapping(value = "{ingresoId}")
	public String mostarIngreso(@PathVariable("ingresoId") int ingresoId, Map<String, Object> model) {
		// String vista="owners/{ownerId}/listadoCitas";
		Ingreso ingreso = ingresoService.findIngresoById(ingresoId);
		if(ingreso==null) {
			return "exception";
		}else {
			model.put("ingreso", ingreso);
			return "ingresos/ingresosShow";			
		}

	}

	@GetMapping(value = "/{ingresoId}/edit")
	public String initEditIngreso(@PathVariable("ingresoId") int ingresoId, Map<String, Object> model) {
		Ingreso ingreso = this.ingresoService.findIngresoById(ingresoId);
		System.out.println(ingreso + "INGRESO");
		model.put("ingreso", ingreso);
		return "ingresos/crearOEditarIngreso";
	}

	@PostMapping(value = "/{ingresoId}/edit")
	public String processEditIngreso(final Principal principal, @Valid Ingreso ingreso, BindingResult result,
			@PathVariable("ingresoId") int ingresoId) {
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "ingresos/crearOEditarIngreso";
		} else {
			int idEcon = this.economistaService.findEconomistaIdByUsername(principal.getName());
			Economista econ = this.economistaService.findEconomistaById(idEcon);
			ingreso.setEconomista(econ);
			System.out.println(ingreso.getEconomista());
			ingreso.setId(ingresoId);
			this.ingresoService.saveIngreso(ingreso);
			return "redirect:/economistas/ingreso/{ingresoId}";
		}
	}

	@GetMapping(value = "/create")
	public String initCreateIngreso(Map<String, Object> model) {
		Ingreso ingreso = new Ingreso();
		model.put("ingreso", ingreso);
		return "ingresos/crearOEditarIngreso";
	}

	@PostMapping(value = "/create")
	public String processCreateIngreso(@Valid Ingreso ingreso, BindingResult result) {
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "ingresos/crearOEditarIngreso";
		} else {
			this.ingresoService.saveIngreso(ingreso);
			return "redirect:/economistas/ingreso/" + ingreso.getId();
		}
	}

}
