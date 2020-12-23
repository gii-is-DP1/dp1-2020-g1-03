package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.TipoEnfermedad;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vacuna;
import org.springframework.samples.petclinic.service.exceptions.DistanciaEntreDiasException;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class VacunaServiceTest {

	@Autowired
	private VacunaService vacunaService;
	
	@Autowired
	private VetService vetService;
	
	@Autowired
	private PetService petService;

	@Test
	void shouldFindVacunaWithCorrectId() {
		Vacuna vacuna1 = this.vacunaService.findVacunaById(1);
		assertThat(vacuna1.getTipoEnfermedad().getName()).startsWith("Rabia");
	}

	@Test
	void shouldFindVacunasWithCorrectOwnerId() {
		List<Vacuna> vacunas = new ArrayList<>(this.vacunaService.findAllVacunasByOwnerId(1));
		assertThat(vacunas.get(0).getPet().getOwner().getId()).isEqualTo(1);
		assertThat(vacunas.get(0).getTipoEnfermedad().getName()).isEqualTo("Rabia");
	}
	
	@Test
	void shouldFindVacunasAllForVets() {
		List<Vacuna> vacunas = new ArrayList<>(this.vacunaService.findAllVacunas());
		assertThat(vacunas.get(0).getPet().getOwner().getId()).isEqualTo(1);
		assertThat(vacunas.get(0).getTipoEnfermedad().getName()).isEqualTo("Rabia");
		assertThat(vacunas.get(1).getPet().getOwner().getId()).isEqualTo(2);
		assertThat(vacunas.get(1).getTipoEnfermedad().getName()).isEqualTo("Parvovirus");
	}
	
	@Test
	void shouldFindAllTipoEnfermedad() {
		Collection<TipoEnfermedad> tipoEnfermedades = this.vacunaService.findTipoEnfermedades();
		TipoEnfermedad Tipoenfermedad1 = EntityUtils.getById(tipoEnfermedades, TipoEnfermedad.class, 1);
		assertThat(Tipoenfermedad1.getName()).isEqualTo("Rabia");
		TipoEnfermedad Tipoenfermedad3 = EntityUtils.getById(tipoEnfermedades, TipoEnfermedad.class, 3);
		assertThat(Tipoenfermedad3.getName()).isEqualTo("Parvovirus");
	}
	
	@Test
	void shouldFindPetsByEspecie() {
		Collection<Pet> mascotas = this.vacunaService.findMascotaByEspecie("hamster");
		assertThat(mascotas.size()).isEqualTo(1);

		mascotas = this.vacunaService.findMascotaByEspecie("hasmter");
		assertThat(mascotas.isEmpty()).isTrue();
	}
	
	@Test
	@Transactional
	public void shouldInsertVacuna() {
		Collection<Vacuna> vacunas = this.vacunaService.findAllVacunas();
		int found = vacunas.size();
		Collection<TipoEnfermedad> tipoEnfermedades = this.vacunaService.findTipoEnfermedades();
		
		Vacuna vacuna = new Vacuna();
		vacuna.setTipoEnfermedad(EntityUtils.getById(tipoEnfermedades, TipoEnfermedad.class, 1));
		vacuna.setFecha(LocalDate.of(2020, 11, 11));
		vacuna.setDescripcion("Prueba de que se ha añadido correactamente la vacuna");
		vacuna.setPet(this.petService.findPetById(3));
		vacuna.setVet(this.vetService.findVetById(1));                
        
		try {
            this.vacunaService.saveVacuna(vacuna, vacuna.getPet().getId());;
        } catch (DistanciaEntreDiasException ex) {
            Logger.getLogger(VacunaServiceTest.class.getName()).log(Level.SEVERE, null, ex);
        }

		vacunas = this.vacunaService.findAllVacunas();
		assertThat(vacunas.size()).isEqualTo(found + 1);
	}
	
	@Test
	@Transactional
	public void shouldThrowExceptionDistanciaEntreDiasException() {
		Collection<TipoEnfermedad> tipoEnfermedades = this.vacunaService.findTipoEnfermedades();
		
		Vacuna vacuna = new Vacuna();
		vacuna.setTipoEnfermedad(EntityUtils.getById(tipoEnfermedades, TipoEnfermedad.class, 1));
		vacuna.setFecha(LocalDate.of(2020, 11, 11));
		vacuna.setDescripcion("Prueba de que se ha añadido correactamente la vacuna");
		vacuna.setPet(this.petService.findPetById(4));
		vacuna.setVet(this.vetService.findVetById(1));                
        
		try {
            this.vacunaService.saveVacuna(vacuna, vacuna.getPet().getId());;
        } catch (DistanciaEntreDiasException ex) {
            ex.printStackTrace();
        }
		
		Vacuna vacuna2 = new Vacuna();
		vacuna2.setTipoEnfermedad(EntityUtils.getById(tipoEnfermedades, TipoEnfermedad.class, 2));
		vacuna2.setFecha(LocalDate.of(2020, 11, 13));
		vacuna2.setDescripcion("Prueba de que se ha añadido correactamente la vacuna");
		vacuna2.setPet(this.petService.findPetById(4));
		vacuna2.setVet(this.vetService.findVetById(1));
		Assertions.assertThrows(DistanciaEntreDiasException.class, () ->{
			vacunaService.saveVacuna(vacuna2, vacuna2.getPet().getId());
		});		
	}
}
