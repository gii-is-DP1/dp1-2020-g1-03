package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;

import org.hamcrest.Matchers;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.TipoEnfermedad;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vacuna;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VacunaService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.samples.petclinic.service.exceptions.DistanciaEntreDiasException;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = VacunaController.class,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
classes = WebSecurityConfigurer.class),includeFilters = @ComponentScan.Filter(value = VacunaFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
excludeAutoConfiguration= SecurityConfiguration.class)
public class VacunaControllerTests {
	
	private static final int TEST_VACUNA_ID = 1;
	private static final int TEST_NO_VACUNA_ID = 100;
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_VETERINARIO_ID = 1;
	private static final int TEST_PET_ID = 2;

	@MockBean
	private VacunaService vacunaService;
	@MockBean
	private PetService petService;
	@MockBean
	private VetService vetService;
	@MockBean
	private OwnerService ownerService;

	@Autowired
	private MockMvc mockMvc;
	
	private Owner pedro;
	private Vet	josue;
	private Vacuna vacuna1;
	private Pet pet1;
	private LocalDate fecha = LocalDate.parse("2012-08-06");

	@BeforeEach
	void setup() throws DataAccessException, DistanciaEntreDiasException {
		
		TipoEnfermedad rabia = new TipoEnfermedad();
		rabia.setName("Rabia");
		rabia.setId(3);
		
		PetType hamster = new PetType();
		hamster.setId(6);
		hamster.setName("hamster");
		
		this.pedro = new Owner();
		User username= new User();
		username.setUsername("pedro1");
		this.pedro.setUser(username);
		this.pedro.setId(VacunaControllerTests.TEST_OWNER_ID);
		this.pedro.setAddress("Plaza San Pedro");
		this.pedro.setCity("Roma");
		this.pedro.setTelephone("954442211");
		this.pedro.setFirstName("Josue");
		this.pedro.setLastName("Perez Gutierrez");
		
		this.josue = new Vet();
		this.josue.setId(VacunaControllerTests.TEST_VETERINARIO_ID);
		this.josue.setFirstName("Josue");
		this.josue.setLastName("Perez Gutierrez");
		 
		this.pet1 = new Pet();
		this.pet1.setId(VacunaControllerTests.TEST_PET_ID);
		this.pet1.setBirthDate(fecha);
		this.pet1.setName("Basil");
		this.pet1.setType(hamster);
		this.pet1.setOwner(this.pedro);
		
		
		this.vacuna1= new Vacuna();
		this.vacuna1.setFecha(LocalDate.parse("2020-08-06"));
		this.vacuna1.setDescripcion("Vacuna de prueba");
		this.vacuna1.setPet(this.pet1);
		this.vacuna1.setTipoEnfermedad(rabia);
		this.vacuna1.setId(VacunaControllerTests.TEST_VACUNA_ID);
		this.vacuna1.setVet(this.josue);

		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(hamster));
		BDDMockito.given(this.vacunaService.findVacunaById(VacunaControllerTests.TEST_VACUNA_ID)).willReturn(this.vacuna1);
		BDDMockito.given(this.vacunaService.findVacunaById(VacunaControllerTests.TEST_NO_VACUNA_ID)).willReturn(null);
		BDDMockito.given(this.vacunaService.findTipoEnfermedades()).willReturn(Lists.newArrayList(rabia));
		BDDMockito.given(this.vetService.findVetByUsername("josue")).willReturn(this.josue);
		BDDMockito.given(this.petService.findPetById(VacunaControllerTests.TEST_PET_ID)).willReturn(this.pet1);
		BDDMockito.given(this.ownerService.findOwnerByUsername("pedro1")).willReturn(this.pedro);
		BDDMockito.doAnswer(setIdVacuna()).when(this.vacunaService).saveVacuna(BDDMockito.any(), BDDMockito.anyInt());
	}
	
	private Answer<Void> setIdVacuna() {
		return new Answer<Void>() {
			public Void answer(InvocationOnMock invocation) {
				Vacuna vacuna = (Vacuna) invocation.getArguments()[0];
				vacuna.setId(VacunaControllerTests.TEST_VACUNA_ID);
			       return null;
			}
		};
	}

	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testVacunaListOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/vacuna/", VacunaControllerTests.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("vacunas/vacunasListOwner"));
	}

	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testVacunaShowOwnerErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/vacuna/{vacunaId}", VacunaControllerTests.TEST_OWNER_ID, VacunaControllerTests.TEST_NO_VACUNA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("vacuna","descripcion","tipoEnfermedad"));
	}
	
	@WithMockUser(value = "josue", roles = "vet")
	@Test
	void testVacunaListVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/vets/vacuna")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("vacunas/vacunasListVet"));
	}

	@WithMockUser(value = "josue", roles = "vet")
	@Test
	void testVacunaShowVet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/vets/vacuna/{vacunaId}", VacunaControllerTests.TEST_VACUNA_ID))
		.andExpect(MockMvcResultMatchers.model().attribute("vacuna", Matchers.hasProperty("tipoEnfermedad", Matchers.hasProperty("name", Matchers.is("Rabia")))))
		.andExpect(MockMvcResultMatchers.model().attribute("vacuna", Matchers.hasProperty("fecha", Matchers.is(vacuna1.getFecha()))))
		.andExpect(MockMvcResultMatchers.model().attribute("vacuna", Matchers.hasProperty("descripcion", Matchers.is(vacuna1.getDescripcion()))))
		.andExpect(MockMvcResultMatchers.model().attribute("vacuna", Matchers.hasProperty("pet", Matchers.is(vacuna1.getPet()))))
		.andExpect(MockMvcResultMatchers.model().attribute("vacuna", Matchers.hasProperty("vet", Matchers.is(vacuna1.getVet()))))		
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("vacunas/vacunasShow"));
	}
	
	@WithMockUser(value = "josue", roles = "vet")
    @Test
    void testInitFindForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/vets/vacuna/pets/find"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeExists("pet"))
			.andExpect(view().name("vacunas/EncontrarMascotas"));
	}
	
	@WithMockUser(value = "josue", roles = "vet")
    @Test
    void testProcessFindFormSuccess() throws Exception {
		given(this.petService.findAllPets()).willReturn(Lists.newArrayList(pet1));
		mockMvc.perform(MockMvcRequestBuilders.get("/vets/vacuna/pets").param("type.name", "")).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("vacunas/MascotasList"));
	}
	
	@WithMockUser(value = "josue", roles = "vet")
    @Test
    void testProcessFindFormByEspecie() throws Exception {
		given(this.vacunaService.findMascotaByEspecie(pet1.getType().getName())).willReturn(Lists.newArrayList(pet1));
		mockMvc.perform(get("/vets/vacuna/pets").param("type.name", "hamster")).andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/vets/vacuna/pets/" + VacunaControllerTests.TEST_PET_ID));
	}
	
    @WithMockUser(value = "josue", roles = "vet")
	@Test
	void testProcessFindFormNoMascotasFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/vets/vacuna/pets").param("type.name", "hasmter"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("pet", "type.name"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("pet", "type.name", "notFound"))
				.andExpect(MockMvcResultMatchers.view().name("vacunas/EncontrarMascotas"));
	}
    @WithMockUser(value = "josue", roles = "vet")
	@Test
	void testShowPet() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/vets/vacuna/pets/{petId}", VacunaControllerTests.TEST_PET_ID))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("pet"))
				.andExpect(MockMvcResultMatchers.model().attribute("pet", Matchers.hasProperty("name", Matchers.is("Basil"))))
				.andExpect(MockMvcResultMatchers.model().attribute("pet", Matchers.hasProperty("type", Matchers.is(pet1.getType()))))
				.andExpect(MockMvcResultMatchers.model().attribute("pet", Matchers.hasProperty("birthDate", Matchers.is(LocalDate.of(2012, 8, 06)))))
				.andExpect(MockMvcResultMatchers.model().attribute("pet", Matchers.hasProperty("owner", Matchers.is(pet1.getOwner()))))
				.andExpect(MockMvcResultMatchers.view().name("vacunas/MascotaShow"));
	}
    
    @WithMockUser(value = "josue", roles = "vet")
    @Test
    void testInitCreationVacunaForm() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders.get("/vets/vacuna/pets/{petId}/create", VacunaControllerTests.TEST_PET_ID)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("vacunas/crearVacuna")).andExpect(model().attributeExists("vacuna"));
    }
    
    @WithMockUser(value = "josue", roles = "vet")
    @Test
    void testProcessCreationVacunaFormSuccess() throws Exception {
    	mockMvc.perform(MockMvcRequestBuilders.post("/vets/vacuna/pets/{petId}/create", VacunaControllerTests.TEST_PET_ID)
						.with(csrf())
						.param("fecha", "2020-08-06")
						.param("tipoEnfermedad", "Rabia")
						.param("descripcion", "Prueba de vacuna"))
    		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/vets/vacuna/"+VacunaControllerTests.TEST_VACUNA_ID));
    }
    
	@WithMockUser(value = "josue", roles = "vet")
    @Test
    void testProcessCreationVacunaFormHasErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/vets/vacuna/pets/{petId}/create", VacunaControllerTests.TEST_PET_ID)
				.with(csrf())
				.param("fecha", "2020-08-06")
				.param("descripcion", "aaaaaa"))
			.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("pet"))
			.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("vet"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("vacuna"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("vacuna", "tipoEnfermedad"))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("vacunas/crearVacuna"));
	}
	

}