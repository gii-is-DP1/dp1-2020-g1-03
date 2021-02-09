
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Estado;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.exceptions.CitaPisadaDelOwnerException;
import org.springframework.samples.petclinic.service.exceptions.CitaPisadaDelVetException;
import org.springframework.samples.petclinic.service.exceptions.LimiteDeCitasAlDiaDelVet;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CitaController {

	private final CitaService citaService;
	private final VetService vetService;
	private final OwnerService ownerService;
	private final PetService petService;
	private static final Logger logger =
			Logger.getLogger(CitaController.class.getName());
	@Autowired
	public CitaController(CitaService citaService, VetService vetService, SecretarioService secretarioService,
			PetService petService, OwnerService ownerService) {
		this.citaService = citaService;
		this.vetService = vetService;
		this.ownerService = ownerService;
		this.petService = petService;
	}

	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}

	@GetMapping(value = "/vets/citas")
	public String listadoCitasVets(Map<String, Object> model, Principal principal) {
		Vet vet = this.vetService.findVetByUsername(principal.getName());
		List<Cita> citas = citaService.findCitasByVet(vet);
		model.put("citas", citas);
		return "citas/citasList";
	}

	@GetMapping(value = "/vets/citas/{citaId}")
	public String mostarCitaVet(Map<String, Object> model, Principal principal, @PathVariable("citaId") int citaId) {
		Cita cita = this.citaService.findCitaById(citaId);
		List<Pet> mascotas = cita.getPets();
		Owner owner = mascotas.get(0).getOwner();
		if (cita.getVet() != null && cita.getVet().equals(this.vetService.findVetByUsername(principal.getName()))) {
			model.put("owner", owner);
			model.put("mascotas", mascotas);
			model.put("cita", cita);
			return "citas/showCitaVet";
		} else {
			return "exception";
		}
	}

	@GetMapping(value = "/owners/citas")
	public String listadoCitasOwners(Map<String, Object> model, Principal principal) {
		Owner owner = this.ownerService.findOwnerByUsername(principal.getName());
		List<Cita> citas = citaService.findAllCitas();
		List<Cita> res = new ArrayList<>();
		List<Pet> pets;
		for (int i = 0; i < citas.size(); i++) {
			pets = citas.get(i).getPets();
			for (int j = 0; j < pets.size(); j++) {
				Pet pet = pets.get(j);
				if (pet.getOwner().equals(owner)) {
					res.add(citas.get(i));
					j = pets.size() - 1;
				}
			}
		}
		model.put("citas", res);
		return "citas/citasOwnerList";
	}

	@GetMapping(value = "/owners/citas/{citaId}")
	public String mostarCitaOwner(Map<String, Object> model, Principal principal, @PathVariable("citaId") int citaId) {
		Cita cita = this.citaService.findCitaById(citaId);
		List<Pet> mascotas = cita.getPets();// this.citaService.findCitaMascotaByCitaId(citaId);
		if (cita.getPets().get(0).getOwner().equals(this.ownerService.findOwnerByUsername(principal.getName()))) {
			model.put("mascotas", mascotas);
			model.put("cita", cita);
			return "citas/showCitaOwner";
		} else {
			return "exception";
		}
	}

	@GetMapping(value = "/owners/citas/new")
	public String initCreateCitaOwner(Map<String, Object> model, final Principal principal) {
		Cita cita = new Cita();
		List<Pet> petsEnt = new ArrayList<>();
		cita.setPets(petsEnt);
		model.put("cita", cita);
		return "citas/crearOEditarCitaOwner";
	}

	@PostMapping(value = "/owners/citas/new")
	public String processCrearCitaOwner(Map<String, Object> model, @Valid Cita cita, BindingResult result,
			final Principal principal) throws DataAccessException, CitaPisadaDelVetException, LimiteDeCitasAlDiaDelVet,
			CitaPisadaDelOwnerException {
		Owner owner = this.ownerService.findOwnerByUsername(principal.getName());
		List<Pet> pets = this.petService.findMascotasOwner(owner);
		cita.setName(cita.getTitulo());
		cita.setEstado(Estado.PENDIENTE);
		cita.setPets(pets);
		if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
			result.rejectValue("fechaHora", "La fecha no puede ser una fecha pasada",
					"La fecha no puede ser una fecha pasada");
			return "citas/crearOEditarCitaOwner";
		}
		model.put("cita", cita);
		try {
			this.citaService.saveCita(cita);
		} catch (CitaPisadaDelOwnerException ex3) {
			result.rejectValue("fechaHora",
					"No puede aceptar esta cita porque ya tiene otra con la misma fecha y hora.",
					"No puede aceptar esta cita porque ya tiene otra con la misma fecha y hora.");
			return "citas/crearOEditarCitaOwner";
		}
		return "redirect:/owners/citas/";
	}

	@GetMapping(value = "/owners/citas/{citaId}/edit")
	public String getEditarCitaOwner(Map<String, Object> model, Principal principal,
			@PathVariable("citaId") int citaId) {
		Cita cita = this.citaService.findCitaById(citaId);
		if (!cita.getEstado().equals(Estado.PENDIENTE)) {
			return "redirect:/owners/citas/" + citaId;
		} else if (!cita.getPets().get(0).getOwner()
				.equals(this.ownerService.findOwnerByUsername(principal.getName()))) {
			return "exception";
		} else {
			model.put("cita", cita);
			return "citas/crearOEditarCitaOwner";
		}
	}

	@PostMapping(value = "/owners/citas/{citaId}/edit")
	public String processEditarCitaOwner(@Valid Cita cita, @PathVariable("citaId") int citaId, BindingResult result,
			final Principal principal) throws DataAccessException, CitaPisadaDelVetException, LimiteDeCitasAlDiaDelVet,
			CitaPisadaDelOwnerException {
		List<Pet> pets = this.citaService.findCitaById(citaId).getPets();
		if (result.hasErrors()) {
			logger.log(Level.WARNING, "Error detected", result.getAllErrors());
			return "citas/crearOEditarCitaOwner";
		} else if (!pets.get(0).getOwner().equals(this.ownerService.findOwnerByUsername(principal.getName()))) {
			return "exception";
		} else if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
			result.rejectValue("fechaHora", "La fecha no puede ser una fecha pasada",
					"La fecha no puede ser una fecha pasada");
			return "citas/crearOEditarCitaOwner";
		} else {
			cita.setPets(pets);
			cita.setId(citaId);
			cita.setEstado(Estado.PENDIENTE);
			try {
				this.citaService.saveCita(cita);
			} catch (CitaPisadaDelOwnerException ex3) {
				result.rejectValue("owner",
						"No puede aceptar esta cita porque ya tiene otra con la misma fecha y hora.");
				return "citas/crearOEditarCitaOwner";
			}

			return "redirect:/owners/citas/";
		}
	}

	@GetMapping(value = "/owners/citas/{citaId}/delete")
	public String deleteCita(Map<String, Object> model, @PathVariable("citaId") int citaId) {
		this.citaService.deleteCita(this.citaService.findCitaById(citaId));
		return "redirect:/owners/citas/";
	}

	@GetMapping(value = "/secretarios/citas")
	public String listadoCitasSecretarios(Map<String, Object> model, Principal principal) {
		List<Cita> citas = citaService.findAllCitas();
		model.put("citas", citas);
		return "citas/citasSecretarioList";
	}

	@GetMapping(value = "/secretarios/citas/sinVet")
	public String listadoCitasSecretariosSinVet(Map<String, Object> model, Principal principal) {
		List<Cita> citas = citaService.findCitasSinVet(Estado.PENDIENTE);
		model.put("citas", citas);
		return "citas/citasSecretarioSinVetList";
	}

	@GetMapping(value = "/secretarios/citas/{citaId}")
	public String mostarCitaSecretario(Map<String, Object> model, Principal principal,
			@PathVariable("citaId") int citaId) {
		Cita cita = this.citaService.findCitaById(citaId);
		List<Pet> mascotas = cita.getPets();
		Owner owner = mascotas.get(0).getOwner();
		model.put("owner", owner);
		model.put("mascotas", mascotas);
		model.put("cita", cita);
		return "citas/showCitaSecretario";
	}

	@GetMapping(value = "/secretarios/citas/{citaId}/edit")
	public String getEditarCitaSecretario(Map<String, Object> model, Principal principal,
			@PathVariable("citaId") int citaId) {
		Cita cita = this.citaService.findCitaById(citaId);
		if (cita.getEstado().equals(Estado.PENDIENTE)) {
			model.put("cita", cita);
			List<Vet> vets = new ArrayList<>(this.vetService.findVets());
			List<Estado> estados = new ArrayList<Estado>();
			estados.add(Estado.ACEPTADA);
			estados.add(Estado.RECHAZADA);
			model.put("estados", estados);
			model.put("vets", vets);
			return "citas/editarCitaSecretario";
		} else {
			return "redirect:/secretarios/citas/" + citaId;
		}
	}
