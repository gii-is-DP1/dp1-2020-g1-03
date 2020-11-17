/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.springframework.samples.petclinic.service.EconomistaService;
import org.springframework.samples.petclinic.service.GastoService;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/economistas/gasto")
public class GastoController {

	//private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final GastoService gastoService;
        private final EconomistaService economistaService;

	@Autowired
	public GastoController(GastoService gastoService, EconomistaService economistaService) {
		this.gastoService = gastoService;
                this.economistaService = economistaService;
	}
	
//	@ModelAttribute("economista")
//	public Owner findOwner(@PathVariable("economistaId") int economistaId) {
//		return this.economistaService.findEconomistaById(economistaId);
//	}

	@GetMapping()
	public String listadoGastos(Map<String, Object> model, Principal principal) {
		//String vista="owners/{ownerId}/listadoCitas";
		System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
		if(SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString().contains("economista")) {
			List<Gasto> gastos= gastoService.findAllGastosS();
			model.put("gastos", gastos);
			return "gastos/gastosList";
		} else {
			//ModelAndView exception = new ModelAndView("exception");
			return "exception";
		}
		
		}
	
	@GetMapping(value = "{gastoId}")
	public String mostarGastos(@PathVariable("gastoId") int gastoId,Map<String, Object> model) {
		//String vista="owners/{ownerId}/listadoCitas";
		Gasto gasto= gastoService.findGastoById(gastoId);
		System.out.println(gasto);
		if(gasto==null) {
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
			return "gastos/crearOEditarGasto";
		}
		else {
			int idEcon = this.economistaService.findEconomistaIdByUsername(principal.getName());
			Economista econ= this.economistaService.findEconomistaById(idEcon);
			gasto.setEconomista(econ);
			//gasto.setId(gastoId);
			this.gastoService.saveGasto(gasto);
			return "redirect:/economistas/gasto";
		}
	}
                
}
