package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.model.Economista;
import org.springframework.samples.petclinic.model.Ingreso;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ComentarioService;
import org.springframework.samples.petclinic.service.EconomistaService;
import org.springframework.samples.petclinic.service.IngresoService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.exceptions.ComentariosMaximoPorCitaException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaTipoMascotaException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
//@RequestMapping("/vets")
public class ComentarioController {
	private final ComentarioService comentarioService;
    private final OwnerService ownerService;
    private final VetService vetService;

	@Autowired
	public ComentarioController(ComentarioService comentarioService, 
			OwnerService ownerService, VetService vetService) {
		this.comentarioService = comentarioService;
        this.ownerService = ownerService;
        this.vetService = vetService;
	}
	
	
	
	//VETERINARIO
	@InitBinder("vet")
	public void initVetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new ComentarioValidator());
	}
	
	
	
	@GetMapping(value = "/vets/comentarios")
	public String listadoComentariosByVetId(Map<String, Object> model, final Principal principal) {
		System.out.println(principal.getName());
		int idVet = this.vetService.findVetIdByUsername(principal.getName());
		System.out.println(idVet);
		System.out.println("ID DEL VETERINARIO"+idVet);
		Collection<Comentario> comentarios= comentarioService.findAllComentariosByVetId(idVet);
		model.put("comentarios", comentarios);
		return "comentarios/comentariosList";
		}

	@GetMapping(value = "/vets/comentarios/show/{comentarioId}")
	public String mostarComentariosDeVet(@PathVariable("comentarioId") int comentarioId, Map<String, Object> model, final Principal principal) {
		Comentario comentario= comentarioService.findComentarioByComentarioId(comentarioId);
		model.put("comentario", comentario);
		return "comentarios/showVet";
		}
	
	

	
	
	//    DUEÑO ABAJO	
	
	
	
	
	
	
	
	@GetMapping(value = "/owners/comentarios")
	public String listadoComentariosByOwnerId(Map<String, Object> model,final Principal principal) {
		int idOwner = this.ownerService.findOwnerIdByUsername(principal.getName());	
		//Owner owner= this.ownerService.findOwnerById(idOwner);
		Collection<Comentario> comentario= comentarioService.findAllComentariosByOwnerId(idOwner);
		model.put("comentarios", comentario);
		return "comentarios/comentariosListOwner";
		}
	@GetMapping(value = "/owners/comentarios/show/{comentarioId}/{ownerId}")
	public String mostarComentariosDeOwner(@PathVariable("comentarioId") int comentarioId,@PathVariable("ownerId") int ownerId, Map<String, Object> model, final Principal principal) {
		
		Comentario comentario= comentarioService.findComentarioByComentarioId(comentarioId);
		model.put("comentario", comentario);
		return "comentarios/show";
		}
	@GetMapping(value = "/owners/comentarios/edit/{comentarioId}/{vetId}")
	public String initEditComentario(@PathVariable("comentarioId") int comentarioId, @PathVariable("vetId") int vetId, Map<String, Object> model) {
		Comentario comentario= this.comentarioService.findComentarioByComentarioId(comentarioId);
		model.put("comentario", comentario);
		Collection<Vet> vets = this.vetService.findVets();
		model.put("vets", vets);
		return "comentarios/crearOEditarComentario";
	}
	
	@PostMapping(value = "/owners/comentarios/edit/{comentarioId}/{vetId}")
	public String processEditComentario(Map<String, Object> model,final Principal principal,@Valid Comentario comentario, @PathVariable("vetId") int vetId, BindingResult result,
			@PathVariable("comentarioId") int comentarioId) {
		int idOw = this.ownerService.findOwnerIdByUsername(principal.getName());
		Owner ow= this.ownerService.findOwnerById(idOw);
		comentario.setOwner(ow);
		comentario.setId(comentarioId);
		comentario.setVet(this.vetService.findVetById(vetId));
		Collection<Vet> vets = this.vetService.findVets();
		model.put("vets", vets);
		
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors()+ "Errores");
			return "comentarios/crearOEditarComentario";
		}
		else {
			try{
				this.comentarioService.saveComentario(comentario);
			}catch(ComentariosMaximoPorCitaException ex){
	        result.rejectValue("vet", "El dueño ha puesto un comentario con un veterinario que no ha tenido cita o ha puesto más de un comentario a un veterinario con el que ya tuvo cita y comentó, violación de la regla de negocio", 
	        		"El dueño ha puesto un comentario con un veterinario que no ha tenido cita o ha puesto más de un comentario a un veterinario con el que ya tuvo cita y comentó, violación de la regla de negocio");
	        return "comentarios/crearOEditarComentario";
	    }
			return "redirect:/owners/comentarios/show/{comentarioId}";
		}
	}
	@GetMapping(value = "/owners/comentarios/new")  ///owners/comentarios/new
	public String initCreateComentario(Map<String, Object> model, final Principal principal) {
		Comentario comentario = new Comentario();
		int idOw = this.ownerService.findOwnerIdByUsername(principal.getName());
		Owner ow= this.ownerService.findOwnerById(idOw);
		comentario.setOwner(ow);
		model.put("comentario", comentario);
		Collection<Vet> vets = this.vetService.findVets();
		model.put("vets", vets);
		return "comentarios/crearOEditarComentario";
	}

	@PostMapping(value = "/owners/comentarios/new")
	public String processCreateComentario(Map<String, Object> model, @Valid Comentario comentario, BindingResult result,final Principal principal) {
		int idOw = this.ownerService.findOwnerIdByUsername(principal.getName());
		Owner ow= this.ownerService.findOwnerById(idOw);
		comentario.setOwner(ow);
		comentario.setId(comentario.getId());
		Collection<Vet> vets = this.vetService.findVets();
		model.put("vets", vets);
		
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "comentarios/crearOEditarComentario";
		} else {
			try{
				this.comentarioService.saveComentario(comentario);
			}catch(ComentariosMaximoPorCitaException ex){
	        result.rejectValue("vet", "El dueño ha puesto un comentario con un veterinario que no ha tenido cita o ha puesto más de un comentario a un veterinario con el que ya tuvo cita y comentó, violación de la regla de negocio", 
	        		"El dueño ha puesto un comentario con un veterinario que no ha tenido cita o ha puesto más de un comentario a un veterinario con el que ya tuvo cita y comentó, violación de la regla de negocio");
	        return "comentarios/crearOEditarComentario";
	    }
			return "redirect:/owners/comentarios";
		}
	}
}
