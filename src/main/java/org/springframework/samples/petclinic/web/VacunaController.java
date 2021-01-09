package org.springframework.samples.petclinic.web;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.TipoEnfermedad;
import org.springframework.samples.petclinic.model.Vacuna;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VacunaService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.exceptions.DistanciaEntreDiasException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VacunaController {
	
	private final VacunaService vacunaService;
	private final PetService petService;
	private final VetService vetService;

	@Autowired
	public VacunaController(VacunaService vacunaService,PetService petService,VetService vetService) {
		this.vacunaService = vacunaService;
		this.petService=petService;
		this.vetService=vetService;
	}
	
	@ModelAttribute("tipoenfermedades")
	public Collection<TipoEnfermedad> populateTipoEnfermedad() {
		return this.vacunaService.findTipoEnfermedades();
	}
	
	@InitBinder("vacuna")
	public void initTipoEnfermedadBinder(WebDataBinder dataBinder) {
		dataBinder.setValidator(new VacunaValidator());
	}
	
	//OWNERS
	@GetMapping(value = "/owners/{ownerId}/vacuna")
	public String listadoVacunasOwner(Map<String, Object> model, @PathVariable("ownerId") int ownerId) {
		Collection<Vacuna> vacunas = vacunaService.findAllVacunasByOwnerId(ownerId);
		model.put("vacunas", vacunas);
		return "vacunas/vacunasListOwner";
	}
	
	@GetMapping(value = "/owners/{ownerId}/vacuna/{vacunaId}")
	public String mostarVacunaOwnerDeOwner(@PathVariable("vacunaId") int Id,Map<String, Object> model) {
		Vacuna vacuna= vacunaService.findVacunaById(Id);
		model.put("vacuna", vacuna);
		return "vacunas/vacunasShow";
		}
	
	//VETS
	@GetMapping(value = "/vets/vacuna")
	public String listadoVacunas(Map<String, Object> model) {
		List<Vacuna> vacunas = vacunaService.findAllVacunas();
		model.put("vacunas", vacunas);
		return "vacunas/vacunasListVet";
	}
	
	@GetMapping(value = "/vets/vacuna/{vacunaId}")
	public String mostarVacuna(@PathVariable("vacunaId") int Id,Map<String, Object> model) {
		Vacuna vacuna= vacunaService.findVacunaById(Id);
		model.put("vacuna", vacuna);
		return "vacunas/vacunasShow";
		}
	
	@GetMapping(value = "/vets/vacuna/pets/{petId}")
	public String mostarMascota(@PathVariable("petId") int Id,Map<String, Object> model) {
		Pet pet= petService.findPetById(Id);
		model.put("pet", pet);
		return "vacunas/MascotaShow";
		}
	
	@GetMapping(value = "/vets/vacuna/pets/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("pet", new Pet());
		return "vacunas/EncontrarMascotas";
	}

	@GetMapping(value = "/vets/vacuna/pets")
	public String processFindForm(Pet pet, BindingResult result, Map<String, Object> model) {
		Collection<Pet> results = this.vacunaService.findMascotaByEspecie(pet.getType().getName());
		if (results.isEmpty()) {
			if (pet.getType().getName().equals("")) {
				Collection<Pet> res = this.petService.findAllPets();
				model.put("pets", res);
				return "vacunas/MascotasList";
			}
			result.rejectValue("type.name", "notFound", "not found");
			return "vacunas/EncontrarMascotas";
		}
		else if (results.size() == 1) {
			pet = results.iterator().next();
			return "redirect:/vets/vacuna/pets/" + pet.getId();
		}
		else {
			model.put("pets", results);
			return "vacunas/MascotasList";
		}
		
	}
	
	
	@GetMapping(value = "/vets/vacuna/pets/{petId}/create")
	public String initCreateVacuna(Map<String, Object> model,@PathVariable("petId") int Id) {
		Vacuna vacuna = new Vacuna();
		vacuna.setPet(this.petService.findPetById(Id));
		model.put("vacuna", vacuna);
		return "vacunas/crearVacuna";
	}

	@PostMapping(value = "/vets/vacuna/pets/{petId}/create")
	public String processCreateVacuna(@Valid Vacuna vacuna, @PathVariable("petId") int Id,BindingResult result, final Principal principal) {
		vacuna.setId(vacuna.getId());
		int idVet = this.vetService.findVetIdByUsername(principal.getName());
		Vet vet= this.vetService.findVetById(idVet);
		vacuna.setVet(vet);
		vacuna.setPet(this.petService.findPetById(Id));
		if (result.hasErrors()) {
			System.out.println(result.getAllErrors());
			return "vacunas/crearVacuna";
		}else if(vacuna.getFecha().compareTo(vacuna.getPet().getBirthDate())<0) {
			System.out.println("Fecha de vacuna anterior a fecha de nacimiento de la mascota");
			result.rejectValue("fecha", "distancia", "Fecha de vacuna anterior a fecha de nacimiento de la mascota");
			return "vacunas/crearVacuna";
		} 
		else {
				try{
					this.vacunaService.saveVacuna(vacuna,Id);
				}catch(DistanciaEntreDiasException ex){
                result.rejectValue("fecha", "distancia", "La vacuna no ha podido ser añadida por violación de la regla de negocio");
                return "vacunas/crearVacuna";
            }
			return "redirect:/vets/vacuna/" + vacuna.getId();
		}
	}
}
