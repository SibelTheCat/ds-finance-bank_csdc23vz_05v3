package net.froihofer.persistence.entity;

import javax.persistence.Entity;

@Entity
public class Customer extends Person {

    private String address;

    public Customer() {}

    public Customer(String firstName, String lastName, String password, String address) {
        super(firstName, lastName, password);
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
