
package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Economista;
import org.springframework.samples.petclinic.model.Gasto;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.samples.petclinic.service.EconomistaService;
import org.springframework.samples.petclinic.service.GastoService;

@Controller
@RequestMapping("/economistas/gasto")
public class GastoController {


	private static final Logger logger =
			Logger.getLogger(GastoController.class.getName());


	private final GastoService gastoService;
        private final EconomistaService economistaService;

	@Autowired
	public GastoController(GastoService gastoService, EconomistaService economistaService) {
		this.gastoService = gastoService;
                this.economistaService = economistaService;
	}

	

	@GetMapping()
	public String listadoGastos(Map<String, Object> model, Principal principal) {
		
			List<Gasto> gastos= gastoService.findAllGastosS();
			model.put("gastos", gastos);
			return "gastos/gastosList";

		
		}
	
	@GetMapping(value = "{gastoId}")
	public String mostarGastos(@PathVariable("gastoId") int gastoId,Map<String, Object> model) {
		Gasto gasto= gastoService.findGastoById(gastoId);
		if(gasto==null) {
			logger.log(Level.WARNING, "Gasto vacio");
			return "exception";
		} else {
		model.put("gasto", gasto);
		return "gastos/gastosShow";
		}
		}
	
	@GetMapping(value = "/{gastoId}/edit")
	public String initEditGasto(@PathVariable("gastoId") int gastoId, Map<String, Object> model) {
		Gasto gasto = this.gastoService.findGastoById(gastoId);
		model.put("gasto", gasto);
		return "gastos/crearOEditarGasto";
	}

	@PostMapping(value = "/{gastoId}/edit")
	public String processEditGasto(final Principal principal,@Valid Gasto gasto, BindingResult result,
			@PathVariable("gastoId") int gastoId) {
		if (result.hasErrors()) {
			logger.log(Level.WARNING, "Error detected", result.getAllErrors());
			return "gastos/crearOEditarGasto";
		}
		else {
			int idEcon = this.economistaService.findEconomistaIdByUsername(principal.getName());
			Economista econ= this.economistaService.findEconomistaById(idEcon);
			gasto.setEconomista(econ);
			gasto.setId(gastoId);
			this.gastoService.saveGasto(gasto);
			return "redirect:/economistas/gasto/{gastoId}";
		}
	}
	
	@GetMapping(value = "/new")
	public String initCrearGasto(final Principal principal, Map<String, Object> model) {
		Gasto gasto = new Gasto();
		int idEcon = this.economistaService.findEconomistaIdByUsername(principal.getName());
		Economista econ= this.economistaService.findEconomistaById(idEcon);
		gasto.setEconomista(econ);
		model.put("gasto", gasto);
		return "gastos/crearOEditarGasto";
	}

	@PostMapping(value = "/new")
	public String processCrearGasto(final Principal principal,@Valid Gasto gasto, BindingResult result) {
		if (result.hasErrors()) {
			logger.log(Level.WARNING, "Error detected", result.getAllErrors());
			return "gastos/crearOEditarGasto";
		}
		else {
			int idEcon = this.economistaService.findEconomistaIdByUsername(principal.getName());
			Economista econ= this.economistaService.findEconomistaById(idEcon);
			gasto.setEconomista(econ);
			this.gastoService.saveGasto(gasto);
			return "redirect:/economistas/gasto";
		}
	}
                
}
