package net.froihofer.persistence.dao;

import net.froihofer.persistence.entity.Employee;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class EmployeeDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Employee createEmployee(String firstName, String lastName, String password) {
        Employee employee = new Employee();
        employee.setFirstName(firstName);
        employee.setLastName(lastName);
        employee.setPassword(password);

        entityManager.persist(employee);
        entityManager.flush();

        return employee;
    }
}
