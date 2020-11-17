
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
import org.springframework.samples.petclinic.model.Economista;
import org.springframework.samples.petclinic.model.Gasto;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.EconomistaService;
import org.springframework.samples.petclinic.service.GastoService;
import org.springframework.samples.petclinic.service.OwnerService;
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
@WebMvcTest(controllers = GastoController.class,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
	classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration= SecurityConfiguration.class)
class GastoControllerTests {

	private static final int	TEST_GASTO_ID				= 1;
	private static final int	TEST_GASTO_ID_INEXISTENTE	= 100;
	private static final int	TEST_ECONOMISTA_ID				= 1;

	@MockBean
	private GastoService		gastoService;

	@MockBean
	private EconomistaService	economistaService;
	
	@MockBean
	private OwnerService	ownerService;


	@Autowired
	private MockMvc				mockMvc;

	private Gasto				gasto1;

	private Economista			josue;
	
	private Owner				pedro;
	
	private Economista			error;

	private LocalDate			fecha = LocalDate.parse("2020-10-04");

	private Gasto 				gasto2;


	@BeforeEach
	void setup() {
		this.pedro = new Owner();
		this.pedro.setId(GastoControllerTests.TEST_ECONOMISTA_ID);
		this.pedro.setAddress("Plaza San Pedro");
		this.pedro.setCity("Roma");
		this.pedro.setTelephone("954442211");
		this.pedro.setFirstName("Josue");
		this.pedro.setLastName("Perez Gutierrez");
		
		this.josue = new Economista();
		this.josue.setId(GastoControllerTests.TEST_ECONOMISTA_ID);
		this.josue.setEstudios("Administracion y Direccion de Empresas");
		this.josue.setFirstName("Josue");
		this.josue.setLastName("Perez Gutierrez");


		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2012);
		cal.set(Calendar.MONTH, Calendar.JANUARY);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date dateRepresentation = cal.getTime();


		this.error = new Economista();
		this.error.setId(2);
		this.error.setEstudios("Error");
		this.error.setFirstName("Error");
		this.error.setLastName("Error");

		this.gasto1 = new Gasto();
		this.gasto1.setId(GastoControllerTests.TEST_GASTO_ID);
		this.gasto1.setFecha(this.fecha);
		this.gasto1.setCantidad(250);
		this.gasto1.setDescription("Gasto correspondiente a la compra de material esterilizante para la clinica");
		this.gasto1.setTitulo("Material esterilizante");
		this.gasto1.setEconomista(josue);
		
		BDDMockito.given(this.gastoService.findGastoById(GastoControllerTests.TEST_GASTO_ID)).willReturn(this.gasto1);
		BDDMockito.given(this.economistaService.findEconomistaIdByUsername("josue")).willReturn(GastoControllerTests.TEST_ECONOMISTA_ID);

	}

	@WithMockUser(value = "josue", roles = "economista")
	@Test
	void testShowGastoForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/gasto/{gastoId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("gasto"))

			.andExpect(MockMvcResultMatchers.model().attribute("gasto", Matchers.hasProperty("titulo", Matchers.is("Material esterilizante"))))

			.andExpect(MockMvcResultMatchers.model().attribute("gasto", Matchers.hasProperty("cantidad", Matchers.is(250))))

			.andExpect(MockMvcResultMatchers.view().name("gastos/gastosShow"));
	}


	@WithMockUser(value = "josue", roles = "economista")
	@Test
	void testShowGastoFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/gasto/{gastoId}", 16))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("gasto"))

			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	// Escenario positivo
	@WithMockUser(value = "josue", roles = "economista")
	@Test
	void testGastoList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/gasto", GastoControllerTests.TEST_GASTO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("gastos/gastosList"));

	}
	
	// Escenario negativo
	//PREGUNTAR
//		@WithMockUser(value = "pedro", roles = "owner")
//		@Test
//		void testGastoListError() throws Exception {
//			this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/gasto", GastoControllerTests.TEST_GASTO_ID))
//			.andExpect(MockMvcResultMatchers.status().is);
//
//		}

}
