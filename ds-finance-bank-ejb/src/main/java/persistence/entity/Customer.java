package persistence.entity;

import javax.persistence.*;

@Entity
public class Customer extends Person {

    private String address;

    @OneToOne
    @JoinColumn(name = "depot")
    private Depot depot;

    public Customer() {
        this.depot = new Depot();
    }

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

    public Depot getDepot() {
        return depot;
    }
    public void setDepot(Depot depot) {
        this.depot = depot;
    }
}
