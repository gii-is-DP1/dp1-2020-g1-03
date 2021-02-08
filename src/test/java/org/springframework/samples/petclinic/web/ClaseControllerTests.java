package org.springframework.samples.petclinic.web;

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
import org.springframework.format.Formatter;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.model.CategoriaClase;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AdiestradorService;
import org.springframework.samples.petclinic.service.ClaseService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.samples.petclinic.service.VacunaService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = ClaseController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
includeFilters = @ComponentScan.Filter(value = Formatter.class, type = FilterType.ASSIGNABLE_TYPE),excludeAutoConfiguration = SecurityConfiguration.class)
public class ClaseControllerTests {

	private static final int TEST_CLASE_ID = 1;
	private static final int TEST_CLASE_ID_INEXISTENTE = 100;
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_SECRETARIO_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_ADIESTRADOR_ID = 1;

	@MockBean
	private ClaseService claseService;

	@MockBean
	private OwnerService ownerService;

	@MockBean
	private AdiestradorService adiService;

	@MockBean
	private SecretarioService secretarioService;

	@MockBean
	private PetService petService;
	
	@MockBean
	private VetService vetService;
	
	@MockBean
	private VacunaService vacunaService;

	@Autowired
	private MockMvc mockMvc;

	private Clase clase1;

	private Owner pedro;

	private Adiestrador josue = new Adiestrador();

	private Secretario angel;

	private Pet max;

	private LocalDate fecha = LocalDate.parse("2020-10-04");
	private LocalDateTime fechaClaseInicio = LocalDateTime.of(2021,01,14,13,30);
	private LocalDateTime fechaClaseFin = LocalDateTime.of(2021,01,14,16,30);
	private CategoriaClase adiestrar = new CategoriaClase();
	@BeforeEach
	void setup() {
		
		adiestrar.setId(1);
		adiestrar.setName("Adiestrar");
		PetType dog = new PetType();
		dog.setId(6);
		dog.setName("dog");

		this.pedro = new Owner();
		this.pedro.setId(ClaseControllerTests.TEST_OWNER_ID);
		this.pedro.setAddress("Gran Via");
		this.pedro.setCity("Madrid");
		this.pedro.setTelephone("954263514");
		this.pedro.setFirstName("Alenjandro");
		this.pedro.setLastName("Perez");

		User username = new User();
		this.josue.setId(ClaseControllerTests.TEST_ADIESTRADOR_ID);
		this.josue.setFirstName("Josue");
		this.josue.setLastName("Martinez");
		this.josue.setUser(username);
		this.josue.setCompetencias("competencia");

		this.angel = new Secretario();
		this.angel.setId(ClaseControllerTests.TEST_SECRETARIO_ID);
		this.angel.setFirstName("Angel");
		this.angel.setLastName("Sanz");
		this.angel.setProgramasDominados("Word");

		this.max = new Pet();
		this.max.setId(ClaseControllerTests.TEST_PET_ID);
		this.max.setName("Max");
		this.max.setBirthDate(this.fecha);
		this.max.setType(dog);
		this.max.setOwner(this.pedro);

		this.clase1 = new Clase();
		this.clase1.setId(ClaseControllerTests.TEST_CLASE_ID);
		this.clase1.setName("Clase 1");
		this.clase1.setFechaHoraInicio(this.fechaClaseInicio);
		this.clase1.setFechaHoraFin(this.fechaClaseFin);
		this.clase1.setNumeroPlazasTotal(15);
		this.clase1.setNumeroPlazasDisponibles(12);
		this.clase1.setAdiestrador(this.josue);
		this.clase1.setType(dog);
		this.clase1.setCategoriaClase(this.adiestrar);

		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(dog));
		BDDMockito.given(this.claseService.findClaseById(TEST_CLASE_ID)).willReturn(this.clase1);

