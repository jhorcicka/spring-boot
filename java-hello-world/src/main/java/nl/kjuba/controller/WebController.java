package nl.kjuba.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/web")
public class WebController {
	@GetMapping(path="/")
	public @ResponseBody String hello() {
		final StringBuilder html = new StringBuilder();
		html.append("<form action=\"/logout\" method=\"post\"><button>logout</button></form>");
		//html.append("<form action=\"/j_spring_security_logout\" method=\"post\"><button>logout2</button></form>");
		return html.toString();
	}

	@GetMapping(path="/user")
	public @ResponseBody String helloUser() {
		return "hello user!";
	}

	@GetMapping(path="/admin")
	public @ResponseBody String helloAdmin() {
		return "hello admin!";
	}
}
