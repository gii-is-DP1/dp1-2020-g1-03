
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "citas")
public class Cita extends NamedEntity {

	
	@NotEmpty
	private String titulo;

	@Column(name = "fecha_hora")
	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime fechaHora;


	
	//@NotEmpty
	private Estado estado;
	
	
	@NotEmpty
	private String razon;
	
	@ManyToOne
	@JoinColumn(name = "vet_id")
	private Vet vet;
	
}
