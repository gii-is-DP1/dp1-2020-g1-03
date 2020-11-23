package org.springframework.samples.petclinic.model;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

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
	@Column(name = "titulo")
	private String titulo;
	
	@NotNull
	@Column(name = "cantidad")
	private Integer cantidad;
	
	@Column(name = "fecha")        
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate fecha;

	/**
	 * Holds value of property description.
	 */
	@NotEmpty
	@Column(name = "description")
	private String description;

	/**
	 * Holds value of property pet.
	 */
	@ManyToOne
	@JoinColumn(name = "economista_id")
	private Economista economista;

}
