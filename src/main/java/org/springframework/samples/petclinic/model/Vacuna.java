package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vacunas")
public class Vacuna extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(name = "tipoenfermedad_id")
	private TipoEnfermedad tipoenfermedad;

	@Column(name = "fecha")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fecha;

	@NotEmpty
	@Column(name = "descripcion")
	private String descripcion;
	
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;

	@ManyToOne
	@JoinColumn(name = "vet_id")
	private Vet vet;
	
	

}
