package org.springframework.samples.petclinic.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Secretario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class SecretarioServiceTests {
	@Autowired
	protected SecretarioService secretarioService;
	
	@Test
	void shouldFindSecretarioByUsername() {
		Secretario secretario = this.secretarioService.findSecretarioByUsername("secretario1");
		Assert.assertTrue(secretario.getId()==1);
	}
//	@Test
//	@Transactional
//	public void shouldUpdateSecretario() throws Exception {
//		Secretario secretario = this.secretarioService.findSecretarioByUsername("secretario1");
//
//		String firstName = "NombrePrueba";
//		String lastName = "ApellidoPrueba";
//		String programasDominados = "ProgramasPrueba";
//
//		secretario.setFirstName(firstName);
//		secretario.setId(2);
//		secretario.setLastName(lastName);
//		secretario.setProgramasDominados(programasDominados);
//		secretario.setUser(secretario.getUser());
//		this.secretarioService.saveSecretario(secretario);
//
//		Secretario secretarioRev = this.secretarioService.findSecretarioByUsername("secretario1");
//		Assert.assertTrue(secretarioRev.getFirstName().equals(firstName));
//	}
}
