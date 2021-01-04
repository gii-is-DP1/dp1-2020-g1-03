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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.model.Competicion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Secretario;
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
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
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
public class CompeticionController {
	
	private final CompeticionService competicionService;
	private final SecretarioService secretarioService;
	private final PetService petService;
	private final OwnerService ownerService;
	
	@Autowired
	public CompeticionController(CompeticionService competicionService, SecretarioService secretarioService, 
			PetService petService, OwnerService ownerService) {
		this.competicionService = competicionService;
		//this.adiestradorService = adiestradorService;
		this.secretarioService = secretarioService;
		this.petService = petService;
		this.ownerService = ownerService;
	}
	
	@InitBinder("pet")
	public void initPetBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new CompeticionPetValidator());
	}
	
	
	@ModelAttribute("types")
	public Collection<PetType> populatePetTypes() {
		return this.petService.findPetTypes();
	}
	
//	@ModelAttribute("adiestradores")
//	public Collection<String> populateAdiestradores() {
//		return this.secretarioService.findNameAndLastnameAdiestrador();
//	}
	
	
//	@ModelAttribute("pets")
//	public Collection<String> populatePet(final Principal principal) {
//		Integer idOwner = this.ownerService.findOwnerIdByUsername(principal.getName());
//		if(idOwner!=null) {
//		Collection<String> mascotas = this.petService.findNameMascota(idOwner);
//		return mascotas;
//		}else {
//		return new ArrayList<String>();
//		}
//	}
	
	//ADIESTRADOR
	
	@GetMapping(value = "/secretarios/competiciones")
	public String listadoCompeticionesBySecretarioId(Map<String, Object> model, final Principal principal) {
		Collection<Competicion> competiciones= competicionService.findAllCompeticiones();
		model.put("competiciones", competiciones);
		return "competiciones/competicionesList";
		}
	
	@GetMapping(value = "/secretarios/competiciones/show/{competicionId}")
	public String mostarCompeticionesSecretario(@PathVariable("competicionId") int competicionId, Map<String, Object> model, final Principal principal) {
		Competicion competicion= competicionService.findCompeticionById(competicionId);
		if(competicion.getNombre()==null) {
			return "exception";
		} else {
		model.put("competicion", competicion);
		return "competiciones/competicionesShow";
		}
	}
	
	//Dueño 
	
		@GetMapping(value = "/owners/competiciones")
		public String listadoCompeticionesByOwnerId(Map<String, Object> model, final Principal principal) {
//			Integer ownerId=this.ownerService.findOwnerIdByUsername(principal.getName());
//			Owner owner= this.ownerService.findOwnerById(ownerId);
			Collection<Competicion> competiciones= competicionService.findAllCompeticiones();
			model.put("competiciones", competiciones);
			return "competiciones/competicionesListOwner";
			}
		
		@GetMapping(value = "/owners/competiciones/show/{competicionId}")
		public String mostarCompeticionesOwner(@PathVariable("competicionId") int competicionId, Map<String, Object> model, final Principal principal) {
			Competicion competicion= competicionService.findCompeticionById(competicionId);
			if(competicion.getNombre()==null) {
				return "exception";
			} else {
			model.put("competicion", competicion);
			return "competiciones/competicionesShow";
			}
		}
	
	
	}