
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "citas")
public class Cita extends NamedEntity {

	
	@NotEmpty
	private String titulo;

	@Column(name = "fecha_hora")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime fechaHora;


	
	private Estado estado;
	
	
	@NotEmpty
	private String razon;
	
	@ManyToOne
	@JoinColumn(name = "vet_id")
	private Vet vet;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "citas_Pets", joinColumns = @JoinColumn(name = "cita_id"),
			inverseJoinColumns = @JoinColumn(name = "pet_id"))
	private List<Pet> pets;
	
}
