package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "tipo_enfermedades")
public class TipoEnfermedad extends NamedEntity{

}
