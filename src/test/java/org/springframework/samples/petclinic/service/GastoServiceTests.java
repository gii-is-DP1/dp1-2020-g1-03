
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
import org.springframework.samples.petclinic.model.Gasto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class GastoServiceTests {

	@Autowired
	private GastoService		gastoService;

	@Test
	void shouldFindGastoWithCorrectId() {
		Gasto gasto1 = this.gastoService.findGastoById(1);
		Assertions.assertEquals(gasto1.getDescription(), "Gasto correspondiente a la compra de material esterilizante para la clinica");
		Assertions.assertEquals(gasto1.getEconomista().getId(), 1);
	}

	@Test
	void shouldNotFindGastoWithIncorrectId() {
		Assertions.assertNull(this.gastoService.findGastoById(100));
	}

	@Test
	void shouldFindAllGastos() {
		Collection<Gasto> gastos = this.gastoService.findAllGastosS();

		Assertions.assertEquals(gastos.size(), 3);
	}


	@Test
	@Transactional
	public void shouldUpdateVisitDate() throws Exception {
		Gasto gasto2 = this.gastoService.findGastoById(2);

		LocalDate newDate = LocalDate.parse("2021-12-15");
		gasto2.setFecha(newDate);
		this.gastoService.saveGasto(gasto2);

		Gasto gastoRev = this.gastoService.findGastoById(2);
		Assert.assertTrue(gastoRev.getFecha().isEqual(newDate));
	}

}
