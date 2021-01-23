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
import org.springframework.ui.ModelMap;
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

}
