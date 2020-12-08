package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.TipoEnfermedad;
import org.springframework.samples.petclinic.model.Vacuna;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class VacunaServiceTest {

	@Autowired
	private VacunaService vacunaService;

	@Test
	void shouldFindVacunaWithCorrectId() {
		Vacuna vacuna1 = this.vacunaService.findVacunaById(1);
		//assertThat(vacuna1.getNombre()).startsWith("Rabia");
		//assertThat(vacuna1.getTipoenfermedad()).isEqualTo(TipoEnfermedad.RABIA);
	}

	@Test
	void shouldFindVacunasWithCorrectOwnerId() {
		List<Vacuna> vacunas = new ArrayList<>(this.vacunaService.findAllVacunasByOwnerId(1));
		assertThat(vacunas.get(0).getPet().getOwner().getId()).isEqualTo(1);
		//assertThat(vacunas.get(0).getTipoenfermedad()).isEqualTo(TipoEnfermedad.RABIA);
	}
}
