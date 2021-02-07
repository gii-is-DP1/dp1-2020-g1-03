package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
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
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.model.ApuntarClase;
import org.springframework.samples.petclinic.model.CategoriaClase;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.model.TipoEnfermedad;
import org.springframework.samples.petclinic.service.exceptions.ClasePisadaDelAdiestradorException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaClasesDiasException;
import org.springframework.samples.petclinic.service.exceptions.DiferenciaTipoMascotaException;
import org.springframework.samples.petclinic.service.exceptions.DistanciaEntreDiasException;
import org.springframework.samples.petclinic.service.exceptions.LimiteAforoClaseException;
import org.springframework.samples.petclinic.service.exceptions.MacostaYaApuntadaException;
import org.springframework.samples.petclinic.service.exceptions.SolapamientoDeClasesException;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.junit.Assert;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ClaseServiceTests {
	
	@Autowired
	protected ClaseService claseService;
	@Autowired
	protected AdiestradorService adiestradorService;
	@Autowired
	protected SecretarioService secretarioService;
	@Autowired
	protected PetService petService;

	@Test
	@Transactional
	public void shouldUpdateClase() throws Exception {
		Clase clase = this.claseService.findClaseById(1);
		Secretario secretario = this.secretarioService.findSecretarioByUsername("secretario1");
		Adiestrador adiestrador = this.adiestradorService.findAdiestradorByUsername("adiestrador1");
		PetType type = new PetType();
		type.setId(1);
		type.setName("dog");
		String nombreClase = "Clase";
		CategoriaClase cateClase = new CategoriaClase();
		cateClase.setId(1);
		cateClase.setName("Adiestrar");

		clase.setCategoriaClase(cateClase);
		clase.setFechaHoraInicio(LocalDateTime.of(2021, 12, 27, 15, 00));
		clase.setFechaHoraFin(LocalDateTime.of(2021, 12, 27, 16, 00));
		clase.setNumeroPlazasTotal(20);
		clase.setNumeroPlazasDisponibles(15);
		clase.setType(type);
		clase.setName(nombreClase);
		clase.setAdiestrador(adiestrador);
		clase.setSecretario(secretario);
		
		try{
			this.claseService.saveClase(clase);
		}catch(ClasePisadaDelAdiestradorException ex){
			Logger.getLogger(ClaseServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }

		Clase claseRev = this.claseService.findClaseById(1);
		Assert.assertTrue(claseRev.getName().equals(nombreClase));
	}
	
	@Test
	@Transactional
	public void shouldThorwClasePisadaDelAdiestradorExceptionInUpdateClase() throws Exception {
		Clase clase = new Clase();
		Secretario secretario = this.secretarioService.findSecretarioByUsername("secretario1");
		Adiestrador adiestrador = this.adiestradorService.findAdiestradorByUsername("adiestrador1");
		PetType type = new PetType();
		type.setId(1);
		type.setName("dog");
		String nombreClase = "Clase";
		CategoriaClase cateClase = new CategoriaClase();
		cateClase.setId(1);
		cateClase.setName("Adiestrar");

		clase.setCategoriaClase(cateClase);
		clase.setFechaHoraInicio(LocalDateTime.of(2021, 12, 27, 15, 00));
		clase.setFechaHoraFin(LocalDateTime.of(2021, 12, 27, 16, 00));
		clase.setNumeroPlazasTotal(20);
		clase.setNumeroPlazasDisponibles(15);
		clase.setType(type);
		clase.setName(nombreClase);
		clase.setAdiestrador(adiestrador);
		clase.setSecretario(secretario);
		
		try{
			this.claseService.saveClase(clase);
		}catch(ClasePisadaDelAdiestradorException ex){
			 ex.printStackTrace();
        }

		Clase clase2=this.claseService.findClaseById(1);
		clase2.setCategoriaClase(cateClase);
		clase2.setFechaHoraInicio(LocalDateTime.of(2021, 12, 27, 15, 30));
		clase2.setFechaHoraFin(LocalDateTime.of(2021, 12, 27, 16, 30));
		clase2.setNumeroPlazasTotal(20);
		clase2.setNumeroPlazasDisponibles(15);
		clase2.setType(type);
		clase2.setName("Clase que se pisa");
		clase2.setAdiestrador(adiestrador);
		clase2.setSecretario(secretario);
		Assertions.assertThrows(ClasePisadaDelAdiestradorException.class, () ->{
			claseService.saveClase(clase);
		});
	}
	
	@Test
	@Transactional
	public void shouldThorwClasePisadaDelAdiestradorExceptionInInsertClase() throws Exception {
		Clase clase = new Clase();
		Secretario secretario = this.secretarioService.findSecretarioByUsername("secretario1");
		Adiestrador adiestrador = this.adiestradorService.findAdiestradorByUsername("adiestrador1");
		PetType type = new PetType();
		type.setId(1);
		type.setName("dog");
		String nombreClase = "Clase";
		CategoriaClase cateClase = new CategoriaClase();
		cateClase.setId(1);
		cateClase.setName("Adiestrar");

		clase.setId(33);
		clase.setCategoriaClase(cateClase);
		clase.setFechaHoraInicio(LocalDateTime.of(2021, 12, 27, 15, 00));
		clase.setFechaHoraFin(LocalDateTime.of(2021, 12, 27, 16, 00));
		clase.setNumeroPlazasTotal(20);
		clase.setNumeroPlazasDisponibles(15);
		clase.setType(type);
		clase.setName(nombreClase);
		clase.setAdiestrador(adiestrador);
		clase.setSecretario(secretario);
		
		try{
			this.claseService.saveClase(clase);
		}catch(ClasePisadaDelAdiestradorException ex){
			 ex.printStackTrace();
        }

		Clase clase2=new Clase();
		clase2.setId(34);
		clase2.setCategoriaClase(cateClase);
		clase2.setFechaHoraInicio(LocalDateTime.of(2021, 12, 27, 15, 30));
		clase2.setFechaHoraFin(LocalDateTime.of(2021, 12, 27, 16, 30));
		clase2.setNumeroPlazasTotal(20);
		clase2.setNumeroPlazasDisponibles(15);
		clase2.setType(type);
		clase2.setName("Clase que se pisa");
		clase2.setAdiestrador(adiestrador);
		clase2.setSecretario(secretario);
		Assertions.assertThrows(ClasePisadaDelAdiestradorException.class, () ->{
			claseService.saveClase(clase);
		});
	}
	
	
	@Test
	@Transactional
	public void shouldInsertClase() throws Exception {
		Clase clase = new Clase();
		Secretario secretario = this.secretarioService.findSecretarioByUsername("secretario1");
		Adiestrador adiestrador = this.adiestradorService.findAdiestradorByUsername("adiestrador1");
		PetType type = new PetType();
		type.setId(1);
		type.setName("dog");
		String nombreClase = "Clase";
		CategoriaClase cateClase = new CategoriaClase();
		cateClase.setId(1);
		cateClase.setName("Adiestrar");
		int tamOriginal=this.claseService.findAllClases().size();
		
		clase.setCategoriaClase(cateClase);
		clase.setFechaHoraInicio(LocalDateTime.of(2021, 12, 27, 15, 00));
		clase.setFechaHoraFin(LocalDateTime.of(2021, 12, 27, 16, 00));
		clase.setNumeroPlazasTotal(20);
		clase.setNumeroPlazasDisponibles(15);
		clase.setType(type);
		clase.setName(nombreClase);
		clase.setAdiestrador(adiestrador);
		clase.setSecretario(secretario);
		try{
			this.claseService.saveClase(clase);
		}catch(ClasePisadaDelAdiestradorException ex){
			Logger.getLogger(ClaseServiceTests.class.getName()).log(Level.SEVERE, null, ex);
		}
		Assert.assertTrue(this.claseService.findAllClases().size()==tamOriginal+1);
	}
	
	
	@Test
	@Transactional
	public void shouldDeleteClase() throws Exception {
		Clase clase = this.claseService.findClaseById(1);
		this.claseService.deleteClase(clase);
		clase = this.claseService.findClaseById(1);
		Assert.assertTrue(clase == null);
	}

	@Test
	void shouldFindAllClases() {
		Collection<Clase> clases = this.claseService.findAllClases();
		Assert.assertEquals(clases.size(), 11);
	}

	@Test
	void shouldFindClasesWithCorrectId() {
		Clase clase = this.claseService.findClaseById(2);
		Assert.assertEquals(clase.getName(), "Clase2");
		Assert.assertEquals(clase.getNumeroPlazasDisponibles().toString(), "8");
	}

	@Test
	void shouldNotFindClasesWithCorrectId() {
		Assert.assertNull(this.claseService.findClaseById(100));
	}

	@Test
	void shouldFindClasesByAdiestradorId() {
		Assert.assertNotNull(this.claseService.findClaseByAdiestradorId(1));
	}

	@Test
	void shouldNotFindClasesByAdiestradorId() {
		Collection<Clase> clase = this.claseService.findClaseByAdiestradorId(1);
		List<Clase> listaClase = new ArrayList<>(clase);
		Assert.assertNotEquals(listaClase.get(0).getAdiestrador().getId().toString(), "2");
	}

	@Test
	void shouldFindClasesByName() {
		Assert.assertNotNull(this.claseService.findByName("Clase2"));
	}

	@Test
	void shouldFindClaseById() throws Exception {
		Clase clase = this.claseService.findClaseById(1);
		Assert.assertTrue(clase.getId() == 1);
	}

	@Test
	void shouldFindClasesByPetId() {
		Assert.assertNotNull(this.claseService.findClasesByPetId(2));
	}

	
	@Test
	void shouldFindClaseByAdiestrador() throws Exception {
		Adiestrador ad = this.adiestradorService.findAdiestradorByUsername("adiestrador2");
		List<Clase> clases = this.claseService.findClasesAdiestrador(ad);
		Assert.assertTrue(clases.size() == 2);
	}

	@Test
	@Transactional
	void shouldApuntarMascota() throws Exception {
		Clase clase = this.claseService.findClaseById(11);
		Pet pet = this.petService.findPetById(4);
		int plazas = clase.getNumeroPlazasDisponibles();
		ApuntarClase apClase = new ApuntarClase();
		apClase.setClase(clase);
		apClase.setPet(pet);
		try{
			this.claseService.apuntarMascota(apClase);
		}catch(DiferenciaTipoMascotaException ex){
			Logger.getLogger(ClaseServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }catch(DiferenciaClasesDiasException ex2){
        	Logger.getLogger(ClaseServiceTests.class.getName()).log(Level.SEVERE, null, ex2);
        }catch(LimiteAforoClaseException ex3){
        	Logger.getLogger(ClaseServiceTests.class.getName()).log(Level.SEVERE, null, ex3);
        }catch(SolapamientoDeClasesException ex4){
        	Logger.getLogger(ClaseServiceTests.class.getName()).log(Level.SEVERE, null, ex4);
        }catch(MacostaYaApuntadaException ex5){
        	Logger.getLogger(ClaseServiceTests.class.getName()).log(Level.SEVERE, null, ex5);
        }
		Assert.assertTrue(clase.getNumeroPlazasDisponibles() == plazas-1);
	}
	
	@Test
	@Transactional
	void shouldThrowMacostaYaApuntadaExceptionApuntarMascota() throws Exception {
		Clase clase = this.claseService.findClaseById(11);
		Pet pet = this.petService.findPetById(4);
		int plazas = clase.getNumeroPlazasDisponibles();
		ApuntarClase apClase = new ApuntarClase();
		apClase.setClase(clase);
		apClase.setPet(pet);
		try{
			this.claseService.apuntarMascota(apClase);
		}catch(DiferenciaTipoMascotaException ex){
			ex.printStackTrace();
        }catch(DiferenciaClasesDiasException ex2){
        	ex2.printStackTrace();
        }catch(LimiteAforoClaseException ex3){
        	ex3.printStackTrace();
        }catch(SolapamientoDeClasesException ex4){
        	ex4.printStackTrace();
        }catch(MacostaYaApuntadaException ex5){
        	ex5.printStackTrace();
        }
		clase = this.claseService.findClaseById(11);
		Assert.assertTrue(clase.getNumeroPlazasDisponibles() == plazas-1);
	}
	
	@Test
	@Transactional
	void shouldThrowLimiteAforoClaseExceptionApuntarMascota() throws Exception {
		Clase clase = this.claseService.findClaseById(4);
		Pet pet = this.petService.findPetById(4);
		ApuntarClase apClase = new ApuntarClase();
		apClase.setClase(clase);
		apClase.setPet(pet);
		Assertions.assertThrows(LimiteAforoClaseException.class, () ->{
			claseService.apuntarMascota(apClase);
		});
	}
	
	@Test
	@Transactional
	void shouldThrowsDiferenciaTipoMascotaExceptionApuntarMascota() throws Exception {
		Clase clase = this.claseService.findClaseById(11);
		Pet pet = this.petService.findPetById(1);
		ApuntarClase apClase = new ApuntarClase();
		apClase.setClase(clase);
		apClase.setPet(pet);
		Assertions.assertThrows(DiferenciaTipoMascotaException.class, () ->{
			claseService.apuntarMascota(apClase);
		});
	}
	
	@Test
	@Transactional
	void shouldThrowSolapamientoDeClasesExceptionApuntarMascota() throws Exception {
		Clase clase = this.claseService.findClaseById(11);
		Pet pet = this.petService.findPetById(4);
		ApuntarClase apClase = new ApuntarClase();
		apClase.setClase(clase);
		apClase.setPet(pet);
		try{
			this.claseService.apuntarMascota(apClase);
		}catch(DiferenciaTipoMascotaException ex){
			ex.printStackTrace();
        }catch(DiferenciaClasesDiasException ex2){
        	ex2.printStackTrace();
        }catch(LimiteAforoClaseException ex3){
        	ex3.printStackTrace();
        }catch(SolapamientoDeClasesException ex4){
        	ex4.printStackTrace();
        }catch(MacostaYaApuntadaException ex5){
        	ex5.printStackTrace();
        }
		Clase clase2 = this.claseService.findClaseById(10);
		ApuntarClase apClase2 = new ApuntarClase();
		apClase2.setClase(clase2);
		apClase2.setPet(pet);
		Assertions.assertThrows(SolapamientoDeClasesException.class, () ->{
			claseService.apuntarMascota(apClase2);
		});
	}
	
	@Test
	@Transactional
	void shouldThrowDiferenciaClasesDiasExceptionApuntarMascota() throws Exception {
		Clase clase = this.claseService.findClaseById(5);
		Pet pet = this.petService.findPetById(78);
		ApuntarClase apClase = new ApuntarClase();
		apClase.setClase(clase);
		apClase.setPet(pet);
		
		this.claseService.apuntarMascota(apClase);
		
		Clase clase2 = this.claseService.findClaseById(6);
		ApuntarClase apClase2 = new ApuntarClase();
		apClase2.setClase(clase2);
		apClase2.setPet(pet);
		
		this.claseService.apuntarMascota(apClase2);
		
		Clase clase3 = this.claseService.findClaseById(7);
		ApuntarClase apClase3 = new ApuntarClase();
		apClase3.setClase(clase3);
		apClase3.setPet(pet);
		
		
		try{
			this.claseService.apuntarMascota(apClase3);
		}catch(DiferenciaTipoMascotaException ex){
			ex.printStackTrace();
        }catch(DiferenciaClasesDiasException ex2){
        	ex2.printStackTrace();
        }catch(LimiteAforoClaseException ex3){
        	ex3.printStackTrace();
        }catch(SolapamientoDeClasesException ex4){
        	ex4.printStackTrace();
        }catch(MacostaYaApuntadaException ex5){
        	ex5.printStackTrace();
        }
		Clase clase4 = this.claseService.findClaseById(8);
		ApuntarClase apClase4 = new ApuntarClase();
		apClase4.setClase(clase4);
		apClase4.setPet(pet);
		Assertions.assertThrows(DiferenciaClasesDiasException.class, () ->{
			claseService.apuntarMascota(apClase4);
		});
	}
	
	@Test
	void shouldFindAllCategoriasClases() {
		Collection<CategoriaClase> CategoriasClases = this.claseService.findAllCategoriasClase();
		CategoriaClase CategoriasClase1 = EntityUtils.getById(CategoriasClases, CategoriaClase.class, 1);
		assertThat(CategoriasClase1.getName()).isEqualTo("Adiestrar");
		CategoriaClase CategoriasClase2 = EntityUtils.getById(CategoriasClases, CategoriaClase.class, 3);
		assertThat(CategoriasClase2.getName()).isEqualTo("Trucos basicos");
	}
	
	@Test
	void shouldFindAllPetTypes() {
		Collection<PetType> petTypes = this.petService.findPetTypes();
		PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
		assertThat(petType1.getName()).isEqualTo("cat");
		PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
		assertThat(petType4.getName()).isEqualTo("snake");
	}
	
	@Test
	void shouldFindAllAdiestradores() {
		List<String> adiestradores = new ArrayList<String>(this.adiestradorService.findNameAndLastnameAdiestrador());
		String adiestrador1 = "Daniel,Castroviejo";
		assertThat(adiestrador1).isEqualTo(adiestradores.get(0));
		String adiestrador2 = "Manuel,Castroviejo";
		assertThat(adiestrador2).isEqualTo(adiestradores.get(1));
	}
}
