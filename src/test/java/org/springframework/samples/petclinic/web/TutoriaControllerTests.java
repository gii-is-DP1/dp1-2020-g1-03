package org.springframework.samples.petclinic.web;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Tutoria;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AdiestradorService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.TutoriaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = TutoriaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class TutoriaControllerTests {
	
	private static final int TEST_TUTORIA_ID = 1;
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_ADI_ID = 1;
	private static final int TEST_PET_ID = 1;
	
	@MockBean
	private TutoriaService tutoriaService;
	
	@MockBean
	private OwnerService ownerService;

	@MockBean
	private AdiestradorService adiService;

	@MockBean
	private PetService petService;

	@Autowired
	private MockMvc mockMvc;
	
	private Tutoria tutoria1;
	
	private Owner pedro;

	private Adiestrador josue;

	private Pet max;
	
	private LocalDate fecha = LocalDate.parse("2020-10-04");
	private LocalDateTime fechaHora = LocalDateTime.of(2021,01,14,13,30);

	
	@BeforeEach
	void setup() {
		
		PetType dog = new PetType();
		dog.setId(6);
		dog.setName("dog");
		
		this.pedro = new Owner();
		this.pedro.setId(TutoriaControllerTests.TEST_OWNER_ID);
		this.pedro.setAddress("Gran Via");
		this.pedro.setCity("Madrid");
		this.pedro.setTelephone("954263514");
		this.pedro.setFirstName("Alenjandro");
		this.pedro.setLastName("Perez");
		
		this.josue = new Adiestrador();
		User username = new User();
		this.josue.setId(TutoriaControllerTests.TEST_ADI_ID);
		this.josue.setFirstName("Josue");
		this.josue.setLastName("Martinez");
		this.josue.setUser(username);
		this.josue.setCompetencias("competencia");
		
		this.max = new Pet();
		this.max.setId(TutoriaControllerTests.TEST_PET_ID);
		this.max.setName("Max");
		this.max.setBirthDate(this.fecha);
		this.max.setType(dog);
		
		this.tutoria1 = new Tutoria();
		this.tutoria1.setId(TutoriaControllerTests.TEST_TUTORIA_ID);
		this.tutoria1.setFechaHora(fechaHora);
		this.tutoria1.setTitulo("Primera tutoria");
		this.tutoria1.setRazon("Mejoras en el animal");
		this.tutoria1.setPet(max);
		
		BDDMockito.given(this.ownerService.findOwnerIdByUsername("pedro")).willReturn(TutoriaControllerTests.TEST_OWNER_ID);
		BDDMockito.given(this.adiService.findAdiestradorIdByUsername("josue")).willReturn(TutoriaControllerTests.TEST_ADI_ID);
		BDDMockito.given(this.tutoriaService.findTutoriaById(TEST_TUTORIA_ID)).willReturn(this.tutoria1);
		BDDMockito.given(this.tutoriaService.findMascotaByName("Max")).willReturn(Lists.newArrayList(max));
		
	}
	
	@WithMockUser(value = "josue", roles = "adiestrador")
	@Test	
	void testShowAdiestradorTutoriaForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adiestradores/tutorias/show/{tutoriaId}", TutoriaControllerTests.TEST_ADI_ID))
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("tutoria"))
			
			.andExpect(MockMvcResultMatchers.model().attribute("tutoria", Matchers.hasProperty("titulo", Matchers.is("Primera tutoria"))))
			.andExpect(MockMvcResultMatchers.model().attribute("tutoria", Matchers.hasProperty("fechaHora", Matchers.is(fechaHora))))
			.andExpect(MockMvcResultMatchers.model().attribute("tutoria", Matchers.hasProperty("razon", Matchers.is("Mejoras en el animal"))))
			.andExpect(MockMvcResultMatchers.view().name("tutorias/tutoriaShowAdiestrador"));
	}
	
	@WithMockUser(value = "josue", roles = "adiestrador")
	@Test	
	void testShowAdiestradorListTutorias() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adiestradores/tutorias", TutoriaControllerTests.TEST_ADI_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("tutorias/tutoriasList"));
	}
	
	@WithMockUser(value = "josue", roles = "adiestrador")
	@Test
	void testInitFindNombreMascotaForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adiestradores/tutorias/pets/find", TutoriaControllerTests.TEST_ADI_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("tutorias/EncontrarMascotas"));
	}
	
	@WithMockUser(value = "josue", roles = "adiestrador")
    @Test
    void testAdiestradorProcessFindFormSuccess() throws Exception {
		given(this.petService.findAllPets()).willReturn(Lists.newArrayList(max));
		mockMvc.perform(MockMvcRequestBuilders.get("/adiestradores/tutorias/pets/find").param("name", "")).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("tutorias/EncontrarMascotas"));
	}
	
	@WithMockUser(value = "josue", roles = "adiestrador")
    @Test
    void testAdiestradorProcessFindFormByName() throws Exception {
		given(this.tutoriaService.findMascotaByName(max.getName())).willReturn(Lists.newArrayList(max));
		mockMvc.perform(get("/adiestradores/tutorias/pets").param("name", "Max"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/adiestradores/tutorias/pets/" + TutoriaControllerTests.TEST_PET_ID + "/new"));
	}
	
	@WithMockUser(value = "josue", roles = "vet")
	@Test
	void testAdiestradorProcessFindFormNoMascotasFound() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/adiestradores/tutorias/pets").param("name", "Pakito"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("pet", "name"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("pet", "name", "notFound"))
				.andExpect(MockMvcResultMatchers.view().name("tutorias/EncontrarMascotas"));
	}
	
	@WithMockUser(value = "josue", roles = "adiestrador")
	@Test
	void testAdiestradorInitCreateTutoria() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adiestradores/tutorias/pets/{petId}/new", TutoriaControllerTests.TEST_ADI_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("tutorias/crearOEditarTutoria")).andExpect(MockMvcResultMatchers.model().attributeExists("tutoria"));
	}
	
	@WithMockUser(value = "josue", roles = "adiestrador")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/adiestradores/tutorias/pets/{petId}/new", TEST_PET_ID).with(csrf())
				.param("titulo", "Primera tutoria")
				.param("fechaHora", "2025-01-14 16:30")
				.param("razon", "Mejoras en el animal")
				.param("pet", "max")
				.param("adiestrador", "josue")
				.param("owner", "pedro"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/adiestradores/tutorias"));		
	}
	
	@WithMockUser(value = "josue", roles = "adiestrador")
	@Test
	void testProcessCreationFormNotSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/adiestradores/tutorias/pets/{petId}/new", TEST_PET_ID).with(csrf())
				.param("titulo", "Primera tutoria")
				.param("fechaHora", "2025-01-14 16:30")
				.param("razon", "Mejoras en el animal"))
				.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("pet"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("tutoria"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("tutoria", "fechaHora"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/adiestradores/tutorias"));		
	}
	
	
	
	
	
	
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testShowOwnerTutoriaForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/tutorias/show/{tutoriaId}", TutoriaControllerTests.TEST_TUTORIA_ID))
		.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("tutoria"))
			
			.andExpect(MockMvcResultMatchers.model().attribute("tutoria", Matchers.hasProperty("titulo", Matchers.is("Primera tutoria"))))
			.andExpect(MockMvcResultMatchers.model().attribute("tutoria", Matchers.hasProperty("fechaHora", Matchers.is(fechaHora))))
			.andExpect(MockMvcResultMatchers.model().attribute("tutoria", Matchers.hasProperty("razon", Matchers.is("Mejoras en el animal"))))
			.andExpect(MockMvcResultMatchers.view().name("tutorias/tutoriaShowOwner"));
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testShowOwnerListTutorias() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/tutorias", TutoriaControllerTests.TEST_TUTORIA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("tutorias/tutoriasListOwner"));
	}
	
	

}
