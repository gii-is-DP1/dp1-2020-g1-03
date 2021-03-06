
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
//import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Competicion;
import org.springframework.samples.petclinic.model.CompeticionPet;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.service.CompeticionPetService;
import org.springframework.samples.petclinic.service.CompeticionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.samples.petclinic.service.exceptions.MascotaYaApuntadaCompeticionException;
import org.springframework.samples.petclinic.service.exceptions.SolapamientoDeCompeticionesException;
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
	private static final Logger logger =
			Logger.getLogger(CompeticionController.class.getName());
	@Autowired
	public CompeticionController(CompeticionService competicionService, SecretarioService secretarioService,
			PetService petService, OwnerService ownerService, CompeticionPetService competicionPetService) {
		this.competicionService = competicionService;
		this.secretarioService = secretarioService;
		this.petService = petService;
		this.ownerService = ownerService;
		this.competicionPetService = competicionPetService;
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
			logger.log(Level.WARNING, "Competición vacia");
			return "exception";
		} else {
		model.put("competicion", competicion);
		return "competiciones/competicionesShow";
	}
	}

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
			logger.log(Level.WARNING, "Competición vacia");
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
		Owner owner = this.ownerService.findOwnerByUsername(principal.getName());
		List<String> pets = this.petService.findNameMascota(owner);
		model.put("pets", pets);
		return "competiciones/competicionesInscribePet";
	}

	@PostMapping(value = "/owners/competiciones/show/{competicionId}/inscribir")
	public String processInscribePet(@PathVariable("competicionId") int competicionId,
			@Valid CompeticionPet competicionPet, BindingResult result, final Principal principal, Map<String, Object> model) throws DataAccessException,
	SolapamientoDeCompeticionesException, MascotaYaApuntadaCompeticionException {
		Owner owner = this.ownerService.findOwnerByUsername(principal.getName());
		List<String> pets = this.petService.findNameMascota(owner);
		model.put("pets", pets);
		competicionPet.setPet(competicionPet.getPet());
		Competicion comp = this.competicionService.findCompeticionById(competicionId);
		competicionPet.setCompeticion(comp);
		if(competicionPet.getPet()==null) {
			result.rejectValue("pet", "Debe seleccionar una mascota", "Debe seleccionar una mascota");
			return "competiciones/competicionesInscribePet";
		}
		List<CompeticionPet> competicionesApuntadas = this.competicionPetService.findCompeticionByPetId(competicionPet.getPet().getId());
		if (result.hasErrors()) {
			logger.log(Level.WARNING, "Error detected", result.getAllErrors());
			return "competiciones/competicionesInscribePet";
		}else if (competicionPet.getCompeticion().getFechaHoraInicio().isBefore(LocalDate.now())) {
			result.rejectValue("pet", "La competicion ya ha comenzado", "La competicion ya ha comenzado");

			return "competiciones/competicionesInscribePet";

		} else {
			try{
				this.competicionService.escogerMascota(competicionPet, competicionesApuntadas);

	        }catch(SolapamientoDeCompeticionesException ex4){
	        	result.rejectValue("pet","No puede apuntar a su mascota porque se pisa con otra competición a la que está apuntada o ya se ha apuntado a esta competición",
						"No puede apuntar a su mascota porque se pisa con otra competición a la que está apuntada o ya se ha apuntado a esta competición");
				return "competiciones/competicionesInscribePet";
	        }
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
	public String processCreateCompeticion(@Valid Competicion competicion, BindingResult result,
			final Principal principal) {
		if (result.hasErrors()) {
			logger.log(Level.WARNING, "Error detected", result.getAllErrors());
			return "competiciones/competicionesCreateOrUpdate";
		} else {
			if(competicion.getFechaHoraInicio().isBefore(LocalDate.now())&&competicion.getFechaHoraFin().isBefore(LocalDate.now())) {
				result.rejectValue("fechaHoraFin", "La fecha de fin no puede ser una fecha pasada", "La fecha de fin no puede ser una fecha pasada");
				result.rejectValue("fechaHoraInicio", "La fecha de inicio no puede ser una fecha pasada", "La fecha de inicio no puede ser una fecha pasada");
				return "competiciones/competicionesCreateOrUpdate";
			}else {
				if(competicion.getFechaHoraInicio().isAfter(LocalDate.now())&&competicion.getFechaHoraFin().isBefore(LocalDate.now())) {
					result.rejectValue("fechaHoraFin", "La fecha de fin no puede ser una fecha pasada", "La fecha de fin no puede ser una fecha pasada");
					return "competiciones/competicionesCreateOrUpdate";
				}else if(competicion.getFechaHoraInicio().isBefore(LocalDate.now())&&competicion.getFechaHoraFin().isAfter(LocalDate.now())){
					result.rejectValue("fechaHoraInicio", "La fecha de inicio no puede ser una fecha pasada", "La fecha de inicio no puede ser una fecha pasada");
					return "competiciones/competicionesCreateOrUpdate";
				}else {
					this.competicionService.saveCompeticion(competicion);
					return "redirect:/secretarios/competiciones";
				}
			}
		}
	}

	@GetMapping(value = "/secretarios/competiciones/edit/{competicionId}")
	public String initEditCompeticion(@PathVariable("competicionId") int competicionId, Map<String, Object> model) {
		Competicion competicion = this.competicionService.findCompeticionById(competicionId);
		model.put("competicion", competicion);
		return "competiciones/competicionesCreateOrUpdate";
	}

	@PostMapping(value = "/secretarios/competiciones/edit/{competicionId}")
	public String processEditCompeticion(final Principal principal, @Valid Competicion competicion, BindingResult result,
			@PathVariable("competicionId") int competicionId) {
		Secretario secretario = this.secretarioService.findSecretarioByUsername(principal.getName());
		competicion.setSecretario(secretario);
		if (result.hasErrors()) {
			logger.log(Level.WARNING, "Error detected", result.getAllErrors());
			return "/secretarios/competicionesCreateOrUpdate"; 
		}else {
			if(competicion.getFechaHoraInicio().isBefore(LocalDate.now())&&competicion.getFechaHoraFin().isBefore(LocalDate.now())) {
				result.rejectValue("fechaHoraFin", "La fecha de fin no puede ser una fecha pasada", "La fecha de fin no puede ser una fecha pasada");
				result.rejectValue("fechaHoraInicio", "La fecha de inicio no puede ser una fecha pasada", "La fecha de inicio no puede ser una fecha pasada");
				return "competiciones/competicionesCreateOrUpdate";
			}else {
				if(competicion.getFechaHoraInicio().isAfter(LocalDate.now())&&competicion.getFechaHoraFin().isBefore(LocalDate.now())) {
					result.rejectValue("fechaHoraFin", "La fecha de fin no puede ser una fecha pasada", "La fecha de fin no puede ser una fecha pasada");
					return "competiciones/competicionesCreateOrUpdate";
				}else if(competicion.getFechaHoraInicio().isBefore(LocalDate.now())&&competicion.getFechaHoraFin().isAfter(LocalDate.now())){
					result.rejectValue("fechaHoraInicio", "La fecha de inicio no puede ser una fecha pasada", "La fecha de inicio no puede ser una fecha pasada");
					return "competiciones/competicionesCreateOrUpdate";
				}else {
					competicion.setId(competicionId);
					this.competicionService.saveCompeticion(competicion);
					return "redirect:/secretarios/competiciones/show/" + competicionId;
				}
			}
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