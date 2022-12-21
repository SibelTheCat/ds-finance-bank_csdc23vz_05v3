package interfaces;

import dto.CustomerDTO;

import javax.ejb.Remote;

@Remote
public interface BankInterface {

    CustomerDTO createCustomer(String firstName, String lastname, String address, String password);
    String checkPersonRole();
}
