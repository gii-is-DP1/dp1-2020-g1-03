/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Simple JavaBean domain object representing a visit.
 *
 * @author Ken Krebs
 */
@Entity
@Getter
@Setter
@Table(name = "competiciones")
public class Competicion extends BaseEntity {

	/**
	 * Holds value of property date.
	 */
	@NotEmpty
	@Column(name = "nombre")
	private String nombre;
	
	
	@Column(name = "cantidad")
	@PositiveOrZero
	@NotNull
	private Integer cantidad;
	
	@Column(name = "fecha_hora_inicio")        

	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Future
  @NotNull
	private LocalDate fechaHoraInicio;
	
	@Column(name = "fecha_hora_fin")        
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	@Future
  @NotNull
	private LocalDate fechaHoraFin;
\

	/**
	 * Holds value of property description.
	 */
	@NotEmpty
	@Column(name = "premios")
	private String premios;

	/**
	 * Holds value of property pet.
	 */
	@ManyToOne
	@JoinColumn(name = "secretario_id")
	private Secretario secretario;
	
//	@OneToMany(mappedBy = "competiciones")
//	//@JoinColumn(name = "competicion_pet_id")
//	private CompeticionPet competicionPet;


}
