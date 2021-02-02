/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.ApuntarClase;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.CitaMascota;
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
import org.springframework.samples.petclinic.service.exceptions.DiferenciaClasesDiasException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaTipoMascotaException;
import org.springframework.samples.petclinic.service.exceptions.LimiteAforoClaseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 * @author Michael Isvy
 */
@Controller
public class CitaController {
	
	private final CitaService citaService;
	private final VetService vetService;
	private final SecretarioService secretarioService;
	private final PetService petService;
	private final OwnerService ownerService;
	
	@Autowired
	public CitaController(CitaService citaService,VetService vetService,SecretarioService secretarioService,
			PetService petService,OwnerService ownerService) {
		this.citaService = citaService;
		this.vetService=vetService;
		this.secretarioService = secretarioService;
		this.petService = petService;
		this.ownerService = ownerService;
	}
	
	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new ApuntarClaseValidator());
		dataBinder.addCustomFormatter(new ApuntarClaseFormatter(petService, ownerService));
	}
	
	@InitBinder("fecha")
	public void initFechaBinder(WebDataBinder dataBinder) {
		//dataBinder.setValidator(new ApuntarClaseValidator());
		dataBinder.addCustomFormatter(new FechaFormatter(citaService));
	}
	
	
	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}
	
	@GetMapping(value = "/vets/citas")
	public String listadoCitasVets(Map<String, Object> model, Principal principal) {
		int idVet=this.vetService.findVetIdByUsername(principal.getName());
		Vet vet= this.vetService.findVetById(idVet);
		List<Cita> citas= citaService.findCitasByVet(vet);
//		for(int i=0;i<citas.size();i++) {
//			
//		}
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
	
	@GetMapping(value = "/owners/citas/new")
	public String initCreateCitaOwner(Map<String, Object> model, final Principal principal) {
		Cita cita = new Cita();
		//System.out.println("Owner: "+ this.ownerService.findOwnerIdByUsername(principal.getName()));//OwnerByUsername(principal.getName()));
		Integer ownerId = this.ownerService.findOwnerIdByUsername(principal.getName());
		CitaMascota citaMascota= new CitaMascota();
		citaMascota.setCita(cita);
		//cita.setO(sec);
		model.put("citaMascota", citaMascota);
		//System.out.println("List: "+this.petService.findPetsByOwnerId(ownerId));
		List<Pet> items = this.petService.findPetsByOwnerId(ownerId);
		model.put("items", items);
		return "citas/crearOEditarCitaOwner";
	}

	@PostMapping(value = "/owners/citas/new")
	public String processCrearCitaOwner(@Valid CitaMascota citaMascota, BindingResult result,final Principal principal)
			throws DataAccessException {
		Cita cita=citaMascota.getCita();
		//System.out.println("Cita: "+citaMascota.getCita().getTitulo()+ " "+ cita.getFechaHora());
		//citaMascota.setPet(citaMascota.getPet());
		
		//this.claseService.findClaseById(claseId);
		cita.setEstado(Estado.PENDIENTE);
		citaMascota.setCita(cita);
		
		this.citaService.saveCita(cita);
		System.out.println("Cita 2: "+cita.getTitulo()+ " "+ cita.getFechaHora());
		citaMascota.setCita(cita);
		this.citaService.saveCitaMascota(citaMascota);
		//List<ApuntarClase> clasesApuntadas = this.claseService.findClasesByPetId(apClase.getPet().getId());
		
		return "redirect:/owners/citas";
	}
	
	
	@GetMapping(value = "/secretarios/citas")
	public String listadoCitasSecretarios(Map<String, Object> model, Principal principal) {
		List<Cita> citas= citaService.findAllCitas();
		model.put("citas", citas);
		return "citas/citasSecretarioList";
	}
	 
	@GetMapping(value = "/secretarios/citas/sinVet")
	public String listadoCitasSecretariosSinVet(Map<String, Object> model, Principal principal) {
		List<Cita> citas= citaService.findCitasSinVet(); //QUERY
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
