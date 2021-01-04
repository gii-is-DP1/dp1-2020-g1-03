
package org.springframework.samples.petclinic.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

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
import org.springframework.samples.petclinic.model.Competicion;
import org.springframework.samples.petclinic.model.Economista;
import org.springframework.samples.petclinic.model.Gasto;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.service.CompeticionService;
import org.springframework.samples.petclinic.service.EconomistaService;
import org.springframework.samples.petclinic.service.GastoService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@WebMvcTest(controllers = CompeticionController.class,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
	classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration= SecurityConfiguration.class)
class CompeticionControllerTests {

	private static final int	TEST_COMPETICION_ID				= 1;
	private static final int	TEST_COMPETICION_ID_INEXISTENTE	= 100;
	private static final int	TEST_SECRETARIO_ID				= 1;

	@MockBean
	private CompeticionService		competicionService;

	@MockBean
	private SecretarioService	secretarioService;
	
	@MockBean
	private PetService	petService;
	
	@MockBean
	private OwnerService	ownerService;


	@Autowired
	private MockMvc				mockMvc;

	private Competicion			competicion1;

	private Secretario			josue;
	
	private Owner				pedro;
	
	private Secretario			error;

	private LocalDateTime		fechaInicio = LocalDateTime.parse("2020-10-04T10:00");
	
	private LocalDateTime		fechaFin = LocalDateTime.parse("2020-10-04T14:00");

	private Competicion 		competicion2;


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
		
		BDDMockito.given(this.competicionService.findCompeticionById(CompeticionControllerTests.TEST_COMPETICION_ID)).willReturn(this.competicion1);
		//BDDMockito.given(this.secretarioService.find("josue")).willReturn(CompeticionControllerTests.TEST_ECONOMISTA_ID);

	}
	//Secretario
	@WithMockUser(value = "josue", roles = "secretario")
	@Test
	void testShowCompeticionForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/competiciones/show/{competicionId}", 1))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("competicion"))
			.andExpect(MockMvcResultMatchers.model().attribute("competicion", Matchers.hasProperty("nombre", Matchers.is("nombre1"))))
			.andExpect(MockMvcResultMatchers.model().attribute("competicion", Matchers.hasProperty("cantidad", Matchers.is(250))))
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
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/competiciones", CompeticionControllerTests.TEST_COMPETICION_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("competiciones/competicionesList"));

	}
	
	//Owner
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testShowCompeticionFormOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/competiciones/show/{competicionId}", 1))
			.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("competicion"))
			.andExpect(MockMvcResultMatchers.model().attribute("competicion", Matchers.hasProperty("nombre", Matchers.is("nombre1"))))
			.andExpect(MockMvcResultMatchers.model().attribute("competicion", Matchers.hasProperty("cantidad", Matchers.is(250))))
			.andExpect(MockMvcResultMatchers.view().name("competiciones/competicionesShow"));
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
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/competiciones", CompeticionControllerTests.TEST_COMPETICION_ID))
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

}
