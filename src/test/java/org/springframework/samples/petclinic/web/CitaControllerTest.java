package org.springframework.samples.petclinic.web;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Cita;
import org.springframework.samples.petclinic.model.Estado;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.service.CitaService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.samples.petclinic.service.VetService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = CitaController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class CitaControllerTest {

	private static final int TEST_CITA_ID = 1;
	private static final int TEST_CITA_ID_INEXISTENTE = 100;
	private static final int TEST_SECRETARIO_ID = 1;

	@MockBean
	private CitaService citaService;

	@MockBean
	private SecretarioService secretarioService;

	@MockBean
	private PetService petService;

	@MockBean
	private OwnerService ownerService;

	@MockBean
	private VetService vetService;

	@Autowired
	private MockMvc mockMvc;

	private Secretario josue;

	private Owner pedro;

	private Cita cita;

	private Pet pet;

	private Vet juan;

	@BeforeEach
	void setup() {
		this.pedro = new Owner();
		this.pedro.setId(1);
		this.pedro.setAddress("Plaza San Pedro");
		this.pedro.setCity("Roma");
		this.pedro.setTelephone("954442211");
		this.pedro.setFirstName("Josue");
		this.pedro.setLastName("Perez Gutierrez");

		this.josue = new Secretario();
		this.josue.setId(CitaControllerTest.TEST_SECRETARIO_ID);
		this.josue.setProgramasDominados("Excel");
		this.josue.setFirstName("Josue");
		this.josue.setLastName("Perez Gutierrez");

		this.juan = new Vet();
		this.juan.setFirstName("Juan");
		this.juan.setLastName("Manuel");
		this.juan.setId(1);

		PetType dog = new PetType();
		dog.setId(6);
		dog.setName("perro");

		this.pet = new Pet();
		this.pet.setBirthDate(LocalDate.of(2020, 01, 01));
		this.pet.setId(1);
		this.pet.setName("Tomi");
		this.pet.setType(dog);
		this.pet.setOwner(this.pedro);

		List<Pet> pets = new ArrayList<>();
		pets.add(pet);

		this.cita = new Cita();
		this.cita.setId(CitaControllerTest.TEST_CITA_ID);
		this.cita.setEstado(Estado.PENDIENTE);
		this.cita.setFechaHora(LocalDateTime.of(LocalDate.of(2022, 01, 01), LocalTime.of(15, 30)));
		this.cita.setName("Citatest");
		this.cita.setPets(pets);

		BDDMockito.given(this.citaService.findCitaById(CitaControllerTest.TEST_CITA_ID)).willReturn(this.cita);
		BDDMockito.given(this.ownerService.findOwnerByUsername("pedro")).willReturn(this.pedro);
	}

	@Test
	@WithMockUser(value = "juan", roles = "vet")
	void testListadoCitasVets() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/citas")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("citas/citasList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("citas"));
	}

	@Test
	@WithMockUser(value = "juan", roles = "vet")
	void testMostrarCitaVet() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/citas/{citaId}", CitaControllerTest.TEST_CITA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("cita", "owner", "mascotas"))
				.andExpect(MockMvcResultMatchers.view().name("citas/showCitaVet"));
	}

	@Test
	@WithMockUser(value = "juan", roles = "vet")
	void testMostrarCitaVetError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/vets/citas/{citaId}", CitaControllerTest.TEST_CITA_ID_INEXISTENTE))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("cita", "owner", "mascotas"));
	}

	@Test
	@WithMockUser(value = "pedro", roles = "owner")
	void testListadoCitasOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/citas/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("citas/citasOwnerList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("citas"));
	}

	@Test
	@WithMockUser(value = "pedro", roles = "owner")
	void testMostrarCitaOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/citas/{citaId}", CitaControllerTest.TEST_CITA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeExists("cita"))
				.andExpect(MockMvcResultMatchers.view().name("citas/showCitaOwner"));
				
	}

	@Test
	@WithMockUser(value = "pedro", roles = "owner")
	void testMostrarCitaOwnerError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/citas/{citaId}", CitaControllerTest.TEST_CITA_ID_INEXISTENTE))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("cita"));
	}

	@Test
	@WithMockUser(value = "pedro", roles = "owner")
	void testInitCreateCitaOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/citas/new"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("citas/crearOEditarCitaOwner"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("cita"));
	}

	@Test
	@WithMockUser(value = "pedro", roles = "owner")
	void testProcessCreateCitaOwner() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/citas/new").with(csrf()).param("titulo", "Cita 1")
						.param("fechaHora", "2022-01-01 15:30").param("estado", "PENDIENTE").param("razon", "razon1"))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/citas/"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	@WithMockUser(value = "pedro", roles = "owner")
	void testProcessCreateCitaOwnerError() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/citas/new").with(csrf()).param("titulo", "Cita 1")
						.param("estado", "PENDIENTE").param("razon", "razon1"))
				.andExpect(MockMvcResultMatchers.view().name("exception"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(value = "pedro", roles = "owner")
	void testGetEditarCitaOwner() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/citas/{citaId}/edit", CitaControllerTest.TEST_CITA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("citas/crearOEditarCitaOwner"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("cita"));
	}

	@Test
	@WithMockUser(value = "pedro", roles = "owner")
	void testGetEditarCitaOwnerError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/citas/{citaId}/edit", CitaControllerTest.TEST_CITA_ID_INEXISTENTE))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("cita"));
	}

	@Test
	@WithMockUser(value = "pedro", roles = "owner")
	void testProcessEditarCitaOwner() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/citas/{citaId}/edit", CitaControllerTest.TEST_CITA_ID).with(csrf())
						.param("titulo", "Cita 1").param("fechaHora", "2022-01-01 15:30").param("estado", "PENDIENTE")
						.param("razon", "razon1"))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/citas/"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	@WithMockUser(value = "pedro", roles = "owner")
	void testProcessEditarCitaOwnerError() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/owners/citas/{citaId}/edit", CitaControllerTest.TEST_CITA_ID_INEXISTENTE)
						.with(csrf()).param("titulo", "Cita 1").param("fechaHora", "2022-01-01 15:30")
						.param("estado", "PENDIENTE").param("razon", "razon1"))
				.andExpect(MockMvcResultMatchers.view().name("exception"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	@WithMockUser(value = "pedro", roles = "owner")
	void testDeleteCita() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/owners/citas/{citaId}/delete", CitaControllerTest.TEST_CITA_ID).with(csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/owners/citas/"));
	}

	@Test
	@WithMockUser(value = "josue", roles = "secretario")
	void testListadoCitasSecretario() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/citas"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("citas/citasSecretarioList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("citas"));
	}

	@Test
	@WithMockUser(value = "josue", roles = "secretario")
	void testListadoCitasSecretarioSinVet() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/citas/sinVet"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("citas/citasSecretarioSinVetList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("citas"));
	}

	@Test
	@WithMockUser(value = "josue", roles = "secretario")
	void testMostrarCitaSecretario() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/citas/{citaId}", CitaControllerTest.TEST_CITA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("citas/showCitaSecretario"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("cita"));
	}

	@Test
	@WithMockUser(value = "josue", roles = "secretario")
	void testMostrarCitaSecretarioError() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/citas/{citaId}", CitaControllerTest.TEST_CITA_ID_INEXISTENTE))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("cita"));
	}

	@Test
	@WithMockUser(value = "josue", roles = "secretario")
	void testGetEditarCitaSecretario() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/secretarios/citas/{citaId}/edit", CitaControllerTest.TEST_CITA_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("citas/editarCitaSecretario"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("cita"));
	}

	@Test
	@WithMockUser(value = "josue", roles = "secretario")
	void testGetEditarCitaSecretarioError() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/secretarios/citas/{citaId}/edit", CitaControllerTest.TEST_CITA_ID_INEXISTENTE))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"))
				.andExpect(MockMvcResultMatchers.model().attributeDoesNotExist("cita"));
	}

	@Test
	@WithMockUser(value = "josue", roles = "secretario")
	void testProcessEditarCitaSecretario() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/secretarios/citas/{citaId}/edit", CitaControllerTest.TEST_CITA_ID).with(csrf())
						.param("titulo", "Cita 1").param("fechaHora", "2022-01-01 15:30").param("estado", "ACEPTADA")
						.param("razon", "razon1"))
				.andExpect(MockMvcResultMatchers.view().name("redirect:/secretarios/citas"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@Test
	@WithMockUser(value = "josue", roles = "secretario")
	void testProcessEditarCitaSecretarioError() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/secretarios/citas/{citaId}/edit", CitaControllerTest.TEST_CITA_ID_INEXISTENTE)
						.with(csrf()).param("titulo", "Cita 1").param("fechaHora", "2022-01-01 15:30")
						.param("estado", "ACEPTADA").param("razon", "razon1"))
				.andExpect(MockMvcResultMatchers.view().name("exception"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
}