		BDDMockito.given(this.ownerService.findOwnerByUsername("pedro").getId()).willReturn(ClaseControllerTests.TEST_OWNER_ID);
		BDDMockito.given(this.adiService.findAdiestradorByUsername("josue")).willReturn(this.josue);
		BDDMockito.given(this.claseService.findAllCategoriasClase()).willReturn(Lists.newArrayList(this.adiestrar));
		BDDMockito.given(this.adiService.findNameAndLastnameAdiestrador()).willReturn(Lists.newArrayList(this.josue.getFirstName()+","+this.josue.getLastName()));
		BDDMockito.given(this.adiService.findAllAdiestradores()).willReturn(Lists.newArrayList(this.josue));
		BDDMockito.given(this.petService.findMascotasOwner(this.pedro)).willReturn(Lists.newArrayList(this.max));
		BDDMockito.given(this.petService.findNameMascota(this.pedro)).willReturn(Lists.newArrayList("Max,1"));

	}

	//---------------------------------------ADIESTRADOR--------------------------------------------------

	@WithMockUser(value = "josue", roles = "adiestrador")
	@Test
	void testShowAdiestradorClaseForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adiestradores/clases/show/{claseId}", 1)).andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("clase"))
			.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("name", Matchers.is("Clase 1"))))
			.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("fechaHoraInicio", Matchers.is(fechaClaseInicio))))
			.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("fechaHoraFin", Matchers.is(fechaClaseFin))))
			.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("numeroPlazasTotal", Matchers.is(15))))
			.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("numeroPlazasDisponibles", Matchers.is(12))))
			.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("type", Matchers.is(clase1.getType()))))
			.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("adiestrador", Matchers.is(clase1.getAdiestrador()))))
			.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("categoriaClase", Matchers.is(clase1.getCategoriaClase()))))
			.andExpect(MockMvcResultMatchers.view().name("clases/showAdiestrador"));

	}
	
	@WithMockUser(value = "josue", roles = "adiestrador")
	@Test
	void testShowAdiestradorClaseFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adiestradores/clases/show/{claseId}", ClaseControllerTests.TEST_CLASE_ID_INEXISTENTE))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("clase"));
	}
	
	@WithMockUser(value = "josue", roles = "adiestrador")
	@Test
	void testAdiestradorListClases() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/adiestradores/clases", ClaseControllerTests.TEST_CLASE_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("clases/clasesList"));
	}
	
	
	//---------------------------------------OWNER---------------------------------------------------------
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testShowOwnerClaseForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/clases/show/{claseId}", 1)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("clase"))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("name", Matchers.is("Clase 1"))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("fechaHoraInicio", Matchers.is(fechaClaseInicio))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("fechaHoraFin", Matchers.is(fechaClaseFin))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("numeroPlazasTotal", Matchers.is(15))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("numeroPlazasDisponibles", Matchers.is(12))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("type", Matchers.is(clase1.getType()))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("adiestrador", Matchers.is(clase1.getAdiestrador()))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("categoriaClase", Matchers.is(clase1.getCategoriaClase()))))
		.andExpect(MockMvcResultMatchers.view().name("clases/showClaseOwner"));
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testOwnerListClases() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/clases", ClaseControllerTests.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("clases/clasesListOwner"));
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testOwnerInitApuntarMascotaCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/clases/show/apuntar/{claseId}", ClaseControllerTests.TEST_OWNER_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("clases/apuntarClases"));
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testProcessApuntarMascotaCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/clases/show/apuntar/{claseId}", ClaseControllerTests.TEST_CLASE_ID)
				.with(csrf()))
