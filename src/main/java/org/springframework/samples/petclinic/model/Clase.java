package org.springframework.samples.petclinic.model;

import static java.time.temporal.ChronoUnit.DAYS;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
	
	
	@NotEmpty(message="Este campo no puede estar vacío")
	private String name;
	
	@Column(name = "inicio")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime fechaHoraInicio;
	
	@Column(name = "fin")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime fechaHoraFin;
	
	@Column(name = "plazas")
	@Positive(message="El número de plazas totales debe ser mayor a 0")
	private Integer numeroPlazasTotal;
	
	@Column(name = "disponibles")
	@PositiveOrZero(message="La cantidad debe ser mayor o igual a 0")
	private Integer numeroPlazasDisponibles;
	
	@ManyToOne
	@JoinColumn(name = "categoria")
	@NotNull(message="Debes seleccionar un campo de categorias")
	private CategoriaClase categoriaClase;
	
	@ManyToOne
	@JoinColumn(name = "mascota")
	@NotNull(message="Debes seleccionar un campo de tipo mascota")
	private PetType type;
	
	@ManyToOne
	@JoinColumn(name = "adiestrador_id")
	@NotNull(message="Debes seleccionar un adiestrador")
	private Adiestrador adiestrador;
	
	@ManyToOne
	@JoinColumn(name = "secretario_id")
	private Secretario secretario;
	
	public long numeroDiasEntreDosFechas(LocalDateTime fecha2){
		   return DAYS.between(this.fechaHoraFin, fecha2);
		}
}
