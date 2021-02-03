
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
	
	@NotEmpty
	private String titulo;
	
	@NotNull
	@PositiveOrZero
	private Integer cantidad;
	       
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Past
	private LocalDate fecha;

	@NotEmpty
	private String description;

	@ManyToOne
	@JoinColumn(name = "economista_id")
	private Economista economista;

}
