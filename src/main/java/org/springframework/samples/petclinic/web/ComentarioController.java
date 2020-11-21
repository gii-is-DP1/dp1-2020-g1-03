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
@RequestMapping("/vets")
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
	

	@GetMapping(value = "/{vetId}/comentarios")
	public String listadoComentariosByVetId(Map<String, Object> model, @PathVariable("vetId") int vetId) {
		Collection<Comentario> comentarios= comentarioService.findAllComentariosByVetId(vetId);
		model.put("comentarios", comentarios);
		return "comentarios/comentariosList";
		}
	
//	@GetMapping(value = "/{vet_id}/comentarios")
//	public String mostarComentariosDeVet(@PathVariable("vet_id") int vet_id,Map<String, Object> model) {
//		Vet vet= comentarioService.findComentarioByVetId(vet_id);
//		model.put("veterinario", vet);
//		return "comentario/comentariosShow";
//		}
//	
//	@GetMapping(value = "/{comentarioId}/edit")
//	public String initEditComentario(@PathVariable("comentarioId") int comentarioId, Map<String, Object> model) {
//		Comentario comentario= this.comentarioService.findComentarioByOwnerId(comentarioId);
//		System.out.println(comentario+ "COMENTARIO");
//		model.put("comentario", comentario);
//		return "comentario/crearOEditarComentario";
//	}
//
//	@PostMapping(value = "/{comentarioId}/edit")
//	public String processEditComentario(final Principal principal,@Valid Comentario comentario, BindingResult result,
//			@PathVariable("comentarioId") int comentarioId) {
//		if (result.hasErrors()) {
//			System.out.println(result.getAllErrors());
//			return "comentario/crearOEditarComentario";
//		}
//		else {
//			int idOwner = this.ownerService.findOwnerByLastName(principal.getName()).hashCode();
//			Owner owner= this.ownerService.findOwnerById(idOwner);
//			comentario.setOwner(owner);
//			System.out.println(comentario.getOwner());
//			comentario.setId(comentarioId);
//			this.comentarioService.saveComentario(comentario);
//			return "redirect:/comentarios/";
//		}
//	}
}