// ACLARACIÓN: Un secretario no puede editar una cita aceptada, por eso al intentar acceder al editar no ocurre nada. 
	@PostMapping(value = "/secretarios/citas/{citaId}/edit")
	public String processEditarCitaSecretario(Map<String, Object> model, @Valid Cita cita, BindingResult result,
			final Principal principal, @PathVariable("citaId") int citaId) throws DataAccessException {
		List<Vet> vets = new ArrayList<>(this.vetService.findVets());
		List<Estado> estados = new ArrayList<>();
		estados.add(Estado.ACEPTADA);
		estados.add(Estado.RECHAZADA);
		model.put("estados", estados);
		model.put("cita", cita);
		model.put("vets", vets);
		if (result.hasErrors()) {
			logger.log(Level.WARNING, "Error detected", result.getAllErrors());
			return "citas/citasSecretarioList";
		} else if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
			result.rejectValue("fechaHora", "La fecha no puede ser una fecha pasada",
					"La fecha no puede ser una fecha pasada");
			return "citas/editarCitaSecretario";
		} else if (cita.getEstado().equals(Estado.RECHAZADA)) {
			cita.setEstado(Estado.RECHAZADA);
			cita.setVet(null);
		}

		Cita cita1 = this.citaService.findCitaById(citaId);
		List<Pet> mascotas = cita1.getPets();
		cita.setPets(mascotas);
		cita.setId(citaId);
		if (cita.getEstado().equals(Estado.PENDIENTE)) {
			cita.setEstado(Estado.ACEPTADA);
		}
		try {
			this.citaService.saveCita(cita);
		} catch (CitaPisadaDelVetException ex) {
			result.rejectValue("vet", "No puede aceptar la cita de este veterinario porque se pisa con otra cita",
					"No puede aceptar la cita de este veterinario porque se pisa con otra cita");
			return "citas/editarCitaSecretario";
		} catch (LimiteDeCitasAlDiaDelVet ex2) {
			result.rejectValue("vet",
					"El veterinario seleccionado no puede aceptar la cita debido a que supera el límite de citas en un día, violación de regla de negocio",
					"El veterinario seleccionado no puede aceptar la cita debido a que supera el límite de citas en un día, violación de regla de negocio");
			return "citas/editarCitaSecretario";
		} catch (CitaPisadaDelOwnerException ex3) {
			result.rejectValue("owner",
					"No puede aceptar esta cita porque el propietario ya tiene otra con la misma fecha y hora.");
			return "citas/editarCitaSecretario";
		}
		return "redirect:/secretarios/citas";
	}

}
