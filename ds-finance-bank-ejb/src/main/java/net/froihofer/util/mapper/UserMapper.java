package net.froihofer.util.mapper;

import dto.CustomerDTO;
import net.froihofer.persistence.entity.Customer;

public class UserMapper {

    public static CustomerDTO customerToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getAddress());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setPassword(customer.getPassword());
        customerDTO.setId(customer.getId());

        return customerDTO;
    }

    public static Customer DtoToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setAddress(customerDTO.getAddress());
        customer.setPassword(customerDTO.getPassword());
        customer.setId(customerDTO.getId());

        return customer;
    }
}
