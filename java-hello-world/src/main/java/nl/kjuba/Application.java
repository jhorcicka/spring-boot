package nl.kjuba;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import nl.kjuba.model.Customer;
import nl.kjuba.model.Product;
import nl.kjuba.model.User;
import nl.kjuba.repository.CustomerRepository;
import nl.kjuba.repository.ProductRepository;
import nl.kjuba.repository.UserRepository;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }

    @Bean
    public CommandLineRunner demo(final CustomerRepository customerRepository,
            final ProductRepository productRepository, final UserRepository userRepository,
            final PasswordEncoder encoder) {
        return (args) -> {
            // delete all records
            productRepository.deleteAll();
            customerRepository.deleteAll();
            userRepository.deleteAll();

            // create users
            userRepository.save(new User("user", encoder.encode("user"), "ROLE_USER"));
            userRepository.save(new User("admin", encoder.encode("admin"), "ROLE_ADMIN"));
            // fetch all users
            log.info("Users found with findAll():");
            log.info("-------------------------------");
            for (User user: userRepository.findAll()) {
                log.info(user.toString());
            }
            log.info("");

            // save a couple of customers
            Customer jack = new Customer("Jack", "Bauer");
            customerRepository.save(jack);
            customerRepository.save(new Customer("Chloe", "O'Brian"));
            customerRepository.save(new Customer("Kim", "Bauer"));
            customerRepository.save(new Customer("David", "Palmer"));
            customerRepository.save(new Customer("Michelle", "Dessler"));
            productRepository.save(new Product("Product ABC", jack));
            productRepository.save(new Product("Product XYZ", jack));

            // fetch all customers
            log.info("Customers found with findAll():");
            log.info("-------------------------------");
            for (Customer customer : customerRepository.findAll()) {
                log.info(customer.toString());
            }
            log.info("");

            // fetch an individual customer by ID
            customerRepository.findById(1L).ifPresent(customer -> {
                log.info("Customer found with findById(1L):");
                log.info("--------------------------------");
                log.info(customer.toString());
                log.info("");
            });

            // fetch customers by last name
            log.info("Customer found with findByLastName('Bauer'):");
            log.info("--------------------------------------------");
            customerRepository.findByLastName("Bauer").forEach(bauer -> {
                log.info(bauer.toString());
            });
            log.info("");

            // fetch all products
            log.info("Products found with findAll():");
            log.info("-------------------------------");
            for (Product product : productRepository.findAll()) {
                log.info(product.toString());
            }
            log.info("");
        };
    }

}
