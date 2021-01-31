package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.model.Tutoria;
import org.springframework.samples.petclinic.service.AdiestradorService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.TutoriaService;
import org.springframework.samples.petclinic.service.exceptions.MismaHoraTutoriaException;
import org.springframework.samples.petclinic.service.exceptions.NumeroTutoriasMaximoPorDiaException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TutoriaController {

	private TutoriaService tutoriaService;
	private AdiestradorService adiestradorService;
	private PetService petService;
	
	@Autowired
	public TutoriaController(TutoriaService tutoriaService, AdiestradorService adiestradorService, PetService petService) {
		this.tutoriaService = tutoriaService;
		this.adiestradorService = adiestradorService;
		this.petService = petService;
	}
	@GetMapping(value = "/adiestradores/tutorias")
	public String listadoTutorias(Map<String, Object> model) {
		List<Tutoria> tutorias = tutoriaService.findAllTutorias();
		model.put("tutorias", tutorias);
		return "tutorias/tutoriasList";
	}
	
	@GetMapping(value = "/adiestradores/tutorias/show/{tutoriaId}")
	public String mostrarTutoria(Map<String, Object> model, @PathVariable ("tutoriaId") int Id) {
		Tutoria tutoria = tutoriaService.findTutoriaById(Id);
		model.put("tutoria", tutoria);
		return "tutorias/tutoriaShowAdiestrador";
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
	public String initCreateTutoria(Map<String, Object> model, final Principal principal, @PathVariable("petId") int Id) {
		Tutoria tutoria = new Tutoria();
		tutoria.setPet(this.petService.findPetById(Id));
		int idAdiestrador = this.adiestradorService.findAdiestradorIdByUsername(principal.getName());
		Adiestrador adiestrador = this.adiestradorService.findAdiestradorById(idAdiestrador);
		tutoria.setAdiestrador(adiestrador);
		model.put("tutoria", tutoria);
		return "tutorias/crearOEditarTutoria";
	}

	@PostMapping(value = "adiestradores/tutorias/pets/{petId}/new")
	public String processCreateTutoria(@Valid Tutoria tutoria,@PathVariable("petId") int Id, @Valid BindingResult result,final Principal principal) 
			throws DataAccessException, MismaHoraTutoriaException, NumeroTutoriasMaximoPorDiaException {
		int idAdiestrador = this.adiestradorService.findAdiestradorIdByUsername(principal.getName());
		Adiestrador adiestrador = this.adiestradorService.findAdiestradorById(idAdiestrador);
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
			}catch(NumeroTutoriasMaximoPorDiaException ex) {
				result.rejectValue("fechaHora","No se puede crear la tutoría ya que ha cumplido el máximo de tutorías permitido por día, violación regla de negocio",
						"No se puede crear la tutoría ya que ha cumplido el máximo de tutorías permitido por día, violación regla de negocio");
				return "tutorias/crearOEditarTutoria";
			}
			return "redirect:/adiestradores/tutorias";
		}
	}
	
	@GetMapping(value = "adiestradores/tutorias/show/{tutoriaId}/edit")
	public String initEditClase(@PathVariable("tutoriaId") int tutoriaId, Map<String, Object> model ) {
		Tutoria tutoria = tutoriaService.findTutoriaById(tutoriaId);
		model.put("tutoria", tutoria);
		return "tutorias/crearOEditarTutoria";
	}
	
	@PostMapping(value = "adiestradores/tutorias/show/{tutoriaId}/edit")
	public String processEditClase(@Valid Tutoria tutoria, BindingResult result,final Principal principal, 
			@PathVariable("tutoriaId") int tutoriaId) throws DataAccessException, MismaHoraTutoriaException, NumeroTutoriasMaximoPorDiaException {
		
		if(result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "tutorias/crearOEditarTutoria";
		}else {
			try {
				this.tutoriaService.saveTutoria(tutoria, true);
			}catch(MismaHoraTutoriaException ex) {
				result.rejectValue("fechaHora","No se puede cambiar la hora de la tutoría ya que hay una existente en dicha hora, violación regla de negocio",
						"No se puede cambiar la hora de la tutoría ya que hay una existente en dicha hora, violación regla de negocio");
				return "tutorias/crearOEditarTutoria";
			}catch(NumeroTutoriasMaximoPorDiaException ex) {
				result.rejectValue("fechaHora","No se puede crear la tutoría ya que ha cumplido el máximo de tutorías permitido por día, violación regla de negocio",
						"No se puede crear la tutoría ya que ha cumplido el máximo de tutorías permitido por día, violación regla de negocio");
				return "tutorias/crearOEditarTutoria";
			}
			return "redirect:/adiestradores/tutorias";
		}
	}
}
