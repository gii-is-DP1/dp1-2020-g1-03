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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
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
	@GetMapping(value = "/owners/comentarios/show/{comentarioId}")
	public String mostarComentariosDeOwner(@PathVariable("comentarioId") int comentarioId, Map<String, Object> model, final Principal principal) {
		
		Comentario comentario= comentarioService.findComentarioByComentarioId(comentarioId);
		model.put("comentario", comentario);
		return "comentarios/show";
		}
	@GetMapping(value = "/owners/comentarios/edit/{comentarioId}/{vetId}")
	public String initEditComentario(@PathVariable("comentarioId") int comentarioId, @PathVariable("vetId") int vetId, Map<String, Object> model) {
		Comentario comentario= this.comentarioService.findComentarioByComentarioId(comentarioId);
		model.put("comentario", comentario);
		return "comentarios/crearOEditarComentario";
	}
	
	@PostMapping(value = "/owners/comentarios/edit/{comentarioId}/{vetId}")
	public String processEditComentario(final Principal principal,@Valid Comentario comentario, @PathVariable("vetId") int vetId, BindingResult result,
			@PathVariable("comentarioId") int comentarioId) {
		int idOw = this.ownerService.findOwnerIdByUsername(principal.getName());
		Owner ow= this.ownerService.findOwnerById(idOw);
		comentario.setOwner(ow);
		comentario.setId(comentarioId);
		comentario.setVet(this.vetService.findVetById(vetId));
		this.comentarioService.saveComentario(comentario);
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors()+ "Errores");
			return "comentarios/crearOEditarComentario";
		}
		else {
			
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
		return "comentarios/crearOEditarComentario";
	}

	@PostMapping(value = "/owners/comentarios/new")
	public String processCreateComentario(@Valid Comentario comentario, BindingResult result,final Principal principal) {
		int idOw = this.ownerService.findOwnerIdByUsername(principal.getName());
		Owner ow= this.ownerService.findOwnerById(idOw);
		comentario.setOwner(ow);
		comentario.setId(comentario.getId());
		this.comentarioService.saveComentario(comentario);
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "comentarios/crearOEditarComentario";
		} else {
			this.comentarioService.saveComentario(comentario);
			return "redirect:/owners/comentarios";
		}
	}
}