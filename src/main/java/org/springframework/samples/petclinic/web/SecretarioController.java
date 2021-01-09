package org.springframework.samples.petclinic.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.service.AdiestradorService;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.SecretarioService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

public class SecretarioController {
	private final SecretarioService secretarioService;

	@Autowired
	public SecretarioController(SecretarioService secretarioService, AuthoritiesService authoritiesService) {
		this.secretarioService = secretarioService;
	}

}
