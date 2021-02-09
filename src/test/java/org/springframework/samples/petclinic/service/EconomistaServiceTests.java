
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Economista;
import org.springframework.stereotype.Service;



@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class EconomistaServiceTests {


	@Autowired
	private EconomistaService	economistaService;


	@Test
	void shouldFindEconomistaWithCorrectId() {
		Economista economista = this.economistaService.findEconomistaById(1);
		Assertions.assertEquals(economista.getEstudios(), "Muchos");
		Assertions.assertEquals(economista.getFirstName(), "Jose");
	}

	@Test
	void shouldNotFindEconomistaWithIncorrectId() {
		Assertions.assertNull(this.economistaService.findEconomistaById(100));
	}
	

	@Test
	void shouldFindAllGastos() {
		Collection<Economista> economistas = this.economistaService.findEconomistas();

		Assertions.assertEquals(economistas.size(), 1);
	}



}
