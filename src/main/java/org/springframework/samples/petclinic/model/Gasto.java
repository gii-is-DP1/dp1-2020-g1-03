
package org.springframework.samples.petclinic.model;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "gastos")
public class Gasto extends BaseEntity {

	
	@NotEmpty
	private String titulo;
	
	
	@PositiveOrZero
	@NotNull
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
