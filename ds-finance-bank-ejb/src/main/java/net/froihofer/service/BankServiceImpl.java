package net.froihofer.service;

import dto.CustomerDTO;
import dto.EmployeeDTO;
import interfaces.BankInterface;
import net.froihofer.dsfinance.ws.trading.PublicStockQuote;
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
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Stateless(name = "BankService")
@DeclareRoles({"customer", "employee"})
public class BankServiceImpl implements BankInterface {

    @Resource
    SessionContext ctx;

    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private EmployeeDAO employeeDAO;
    Stockmarket st = new Stockmarket();

    @Override
    @RolesAllowed({"customer", "employee"})
    public String checkPersonRole() {
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

    @Override
    @RolesAllowed({"customer", "employee"})
    public List<String> getStocksbyCompanyName(String companyName) throws Exception {
        st.initService();
        List<String> returnList = new ArrayList<String>();
        try {
            List<PublicStockQuote> stocks = st.getStockByCompanyName(companyName);
            if (stocks.isEmpty()) {
                returnList.add("Keine Firma zu der Eingabe \"" + companyName + "\" gefunden");
                return null;
            } else {
                stocks.forEach((x) -> returnList.add(new String("Symbol: " + x.getSymbol() + " /Company Name: " + x.getCompanyName() + " /Last known trade-price: " + x.getLastTradePrice() + " Kröten /Shares left: " + x.getFloatShares())));
                return returnList;
            }
        } catch
        (Exception e) {
            throw new Exception("Etwas ist schief gelaufen");
        }

    }
    @Override
    @RolesAllowed({"customer", "employee"})
    public String buyStocks(int costumerID, String symbol, int shares) throws Exception {
        BigDecimal pricePerShare;
        PublicStockQuote quote;
        String output;

        //check if Symbol exists, if not Message + return value
        try {
            quote = st.getStockBySymbol(symbol);
        } catch (Exception e) {
            throw new Exception("Es wurden leider keine Aktien zu \"" + symbol + "\"  gefunden");
        }
        //check if Stock has enough shares left
        //check fist if avaiable stocks are "null"
        if (quote.getFloatShares() == null) {
            throw new Exception("Es sind leider nicht genug Shares übrig. Shares die noch für die Firma " + quote.getCompanyName() + " verfügbar sind :" + quote.getFloatShares() + " Shares");
        }
        // then if the wanted shares are too much
        if (quote.getFloatShares().intValue() < shares) {
            throw new Exception("Es sind leider nicht genug Shares übrig. Shares die noch für die Firma " + quote.getCompanyName() + " verfügbar sind :" + quote.getFloatShares() + " Shares");
        }

        try {
            pricePerShare = st.buyStock(symbol, shares);
            output = "Sie haben " + shares + " Shares von der Aktie " + quote.getCompanyName() + " für je " + pricePerShare + " Kröten gekauft";
            return output;

            /** TO DO
             * EINTRAG IN KUNDENDATENBANK
             * Den Preis der Aktie ist unter pricePerShare gespeichert
             */

        } catch (Exception e) {
            throw new Exception("Stock konnte leider nicht gekauft werden, da nicht mehr genug Shares zur Verfügung stehen.");
        }
    }
    @Override
    @RolesAllowed({"customer", "employee"})
    public String sellStocks(int costumerID, String symbol, int shares) throws Exception {
        BigDecimal pricePerShareSell= BigDecimal.valueOf(0);
        String output;
        //check if Symbol exists, if not Message + return value = 0;
        try {
            st.getStockBySymbol(symbol);
        } catch (Exception e) {
            throw new Exception("Es wurden leider keine Aktien zu \"" + symbol + "\"  gefunden");
        }
        try {
            pricePerShareSell = st.sellStock(symbol, shares);
            output ="Sie haben " + shares + " Shares der Aktie " + st.getStockBySymbol(symbol).getCompanyName() + " für je " + pricePerShareSell + "Kröten verkauft";
            return output;
            /** TO DO
             * EINTRAG IN KUNDENDATENBANK
             * Aktuelle Kosten der Aktie sind in pricePerShareSell
             */
        } catch (Exception e) {
            throw new Exception("Transaktion hat leider nicht geklappt");
        }
    }
    @Override
    @RolesAllowed({"customer", "employee"})
    public String getStocksbySymbol(String symbol) throws Exception {
        PublicStockQuote stockQuoteToGetName;
        String aktie;
        try
        {
            stockQuoteToGetName = st.getStockBySymbol(symbol);
            aktie ="\nDer Name der Firma zum eingegebenen Symbol \"" + symbol+ "\"  ist: " + stockQuoteToGetName.getCompanyName() + "\n  Derzeit stehen " + stockQuoteToGetName.getFloatShares() + " Shares am Stockmarket zu diesr Aktie zur Verfügung. \n  Der  aktuelle  Preis pro Share ist " + stockQuoteToGetName.getLastTradePrice() + " Kröten.";
            return aktie;
        } catch (Exception e) {
            throw new Exception("Es wurden leider keine Aktien zum Symbol \"" + symbol+ "\"  gefunden");
        }}

}
