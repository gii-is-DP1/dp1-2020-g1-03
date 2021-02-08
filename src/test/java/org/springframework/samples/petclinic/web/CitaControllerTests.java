package org.springframework.samples.petclinic.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
import org.springframework.samples.petclinic.model.CategoriaClase;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Clase;
import org.springframework.samples.petclinic.model.Estado;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.AdiestradorService;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.ClaseService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(controllers = CitaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class CitaControllerTests {

	private static final int TEST_CITA_ID = 6;
	private static final int TEST_CITA_ID_INEXISTENTE = 100;
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_SECRETARIO_ID = 1;
	private static final int TEST_PET_ID = 1;
	private static final int TEST_VET_ID = 1;

	@MockBean
	private CitaService citaService;

	@MockBean
	private OwnerService ownerService;
	
	@MockBean
	private VetService vetService;

	@MockBean
	private AdiestradorService adiService;

	@MockBean
	private SecretarioService secretarioService;

	@MockBean
	private PetService petService;

	@Autowired
	private MockMvc mockMvc;

	private Cita cita1;

	private Owner pedro;

	private Vet josue;

	private Secretario angel;

	private Pet max;
	
	private List<Pet> pets;

	private LocalDate fecha = LocalDate.parse("2020-10-04");
	private LocalDateTime fechaCita = LocalDateTime.of(2022,01,04,12,30);

	@BeforeEach
	void setup() {
		
		PetType dog = new PetType();
		dog.setId(6);
		dog.setName("perro");

		this.pedro = new Owner();
		this.pedro.setId(CitaControllerTests.TEST_OWNER_ID);
		this.pedro.setAddress("Gran Via");
		this.pedro.setCity("Madrid");
		this.pedro.setTelephone("954263514");
		this.pedro.setFirstName("Alenjandro");
		this.pedro.setLastName("Perez");

		this.josue = new Vet();
		User username = new User();
		this.josue.setId(CitaControllerTests.TEST_VET_ID);
		this.josue.setFirstName("Josue");
		this.josue.setLastName("Martinez");

		this.angel = new Secretario();
		this.angel.setId(CitaControllerTests.TEST_SECRETARIO_ID);
		this.angel.setFirstName("Angel");
		this.angel.setLastName("Sanz");
		this.angel.setProgramasDominados("Word");

		this.max = new Pet();
		this.max.setId(CitaControllerTests.TEST_PET_ID);
		this.max.setName("Max");
		this.max.setBirthDate(this.fecha);
		this.max.setType(dog);
		
		pets=new ArrayList<Pet>();
		//pets.add(max);
		this.cita1 = new Cita();
		this.cita1.setId(CitaControllerTests.TEST_CITA_ID);
		this.cita1.setName("Cita 1");
		this.cita1.setFechaHora(fechaCita);
		this.cita1.setName("CitaPrueba");
		this.cita1.setFechaHora(LocalDateTime.of(2022, 1, 4, 12, 30));
		this.cita1.setEstado(Estado.PENDIENTE);
		this.cita1.setRazon("RazonCitaPrueba");
		this.cita1.setTitulo("TituloCitaPrueba");
		this.cita1.setVet(josue);
		this.cita1.setPets(pets);
		BDDMockito.given(this.citaService.findCitaById(TEST_CITA_ID)).willReturn(this.cita1);
		//BDDMockito.given(this.ownerService.findOwnerByUsername("owner1").getId()).willReturn(CitaControllerTests.TEST_OWNER_ID);
		BDDMockito.given(this.adiService.findAdiestradorByUsername("josue").getId()).willReturn(CitaControllerTests.TEST_VET_ID);


	}

	//---------------------------------------VETERINARIO--------------------------------------------------

	
	@WithMockUser(value = "josue", roles = "vet")
	@Test
	void testVetListCitas() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/citas", CitaControllerTests.TEST_CITA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("citas/citasList"));
	}
	
	
	//---------------------------------------OWNER---------------------------------------------------------
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testOwnerListCitas() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/citas", CitaControllerTests.TEST_CITA_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("citas/citasOwnerList"));
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testShowOwnerCitaForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/citas/{citaId}", 6)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("cita"))
		
		.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("name", Matchers.is("CitaPrueba"))))
		.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("fechaHora", Matchers.is(fechaCita))))
		.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("razon", Matchers.is("RazonCitaPrueba"))))
		.andExpect(MockMvcResultMatchers.model().attribute("cita", Matchers.hasProperty("titulo", Matchers.is("TituloCitaPrueba"))))
			.andExpect(MockMvcResultMatchers.view().name("citas/showCitaOwner"));
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testShowOwnerCitaErrorForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/citas/{citaId}", 200))
			.andExpect(MockMvcResultMatchers.status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testOwnerInitCitaCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/citas/new")).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("citas/crearOEditarCitaOwner"));
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testProcessCitaCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/owners/citas/new")
				.with(csrf())
				.param("titulo", "Clase 1")
				.param("fechaHora", "2021-01-15 15:30")
				.param("razon", "Si"))
		.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
	//--------------------------------------------------SECRETARIO------------------------------------------------
	
	@WithMockUser(value = "angel", roles = "secretario")
	@Test
	void testSecretarioListCitas() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/citas", CitaControllerTests.TEST_SECRETARIO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("citas/citasSecretarioList"));
	}
	
	@WithMockUser(value = "angel", roles = "secretario")
	@Test
	void testSecretarioListCitasSinVet() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/citas/sinVet", CitaControllerTests.TEST_SECRETARIO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("citas/citasSecretarioSinVetList"));
	}
	
	
	

}
