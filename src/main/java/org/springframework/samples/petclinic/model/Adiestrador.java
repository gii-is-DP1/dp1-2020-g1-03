package org.springframework.samples.petclinic.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "adiestrador")
public class Adiestrador extends Person{
	
	
	@NotEmpty
	private String competencias;
	
	public String getCompetencias() {
		return competencias;
	}
	public void setCompetencias(String competencias) {
		this.competencias = competencias;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username")
	private User user;

	@Override
	public String toString() {
		return  firstName + " " + lastName;
	}
	
	
	
}
