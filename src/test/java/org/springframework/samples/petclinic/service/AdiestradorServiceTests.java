package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AdiestradorServiceTests {
	@Autowired
	protected AdiestradorService adiestradorService;
	
	@Test
	void shouldFindAdiestradorById() {
		Adiestrador ad = this.adiestradorService.findAdiestradorById(1);
		Assert.assertTrue(ad.getId()==(1));

	}
	@Test
	void shouldFindAdiestradorIdByUsername() {
		int com = this.adiestradorService.findAdiestradorIdByUsername("adiestrador1");
		Assert.assertTrue(com==1);

	}
	@Test
	void shouldFindAllAdiestradores() {
		Collection<Adiestrador> adiestradores=this.adiestradorService.findAllAdiestradores();
		Assert.assertTrue(adiestradores.size()==2);
	}
	@Test
	void shouldFindFirstnameAndLastnameOfAdiestrador() {
		String adiestradorBD = "Daniel,Castroviejo";
		Collection<String> nombreCompleto=this.adiestradorService.findNameAndLastnameAdiestrador();
		Assert.assertTrue(nombreCompleto.contains(adiestradorBD));
	}
	
	@Test
	@Transactional
	void shouldSaveAdiestrador(){
		Collection<Adiestrador> adiestradores = this.adiestradorService.findAllAdiestradores();
		int tamOriginal = adiestradores.size();
		Adiestrador adiestrador = new Adiestrador();
		User user=new User();
		user.setUsername("adiestrador3");
		String competencias = "Esto es una competencia";
		adiestrador.setUser(user);
		adiestrador.setCompetencias(competencias);
		adiestrador.setFirstName("Paco");
		adiestrador.setLastName("Rodriguez");
		adiestrador.setId(3);
		this.adiestradorService.saveAdiestrador(adiestrador);
		adiestradores = this.adiestradorService.findAllAdiestradores();
		assertThat(adiestradores.size()).isEqualTo(tamOriginal + 1);
	}
}
