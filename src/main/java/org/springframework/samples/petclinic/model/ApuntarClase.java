package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "apuntar")
public class ApuntarClase extends BaseEntity{
	
	
	@ManyToOne
	@NotNull(message="Debes seleccionar una mascota")
	@JoinColumn(name = "pet_id")
	private Pet pet;
	
	@ManyToOne
	@JoinColumn(name = "clase_id")
	private Clase clase;
	
	
}
