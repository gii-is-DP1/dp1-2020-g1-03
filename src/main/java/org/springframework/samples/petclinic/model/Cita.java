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

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
/*
/**
 * Simple JavaBean domain object representing an owner.
 *
 * @author Ken Krebs
 * @author Juergen Hoeller
 * @author Sam Brannen
 * @author Michael Isvy
 */
@Entity
@Getter
@Setter
@Table(name = "citas")
public class Cita extends NamedEntity {

	@Column(name = "titulo")
	@NotEmpty
	private String titulo;

	@Column(name = "fecha_hora")
	@NotNull
	@Future
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)//pattern = "yyyy-MM-dd HH:mm")
	//@JsonFormat(pattern = "yyyy-MM-dd hh:mm")
	private LocalDateTime fechaHora;

	@Column(name = "estado")
	@NotNull
	private Estado estado;
	
	@Column(name = "razon")
	@NotEmpty
	private String razon;
	
	@ManyToOne
	@JoinColumn(name = "vet_id")
	private Vet vet;
	
}
