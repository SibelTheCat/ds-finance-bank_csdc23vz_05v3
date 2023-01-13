package net.froihofer.service;

import dto.CustomerDTO;
import dto.EmployeeDTO;
import interfaces.BankInterface;
import net.froihofer.persistence.dao.EmployeeDAO;
import net.froihofer.persistence.entity.Employee;
import net.froihofer.util.mapper.UserMapper;
import net.froihofer.persistence.dao.CustomerDAO;
import net.froihofer.persistence.entity.Customer;
import net.froihofer.util.jboss.WildflyAuthDBHelper;

import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Stateless(name = "BankService")
@DeclareRoles({"customer", "employee"})
public class BankServiceImpl implements BankInterface {

    @Resource
    SessionContext ctx;

    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private EmployeeDAO employeeDAO;


    @Override
    @RolesAllowed({"customer", "employee"})
    public String  checkPersonRole() {
        return (this.ctx.isCallerInRole("customer")) ? "customer" : "employee";
    }

    @Override
    @RolesAllowed("employee")
    public EmployeeDTO createEmployee(String firstName, String lastname, String password) {
        Employee employee = employeeDAO.createEmployee(firstName, lastname, password);

        createWildflyUser(String.valueOf(employee.getId()), employee.getPassword(), "employee");

        return UserMapper.employeeToDTO(employee);
    }

    @Override
    @RolesAllowed("employee")
    public CustomerDTO createCustomer(String firstName, String lastname, String password, String address) {
        Customer customer = customerDAO.createCustomer(firstName, lastname, password, address);

        createWildflyUser(String.valueOf(customer.getId()), customer.getPassword(), "customer");

        return UserMapper.customerToDTO(customer);
    }

    @Override
    @RolesAllowed("employee")
    public ArrayList<CustomerDTO> searchCustomer(String firstName, String lastName) {
        ArrayList<Customer> customers = customerDAO.searchCustomer(firstName, lastName);
        ArrayList<CustomerDTO> customersDTO = new ArrayList<>();

        for (Customer c : customers) {
            customersDTO.add(UserMapper.customerToDTO(c));
        }

        return customersDTO;
    }



    private void createWildflyUser(String id, String password, String PersonRole) {
        File jbossPath = new File("../");
        WildflyAuthDBHelper wildflyAuthDBHelper = new WildflyAuthDBHelper(jbossPath);

        String[] role = new String[]{PersonRole};

        try {
            wildflyAuthDBHelper.addUser(id, password, role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
