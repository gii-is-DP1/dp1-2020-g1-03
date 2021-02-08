package org.springframework.samples.petclinic.model;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "competiciones")
public class Competicion extends BaseEntity {

	@NotEmpty
	private String nombre;
	
	
	@PositiveOrZero
	@NotNull
	private Integer cantidad;
	
	@Column(name = "fecha_hora_inicio")        
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@NotNull
	private LocalDate fechaHoraInicio;
	
	@Column(name = "fecha_hora_fin")        
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@NotNull
	private LocalDate fechaHoraFin;

	@NotEmpty
	private String premios;

	@ManyToOne
	@JoinColumn(name = "secretario_id")
	private Secretario secretario;



}
