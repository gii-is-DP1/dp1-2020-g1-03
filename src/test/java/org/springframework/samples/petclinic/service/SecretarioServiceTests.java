
package org.springframework.samples.petclinic.service;

import java.util.Collection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.stereotype.Service;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
@AutoConfigureTestDatabase(replace = Replace.NONE)
class SecretarioServiceTests {


	@Autowired
	private SecretarioService	secretarioService;


	@Test
	void shouldFindSecretarioWithCorrectId() {
		Secretario secretario = this.secretarioService.findSecretarioById(1);
		Assertions.assertEquals(secretario.getProgramasDominados(), "Word");
		Assertions.assertEquals(secretario.getFirstName(), "Pedro");
	}

	@Test
	void shouldNotFindSecretarioWithIncorrectId() {
		Assertions.assertNull(this.secretarioService.findSecretarioById(100));
	}
	

	@Test
	void shouldFindAllSecretarios() {
		Collection<Secretario> secretarios = this.secretarioService.findSecretarios();

		Assertions.assertEquals(secretarios.size(), 1);
	}



}

