package net.froihofer.util.mapper;

import dto.CustomerDTO;
import dto.EmployeeDTO;
import net.froihofer.persistence.entity.Customer;
import net.froihofer.persistence.entity.Employee;

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

    public static EmployeeDTO employeeToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setPassword(employee.getPassword());
        employeeDTO.setId(employee.getId());

        return employeeDTO;
    }
}
