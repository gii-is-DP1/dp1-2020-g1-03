package org.springframework.samples.petclinic.web;

import java.time.LocalDate;

import org.assertj.core.util.Lists;
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
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Tutoria;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AdiestradorService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.TutoriaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = TutoriaController.class,excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
classes = WebSecurityConfigurer.class),
excludeAutoConfiguration= SecurityConfiguration.class)

public class TutoriaControllerTests {
	
	private static final int TEST_TUTORIA_ID = 1;
	private static final int TEST_OWNER_ID = 1;
	private static final int TEST_ADISETRADOR_ID = 1;
	private static final int TEST_PET_ID = 1;
	
	@MockBean
	private TutoriaService tutoriaService;
	@MockBean
	private OwnerService ownerService;
	@MockBean
	private AdiestradorService adiestradorService;
	@MockBean
	private PetService petService;
	
	@Autowired
	private MockMvc mockMvc;
	
	private Owner pedro;
	private Adiestrador	josue;
	private Tutoria tutoria1;
	private Pet pet1;
	private LocalDate fecha1 = LocalDate.parse("2012-08-06");
	
	@BeforeEach
	void setup() {
		
		PetType hamster = new PetType();
		hamster.setId(6);
		hamster.setName("hamster");
		
		this.pedro = new Owner();
		User username= new User();
		username.setUsername("josue1");
		this.pedro.setUser(username);
		this.pedro.setId(TutoriaControllerTests.TEST_OWNER_ID);
		this.pedro.setAddress("Plaza San Pedro");
		this.pedro.setCity("Roma");
		this.pedro.setTelephone("954442211");
		this.pedro.setFirstName("Josue");
		this.pedro.setLastName("Perez Gutierrez");
		
		this.josue = new Adiestrador();
		this.josue.setId(TutoriaControllerTests.TEST_ADISETRADOR_ID);
		this.josue.setFirstName("Josue");
		this.josue.setLastName("Perez Gutierrez");
		 
		this.pet1 = new Pet();
		this.pet1.setId(TutoriaControllerTests.TEST_PET_ID);
		this.pet1.setBirthDate(fecha1);
		this.pet1.setName("Basil");
		this.pet1.setType(hamster);

		BDDMockito.given(this.petService.findPetTypes()).willReturn(Lists.newArrayList(hamster));
		BDDMockito.given(this.tutoriaService.findTutoriaById(TutoriaControllerTests.TEST_TUTORIA_ID)).willReturn(this.tutoria1);
		BDDMockito.given(this.adiestradorService.findAdiestradorIdByUsername("josue")).willReturn(TutoriaControllerTests.TEST_ADISETRADOR_ID);
		BDDMockito.given(this.petService.findPetById(TutoriaControllerTests.TEST_PET_ID)).willReturn(this.pet1);
	}
	

	@WithMockUser(value = "josue", roles = "adiestrador")
	@Test
	void testTutoriaShowAdiestrador() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.get("/adiestradores/tutorias/show/{tutoriaId}", TutoriaControllerTests.TEST_TUTORIA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("tutorias/tutoriaShowAdiestrador"));
	}
}
