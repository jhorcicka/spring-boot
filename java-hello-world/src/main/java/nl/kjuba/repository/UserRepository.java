package nl.kjuba.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import nl.kjuba.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
