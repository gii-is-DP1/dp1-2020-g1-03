package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.model.Economista;
import org.springframework.samples.petclinic.model.Gasto;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.ComentarioService;
import org.springframework.samples.petclinic.service.EconomistaService;
import org.springframework.samples.petclinic.service.GastoService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ComentarioController.class,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)
public class ComentarioControllerTests {
	private static final int	TEST_COMENTARIO_ID				= 1;
	private static final int	TEST_COMENTARIO_ID_INEXISTENTE	= 100;
	private static final int	TEST_OWNER_ID				= 1;
	private static final int	TEST_VET_ID				= 1;
	
	@MockBean
	private ComentarioService		comentarioService;

	@MockBean
	private VetService	vetService;
	
	@MockBean
	private OwnerService	ownerService;


	@Autowired
	private MockMvc				mockMvc;

	private Comentario			comentario1;

	private Economista			josue;
	
	private Owner				pedro;
	
	private Vet					error;

	private Comentario 			comentario2;
}
