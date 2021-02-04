package org.springframework.samples.petclinic.web;

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
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ComentarioService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = ComentarioController.class,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class ComentarioControllerTests {
	private static final int	TEST_COMENTARIO_ID				= 1;
	private static final int	TEST_OWNER_ID				= 1;
	private static final int	TEST_VET_ID				= 1;
	
	@MockBean
	private ComentarioService	comentarioService;

	@MockBean
	private VetService	vetService;
	
	@MockBean
	private OwnerService ownerService;


	@Autowired
	private MockMvc				mockMvc;

	private Comentario			comentario1;

	private Vet					josue;
	
	private Owner				pedro;
	
	private Vet					error;

	private Comentario 			comentario2;
	
	@BeforeEach
	void setup() {
		this.pedro = new Owner();
		User username= new User();
		username.setUsername("josue1");
		this.pedro.setUser(username);
		this.pedro.setId(ComentarioControllerTests.TEST_OWNER_ID);
		this.pedro.setAddress("Plaza San Pedro");
		this.pedro.setCity("Roma");
		this.pedro.setTelephone("954442211");
		this.pedro.setFirstName("Josue");
		this.pedro.setLastName("Perez Gutierrez");
		
		this.josue = new Vet();
		this.josue.setId(ComentarioControllerTests.TEST_VET_ID);
		this.josue.setFirstName("Josue");
		this.josue.setLastName("Perez Gutierrez");




		this.error = new Vet();
		this.error.setId(2);
		this.error.setFirstName("Error");
		this.error.setLastName("Error");

		this.comentario1 = new Comentario();
		this.comentario1.setId(ComentarioControllerTests.TEST_COMENTARIO_ID);
		this.comentario1.setCuerpo("Buen servicio y atención");
		this.comentario1.setTitulo("Buen veterinario");
		
		BDDMockito.given(this.comentarioService.findComentarioByComentarioId(TEST_COMENTARIO_ID)).willReturn(this.comentario1);
		BDDMockito.given(this.ownerService.findOwnerIdByUsername("josue1")).willReturn(ComentarioControllerTests.TEST_OWNER_ID);

	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testShowOwnerComentarioForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/comentarios/show/{comentarioId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("comentario"))

			.andExpect(MockMvcResultMatchers.model().attribute("comentario", Matchers.hasProperty("titulo", Matchers.is("Buen veterinario"))))

			.andExpect(MockMvcResultMatchers.model().attribute("comentario", Matchers.hasProperty("cuerpo", Matchers.is("Buen servicio y atención"))))

			.andExpect(MockMvcResultMatchers.view().name("comentarios/show"));
	}
	
	@WithMockUser(value = "josue", roles = "vet")
	@Test
	void testShowVetComentarioForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/comentarios/show/{comentarioId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("comentario"))

			.andExpect(MockMvcResultMatchers.model().attribute("comentario", Matchers.hasProperty("titulo", Matchers.is("Buen veterinario"))))

			.andExpect(MockMvcResultMatchers.model().attribute("comentario", Matchers.hasProperty("cuerpo", Matchers.is("Buen servicio y atención"))))

			.andExpect(MockMvcResultMatchers.view().name("comentarios/showVet"));
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testComentarioList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/comentarios", ComentarioControllerTests.TEST_COMENTARIO_ID)).andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("comentarios/comentariosListOwner"));

	}
	
	
}
