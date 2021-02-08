package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.hamcrest.Matchers;
import org.assertj.core.util.Lists;
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
classes = WebSecurityConfigurer.class),includeFilters = @ComponentScan.Filter(value = ComentarioFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
excludeAutoConfiguration= SecurityConfiguration.class)
public class ComentarioControllerTests {
	private static final int	TEST_COMENTARIO_ID= 1;
	private static final int	TEST_OWNER_ID= 1;
	private static final int	TEST_VET_ID= 1;
	private final static int	TEST_COMENTARIO_INEXISTENTE_ID= 100;
	
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
		this.pedro.setFirstName("Pedro");
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
		this.comentario1.setOwner(this.pedro);
		this.comentario1.setVet(this.josue);
		
		BDDMockito.given(this.comentarioService.findComentarioByComentarioId(TEST_COMENTARIO_ID)).willReturn(this.comentario1);
		BDDMockito.given(this.ownerService.findOwnerIdByUsername("josue1")).willReturn(ComentarioControllerTests.TEST_OWNER_ID);
		BDDMockito.given(this.vetService.findVetsByLastName("Perez Gutierrez")).willReturn(this.josue);

	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testShowOwnerComentarioForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/comentarios/show/{comentarioId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("comentario"))
			.andExpect(MockMvcResultMatchers.model().attribute("comentario", Matchers.hasProperty("titulo", Matchers.is("Buen veterinario"))))
			.andExpect(MockMvcResultMatchers.model().attribute("comentario", Matchers.hasProperty("cuerpo", Matchers.is("Buen servicio y atención"))))
			.andExpect(MockMvcResultMatchers.model().attribute("comentario", Matchers.hasProperty("vet", Matchers.hasProperty("firstName", Matchers.is("Josue")))))
			.andExpect(MockMvcResultMatchers.model().attribute("comentario", Matchers.hasProperty("vet", Matchers.hasProperty("lastName", Matchers.is("Perez Gutierrez")))))
			.andExpect(MockMvcResultMatchers.view().name("comentarios/show"));
	}
	
	@WithMockUser(value = "josue", roles = "vet")
	@Test
	void testShowVetComentarioForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/comentarios/show/{comentarioId}", 1)).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("comentario"))
			.andExpect(MockMvcResultMatchers.model().attribute("comentario", Matchers.hasProperty("titulo", Matchers.is("Buen veterinario"))))
			.andExpect(MockMvcResultMatchers.model().attribute("comentario", Matchers.hasProperty("cuerpo", Matchers.is("Buen servicio y atención"))))
			.andExpect(MockMvcResultMatchers.model().attribute("comentario", Matchers.hasProperty("owner", Matchers.hasProperty("firstName", Matchers.is("Pedro")))))
			.andExpect(MockMvcResultMatchers.model().attribute("comentario", Matchers.hasProperty("owner", Matchers.hasProperty("lastName", Matchers.is("Perez Gutierrez")))))
			.andExpect(MockMvcResultMatchers.view().name("comentarios/showVet"));
	}
	
	
	@WithMockUser(value = "josue", roles = "vet")
	@Test
	void testShowComentarioVetError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/comentarios/show/{comentarioId}", ComentarioControllerTests.TEST_COMENTARIO_INEXISTENTE_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("comentario"));
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testComentarioList() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/comentarios/{vetId}", ComentarioControllerTests.TEST_VET_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("comentarios/comentariosListOwner"));

	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testComentarioListError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/comentarios/{vetId}", ComentarioControllerTests.TEST_VET_ID))
		.andExpect(MockMvcResultMatchers.status().isOk())
		.andExpect(MockMvcResultMatchers.view().name("comentarios/comentariosListOwner"));

	}
	
	@WithMockUser(value = "pedro", roles = "owner")
	@Test
	void testInitCreationComentarioForm() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/owners/comentarios/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("comentarios/crearOEditarComentario")).andExpect(MockMvcResultMatchers.model().attributeExists("comentario"));
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
    @Test
    void testProcessCreationComentarioFormSuccess() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/owners/comentarios/new")
					.with(csrf())
					.param("titulo", "Esto es un titulo")
					.param("cuerpo", "cuerpo")
					.param("vet", "josue"))
			.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
			.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/comentarios/"+ComentarioControllerTests.TEST_VET_ID));
	}
	
	@WithMockUser(value = "pedro", roles = "owner")
    @Test
    void testProcessCreationComentarioFormHasErrors() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.post("/owners/comentarios/new")
					.with(csrf())
					.param("cuerpo", "cuerpo"))
			.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("owner"))
			.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("vet"))
			.andExpect(MockMvcResultMatchers.model().attributeHasErrors("comentario"))
			.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("comentario", "titulo"))
			.andExpect(status().isOk())
			.andExpect(MockMvcResultMatchers.view().name("comentarios/crearOEditarComentario"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testInitUpdateForm() throws Exception {
		mockMvc.perform(get("/owners/comentarios/edit/{comentarioId}/{vetId}", TEST_COMENTARIO_ID, TEST_VET_ID))
				.andExpect(status().isOk()).andExpect(model().attributeExists("comentario"))
				.andExpect(view().name("comentarios/crearOEditarComentario"));
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormSuccess() throws Exception {
		mockMvc.perform(post("/owners/comentarios/edit/{comentarioId}/{vetId}", TEST_COMENTARIO_ID, TEST_VET_ID)
							.with(csrf())
							.param("titulo", "titulo cambiado")
							.param("cuerpo", "cuerpo cambiado"))
				.andExpect(status().is3xxRedirection())
				.andExpect(view().name("redirect:/owners/comentarios/show/{comentarioId}"));
	}
    
    @WithMockUser(value = "spring")
	@Test
	void testProcessUpdateFormHasErrors() throws Exception {
		mockMvc.perform(post("/owners/comentarios/edit/{comentarioId}/{vetId}", TEST_COMENTARIO_ID, TEST_VET_ID)
							.with(csrf())
							.param("titulo", ""))
				.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("owner"))
				.andExpect(MockMvcResultMatchers.model().attributeHasNoErrors("vet"))
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("comentario"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("comentario", "titulo"))
				.andExpect(status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("comentarios/crearOEditarComentario"));
	}
	
	
}
