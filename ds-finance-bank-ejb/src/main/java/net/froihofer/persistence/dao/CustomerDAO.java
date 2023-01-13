package net.froihofer.persistence.dao;

import net.froihofer.persistence.entity.Customer;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

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

    public ArrayList<Customer> searchCustomer(String firstName, String lastName) {
        Query query = entityManager.createQuery("SELECT c FROM Customer c WHERE c.firstName LIKE :firstName AND c.lastName LIKE :lastName");
        query.setParameter("firstName", firstName);
        query.setParameter("lastName", lastName);
        List<Customer> queryResultList = query.getResultList();
        ArrayList<Customer> customerList = new ArrayList<>();
        customerList.addAll(queryResultList);
        return customerList;
    }
}
