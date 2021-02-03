
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.CitaMascota;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CitaController {
	
	private final CitaService citaService;
	private final VetService vetService;
	private final OwnerService ownerService;
	
	@Autowired
	public CitaController(CitaService citaService,VetService vetService,SecretarioService secretarioService,
			PetService petService,OwnerService ownerService) {
		this.citaService = citaService;
		this.vetService=vetService;
		this.ownerService = ownerService;
	}
	
	@GetMapping(value = "/vets/citas")
	public String listadoCitasVets(Map<String, Object> model, Principal principal) {
		int idVet=this.vetService.findVetIdByUsername(principal.getName());
		Vet vet= this.vetService.findVetById(idVet);
		List<Cita> citas= citaService.findCitasByVet(vet);
		model.put("citas", citas);
		return "citas/citasList";
	}
	
	@GetMapping(value = "/vets/citas/{citaId}")
	public String mostarCitaVet(Map<String, Object> model, Principal principal, @PathVariable("citaId") int citaId) {
		Cita cita= this.citaService.findCitaById(citaId);
		List<CitaMascota> mascotas= this.citaService.findCitaMascotaByCitaId(citaId);
		Owner owner= mascotas.get(0).getPet().getOwner();
		model.put("owner",owner);
		model.put("mascotas", mascotas);
		model.put("cita", cita);
		return "citas/showCitaVet";
	}
	
	@GetMapping(value = "/owners/citas")
	public String listadoCitasOwners(Map<String, Object> model, Principal principal) {
		int ownerId=this.ownerService.findOwnerIdByUsername(principal.getName());
		List<Cita> citas= citaService.findCitasByOwnerId(ownerId);
		model.put("citas", citas);
		return "citas/citasOwnerList";
	}
	
	@GetMapping(value = "/owners/citas/{citaId}")
	public String mostarCitaOwner(Map<String, Object> model, Principal principal, @PathVariable("citaId") int citaId) {
		Cita cita= this.citaService.findCitaById(citaId);
		List<CitaMascota> mascotas= this.citaService.findCitaMascotaByCitaId(citaId);
		model.put("mascotas", mascotas);
		model.put("cita", cita);
		return "citas/showCitaOwner";
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
		List<CitaMascota> mascotas= this.citaService.findCitaMascotaByCitaId(citaId);
		Owner owner= mascotas.get(0).getPet().getOwner();
		model.put("owner",owner);
		model.put("mascotas", mascotas);
		model.put("cita", cita);
		return "citas/showCitaSecretario";
	}
	
	@GetMapping(value = "/secretarios/citas/sinVet/{citaId}")
	public String mostarCitaSecretarioSinVet(Map<String, Object> model, Principal principal, @PathVariable("citaId") int citaId) {
		Cita cita= this.citaService.findCitaById(citaId);
		List<CitaMascota> mascotas= this.citaService.findCitaMascotaByCitaId(citaId);
		Owner owner= mascotas.get(0).getPet().getOwner();
		model.put("owner",owner);
		model.put("mascotas", mascotas);
		model.put("cita", cita);
		return "citas/showCitaSecretario";
	}

}
