package nl.kjuba.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import nl.kjuba.model.Customer;

public interface CustomerRepository extends CrudRepository<Customer, Long> {
    List<Customer> findByLastName(String lastName);
}
