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
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Economista;
import org.springframework.samples.petclinic.model.Ingreso;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.EconomistaService;
import org.springframework.samples.petclinic.service.IngresoService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.context.annotation.FilterType;

@WebMvcTest(controllers = IngresoController.class,
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration = SecurityConfiguration.class)
class IngresoControllerTests {
	
	private static final int	TEST_INGRESO_ID = 1;
	private static final int	TEST_INGRESO_ID_INEXISTENTE = 100;
	private static final int	TEST_ECONOMISTA_ID = 1;
	
	@MockBean
	private IngresoService ingresoService;

	@MockBean
	private EconomistaService economistaService;
	
	@MockBean
	private OwnerService ownerService;
	
	@Autowired
	private MockMvc mockMvc;

	private Ingreso ingreso1;

	private Economista pepe;
	
	private Owner alejandro;
	
	private Economista error;

	private LocalDate fecha = LocalDate.parse("2020-10-04");

	private Ingreso ingreso2;
	
	@BeforeEach
	void setup() {
		
		this.alejandro = new Owner();
		this.alejandro.setId(IngresoControllerTests.TEST_ECONOMISTA_ID);
		this.alejandro.setAddress("Gran Via");
		this.alejandro.setCity("Madrid");
		this.alejandro.setTelephone("954263514");
		this.alejandro.setFirstName("Alenjandro");
		this.alejandro.setLastName("Perez");
		
		this.pepe = new Economista();
		this.pepe.setId(IngresoControllerTests.TEST_ECONOMISTA_ID);
		this.pepe.setEstudios("Finanzas y Contabilidad");
		this.pepe.setFirstName("Pepe");
		this.pepe.setLastName("Alvarez");
		

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2014);
		cal.set(Calendar.MONTH, Calendar.APRIL);
		cal.set(Calendar.DAY_OF_MONTH, 12);
	
		
		this.error = new Economista();
		this.error.setId(2);
		this.error.setEstudios("Error");
		this.error.setFirstName("Error");
		this.error.setLastName("Error");
		
		this.ingreso1 = new Ingreso();
		this.ingreso1.setId(IngresoControllerTests.TEST_INGRESO_ID);
		this.ingreso1.setFecha(this.fecha);
		this.ingreso1.setCantidad(250);
		this.ingreso1.setDescription("Ingresos correspondiente a las clases impartidas en el mes de Noviembre");
		this.ingreso1.setTitulo("Clases");
		this.ingreso1.setEconomista(pepe);
		
		BDDMockito.given(this.ingresoService.findIngresoById(IngresoControllerTests.TEST_INGRESO_ID)).willReturn(this.ingreso1);
		BDDMockito.given(this.economistaService.findEconomistaIdByUsername("josue")).willReturn(IngresoControllerTests.TEST_ECONOMISTA_ID);

	}
	
	@WithMockUser(value = "pepe", roles = "economista")
	@Test
	void testShowIngresoForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/ingreso/{ingresoId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("ingreso"))
			.andExpect(MockMvcResultMatchers.model().attribute("ingreso", Matchers.hasProperty("titulo", Matchers.is("Clases"))))
			.andExpect(MockMvcResultMatchers.model().attribute("ingreso", Matchers.hasProperty("cantidad", Matchers.is(250))))
			.andExpect(MockMvcResultMatchers.view().name("ingresos/ingresosShow"));
	}
	
	@WithMockUser(value = "pepe", roles = "economista")
	@Test
	void testShowIngresoFormError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/ingreso/{ingresoId}", 16))
		.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("ingreso"))

			.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
	

	@WithMockUser(value = "pepe", roles = "economista")
	@Test
	void testIngresoList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/ingreso", IngresoControllerTests.TEST_INGRESO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("ingresos/ingresosList"));

	}
	
	@WithMockUser(value = "pepe", roles = "economista")
	@Test
	void testEconomistaInitEditIngreso() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/ingreso/{ingresoId}/edit", IngresoControllerTests.TEST_INGRESO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.model().attributeExists("ingreso"))
		.andExpect(MockMvcResultMatchers.model().attribute("ingreso", Matchers.hasProperty("titulo", Matchers.is("Clases"))))
		.andExpect(MockMvcResultMatchers.model().attribute("ingreso", Matchers.hasProperty("cantidad", Matchers.is(250))))
		.andExpect(MockMvcResultMatchers.model().attribute("ingreso", Matchers.hasProperty("fecha", Matchers.is(fecha))))
		.andExpect(MockMvcResultMatchers.model().attribute("ingreso", Matchers.hasProperty("description", Matchers.is("Ingresos correspondiente a las clases impartidas en el mes de Noviembre"))))
		.andExpect(MockMvcResultMatchers.view().name("ingresos/crearOEditarIngreso"));
	}
	
	@WithMockUser(value = "pepe", roles = "economista")
	@Test
	void testProcessEditFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/economistas/ingreso/{ingresoId}/edit", IngresoControllerTests.TEST_INGRESO_ID)
				.with(csrf())
				.param("titulo", "Clases impartidas")
				.param("cantidad", "300")
				.param("fecha", "2021-01-15")
				.param("description", "Ingresos de las clases de este mes"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
		.andExpect(MockMvcResultMatchers.view().name("redirect:/economistas/ingreso/{ingresoId}"));
	}
	
	@WithMockUser(value = "pepe", roles = "economista")
	@Test
	void testProcessEditFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/economistas/ingreso/{ingresoId}/edit", IngresoControllerTests.TEST_INGRESO_ID)
				.with(csrf())
				.param("titulo", "Clases impartidas")
				.param("cantidad", "300")
				.param("fecha", "2022-01-15")
				.param("description", ""))
		.andExpect(MockMvcResultMatchers.view().name("ingresos/crearOEditarIngreso"));
	}
	
	@WithMockUser(value = "pepe", roles = "economista")
	@Test
	void testEconomistaInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/economistas/ingreso/create", IngresoControllerTests.TEST_INGRESO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("ingresos/crearOEditarIngreso")).andExpect(MockMvcResultMatchers.model().attributeExists("ingreso"));
	}
	
	@WithMockUser(value = "pepe", roles = "economista")
	@Test
	void testProcessCreationFormSuccess() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/economistas/ingreso/create", IngresoControllerTests.TEST_INGRESO_ID)
				.with(csrf())
				.param("titulo", "Nuevos ingresos")
				.param("cantidad", "280")
				.param("fecha", "2020-01-15")
				.param("description", "Nuevos ingresos del mes de diciembre")
				.param("economista", "1"))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@WithMockUser(value = "pepe", roles = "economista")
	@Test
	void testProcessCreationFormHasErrors() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.post("/economistas/ingreso/create", IngresoControllerTests.TEST_INGRESO_ID)
				.with(csrf())
				.param("titulo", "Nuevos ingresos")
				.param("cantidad", "280")
				.param("fecha", "2023-01-15")
				.param("description", "")
				.param("economista", "1"))
		.andExpect(MockMvcResultMatchers.view().name("ingresos/crearOEditarIngreso"));
	}

}
