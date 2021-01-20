package org.springframework.samples.petclinic.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "tutorias")
public class Tutoria extends NamedEntity {
	
	@Column(name = "titulo")
	@NotEmpty
	private String titulo;
	
	@Column(name = "fecha_hora")
	@NotEmpty
	@DateTimeFormat(pattern ="yyyy-MM-dd hh:mm")
	private LocalDateTime fechaHora;
	
	@Column(name = "razon")
	@NotEmpty
	private String razon;
	

}
