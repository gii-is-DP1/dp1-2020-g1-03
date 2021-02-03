
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Competicion;
import org.springframework.samples.petclinic.model.CompeticionPet;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.service.CompeticionPetService;
import org.springframework.samples.petclinic.service.CompeticionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CompeticionController {

	private final CompeticionService competicionService;
	private final SecretarioService secretarioService;
	private final PetService petService;
	private final OwnerService ownerService;
	private final CompeticionPetService competicionPetService;
	@Autowired
	public CompeticionController(CompeticionService competicionService, SecretarioService secretarioService,
			PetService petService, OwnerService ownerService, CompeticionPetService competicionPetService) {
		this.competicionService = competicionService;
		this.secretarioService = secretarioService;
		this.petService = petService;
		this.ownerService = ownerService;
		this.competicionPetService = competicionPetService;
	}

	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
	   dataBinder.setValidator(new CompeticionPetValidator());
	}


	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}


	// SECRETARIO

	@GetMapping(value = "/secretarios/competiciones")
	public String listadoCompeticionesBySecretarioId(Map<String, Object> model, final Principal principal) {
		Collection<Competicion> competiciones = competicionService.findAllCompeticiones();
		model.put("competiciones", competiciones);
		return "competiciones/competicionesList";
	}

	@GetMapping(value = "/secretarios/competiciones/show/{competicionId}")

	public String mostarCompeticionesSecretario(@PathVariable("competicionId") int competicionId,
			Map<String, Object> model, final Principal principal) {
		Competicion competicion = competicionService.findCompeticionById(competicionId);
		if(competicion.getNombre()==null) {
			return "exception";
		} else {
		model.put("competicion", competicion);
		return "competiciones/competicionesShow";
	}
	}

	// Dueño

	@GetMapping(value = "/owners/competiciones")
	public String listadoCompeticionesByOwnerId(Map<String, Object> model, final Principal principal) {
		Collection<Competicion> competiciones = competicionService.findAllCompeticiones();
		model.put("competiciones", competiciones);
		return "competiciones/competicionesListOwner";
	}

	@GetMapping(value = "/owners/competiciones/show/{competicionId}")
	public String mostarCompeticionesOwner(@PathVariable("competicionId") int competicionId, Map<String, Object> model,
			final Principal principal) {
		Competicion competicion = competicionService.findCompeticionById(competicionId);
		if(competicion.getNombre()==null) {
			return "exception";
		} else {
		model.put("competicion", competicion);
		return "competiciones/competicionesShowOwner";
	}
	}

	@GetMapping(value = "/owners/competiciones/show/{competicionId}/inscribir")
	public String initInscribePet(@PathVariable("competicionId") int competicionId, Map<String, Object> model,
			final Principal principal) {
		CompeticionPet competicionPet = new CompeticionPet();
		Competicion comp = this.competicionService.findCompeticionById(competicionId);
		competicionPet.setCompeticion(comp);
		model.put("competicionPet", competicionPet);
		int ownerId = this.ownerService.findOwnerIdByUsername(principal.getName());
		List<String> pets = this.petService.findNameMascota(ownerId);
		model.put("pets", pets);
		return "competiciones/competicionesInscribePet";
	}

	@PostMapping(value = "/owners/competiciones/show/{competicionId}/inscribir")
	public String processInscribePet(@PathVariable("competicionId") int competicionId,
			@Valid CompeticionPet competicionPet, BindingResult result, final Principal principal) {
		competicionPet.setPet(competicionPet.getPet());
		Competicion comp = this.competicionService.findCompeticionById(competicionId);
		competicionPet.setCompeticion(comp);
		List<CompeticionPet> competicionesApuntadas = this.competicionPetService
				.findCompeticionByPetId(competicionPet.getPet().getId());
		Boolean b = true;
		int i = 0;
		Boolean apuntada = false;
		if (!competicionesApuntadas.isEmpty()) {
			while (b && i < competicionesApuntadas.size() && apuntada.equals(false)) {
				if (competicionesApuntadas.get(i).getCompeticion().getFechaHoraFin()
						.isAfter(competicionPet.getCompeticion().getFechaHoraInicio())
						|| competicionesApuntadas.get(i).getCompeticion().getFechaHoraInicio()
								.isBefore(competicionPet.getCompeticion().getFechaHoraFin())
						&& (competicionesApuntadas.get(i).getCompeticion().getId() != competicionPet.getCompeticion()
								.getId())) {
					b = false;
				}
				if (competicionesApuntadas.get(i).getCompeticion().getId()
						.equals(competicionPet.getCompeticion().getId())) {
					apuntada = true;
				}
				i++;
			}
		}
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "exception";
		} else if (competicionPet.getCompeticion().getFechaHoraInicio().isBefore(LocalDate.now())) {
			result.rejectValue("pet", "La competicion ya ha comenzado", "La competicion ya ha comenzado");

			return "exception";

		} else if (b == false) {
			result.rejectValue("pet",
					"No puede apuntar a su mascota porque se pisa con otra competicion a la que está apuntada",
					"No puede apuntar a su mascota porque se pisa con otra competicion a la que está apuntada");
			return "exception";
		} else if (apuntada) {
			result.rejectValue("pet", "Ya se ha apuntado a esta competicion", "Ya se ha apuntado a esta competicion");
			return "exception";
		} else {
			this.competicionPetService.saveCompeticionPet(competicionPet);

			return "redirect:/owners/competiciones/show/" + competicionId;
		}
	}

	@GetMapping(value = "/secretarios/competiciones/new")
	public String initCompeticionesCreationForm(Map<String, Object> model, final Principal principal) {
		Competicion competicion = new Competicion();
		Secretario secretario = this.secretarioService.findSecretarioByUsername(principal.getName());
		competicion.setSecretario(secretario);
		model.put("competicion", competicion);
		return "competiciones/competicionesCreateOrUpdate";
	}

	@PostMapping(value = "/secretarios/competiciones/new")
	public String processCreateComentario(@Valid Competicion competicion, BindingResult result,
			final Principal principal) {
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "competiciones/competicionesCreateOrUpdate";
		} else {
			this.competicionService.saveCompeticion(competicion);
			return "redirect:/secretarios/competiciones";
		}
	}

	@GetMapping(value = "/secretarios/competiciones/edit/{competicionId}")
	public String initEditComentario(@PathVariable("competicionId") int competicionId, Map<String, Object> model) {
		Competicion competicion = this.competicionService.findCompeticionById(competicionId);
		model.put("competicion", competicion);
		return "competiciones/competicionesCreateOrUpdate";
	}

	@PostMapping(value = "/secretarios/competiciones/edit/{competicionId}")
	public String processEditComentario(final Principal principal, @Valid Competicion competicion, BindingResult result,
			@PathVariable("competicionId") int competicionId) {
		Secretario secretario = this.secretarioService.findSecretarioByUsername(principal.getName());
		competicion.setSecretario(secretario);
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors() + "Errores");
			return "/secretarios/competicionesCreateOrUpdate"; 
		} else {
			competicion.setId(competicionId);
			this.competicionService.saveCompeticion(competicion);
			return "redirect:/secretarios/competiciones/show/" + competicionId;
		}
	}

	@GetMapping(value = "/owners/competiciones/show/{competicionId}/pets")
	public String listadoPetsEnCompeticiones(@PathVariable("competicionId") int competicionId,
			Map<String, Object> model, final Principal principal) {
		List<CompeticionPet> competicionPets = new ArrayList<>(
				this.competicionPetService.findCompeticionPetByCompeticionId(competicionId));
		List<Pet> pets = new ArrayList<>();
		Pet aux;
		for (int i = 0; i < competicionPets.size(); i++) {
			int idCompeticionPet = competicionPets.get(i).getId();
			aux = this.competicionPetService.findPetByCompeticionPetId(idCompeticionPet);
			pets.add(aux);
		}
		model.put("pets", pets);
		return "competiciones/competicionesPetsList";
	}

}