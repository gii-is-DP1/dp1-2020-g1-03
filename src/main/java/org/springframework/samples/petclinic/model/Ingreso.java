
package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ingresos")
public class Ingreso extends BaseEntity{
	
	@NotEmpty(message="Este campo no puede estar vacío")
	private String titulo;
	
	
	@PositiveOrZero(message="La cantidad debe ser mayor o igual a 0")
	@NotNull
	private Integer cantidad;
	    
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past(message="La fecha debe ser anterior al día actual")
	private LocalDate fecha;

	
	@NotEmpty(message="Este campo no puede estar vacío")
	private String description;

	@ManyToOne
	@JoinColumn(name = "economista_id")
	private Economista economista;

}
