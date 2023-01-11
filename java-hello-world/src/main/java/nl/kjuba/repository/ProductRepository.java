package nl.kjuba.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import nl.kjuba.model.Product;

public interface ProductRepository extends CrudRepository<Product, Long> {
    List<Product> findByName(String name);
}
