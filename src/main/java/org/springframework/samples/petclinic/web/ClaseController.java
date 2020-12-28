package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.service.AdiestradorService;
import org.springframework.samples.petclinic.service.ClaseService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClaseController {
	private final ClaseService claseService;
	private final AdiestradorService adiestradorService;
	private final SecretarioService secretarioService;
	private final PetService petService;
	
	@Autowired
	public ClaseController(ClaseService claseService, AdiestradorService adiestradorService, SecretarioService secretarioService, 
			PetService petService) {
		this.claseService = claseService;
		this.adiestradorService = adiestradorService;
		this.secretarioService = secretarioService;
		this.petService = petService;
	}
	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}
	
	@ModelAttribute("adiestradores")
	public Collection<String> populateAdiestradores() {
		return this.adiestradorService.findNameAndLastnameAdiestrador();
	}
	
	//ADIESTRADOR
	
	@GetMapping(value = "/adiestradores/clases")
	public String listadoClasesByAdiestradorId(Map<String, Object> model, final Principal principal) {
		System.out.println(principal.getName());
		int idAdiestrador = this.adiestradorService.findAdiestradorIdByUsername(principal.getName());
		System.out.println(idAdiestrador);
		System.out.println("ID DEL Adiestrador"+idAdiestrador);
		Collection<Clase> clases= claseService.findClaseByAdiestradorId(idAdiestrador);
		model.put("clases", clases);
		return "clases/clasesList";
		}
	
	@GetMapping(value = "/adiestradores/clases/show/{claseId}")
	public String mostarClasesAdiestrador(@PathVariable("claseId") int claseId, Map<String, Object> model, final Principal principal) {
		Clase clase= claseService.findClaseById(claseId);
		model.put("clase", clase);
		return "clases/showAdiestrador";
		}
	
	
	//OWNERS
	
	@GetMapping(value = "/owners/clases")

	public String listadoClases(Map<String, Object> model, final Principal principal) {

		Collection<Clase> clases= this.claseService.findAllClases();

		model.put("clases", clases);

		return "clases/clasesListOwner";

		}

	

	@GetMapping(value = "owners/clases/show/{claseId}")

	public String mostarClaseOwner(@PathVariable("claseId") int claseId, Map<String, Object> model, final Principal principal) {

		Clase clase= claseService.findClaseById(claseId);

		model.put("clase", clase);

		return "clases/showClaseOwner";

		}
	
	
	//SECRETARIO
	
	@GetMapping(value = "/secretarios/clases")

	public String listadoClasesSecretario(Map<String, Object> model, final Principal principal) {

		Collection<Clase> clases= this.claseService.findAllClases();

		model.put("clases", clases);

		return "clases/clasesListSecretario";

		}

	

	@GetMapping(value = "secretarios/clases/show/{claseId}")

	public String mostarClaseSecretario(@PathVariable("claseId") int claseId, Map<String, Object> model, final Principal principal) {

		Clase clase= claseService.findClaseById(claseId);

		model.put("clase", clase);

		return "clases/showClaseSecretario";

		}
	
	@GetMapping(value = "secretarios/clases/show/{claseId}/edit")
	public String initEditClase(@PathVariable("claseId") int claseId, Map<String, Object> model ) {
		Clase clase= claseService.findClaseById(claseId);
		model.put("clase", clase);
		return "clases/crearOEditarClase";
	}
	
	@PostMapping(value = "secretarios/clases/show/{claseId}/edit")
	public String processEditClase(@Valid Clase clase, BindingResult result,final Principal principal, 
			@PathVariable("claseId") int claseId) {
		if(result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "clases/crearOEditarClase";
		}else if(clase.getNumeroPlazasTotal()<clase.getNumeroPlazasDisponibles()){
			result.rejectValue("numeroPlazasDisponibles","El número de plazas totales debe ser mayor que el de las plazas disponibles",
					"El número de plazas totales debe ser mayor que el de las plazas disponibles");
			return "clases/crearOEditarClase";
		}else if(clase.getFechaHoraFin().isBefore(clase.getFechaHoraInicio())){
			result.rejectValue("fechaHoraFin","La fecha y hora de inicio de una clase deben ser anteriores a la de fin",
					"La fecha y hora de inicio de una clase deben ser anteriores a la de fin");
			return "clases/crearOEditarClase";
		}else if(this.claseService.findByName(clase.getName()).size()>1){
			result.rejectValue("name","Ya existe una clase con este nombre",
					"Ya existe una clase con este nombre");
			return "clases/crearOEditarClase";
		}else {
			Secretario sec = this.secretarioService.findSecretarioByUsername(principal.getName());
			clase.setSecretario(sec);
			clase.setId(claseId);
			this.claseService.saveClase(clase);
			return "redirect:/secretarios/clases/show/{claseId}";
		}
	}
	
	@GetMapping(value = "secretarios/clases/show/{claseId}/delete")
	public String deleteClase(Map<String, Object> model,@PathVariable("claseId") int claseId) {
		Clase clase= claseService.findClaseById(claseId);
		this.claseService.deleteClase(clase);
		return "redirect:/secretarios/clases";
	}
	
	@GetMapping(value = "secretarios/clases/new")  ///owners/comentarios/new
	public String initCreateClase(Map<String, Object> model, final Principal principal) {
		Clase clase = new Clase();
		Secretario sec = this.secretarioService.findSecretarioByUsername(principal.getName());
		clase.setSecretario(sec);
		model.put("clase", clase);
		return "clases/crearOEditarClase";
	}

	@PostMapping(value = "secretarios/clases/new")
	public String processCreateClase(@Valid Clase clase, BindingResult result,final Principal principal) {
		Secretario sec = this.secretarioService.findSecretarioByUsername(principal.getName());
		clase.setSecretario(sec);
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "clases/crearOEditarClase";
		}else if(clase.getNumeroPlazasTotal()<clase.getNumeroPlazasDisponibles()){
			result.rejectValue("numeroPlazasDisponibles","El número de plazas totales debe ser mayor que el de las plazas disponibles",
					"El número de plazas totales debe ser mayor que el de las plazas disponibles");
			return "clases/crearOEditarClase";
		}else if(clase.getFechaHoraFin().isBefore(clase.getFechaHoraInicio())){
			result.rejectValue("fechaHoraFin","La fecha y hora de inicio de una clase deben ser anteriores a la de fin",
					"La fecha y hora de inicio de una clase deben ser anteriores a la de fin");
			return "clases/crearOEditarClase";
		}else if(this.claseService.findByName(clase.getName()).size()>=1){
			result.rejectValue("name","Ya existe una clase con este nombre",
					"Ya existe una clase con este nombre");
			return "clases/crearOEditarClase";
		} else {
			this.claseService.saveClase(clase);
			return "redirect:/secretarios/clases";
		}
	}
}
