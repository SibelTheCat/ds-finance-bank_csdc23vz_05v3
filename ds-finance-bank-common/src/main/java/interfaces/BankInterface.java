package interfaces;

import dto.CustomerDTO;
import dto.EmployeeDTO;

import javax.ejb.Remote;
import java.util.ArrayList;

@Remote
public interface BankInterface {

    CustomerDTO createCustomer(String firstName, String lastname, String address, String password);
    String checkPersonRole();
    EmployeeDTO createEmployee(String firstName, String lastname, String password);
    ArrayList<CustomerDTO> searchCustomer(String firstName, String lastName);
}
