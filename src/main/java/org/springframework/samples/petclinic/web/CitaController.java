
package org.springframework.samples.petclinic.web;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.ApuntarClase;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Estado;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.exceptions.CitaPisadaDelOwnerException;
import org.springframework.samples.petclinic.service.exceptions.CitaPisadaDelVetException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaClasesDiasException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaTipoMascotaException;
import org.springframework.samples.petclinic.service.exceptions.LimiteAforoClaseException;
import org.springframework.samples.petclinic.service.exceptions.LimiteDeCitasAlDiaDelVet;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

@Controller
public class CitaController {

	private final CitaService citaService;
	private final VetService vetService;
	private final OwnerService ownerService;
	private final PetService petService;
	private final SecretarioService secretarioService;

	@Autowired
	public CitaController(CitaService citaService, VetService vetService, SecretarioService secretarioService,
			PetService petService, OwnerService ownerService) {
		this.citaService = citaService;
		this.vetService = vetService;
		this.ownerService = ownerService;
		this.petService = petService;
		this.secretarioService = secretarioService;
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
//		Owner owner = this.ownerService.findOwnerByUsername(principal.getName());
		model.put("cita", cita);
//		List<String> pets = this.petService.findNameMascota(owner);
//		System.out.println("Pets: "+pets);
//		model.put("pets", pets);
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
		System.out.println("Pets: " + cita.getPets());
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
			System.out.println(result.getAllErrors());
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
		List<Cita> citas = citaService.findCitasSinVet();
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

	@PostMapping(value = "/secretarios/citas/{citaId}/edit")
	public String processEditarCitaSecretario(Map<String, Object> model, @Valid Cita cita, BindingResult result,
			final Principal principal, @PathVariable("citaId") int citaId) throws DataAccessException {
		List<Vet> vets = new ArrayList<>(this.vetService.findVets());
		List<Estado> estados = new ArrayList<>();
//		Cita citaux = this.citaService.findCitaById(citaId);
//		cita.setName(citaux.getName());
//		cita.setFechaHora(citaux.getFechaHora());
//		cita.setRazon(citaux.getRazon());
//		cita.setTitulo(citaux.getTitulo());
//		System.out.println(citaux.getTitulo());
		estados.add(Estado.ACEPTADA);
		estados.add(Estado.RECHAZADA);
		model.put("estados", estados);
		model.put("cita", cita);
		model.put("vets", vets);
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "citas/citasSecretarioList";
		} else if (cita.getFechaHora().isBefore(LocalDateTime.now())) {
			result.rejectValue("fechaHora", "La fecha no puede ser una fecha pasada",
					"La fecha no puede ser una fecha pasada");
			return "citas/editarCitaSecretario";
		} else if (cita.getEstado().equals(Estado.RECHAZADA)) {
			result.rejectValue("vet", "No se puede seleccionar veterinario ya que la cita ha sido rechazada",
					"No se puede seleccionar veterinario ya que la cita ha sido rechazada");
			return "citas/editarCitaSecretario";
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
