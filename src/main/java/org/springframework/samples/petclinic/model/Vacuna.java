package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;
import static java.time.temporal.ChronoUnit.DAYS;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "vacunas")
public class Vacuna extends BaseEntity {
	
	@ManyToOne
	@JoinColumn(name = "tipoenfermedad_id")
	private TipoEnfermedad tipoEnfermedad;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past
	private LocalDate fecha;

	@NotEmpty
	private String descripcion;
	
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;

	@ManyToOne
	@JoinColumn(name = "vet_id")
	private Vet vet;
	
	public long numeroDiasEntreDosFechas(LocalDate fecha2){
	   return DAYS.between(this.fecha, fecha2);
	}

}
