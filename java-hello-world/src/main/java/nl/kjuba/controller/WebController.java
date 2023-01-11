package nl.kjuba.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import nl.kjuba.model.Customer;
import nl.kjuba.repository.CustomerRepository;

@Controller
@RequestMapping(path="/web")
public class WebController {
	@GetMapping(path="/")
	public String hello() {
		return "hello universe!";
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@GetMapping(path="/user")
	public String helloUser() {
		return "hello user!";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(path="/admin")
	public String helloAdmin() {
		return "hello admin!";
	}
}
