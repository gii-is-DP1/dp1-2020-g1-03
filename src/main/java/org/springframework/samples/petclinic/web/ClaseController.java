package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.model.ApuntarClase;
import org.springframework.samples.petclinic.model.CategoriaClase;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.service.AdiestradorService;
import org.springframework.samples.petclinic.service.ClaseService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.samples.petclinic.service.exceptions.ClasePisadaDelAdiestradorException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaClasesDiasException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaTipoMascotaException;
import org.springframework.samples.petclinic.service.exceptions.LimiteAforoClaseException;
import org.springframework.samples.petclinic.service.exceptions.MacostaYaApuntadaException;
import org.springframework.samples.petclinic.service.exceptions.SolapamientoDeClasesException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ClaseController {
	private final ClaseService claseService;
	private final AdiestradorService adiestradorService;
	private final SecretarioService secretarioService;
	private final PetService petService;
	private final OwnerService ownerService;
	
	@Autowired
	public ClaseController(ClaseService claseService, AdiestradorService adiestradorService, SecretarioService secretarioService, 
			PetService petService, OwnerService ownerService) {
		this.claseService = claseService;
		this.adiestradorService = adiestradorService;
		this.secretarioService = secretarioService;
		this.petService = petService;
		this.ownerService = ownerService;
	}
	
	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new ApuntarClaseValidator());
		dataBinder.addCustomFormatter(new ApuntarClaseFormatter(petService));
	}
	
	
	@InitBinder("fechaHoraInicio")
	public void initFechaHoraInicioBinder(WebDataBinder dataBinder) {
		dataBinder.addCustomFormatter(new FechaHoraFormatter());
	}
	@InitBinder("fechaHoraFin")
	public void initFechaHoraFinBinder(WebDataBinder dataBinder) {
		dataBinder.addCustomFormatter(new FechaHoraFormatter());
	}
	
	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}
	
	@ModelAttribute("adiestradores")
	public Collection<String> populateAdiestradores() {
		return this.adiestradorService.findNameAndLastnameAdiestrador();
	}
	
	@ModelAttribute("categoriaClase")
	public Collection<CategoriaClase> categoriasClase() {
		return this.claseService.findAllCategoriasClase();
	}
	
	//ADIESTRADOR
	
	@GetMapping(value = "/adiestradores/clases")
	public String listadoClasesByAdiestradorId(Map<String, Object> model, final Principal principal) {
		System.out.println(principal.getName());
		Adiestrador adiestrador = this.adiestradorService.findAdiestradorByUsername(principal.getName());
		Collection<Clase> clases= claseService.findClaseByAdiestradorId(adiestrador.getId());
		model.put("clases", clases);
		return "clases/clasesList";
		}
	
	@GetMapping(value = "/adiestradores/clases/show/{claseId}")
	public String mostarClasesAdiestrador(@PathVariable("claseId") int claseId, Map<String, Object> model, final Principal principal) {
		Clase clase= claseService.findClaseById(claseId);
		if(clase.isNew()) {
			return "exception";
		}if(clase.getAdiestrador().equals(this.adiestradorService.findAdiestradorByUsername(principal.getName()))) {
		model.put("clase", clase);
		return "clases/showAdiestrador";
		}else {
			return "exception";
		}
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
		if(clase.getAdiestrador().equals(this.ownerService.findOwnerByUsername(principal.getName()))) {
		model.put("clase", clase);
		return "clases/showClaseOwner";
		}else {
			return "exception";
		}
	}
	@GetMapping(value = "owners/clases/show/apuntar/{claseId}")	
	public String initApuntarMascota(@PathVariable("claseId") int claseId, Map<String, Object> model, final Principal principal) {
		Clase clas = this.claseService.findClaseById(claseId);
		ApuntarClase apClase = new ApuntarClase();
		apClase.setClase(clas);
		model.put("apuntarClase", apClase);
		Owner owner = this.ownerService.findOwnerByUsername(principal.getName());
		List<String> pets = this.petService.findNameMascota(owner);
		model.put("pets", pets);
		return "clases/apuntarClases";
	}
	
	@PostMapping(value = "owners/clases/show/apuntar/{claseId}")
	public String processApuntarMascota(@Valid ApuntarClase apClase, BindingResult result,final Principal principal, 
			@PathVariable("claseId") int claseId, Map<String, Object> model) throws DataAccessException, LimiteAforoClaseException, DiferenciaClasesDiasException, ClasePisadaDelAdiestradorException {
		apClase.setPet(apClase.getPet());
		Owner owner = this.ownerService.findOwnerByUsername(principal.getName());
		List<String> pets = this.petService.findNameMascota(owner);
		model.put("pets", pets);
		Clase clas = this.claseService.findClaseById(claseId);
		apClase.setClase(clas);
		if(result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "clases/apuntarClases";
		}else if(apClase.getClase().getFechaHoraInicio().isBefore(LocalDateTime.now())){
			result.rejectValue("pet","La clase ya ha comenzado o ha terminado",
					"La clase ya ha comenzado o ha terminado");
			
			return "clases/apuntarClases";
		}else {
			try{
				this.claseService.apuntarMascota(apClase);
			}catch(DiferenciaTipoMascotaException ex){
            result.rejectValue("pet", "El tipo de la mascota no es el adecuado para esta clase, violación de la regla de negocio", "El tipo de la mascota no es el adecuado para esta clase, violación de la regla de negocio");
            return "clases/apuntarClases";
	        }catch(DiferenciaClasesDiasException ex2){
	            result.rejectValue("pet", "No puede apuntar a su mascota en clase, límite de clase semanal alcanzado. Violación de regla de negocio", "No puede apuntar a su mascota en clase, límite de clase semanal alcanzado. Violación de regla de negocio");
	            return "clases/apuntarClases";
	        }catch(LimiteAforoClaseException ex3){
	            result.rejectValue("pet", "Aforo completo de la clase, violación de regla de negocio", "Aforo completo de la clase, violación de regla de negocio");
	            return "clases/apuntarClases";
	        }catch(SolapamientoDeClasesException ex4){
	        	result.rejectValue("pet","No puede apuntar a su mascota porque se pisa con otra clase a la que está apuntada, violación de regla de negocio",
						"No puede apuntar a su mascota porque se pisa con otra clase a la que está apuntada, violación de regla de negocio");
				return "clases/apuntarClases";
	        }catch(MacostaYaApuntadaException ex5){
	        	result.rejectValue("pet","Ya se ha apuntado a esta clase, violación de regla de negocio",
						"Ya se ha apuntado a esta clase, violación de regla de negocio");
				return "clases/apuntarClases";
	        }
			
			
			return "redirect:/owners/clases";
		}
	}
	
	@GetMapping(value = "/owners/clases/show/{claseId}/pets")
	public String listadoPetsEnClases(@PathVariable("claseId") int claseId,
			Map<String, Object> model, final Principal principal) {
		List<ApuntarClase> mascotasEnClase = new ArrayList<>(
				this.claseService.findMascotasApuntadasEnClaseByClaseId(claseId));
		List<Pet> pets = new ArrayList<>();
		for (int i = 0; i < mascotasEnClase.size(); i++) {
			pets.add(mascotasEnClase.get(i).getPet());
		}
		model.put("pets", pets);
		return "clases/mascotasApuntadas";
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
			@PathVariable("claseId") int claseId) throws DataAccessException, ClasePisadaDelAdiestradorException{
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
			try{
				this.claseService.saveClase(clase);
			}catch(ClasePisadaDelAdiestradorException ex){
				result.rejectValue("adiestrador","No puede dar la clase este adiestrador porque se pisa con otra clase a la que debe impartir",
						"No puede dar la clase este adiestrador porque se pisa con otra clase a la que debe impartir");
            return "clases/crearOEditarClase";
	        }
			return "redirect:/secretarios/clases/show/{claseId}";
		}
	}
	
	@GetMapping(value = "secretarios/clases/show/{claseId}/delete")
	public String deleteClase(Map<String, Object> model,@PathVariable("claseId") int claseId) {
		Clase clase= claseService.findClaseById(claseId);
		this.claseService.deleteClase(clase);
		return "redirect:/secretarios/clases";
	}
	
	@GetMapping(value = "secretarios/clases/new")  
	public String initCreateClase(Map<String, Object> model, final Principal principal) {
		Clase clase = new Clase();
		Secretario sec = this.secretarioService.findSecretarioByUsername(principal.getName());
		clase.setSecretario(sec);
		model.put("clase", clase);
		return "clases/crearOEditarClase";
	}

	@PostMapping(value = "secretarios/clases/new")
	public String processCreateClase(@Valid Clase clase, BindingResult result,final Principal principal) throws DataAccessException, ClasePisadaDelAdiestradorException {
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
		}else {
			try{
				this.claseService.saveClase(clase);
			}catch(ClasePisadaDelAdiestradorException ex){
				result.rejectValue("adiestrador","No puede dar la clase este adiestrador porque se pisa con otra clase a la que debe impartir",
						"No puede dar la clase este adiestrador porque se pisa con otra clase a la que debe impartir");
            return "clases/crearOEditarClase";
	        }
			return "redirect:/secretarios/clases";
		}
	}
}