//				.param("pet", "Max,1"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/clases"));
	}
	
	
	//--------------------------------------------------SECRETARIO------------------------------------------------
	
	@WithMockUser(value = "angel", roles = "secretario")
	@Test
	void testSecretarioListClases() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/clases", ClaseControllerTests.TEST_SECRETARIO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("clases/clasesListSecretario"));
	}
	
	@WithMockUser(value = "angel", roles = "secretario")
	@Test
	void testShowSecretarioClaseForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/clases/show/{claseId}", 1)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("clase"))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("name", Matchers.is("Clase 1"))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("fechaHoraInicio", Matchers.is(fechaClaseInicio))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("fechaHoraFin", Matchers.is(fechaClaseFin))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("numeroPlazasTotal", Matchers.is(15))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("numeroPlazasDisponibles", Matchers.is(12))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("type", Matchers.is(clase1.getType()))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("adiestrador", Matchers.is(clase1.getAdiestrador()))))
		.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("categoriaClase", Matchers.is(clase1.getCategoriaClase()))))
		.andExpect(MockMvcResultMatchers.view().name("clases/showClaseSecretario"));
	}
	
	@WithMockUser(value = "angel", roles = "secretario")
	@Test
	void testSecretarioInitEditClase() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/clases/show/{claseId}/edit", ClaseControllerTests.TEST_SECRETARIO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("clase"))
				.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("name", Matchers.is("Clase 1"))))
				.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("fechaHoraInicio", Matchers.is(fechaClaseInicio))))
				.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("fechaHoraFin", Matchers.is(fechaClaseFin))))
				.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("numeroPlazasTotal", Matchers.is(15))))
				.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("numeroPlazasDisponibles", Matchers.is(12))))
				.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("type", Matchers.is(clase1.getType()))))
				.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("adiestrador", Matchers.is(clase1.getAdiestrador()))))
				.andExpect(MockMvcResultMatchers.model().attribute("clase", Matchers.hasProperty("categoriaClase", Matchers.is(clase1.getCategoriaClase()))))
				.andExpect(MockMvcResultMatchers.view().name("clases/crearOEditarClase"));
	}
	
	@WithMockUser(value = "angel", roles = "secretario")
	@Test
	void testProcessEditFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/secretarios/clases/show/{claseId}/edit", ClaseControllerTests.TEST_SECRETARIO_ID)
				.with(csrf())
				.param("name", "Clase 1")
				.param("fechaHoraInicio", "2021-01-15 15:30")
				.param("fechaHoraFin", "2021-01-15 16:30")
				.param("numeroPlazasTotal", "25")
				.param("numeroPlazasDisponibles", "13")
				.param("type", "dog")
				.param("adiestrador", "Josue,Martinez")
				.param("categoriaClase", "Adiestrar")
				)
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/secretarios/clases/show/{claseId}"));
	}
	
	@WithMockUser(value = "angel", roles = "secretario")
	@Test
	void testProcessEditFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/secretarios/clases/show/{claseId}/edit", ClaseControllerTests.TEST_CLASE_ID)
				.with(csrf())
				.param("name", "Clase 1")
				.param("fechaHoraInicio", "2025-01-14 15:30")
				.param("fechaHoraFin", "2025-01-14 16:30")
				.param("numeroPlazasTotal", "20")
				.param("numeroPlazasDisponibles", "12")
				.param("categoriaClase", "Adiestrar")
				.param("adiestrador", "Josue,Martinez"))
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("clase"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("clase", "type"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("clases/crearOEditarClase"));
	}
	
	@WithMockUser(value = "angel", roles = "secretario")
	@Test
	void testSecretarioInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/clases/new", ClaseControllerTests.TEST_SECRETARIO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("clases/crearOEditarClase")).andExpect(MockMvcResultMatchers.model().attributeExists("clase"));
	}
	
	@WithMockUser(value = "angel", roles = "secretario")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/secretarios/clases/new")
				.with(csrf())
				.param("name", "Clase 1")
				.param("fechaHoraInicio", "2025-01-14 15:30")
				.param("fechaHoraFin", "2025-01-14 16:30")
				.param("numeroPlazasTotal", "20")
				.param("numeroPlazasDisponibles", "12")
				.param("categoriaClase", "Adiestrar")
				.param("type", "dog")
				.param("adiestrador", "Josue,Martinez"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/secretarios/clases"));
	}
	
	@WithMockUser(value = "angel", roles = "secretario")
	@Test
	void testProcessCreationFormSuccessHasError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/secretarios/clases/new")
				.with(csrf())
				.param("name", "Clase 1")
				.param("fechaHoraInicio", "2025-01-14 15:30")
				.param("fechaHoraFin", "2025-01-14 16:30")
				.param("numeroPlazasTotal", "20")
				.param("numeroPlazasDisponibles", "12")
				.param("categoriaClase", "Adiestrar")
				.param("adiestrador", "Josue,Martinez"))
		.andExpect(MockMvcResultMatchers.model().attributeHasErrors("clase"))
		.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("clase", "type"))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("clases/crearOEditarClase"));
	}
	
	@WithMockUser(value = "angel", roles = "secretario")
	@Test
	void testDeleteClase() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.delete("secretarios/clases/show/{claseId}/delete", TEST_CLASE_ID))
		.andExpect(MockMvcResultMatchers.status().isForbidden());
	}

}
