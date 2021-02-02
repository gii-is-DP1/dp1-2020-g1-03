package org.springframework.samples.petclinic.model;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "clases")
public class Clase extends BaseEntity{
	
	@Column(name = "name")
	@NotEmpty
	private String name;
	
	@Column(name = "inicio")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime fechaHoraInicio;
	
	@Column(name = "fin")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime fechaHoraFin;
	
	@Column(name = "plazas")
	@Positive
	private Integer numeroPlazasTotal;
	
	@Column(name = "disponibles")
	@PositiveOrZero
	private Integer numeroPlazasDisponibles;
	
	@ManyToOne
	@JoinColumn(name = "categoria")
	private CategoriaClase categoriaClase;
	
	@ManyToOne
	@JoinColumn(name = "mascota")
	private PetType type;
	
	@ManyToOne
	@JoinColumn(name = "adiestrador_id")
	private Adiestrador adiestrador;
	
	@ManyToOne
	@JoinColumn(name = "secretario")
	private Secretario secretario;
	
	public long numeroDiasEntreDosFechas(LocalDateTime fecha2){
		   return DAYS.between(this.fechaHoraFin, fecha2);
		}
}
