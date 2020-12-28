package org.springframework.samples.petclinic.service;

import java.util.Collection;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Adiestrador;
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class AdiestradorServiceTests {
	@Autowired
	protected AdiestradorService adiestradorService;
	
	@Autowired
	private OwnerService	ownerService;
	
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
//	@Test
//	@Transactional
//	public void shouldUpdateAdiestrador() throws Exception {
//		Adiestrador ad = this.adiestradorService.findAdiestradorById(1);
//
//		String titulo = "Titulo Cambiado";
//		String cuerpo = "Cuerpo Cambiado";
//
//		comentario2.setTitulo(titulo);
//		comentario2.setCuerpo(cuerpo);
//		this.comentarioService.saveComentario(comentario2);
//
//		Comentario comentarioRev = this.comentarioService.findComentarioByComentarioId(2);
//		Assert.assertTrue(comentarioRev.getTitulo()==titulo);
//	}
	@Test
	void shouldFindAllAdiestradores() {
		Collection<Adiestrador> adiestradores=this.adiestradorService.findAllAdiestradores();
		Assert.assertTrue(adiestradores.size()==1);
	}
	//PREGUNTAR
	@Test
	void shouldFindFirstnameAndLastnameOfAdiestrador() {
		String adiestradorBD = "Daniel Castroviejo";
		Collection<String> nombreCompleto=this.adiestradorService.findNameAndLastnameAdiestrador();
		Assert.assertTrue(nombreCompleto.equals(adiestradorBD));
	}
}
