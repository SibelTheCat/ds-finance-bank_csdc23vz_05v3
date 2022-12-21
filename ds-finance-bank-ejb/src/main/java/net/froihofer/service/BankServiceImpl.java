package net.froihofer.service;

import dto.CustomerDTO;
import interfaces.BankInterface;
import net.froihofer.util.mapper.UserMapper;
import net.froihofer.persistence.dao.CustomerDAO;
import net.froihofer.persistence.entity.Customer;
import net.froihofer.util.jboss.WildflyAuthDBHelper;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

@Stateless(name = "BankService")
@PermitAll
public class BankServiceImpl implements BankInterface {

    @Resource
    SessionContext ctx;

    @Inject
    private CustomerDAO customerDAO;


    @Override
    public String  checkPersonRole() {
        return (this.ctx.isCallerInRole("customer")) ? "customer" : "employee";
    }

    @Override
    public CustomerDTO createCustomer(String firstName, String lastname, String password, String address) {
        Customer customer = customerDAO.createCustomer(firstName, lastname, password, address);

        createWildflyUser(String.valueOf(customer.getId()), customer.getPassword());

        return UserMapper.customerToDTO(customer);
    }

    private void createWildflyUser(String id, String password) {
        File jbossPath = new File("../");
        WildflyAuthDBHelper wildflyAuthDBHelper = new WildflyAuthDBHelper(jbossPath);

        String[] role = new String[]{"customer"};

        try {
            wildflyAuthDBHelper.addUser(id, password, role);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
