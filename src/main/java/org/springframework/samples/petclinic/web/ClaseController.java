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
import org.springframework.samples.petclinic.model.ApuntarClase;
import org.springframework.samples.petclinic.model.CategoriaClase;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Competicion;
import org.springframework.samples.petclinic.model.CompeticionPet;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.service.AdiestradorService;
import org.springframework.samples.petclinic.service.ClaseService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaClasesDiasException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaTipoMascotaException;
import org.springframework.samples.petclinic.service.exceptions.LimiteAforoClaseException;
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
		dataBinder.addCustomFormatter(new ApuntarClaseFormatter(petService, ownerService));
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
	
	
	
//	@ModelAttribute("pets")
//	public Collection<String> populatePet(final Principal principal) {
//		Integer idOwner = this.ownerService.findOwnerIdByUsername(principal.getName());
//		if(idOwner!=null) {
//		Collection<String> mascotas = this.petService.findNameMascota(idOwner);
//		return mascotas;
//		}else {
//		return new ArrayList<String>();
//		}
//	}
	
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
	
	@GetMapping(value = "owners/clases/show/apuntar/{claseId}")	
	public String initApuntarMascota(@PathVariable("claseId") int claseId, Map<String, Object> model, final Principal principal) {
		Clase clas = this.claseService.findClaseById(claseId);
		ApuntarClase apClase = new ApuntarClase();
		apClase.setClase(clas);
		model.put("apuntarClase", apClase);
		int ownerId = this.ownerService.findOwnerIdByUsername(principal.getName());
		List<String> pets = this.petService.findNameMascota(ownerId);
		model.put("pets", pets);
		return "clases/apuntarClases";
	}
	
	@PostMapping(value = "owners/clases/show/apuntar/{claseId}")
	public String processApuntarMascota(@Valid ApuntarClase apClase, BindingResult result,final Principal principal, 
			@PathVariable("claseId") int claseId, Map<String, Object> model) throws DataAccessException, LimiteAforoClaseException, DiferenciaClasesDiasException {
		apClase.setPet(apClase.getPet());
		int ownerId = this.ownerService.findOwnerIdByUsername(principal.getName());
		List<String> pets = this.petService.findNameMascota(ownerId);
		model.put("pets", pets);
		Clase clas = this.claseService.findClaseById(claseId);
		apClase.setClase(clas);
		List<ApuntarClase> clasesApuntadas = this.claseService.findClasesByPetId(apClase.getPet().getId());
		Boolean b=true;
		int i=0;
		Boolean apuntada=false;
		if(!clasesApuntadas.isEmpty()) {
			while(b && i<clasesApuntadas.size() && apuntada.equals(false)) {
				if(clasesApuntadas.get(i).getClase().getFechaHoraFin().isAfter(apClase.getClase().getFechaHoraInicio()) 
						&& clasesApuntadas.get(i).getClase().getFechaHoraInicio().isBefore(apClase.getClase().getFechaHoraFin())
						&& clasesApuntadas.get(i).getClase().getId()!=apClase.getClase().getId()) {
					b=false;		
				}
				if(clasesApuntadas.get(i).getClase().getId().equals(apClase.getClase().getId())){
					apuntada=true;
				}
				i++;
			}
		}
		if(result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "clases/apuntarClases";
		}else if(apClase.getClase().getFechaHoraInicio().isBefore(LocalDateTime.now())){
			result.rejectValue("pet","La clase ya ha comenzado o ha terminado",
					"La clase ya ha comenzado o ha terminado");
			
			return "clases/apuntarClases";
		
		}else if(b==false){
			result.rejectValue("pet","No puede apuntar a su mascota porque se pisa con otra clase a la que está apuntada",
					"No puede apuntar a su mascota porque se pisa con otra clase a la que está apuntada");
			return "clases/apuntarClases";
		}else if(apuntada){
			result.rejectValue("pet","Ya se ha apuntado a esta clase",
					"Ya se ha apuntado a esta clase");
			return "clases/apuntarClases";
		}else {
			try{
				this.claseService.escogerMascota(apClase);
			}catch(DiferenciaTipoMascotaException ex){
            result.rejectValue("pet", "El tipo de la mascota no es el adecuado para esta clase, violación de la regla de negocio", "El tipo de la mascota no es el adecuado para esta clase, violación de la regla de negocio");
            return "clases/apuntarClases";
        }catch(DiferenciaClasesDiasException ex2){
            result.rejectValue("pet", "No puede apuntar a su mascota en clase, límite de clase semanal alcanzado. Violación de regla de negocio", "No puede apuntar a su mascota en clase, límite de clase semanal alcanzado. Violación de regla de negocio");
            return "clases/apuntarClases";
        }catch(LimiteAforoClaseException ex3){
            result.rejectValue("pet", "Aforo completo de la clase, violación de regla de negocio", "Aforo completo de la clase, violación de regla de negocio");
            return "clases/apuntarClases";
        }	
			apClase.getClase().setNumeroPlazasDisponibles(apClase.getClase().getNumeroPlazasDisponibles()-1);
			this.claseService.saveClase(apClase.getClase());
			
			return "redirect:/owners/clases";
		}
	}
	
	@GetMapping(value = "/owners/clases/show/{claseId}/pets")
	public String listadoPetsEnClases(@PathVariable("claseId") int claseId,
			Map<String, Object> model, final Principal principal) {
		List<ApuntarClase> clases = new ArrayList<>(
				this.claseService.findMascotasApuntadasEnClaseByClaseId(claseId));
		List<Pet> pets = new ArrayList<>();
		Pet aux;
		for (int i = 0; i < clases.size(); i++) {
			int idClasePet = clases.get(i).getClase().getId();
			aux = this.claseService.findPetByClasePetId(idClasePet);
			pets.add(aux);
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
			@PathVariable("claseId") int claseId) {
		List<Clase>clases=this.claseService.findClasesAdiestrador(clase.getAdiestrador());
		boolean b=true;
		int i=0;
		if(!clases.isEmpty()) {
			while(b && i<clases.size()) {
				if(clases.get(i).getFechaHoraFin().isAfter(clase.getFechaHoraInicio())) {
					b=false;		
				}
				i++;
			}
		}
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
		}else if(b==false){
			result.rejectValue("adiestrador","No puede dar la clase este adiestrador porque se pisa con otra clase a la que debe impartir",
					"No puede dar la clase este adiestrador porque se pisa con otra clase a la que debe impartir");
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
		List<ApuntarClase> clases = claseService.findMascotasApuntadasEnClaseByClaseId(claseId);
		if(clases.isEmpty() || clases==null) {
			this.claseService.deleteClase(clase);
		}else {
			for(int i=0; i<clases.size(); i++) {
				this.claseService.deleteApuntarClase(clases.get(i));
			}
			this.claseService.deleteClase(clase);
		}
		
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
		List<Clase>clases=this.claseService.findClasesAdiestrador(clase.getAdiestrador());
		boolean b=true;
		int i=0;
		if(!clases.isEmpty()) {
			while(b && i<clases.size()) {
				if(clases.get(i).getFechaHoraFin().isAfter(clase.getFechaHoraInicio())) {
					b=false;		
				}
				i++;
			}
		}
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
		}else if(b==false){
			result.rejectValue("adiestrador","No puede dar la clase este adiestrador porque se pisa con otra clase a la que debe impartir",
					"No puede dar la clase este adiestrador porque se pisa con otra clase a la que debe impartir");
			return "clases/crearOEditarClase";
		}else {
			this.claseService.saveClase(clase);
			return "redirect:/secretarios/clases";
		}
	}
}