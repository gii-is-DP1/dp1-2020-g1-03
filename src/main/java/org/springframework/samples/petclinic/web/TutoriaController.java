package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Tutoria;
import org.springframework.samples.petclinic.service.AdiestradorService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.TutoriaService;
import org.springframework.samples.petclinic.service.exceptions.MismaHoraTutoriaException;
import org.springframework.samples.petclinic.service.exceptions.MismaHoraTutoriaPetException;
import org.springframework.samples.petclinic.service.exceptions.NumeroTutoriasMaximoPorDiaException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TutoriaController {

	private TutoriaService tutoriaService;
	private OwnerService ownerService;
	private AdiestradorService adiestradorService;
	private PetService petService;
	private static final Logger logger =
			Logger.getLogger(VacunaController.class.getName());
	
	@Autowired
	public TutoriaController(TutoriaService tutoriaService, AdiestradorService adiestradorService, PetService petService, OwnerService ownerService) {
		this.ownerService = ownerService;
		this.tutoriaService = tutoriaService;
		this.adiestradorService = adiestradorService;
		this.petService = petService;
	}
	@GetMapping(value = "/adiestradores/tutorias")
	public String listadoTutorias(Map<String, Object> model) {
		List<Tutoria> tutorias = this.tutoriaService.findAllTutorias();
		model.put("tutorias", tutorias);
		return "tutorias/tutoriasList";
	}
	
	@GetMapping(value = "/adiestradores/tutorias/show/{tutoriaId}")
	public String mostrarTutoria(Tutoria tutoria,final Principal principal,@PathVariable ("tutoriaId") int Id, Map<String, Object> model) {
		tutoria = this.tutoriaService.findTutoriaById(Id);
		if(tutoria.getAdiestrador().equals(this.adiestradorService.findAdiestradorByUsername(principal.getName()))) {
		model.put("tutoria", tutoria);
		return "tutorias/tutoriaShowAdiestrador";
		}else {
			return "exception";
		}
	}
	
	@GetMapping(value = "/owners/tutorias") 
	public String listadoTutoriasOwner(final Principal principal, Map<String, Object> model) {
		int idOwner = this.ownerService.findOwnerByUsername(principal.getName()).getId();
		Collection<Tutoria> tutorias = this.tutoriaService.findTutoriaByOwnerId(idOwner);
		model.put("tutorias", tutorias);
		return "tutorias/tutoriasListOwner";
	}
	
	@GetMapping(value = "/owners/tutorias/show/{tutoriaId}")
	public String mostrarTutoriaOwner(final Principal principal,@PathVariable ("tutoriaId") int Id, Map<String, Object> model, Tutoria tutoria) {
		tutoria = this.tutoriaService.findTutoriaById(Id);
		if(tutoria.getOwner().equals(this.ownerService.findOwnerByUsername(principal.getName()))) {
		model.put("tutoria", tutoria);
		return "tutorias/tutoriaShowOwner";
		}else {
			return "exception";
		}
	}
	
	

	/*CREAR Y EDITAR TUTORIAS */
	
	
	@GetMapping(value = "/adiestradores/tutorias/pets/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("pet", new Pet());
		return "tutorias/EncontrarMascotas";
	}

	@GetMapping(value = "/adiestradores/tutorias/pets")
	public String processFindForm(Pet pet, BindingResult result, Map<String, Object> model) {
		Collection<Pet> results = this.tutoriaService.findMascotaByName(pet.getName());
		if (results.isEmpty()) {
			if (pet.getName().equals("")) {
				Collection<Pet> res = this.petService.findAllPets();
				model.put("pets", res);
				return "tutorias/MascotasList";
			}
			result.rejectValue("name", "notFound", "not found");
			return "tutorias/EncontrarMascotas";
		}
		else if (results.size() == 1) {
			pet = results.iterator().next();
			return "redirect:/adiestradores/tutorias/pets/" + pet.getId()+"/new";
		}
		else {
			model.put("pets", results);
			return "tutorias/MascotasList";
		}
		
	}
	
	@GetMapping(value = "adiestradores/tutorias/pets/{petId}/new")  ///owners/comentarios/new
	public String initCreateTutoria(Pet pet, Map<String, Object> model, final Principal principal, @PathVariable("petId") int Id) {
		Tutoria tutoria = new Tutoria();
		tutoria.setPet(this.petService.findPetById(Id));
		Adiestrador adiestrador = this.adiestradorService.findAdiestradorByUsername(principal.getName());
		tutoria.setAdiestrador(adiestrador);
		model.put("tutoria", tutoria);
		return "tutorias/crearOEditarTutoria";
	}

	@PostMapping(value = "adiestradores/tutorias/pets/{petId}/new")
	public String processCreateTutoria(Pet pet,@Valid Tutoria tutoria, BindingResult result, @PathVariable("petId") int Id,final Principal principal) 
			throws DataAccessException, MismaHoraTutoriaException, NumeroTutoriasMaximoPorDiaException, MismaHoraTutoriaPetException {
		pet=this.petService.findPetById(Id);
		tutoria.setId(tutoria.getId());
		tutoria.setPet(pet);
		Adiestrador adiestrador = this.adiestradorService.findAdiestradorByUsername(principal.getName());
		tutoria.setAdiestrador(adiestrador);
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "tutorias/crearOEditarTutoria";
		}else {
			try {
				this.tutoriaService.saveTutoria(tutoria, false);
			}catch(MismaHoraTutoriaException ex) {
				result.rejectValue("fechaHora","No se puede crear la tutoría ya que hay una existente en dicha hora, violación regla de negocio",
						"No se puede crear la tutoría ya que hay una existente en dicha hora, violación regla de negocio");
				return "tutorias/crearOEditarTutoria";
			}catch(NumeroTutoriasMaximoPorDiaException ex2) {
				result.rejectValue("fechaHora","No se puede crear la tutoría ya que ha cumplido el máximo de tutorías permitido por día, violación regla de negocio",
						"No se puede crear la tutoría ya que ha cumplido el máximo de tutorías permitido por día, violación regla de negocio");
				return "tutorias/crearOEditarTutoria";
			}catch(MismaHoraTutoriaPetException ex4) {
				result.rejectValue("fechaHora","No se puede crear la tutoría ya que hay una existente en dicha hora por parte de la mascota, violación regla de negocio",
						"No se puede crear la tutoría ya que hay una existente en dicha hora por parte de la mascota, violación regla de negocio");
				return "tutorias/crearOEditarTutoria";
			}
			return "redirect:/adiestradores/tutorias";
		}
	}
	
	@GetMapping(value = "adiestradores/tutorias/show/{tutoriaId}/edit")
	public String initEditTutoria(final Principal principal,Map<String, Object> model, @PathVariable ("tutoriaId") int tutoriaId) {
		Tutoria tutoria = this.tutoriaService.findTutoriaById(tutoriaId);
		if(tutoria.getAdiestrador().equals(this.adiestradorService.findAdiestradorByUsername(principal.getName()))) {
		model.put("tutoria", tutoria);
		return "tutorias/crearOEditarTutoria";
		}else {
			return "exception";
		}
	}
	
	@PostMapping(value = "adiestradores/tutorias/show/{tutoriaId}/edit")
	public String processEditTutoria(@Valid Tutoria tutoria, BindingResult result,final Principal principal, 
			@PathVariable("tutoriaId") int tutoriaId) throws DataAccessException, MismaHoraTutoriaException, NumeroTutoriasMaximoPorDiaException, MismaHoraTutoriaPetException {
		Tutoria tutoria2 = this.tutoriaService.findTutoriaById(tutoriaId);
		tutoria.setAdiestrador(tutoria2.getAdiestrador());
		tutoria.setPet(tutoria2.getPet());
		
		if(result.hasErrors()) {
			logger.log(Level.WARNING, "Error detected", result.getAllErrors());
			return "tutorias/crearOEditarTutoria";
		}else {
			try {
				this.tutoriaService.saveTutoria(tutoria, true);
			}catch(MismaHoraTutoriaException ex) {
				result.rejectValue("fechaHora","No se puede cambiar la hora de la tutoría ya que hay una existente en dicha hora, violación regla de negocio",
						"No se puede cambiar la hora de la tutoría ya que hay una existente en dicha hora, violación regla de negocio");
				return "tutorias/crearOEditarTutoria";
			}catch(NumeroTutoriasMaximoPorDiaException ex2) {
				result.rejectValue("fechaHora","No se puede crear la tutoría ya que ha cumplido el máximo de tutorías permitido por día, violación regla de negocio",
						"No se puede crear la tutoría ya que ha cumplido el máximo de tutorías permitido por día, violación regla de negocio");
				return "tutorias/crearOEditarTutoria";
			}catch(MismaHoraTutoriaPetException ex4) {
				result.rejectValue("fechaHora","No se puede crear la tutoría ya que hay una existente en dicha hora por parte de la mascota, violación regla de negocio",
						"No se puede crear la tutoría ya que hay una existente en dicha hora por parte de la mascota, violación regla de negocio");
				return "tutorias/crearOEditarTutoria";
			}
			return "redirect:/adiestradores/tutorias";
		}
	}
}
