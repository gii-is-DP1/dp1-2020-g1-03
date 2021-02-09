
package org.springframework.samples.petclinic.service;

import java.time.LocalDate;
import java.util.Collection;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Competicion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class CompeticionServiceTests {

	@Autowired
	private CompeticionService		competicionService;

	@Test
	void shouldFindCompeticionWithCorrectId() {
		Competicion competicion1 = this.competicionService.findCompeticionById(1);
		Assertions.assertEquals(competicion1.getNombre(), "CompeticionPrueba");
		Assertions.assertEquals(competicion1.getSecretario().getId(), 1);
	}

	@Test
	void shouldNotFindCompeticionWithIncorrectId() {
		Assertions.assertNull(this.competicionService.findCompeticionById(100));
	}

	@Test
	void shouldFindAllCompeticiones() {
		Collection<Competicion> gastos = this.competicionService.findAllCompeticiones();

		Assertions.assertEquals(gastos.size(), 3);
	}


	@Test
	@Transactional
	public void shouldUpdateInicioDate() throws Exception {
		Competicion competicion1 = this.competicionService.findCompeticionById(1);

		LocalDate newDate = LocalDate.of(2021,12,15);
		competicion1.setFechaHoraInicio(newDate);
		this.competicionService.saveCompeticion(competicion1);

		Competicion compeRev = this.competicionService.findCompeticionById(1);
		Assert.assertTrue(compeRev.getFechaHoraInicio().isEqual(newDate));
	}

}
