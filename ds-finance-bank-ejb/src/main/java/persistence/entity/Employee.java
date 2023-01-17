package persistence.entity;

import javax.persistence.Entity;

@Entity
public class Employee extends Person {

    public Employee() {}

    public Employee(String firstName, String lastName, String password) {
        super(firstName, lastName, password);
    }
}
