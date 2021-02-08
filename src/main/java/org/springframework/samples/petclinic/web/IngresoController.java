
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Economista;
import org.springframework.samples.petclinic.model.Ingreso;
import org.springframework.samples.petclinic.service.EconomistaService;

import org.springframework.samples.petclinic.service.IngresoService;
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
	private static final Logger logger =
			Logger.getLogger(IngresoController.class.getName());

	@Autowired
	public IngresoController(IngresoService ingresoService, EconomistaService economistaService) {
		this.ingresoService = ingresoService;
		this.economistaService = economistaService;
	}

	@GetMapping()
	public String listadoIngresos(Map<String, Object> model, Principal principal) {
			List<Ingreso> ingresos = ingresoService.findAllIngresos();
			model.put("ingresos", ingresos);
			return "ingresos/ingresosList";
		

	}

	@GetMapping(value = "{ingresoId}")
	public String mostarIngreso(@PathVariable("ingresoId") int ingresoId, Map<String, Object> model) {
		Ingreso ingreso = ingresoService.findIngresoById(ingresoId);
		if(ingreso==null) {
			logger.log(Level.WARNING, "Ingreso vacio");
			return "exception";
		}else {
			model.put("ingreso", ingreso);
			return "ingresos/ingresosShow";			
		}

	}

	@GetMapping(value = "/{ingresoId}/edit")
	public String initEditIngreso(@PathVariable("ingresoId") int ingresoId, Map<String, Object> model) {
		Ingreso ingreso = this.ingresoService.findIngresoById(ingresoId);
		model.put("ingreso", ingreso);
		return "ingresos/crearOEditarIngreso";
	}

	@PostMapping(value = "/{ingresoId}/edit")
	public String processEditIngreso(final Principal principal, @Valid Ingreso ingreso, BindingResult result,
			@PathVariable("ingresoId") int ingresoId) {
		if (result.hasErrors()) {
			logger.log(Level.WARNING, "Error detected", result.getAllErrors());
			return "ingresos/crearOEditarIngreso";
		} else {
			int idEcon = this.economistaService.findEconomistaIdByUsername(principal.getName());
			Economista econ = this.economistaService.findEconomistaById(idEcon);
			ingreso.setEconomista(econ);
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
			logger.log(Level.WARNING, "Error detected", result.getAllErrors());
			return "ingresos/crearOEditarIngreso";
		} else {
			this.ingresoService.saveIngreso(ingreso);
			return "redirect:/economistas/ingreso/" + ingreso.getId();
		}
	}

}
