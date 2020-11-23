package org.springframework.samples.petclinic.web;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.service.VacunaService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = VacunaVetController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class VacunaVetControllerTest {
	private static final int TEST_VACUNA_ID = 1;

	@Autowired
	private VacunaVetController vacunaController;

	@MockBean
	private VacunaService vacunaService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	void setup() {

	}

	@WithMockUser(value = "spring")
	@Test
	void testVacunaList() throws Exception {
		mockMvc.perform(get("/vets/vacuna/")).andExpect(status().isOk())
				.andExpect(view().name("vacunas/vacunasListVet"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testVacunaShow() throws Exception {
		;
		mockMvc.perform(get("/vets/vacuna/{vacunaId}", TEST_VACUNA_ID))
				.andExpect(status().isOk()).andExpect(view().name("vacunas/vacunasShow"));
	}
}

