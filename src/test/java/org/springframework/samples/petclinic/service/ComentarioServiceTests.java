package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Comentario;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class ComentarioServiceTests {
	@Autowired
	protected ComentarioService comentarioService;
	
	
	@Test
	void shouldFindComentariosByVetId() {
		Collection<Comentario> com = this.comentarioService.findAllComentariosByVetId(1);
		assertThat(com.size()).isEqualTo(1);

	}
	@Test
	void shouldFindComentariosByOwnerId() {
		Collection<Comentario> com = this.comentarioService.findAllComentariosByOwnerId(1);
		assertThat(com.size()).isEqualTo(2);

	}
}
