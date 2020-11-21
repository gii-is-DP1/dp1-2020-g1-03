package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Economista;
import org.springframework.samples.petclinic.model.Gasto;
import org.springframework.samples.petclinic.model.Ingreso;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Visit;
import org.springframework.samples.petclinic.service.EconomistaService;
import org.springframework.samples.petclinic.service.GastoService;
import org.springframework.samples.petclinic.service.IngresoService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/economistas/ingreso")
public class IngresoController {

	//private static final String VIEWS_PETS_CREATE_OR_UPDATE_FORM = "pets/createOrUpdatePetForm";

	private final IngresoService ingresoService;
    private final EconomistaService economistaService;

	@Autowired
	public IngresoController(IngresoService ingresoService, EconomistaService economistaService) {
		this.ingresoService = ingresoService;
                this.economistaService = economistaService;
	}
	

	@GetMapping()
	public String listadoIngresos(Map<String, Object> model) {
		List<Ingreso> ingresos= ingresoService.findAllIngresos();
		model.put("ingresos", ingresos);
		return "ingresos/ingresosList";
		}
	
	@GetMapping(value = "{ingresoId}")
	public String mostarIngresos(@PathVariable("ingresoId") int ingresoId,Map<String, Object> model) {
		Ingreso ingreso= ingresoService.findIngresoById(ingresoId);
		model.put("ingreso", ingreso);
		return "ingresos/ingresosShow";
		}
	
	@GetMapping(value = "/{ingresoId}/edit")
	public String initEditIngreso(@PathVariable("ingresoId") int ingresoId, Map<String, Object> model) {
		Ingreso ingreso= this.ingresoService.findIngresoById(ingresoId);
		System.out.println(ingreso+ "INGRESO");
		model.put("ingreso", ingreso);
		return "ingresos/crearOEditarIngreso";
	}

	@PostMapping(value = "/{ingresoId}/edit")
	public String processEditIngreso(final Principal principal,@Valid Ingreso ingreso, BindingResult result,
			@PathVariable("ingresoId") int ingresoId) {
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "ingresos/crearOEditarIngreso";
		}
		else {
			int idEcon = this.economistaService.findEconomistaIdByUsername(principal.getName());
			Economista econ= this.economistaService.findEconomistaById(idEcon);
			ingreso.setEconomista(econ);
			System.out.println(ingreso.getEconomista());
			ingreso.setId(ingresoId);
			this.ingresoService.saveIngreso(ingreso);
			return "redirect:/economistas/ingreso/{ingresoId}";
		}
	}
                
}