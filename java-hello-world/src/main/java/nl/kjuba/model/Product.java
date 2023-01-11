package nl.kjuba.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name="customer_id", nullable=false)
    private Customer customer;

    protected Product() {}

    public Product(String name, Customer customer) {
        this.name = name;
        this.customer = customer;
    }

    @Override
    public String toString() {
        return String.format("Product[id=%d, name='%s', customer='%s']", id, name, getCustomer().getLastName());
    }

    public Long getId() {
      return id;
    }

    public String getName() {
      return name;
    }

    public Customer getCustomer() {
      return customer;
    }
}

