package org.springframework.samples.petclinic.model;



import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comentarios")
public class Comentario extends BaseEntity{
	
	@NotEmpty(message="Este campo no puede estar vacío")
	private String titulo;
	
	@NotEmpty(message="Este campo no puede estar vacío")
	private String cuerpo;
	
	@ManyToOne
	@JoinColumn(name = "vet_id")
	private Vet vet;
	
	@ManyToOne
	@JoinColumn(name = "owner_id")
	private Owner owner;
}
