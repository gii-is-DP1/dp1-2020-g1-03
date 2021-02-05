
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
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaClasesDiasException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaTipoMascotaException;
import org.springframework.samples.petclinic.service.exceptions.LimiteAforoClaseException;
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
	
	@Autowired
	public CitaController(CitaService citaService,VetService vetService,SecretarioService secretarioService,
			PetService petService,OwnerService ownerService) {
		this.citaService = citaService;
		this.vetService=vetService;
		this.ownerService = ownerService;
		this.petService = petService;
	}
	
	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new ApuntarClaseValidator());
		dataBinder.addCustomFormatter(new CitaPetFormatter(petService));
	}
	
	@InitBinder("fecha")
	public void initFechaBinder(WebDataBinder dataBinder) {
		dataBinder.addCustomFormatter(new FechaFormatter(citaService));
	}
	
	
	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}
	
	@GetMapping(value = "/vets/citas")
	public String listadoCitasVets(Map<String, Object> model, Principal principal) {
		Vet vet=this.vetService.findVetIdByUsername(principal.getName());
		List<Cita> citas= citaService.findCitasByVet(vet);
		model.put("citas", citas);
		return "citas/citasList";
	}
	
	@GetMapping(value = "/vets/citas/{citaId}")
	public String mostarCitaVet(Map<String,  Object> model, Principal principal, @PathVariable("citaId") int citaId) {
		Cita cita= this.citaService.findCitaById(citaId);
		List<Pet> mascotas= cita.getPets();
		Owner owner= mascotas.get(0).getOwner();
		model.put("owner",owner);
		model.put("mascotas", mascotas);
		model.put("cita", cita);
		return "citas/showCitaVet";
	}
	
	@GetMapping(value = "/owners/citas")
	public String listadoCitasOwners(Map<String, Object> model, Principal principal) {
		Owner owner=this.ownerService.findOwnerByUsername(principal.getName());
		List<Cita> citas=citaService.findAllCitas();
		List<Cita> res=new ArrayList<>();
		List<Pet> pets;
		for(int i=0;i<citas.size();i++) {
			pets=citas.get(i).getPets();
			for(int j=0; j<pets.size();j++) {
				Pet pet=pets.get(j);
				if(pet.getOwner().equals(owner)) {
					res.add(citas.get(i));
					j=pets.size()-1;
				}
			}
		}
		model.put("citas", res);
		return "citas/citasOwnerList";
	}
	
	@GetMapping(value = "/owners/citas/{citaId}")
	public String mostarCitaOwner(Map<String, Object> model, Principal principal, @PathVariable("citaId") int citaId) {
		Cita cita= this.citaService.findCitaById(citaId);
		List<Pet> mascotas= cita.getPets();//this.citaService.findCitaMascotaByCitaId(citaId);
		model.put("mascotas", mascotas);
		model.put("cita", cita);
		return "citas/showCitaOwner";
	}
	
	@GetMapping(value = "/owners/citas/new")
	public String initCreateCitaOwner(Map<String, Object> model, final Principal principal) {
		Cita cita = new Cita();
		List<Pet> petsEnt=new ArrayList<>();
		cita.setPets(petsEnt);
//		Owner owner = this.ownerService.findOwnerByUsername(principal.getName());
		model.put("cita", cita);
//		List<String> pets = this.petService.findNameMascota(owner);
//		System.out.println("Pets: "+pets);
//		model.put("pets", pets);
		return "citas/crearOEditarCitaOwner";
	}

	@PostMapping(value = "/owners/citas/new")
	public String processCrearCitaOwner(Map<String, Object> model,@Valid Cita cita, BindingResult result,final Principal principal)
			throws DataAccessException {
		Owner owner = this.ownerService.findOwnerByUsername(principal.getName());
		List<Pet> pets = this.petService.findMascotasOwner(owner);
		cita.setName(cita.getTitulo());
		cita.setEstado(Estado.PENDIENTE);
		cita.setPets(pets);
		if(cita.getFechaHora().isBefore(LocalDateTime.now())) {
			result.rejectValue("fechaHora", "La fecha no puede ser una fecha pasada", "La fecha no puede ser una fecha pasada");
			return "citas/crearOEditarCitaOwner";
		}
		System.out.println("Pets: "+ cita.getPets());
		this.citaService.saveCita(cita);
		return "redirect:/owners/citas/";
	}
	

	
	@GetMapping(value = "/secretarios/citas")
	public String listadoCitasSecretarios(Map<String, Object> model, Principal principal) {
		List<Cita> citas= citaService.findAllCitas(); 
		model.put("citas", citas);
		return "citas/citasSecretarioList";
	}
	 
	@GetMapping(value = "/secretarios/citas/sinVet")
	public String listadoCitasSecretariosSinVet(Map<String, Object> model, Principal principal) {
		List<Cita> citas= citaService.findCitasSinVet(); 
		model.put("citas", citas);
		return "citas/citasSecretarioSinVetList";
	}
	
	@GetMapping(value = "/secretarios/citas/{citaId}")
	public String mostarCitaSecretario(Map<String, Object> model, Principal principal, @PathVariable("citaId") int citaId) {
		Cita cita= this.citaService.findCitaById(citaId);
		List<Pet> mascotas= cita.getPets();
		Owner owner= mascotas.get(0).getOwner();
		model.put("owner",owner);
		model.put("mascotas", mascotas);
		model.put("cita", cita);
		return "citas/showCitaSecretario";
	}
	
	@GetMapping(value = "/secretarios/citas/sinVet/{citaId}")
	public String mostarCitaSecretarioSinVet(Map<String, Object> model, Principal principal, @PathVariable("citaId") int citaId) {
		Cita cita= this.citaService.findCitaById(citaId);
		List<Pet> mascotas= cita.getPets();
		Owner owner= mascotas.get(0).getOwner();
		model.put("owner",owner);
		model.put("mascotas", mascotas);
		model.put("cita", cita);
		return "citas/showCitaSecretario";
	}

}
