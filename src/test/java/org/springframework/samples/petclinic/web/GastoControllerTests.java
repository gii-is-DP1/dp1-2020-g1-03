
package org.springframework.samples.petclinic.web;



import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.util.Calendar;

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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = GastoController.class,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
	classes = WebSecurityConfigurer.class),
	excludeAutoConfiguration= SecurityConfiguration.class)
class GastoControllerTests {

	private static final int	TEST_GASTO_ID				= 1;
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

	@WithMockUser(value = "josue", roles = "economista")
	@Test
	void testGastoList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/gasto", GastoControllerTests.TEST_GASTO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("gastos/gastosList"));

	}
	
	@WithMockUser(value = "josue", roles = "economista")
	@Test
	void testEconomistaInitEditGasto() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/gasto/{gastoId}/edit", GastoControllerTests.TEST_GASTO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("gasto"))
		.andExpect(MockMvcResultMatchers.model().attribute("gasto", Matchers.hasProperty("titulo", Matchers.is("Material esterilizante"))))
		.andExpect(MockMvcResultMatchers.model().attribute("gasto", Matchers.hasProperty("cantidad", Matchers.is(250))))
		.andExpect(MockMvcResultMatchers.model().attribute("gasto", Matchers.hasProperty("fecha", Matchers.is(fecha))))
		.andExpect(MockMvcResultMatchers.model().attribute("gasto", Matchers.hasProperty("description", Matchers.is("Gasto correspondiente a la compra de material esterilizante para la clinica"))))
		.andExpect(MockMvcResultMatchers.view().name("gastos/crearOEditarGasto"));
	}
	
	@WithMockUser(value = "josue", roles = "economista")
	@Test
	void testProcessEditFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/economistas/gasto/{gastoId}/edit", GastoControllerTests.TEST_GASTO_ID)
				.with(csrf())
				.param("titulo", "Nuevo material esterilizante")
				.param("cantidad", "280")
				.param("fecha", "2021-01-15")
				.param("description", "Nuevos esterilizantes"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/economistas/gasto/{gastoId}"));
	}
	
	@WithMockUser(value = "josue", roles = "economista")
	@Test
	void testProcessEditFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/economistas/gasto/{gastoId}/edit", GastoControllerTests.TEST_GASTO_ID)
				.with(csrf())
				.param("titulo", "Nuevo material esterilizante")
				.param("cantidad", "280")
				.param("fecha", "2022-01-15")
				.param("description", "Nuevos esterilizantes"))
		.andExpect(MockMvcResultMatchers.view().name("gastos/crearOEditarGasto"));
	}
	
	@WithMockUser(value = "josue", roles = "economista")
	@Test
	void testEconomistaInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/gasto/new", GastoControllerTests.TEST_GASTO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("gastos/crearOEditarGasto")).andExpect(MockMvcResultMatchers.model().attributeExists("gasto"));
	}
	
	@WithMockUser(value = "josue", roles = "economista")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/economistas/gasto/new", GastoControllerTests.TEST_GASTO_ID)
				.with(csrf())
				.param("titulo", "Nuevo material para clases")
				.param("cantidad", "270")
				.param("fecha", "2019-03-15")
				.param("description", "Nuevos materiales")
				.param("economista", "1"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@WithMockUser(value = "josue", roles = "economista")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/economistas/gasto/new", GastoControllerTests.TEST_GASTO_ID)
				.with(csrf())
				.param("titulo", "Nuevo material para clases")
				.param("cantidad", "270")
				.param("fecha", "2019-03-15")
				.param("description", "")
				.param("economista", "1"))
		.andExpect(MockMvcResultMatchers.view().name("gastos/crearOEditarGasto"));
	}

}
