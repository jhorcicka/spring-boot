package nl.kjuba.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import java.util.Set;
import java.util.HashSet;

@Entity
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @OneToMany(mappedBy="customer", fetch = FetchType.EAGER)
    private Set<Product> products;

    private String firstName;
    private String lastName;

    protected Customer() {}

    public Customer(String firstName, String lastName) {
      this(firstName, lastName, new HashSet());
    }

    public Customer(String firstName, String lastName, Set<Product> products) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.products = products;
    }

    @Override
    public String toString() {
        return String.format("Customer[id=%d, productsCount=%d, firstName='%s', lastName='%s']", id, getProducts().size(), firstName, lastName);
    }

    public Long getId() {
      return id;
    }

    public String getFirstName() {
      return firstName;
    }

    public String getLastName() {
      return lastName;
    }

    public Set<Product> getProducts() {
      if (products == null) {
        products = new HashSet();
      }
      return products;
    }
}

