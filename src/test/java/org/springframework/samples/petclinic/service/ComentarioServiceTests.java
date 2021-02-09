package org.springframework.samples.petclinic.service;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.samples.petclinic.service.exceptions.ComentariosMaximoPorCitaException;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ComentarioServiceTests {
	
	@Autowired
	protected ComentarioService comentarioService;
	@Autowired
	protected OwnerService ownerService;
	@Autowired
	protected VetService vetService;
	
	@Test
	void shouldFindComentariosByVetId() {
		Collection<Comentario> com = this.comentarioService.findAllComentariosByVetId(1);
		Assert.assertTrue(com.size()==(0));

	}
	@Test
	void shouldFindComentariosByOwnerId() {
		Collection<Comentario> com = this.comentarioService.findAllComentariosByOwnerId(1);
		Assert.assertTrue(com.size()==(3));

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
	
	@Test
	@Transactional
	public void shouldSaveComentario() throws Exception {
		
		Comentario comentario = new Comentario();
		
		String titulo = "Titulo Cambiado";
		String cuerpo = "Cuerpo Cambiado";
		
		comentario.setVet(this.vetService.findVetById(1));
		comentario.setOwner(this.ownerService.findOwnerById(1));
		List<Comentario> comentarios = this.comentarioService.findAllComentariosByOwnerId(comentario.getOwner().getId());
		int tamOriginal = comentarios.size();
		comentario.setTitulo(titulo);
		comentario.setCuerpo(cuerpo);
		comentario.setId(15);
		
		try {
            this.comentarioService.saveComentario(comentario,false);
        } catch (ComentariosMaximoPorCitaException ex) {
            Logger.getLogger(ComentarioServiceTests.class.getName()).log(Level.SEVERE, null, ex);
        }
		
		comentarios = this.comentarioService.findAllComentariosByOwnerId(comentario.getOwner().getId());
		Assert.assertTrue(comentarios.size()==tamOriginal+1);
	}
	
	@Test
	@Transactional
	public void shouldThrowExceptionComentariosMaximoPorCitaException() {
		
		Comentario comentario = new Comentario();
		
		String titulo = "Titulo Cambiado";
		String cuerpo = "Cuerpo Cambiado";
		
		comentario.setVet(this.vetService.findVetById(5));
		comentario.setOwner(this.ownerService.findOwnerById(1));
		comentario.setTitulo(titulo);
		comentario.setCuerpo(cuerpo);
		comentario.setId(15);
		
		
		Assertions.assertThrows(ComentariosMaximoPorCitaException.class, () ->{
			comentarioService.saveComentario(comentario, false);
		});	
	}
}

