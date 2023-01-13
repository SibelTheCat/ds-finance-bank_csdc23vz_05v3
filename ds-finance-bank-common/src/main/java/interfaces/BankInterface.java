package interfaces;

import dto.CustomerDTO;
import dto.EmployeeDTO;

import javax.ejb.Remote;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Remote
public interface BankInterface {

    CustomerDTO createCustomer(String firstName, String lastname, String address, String password);
    String checkPersonRole();
    EmployeeDTO createEmployee(String firstName, String lastname, String password);
    ArrayList<CustomerDTO> searchCustomer(String firstName, String lastName);
    List<String> getStocksbyCompanyName(String companyName) throws Exception;
    BigDecimal sellStocks(int costumerID, String symbol, int shares) throws Exception;
    BigDecimal buyStocks(int costumerID, String symbol, int shares) throws Exception;
    String getStocksbySymbol(String symbol) throws Exception;
}

