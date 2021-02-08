package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "competicionpets")
public class CompeticionPet extends BaseEntity {

	@ManyToOne
    @JoinColumn(name = "competicion_id")
    Competicion competicion;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    Pet pet;
    
}

