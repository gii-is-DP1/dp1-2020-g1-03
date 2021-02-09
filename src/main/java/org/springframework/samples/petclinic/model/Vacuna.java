package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
	@NotNull(message="Debe seleccionar un campo")
	private TipoEnfermedad tipoEnfermedad;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message="La fecha debe ser anterior al día actual")
	@NotNull(message="Debe seleccionar una fecha")
	private LocalDate fecha;

	@NotEmpty(message="Este campo no puede estar vacío")
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
