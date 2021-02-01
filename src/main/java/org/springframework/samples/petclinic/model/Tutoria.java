package org.springframework.samples.petclinic.model;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
	private String titulo;
	
	@Column(name = "fecha_hora")
	@DateTimeFormat(pattern ="yyyy-MM-dd HH:mm")
	private LocalDateTime fechaHora;
	
	@Column(name = "razon")
	private String razon;
	
	@ManyToOne
	@JoinColumn(name = "pet_id")
	private Pet pet;

	@ManyToOne
	@JoinColumn(name = "adiestrador_id")
	private Adiestrador adiestrador;
	

}
