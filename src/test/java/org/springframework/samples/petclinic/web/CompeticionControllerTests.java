package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

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
import org.springframework.samples.petclinic.model.Competicion;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.service.CompeticionPetService;
import org.springframework.samples.petclinic.service.CompeticionService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = CompeticionController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class CompeticionControllerTests {

	private static final int TEST_COMPETICION_ID = 1;
	private static final int TEST_COMPETICION_ID_INEXISTENTE = 100;
	private static final int TEST_SECRETARIO_ID = 1;

	@MockBean
	private CompeticionService competicionService;

	@MockBean
	private SecretarioService secretarioService;

	@MockBean
	private PetService petService;

	@MockBean
	private OwnerService ownerService;

	@MockBean
	private CompeticionPetService competicionPetService;

	@Autowired
	private MockMvc mockMvc;

	private Competicion competicion1;

	private Secretario josue;

	private Owner pedro;

	private Secretario error;

	private LocalDate fechaInicio = LocalDate.of(2020, 10, 4);

	private LocalDate fechaFin = LocalDate.of(2020, 10, 4);

	private Competicion competicion2;

	@BeforeEach
	void setup() {
		this.pedro = new Owner();
		this.pedro.setId(CompeticionControllerTests.TEST_SECRETARIO_ID);
		this.pedro.setAddress("Plaza San Pedro");
		this.pedro.setCity("Roma");
		this.pedro.setTelephone("954442211");
		this.pedro.setFirstName("Josue");
		this.pedro.setLastName("Perez Gutierrez");

		this.josue = new Secretario();
		this.josue.setId(CompeticionControllerTests.TEST_SECRETARIO_ID);
		this.josue.setProgramasDominados("Excel");
		this.josue.setFirstName("Josue");
		this.josue.setLastName("Perez Gutierrez");

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2012);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date dateRepresentation = cal.getTime();

		this.error = new Secretario();
		this.error.setId(2);
		this.error.setProgramasDominados("Error");
		this.error.setFirstName("Error");
		this.error.setLastName("Error");

		this.competicion1 = new Competicion();
		this.competicion1.setId(CompeticionControllerTests.TEST_COMPETICION_ID);
		this.competicion1.setFechaHoraInicio(this.fechaInicio);
		this.competicion1.setFechaHoraFin(this.fechaFin);
		this.competicion1.setCantidad(250);
		this.competicion1.setNombre("nombre1");
		this.competicion1.setPremios("Comida para peces");
		this.competicion1.setSecretario(josue);

		BDDMockito.given(this.competicionService.findCompeticionById(CompeticionControllerTests.TEST_COMPETICION_ID))
				.willReturn(this.competicion1);
		// BDDMockito.given(this.secretarioService.find("josue")).willReturn(CompeticionControllerTests.TEST_ECONOMISTA_ID);

	}

	// Secretario
	@WithMockUser(value = "josue", roles = "secretario")
	@Test
	void testShowCompeticionForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/competiciones/show/{competicionId}", 1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("competicion"))
				.andExpect(MockMvcResultMatchers.model().attribute("competicion",
						Matchers.hasProperty("nombre", Matchers.is("nombre1"))))
				.andExpect(MockMvcResultMatchers.model().attribute("competicion",
						Matchers.hasProperty("cantidad", Matchers.is(250))))
				.andExpect(MockMvcResultMatchers.view().name("competiciones/competicionesShow"));
	}

	@WithMockUser(value = "josue", roles = "secretario")
	@Test
	void testShowCompeticionFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/competiciones/show/{competicionId}", 16))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("competicion"))

				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// Escenario positivo
	@WithMockUser(value = "josue", roles = "secretario")
	@Test
	void testCompeticionList() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/secretarios/competiciones",
						CompeticionControllerTests.TEST_COMPETICION_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("competiciones/competicionesList"));

	}

	// Owner
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testShowCompeticionFormOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/competiciones/show/{competicionId}", 1))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("competicion"))
				.andExpect(MockMvcResultMatchers.model().attribute("competicion",
						Matchers.hasProperty("nombre", Matchers.is("nombre1"))))
				.andExpect(MockMvcResultMatchers.model().attribute("competicion",
						Matchers.hasProperty("cantidad", Matchers.is(250))))
				.andExpect(MockMvcResultMatchers.view().name("competiciones/competicionesShowOwner"));
	}

	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testShowCompeticionFormErrorOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/competiciones/show/{competicionId}", 16))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("competicion"))

				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// Escenario positivo
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testCompeticionListOwner() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/competiciones",
						CompeticionControllerTests.TEST_COMPETICION_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("competiciones/competicionesListOwner"));

	}

//	// Escenario negativo
//	//PREGUNTAR
//		@WithMockUser(value = "pedro", roles = "owner")
//		@Test
//		void testGastoListError() throws Exception {
//			this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/gasto", GastoControllerTests.TEST_GASTO_ID))
//			.andExpect(MockMvcResultMatchers.status().is);
//
//		}
	@WithMockUser(value = "josue", roles = "secretario")
	@Test
	void testInitCompeticionCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/competiciones/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("competiciones/competicionesCreateOrUpdate"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("competicion"));
	}

	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testInitCompeticionCreationFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/competiciones/new"))
				.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
				.andExpect(MockMvcResultMatchers.view().name("competiciones/competicionesCreateOrUpdate"));
	}

	@WithMockUser(value = "josue", roles = "secretario")
	@Test
	void testProcessCompeticionCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/secretarios/competiciones/new").with(csrf())
				.param("nombre", "Torneo Sevilla").param("cantidad", "25").param("fechaHoraInicio", "2022-01-01")
				.param("fechaHoraFin", "2022-01-02").param("premios", "muchos"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("competicion"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "josue", roles = "secretario")
	@Test
	void testInitCompetitionEditForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/secretarios/competiciones/edit/{competicionId}",
						TEST_COMPETICION_ID))
				.andExpect(MockMvcResultMatchers.view().name("competiciones/competicionesCreateOrUpdate"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("competicion"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "josue", roles = "secretario")
	@Test
	void testInitCompetitionEditFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/competiciones/edit/{competicionId}", 98))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("competicion"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "josue", roles = "secretario")
	@Test
	void testProcessCompetitionEditForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders
				.post("/secretarios/competiciones/edit/{competicionId}", TEST_COMPETICION_ID).with(csrf())
				.param("nombre", "Torneo Sevilla").param("cantidad", "25").param("fechaHoraInicio", "2022-01-01")
				.param("fechaHoraFin", "2022-01-02").param("premios", "muchos"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("competicion"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testInitInscribePet() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/owners/competiciones/show/{competicionId}/inscribir",
						TEST_COMPETICION_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("competiciones/competicionesInscribePet"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("competicionPet"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("pets"));
	}

	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testInitInscribePetError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/competiciones/show/{competicionId}/inscribir", 99))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("competiciones/competicionesInscribePet"))
				.andExpect(MockMvcResultMatchers.model().attribute("competicionPet",
						Matchers.hasProperty("competicion", Matchers.nullValue())))
				.andExpect(MockMvcResultMatchers.model().attributeExists("pets"));
	}

	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testProcessInscribePet() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/owners/competiciones/show/{competicionId}/inscribir", TEST_COMPETICION_ID).with(csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
