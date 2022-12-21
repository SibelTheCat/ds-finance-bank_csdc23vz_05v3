package net.froihofer.persistence.dao;

import net.froihofer.persistence.entity.Customer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class CustomerDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Customer createCustomer(String firstName, String lastName, String address, String password) {
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setPassword(password);
        customer.setAddress(address);

        entityManager.persist(customer);
        entityManager.flush();

        return customer;
    }
}
