package org.springframework.samples.petclinic.service;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



import java.util.Collection;

import org.springframework.samples.petclinic.model.Comentario;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ComentarioServiceTests {
	
	@Autowired
	protected ComentarioService comentarioService;
	
	@Autowired
	private OwnerService	ownerService;
	
	@Test
	void shouldFindComentariosByVetId() {
		Collection<Comentario> com = this.comentarioService.findAllComentariosByVetId(1);
		Assert.assertTrue(com.size()==(1));

	}
	@Test
	void shouldFindComentariosByOwnerId() {
		Collection<Comentario> com = this.comentarioService.findAllComentariosByOwnerId(1);
		Assert.assertTrue(com.size()==(4));

	}
	@Test
	@Transactional
	public void shouldUpdateComentario() throws Exception {
		Comentario comentario2 = this.comentarioService.findComentarioByComentarioId(1);

		String titulo = "Titulo Cambiado";
		String cuerpo = "Cuerpo Cambiado";

		comentario2.setTitulo(titulo);
		comentario2.setCuerpo(cuerpo);
		this.comentarioService.saveComentario(comentario2,true);

		Comentario comentarioRev = this.comentarioService.findComentarioByComentarioId(1);
		Assert.assertTrue(comentarioRev.getTitulo()==titulo);
	}
	@Test
	void shouldFindComentarioByComentarioId() {
		Comentario comentario=this.comentarioService.findComentarioByComentarioId(1);
		Assert.assertTrue(comentario.getId()==(1));
	}
}
