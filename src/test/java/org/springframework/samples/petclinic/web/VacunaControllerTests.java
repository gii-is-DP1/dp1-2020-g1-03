package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vacuna;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.VacunaService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = VacunaController.class,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class VacunaControllerTests {
	
	private static final int TEST_VACUNA_ID = 1;
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_VETERINARIO_ID = 1;
	private static final int TEST_PET_ID = 1;

	@MockBean
	private VacunaService vacunaService;
	
	@MockBean
	private PetService petService;
	@MockBean
	private VetService vetService;

	@Autowired
	private MockMvc mockMvc;
	
	private Owner pedro;
	private Vet	josue;
	private Vacuna vacuna1;
	private Pet pet1;

	@BeforeEach
	void setup() {
		
		this.pedro = new Owner();
		User username= new User();
		username.setUsername("josue1");
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

		BDDMockito.given(this.vacunaService.findVacunaById(VacunaControllerTests.TEST_VACUNA_ID)).willReturn(this.vacuna1);
		BDDMockito.given(this.vetService.findVetIdByUsername("josue")).willReturn(VacunaControllerTests.TEST_VETERINARIO_ID);
		BDDMockito.given(this.petService.findPetById(VacunaControllerTests.TEST_PET_ID)).willReturn(this.pet1);
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testVacunaListOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/vacuna/", VacunaControllerTests.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("vacunas/vacunasListOwner"));
	}

	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testVacunaShowOwner() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/owners/{ownerId}/vacuna/{vacunaId}", VacunaControllerTests.TEST_OWNER_ID, VacunaControllerTests.TEST_VACUNA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("vacunas/vacunasShow"));
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
		given(this.petService.findAllPets()).willReturn(Lists.newArrayList(pet1, new Pet()));
		mockMvc.perform(MockMvcRequestBuilders.get("/vets/vacuna/pets")).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("vacunas/MascotasList"));
	}
	/*
	@WithMockUser(value = "josue", roles = "vet")
    @Test
    void testProcessFindFormByEspecie() throws Exception {
		given(this.vacunaService.findMascotaByEspecie(pet1.getType().getName())).willReturn(Lists.newArrayList(pet1));

		mockMvc.perform(get("/vets/vacuna/pets").param("pet", "cat")).andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/vets/vacuna/pets/" + VacunaControllerTests.TEST_PET_ID));
	}*/

}
