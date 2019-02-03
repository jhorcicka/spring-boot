package nl.kjuba;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/demo")
public class MainController {
	@Autowired
	private CustomerRepository repository;
	
	@GetMapping(path="/add")
	public @ResponseBody String addNewCustomer(@RequestParam String firstName, @RequestParam String lastName) {
	    final Customer customer = new Customer(firstName, lastName);
		repository.save(customer);
		return "Saved";
	}
	
	@GetMapping(path="/all")
	public @ResponseBody Iterable<Customer> getAllCustomers() {
		return repository.findAll();
	}
}
