package net.froihofer.service;

import dto.*;
import interfaces.BankInterface;
import net.froihofer.dsfinance.ws.trading.PublicStockQuote;
import net.froihofer.util.mapper.BankMapper;
import persistence.dao.*;
import persistence.entity.*;
import net.froihofer.util.mapper.UserMapper;
import net.froihofer.util.jboss.WildflyAuthDBHelper;


import javax.annotation.Resource;
import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Stateless(name = "BankService")
@DeclareRoles({"customer", "employee"})
public class BankServiceImpl implements BankInterface {

    @Resource
    SessionContext ctx;

    @Inject
    private CustomerDAO customerDAO;
    @Inject
    private EmployeeDAO employeeDAO;
    @Inject
    private DepotDAO depotDAO;
    @Inject
    private BankDAO bankDAO;
    @Inject
    private StockDAO stockDAO;

    Stockmarket st = new Stockmarket();

    @Override
    @RolesAllowed({"customer", "employee"})
    public String checkPersonRole() {
        return (this.ctx.isCallerInRole("customer")) ? "customer" : "employee";
    }

    @Override
    @RolesAllowed({"customer", "employee"})
    public String getID() {
        return ctx.getCallerPrincipal().getName();
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

        Depot depot = depotDAO.createDepot();

        Customer customer = customerDAO.createCustomer(firstName, lastname, password, address, depot);

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
    public DepotDTO getUserDepot(int userId) {
        Customer customer = null;
        Depot depot = null;
        try {
            customer = customerDAO.searchCustomerByCustomerId(userId);
            depot = customer.getDepot();

        } catch (Exception e) {
            throw new RuntimeException(e);}
            return UserMapper.depotToDTO(depot);
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
        st.initService();
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
            throw new Exception(shares+"  Es sind leider nicht genug Shares übrig. Shares die noch für die Firma " + quote.getCompanyName() + " verfügbar sind :" + quote.getFloatShares() + " Shares");
        }
        // then if the wanted shares are too much


        if (quote.getFloatShares().intValue() < shares) {
            throw new Exception("Es sind leider nicht genug Shares übrig. Shares die noch für die Firma " + quote.getCompanyName() + " verfügbar sind :" + quote.getFloatShares() + " Shares");
        }
        BigDecimal costOfShare = quote.getLastTradePrice();
        BigDecimal needAmount = new BigDecimal(shares).multiply(costOfShare);

        if (checkVolume().compareTo(needAmount)<0){
            throw new Exception("Es ist leider nicht mehr genung Bankvolumen verfügbar");
        }
        try {
            pricePerShare = st.buyStock(symbol, shares);
            output = "Sie haben " + shares + " Shares von der Aktie " + quote.getCompanyName() + " für je " + pricePerShare + " Kröten gekauft";


                Bank bank = bankDAO.getBank();
                BigDecimal currentVolume = bank.getBankVolume();
                BigDecimal newVolume = currentVolume.subtract(needAmount);
                bank.setBankVolume(newVolume);
                bankDAO.update(bank);


            Stock stock = checkIfUserHasStock(costumerID, symbol);
            if (stock == null){
                Stock newstock = stockDAO.createNewStockInDepot(quote.getCompanyName(), shares, symbol);
                customerDAO.searchCustomerByCustomerId(costumerID).getDepot().getStockList().add(newstock);
            }
            else {
              stock.setSharesAmount(stock.getSharesAmount()+shares);
            }
            //DepotVolumen wird aktuelisiert
          //  Depot depot =  customerDAO.searchCustomerByCustomerId(costumerID).getDepot();
          //  BigDecimal depotVolumeAktuell = depot.getTotalValue();
          //  BigDecimal newDepotVolume = depotVolumeAktuell.add(needAmount);
           // depot.setTotalValue(newDepotVolume);

            return output;


        } catch (Exception e) {

            throw new Exception(e);
        }
    }
    @Override
    @RolesAllowed({"customer", "employee"})
    public String sellStocks(int costumerID, String symbol, int shares) throws Exception {
        st.initService();
        BigDecimal pricePerShareSell= BigDecimal.valueOf(0);
        String output;
        //check if Symbol exists, if not Message + return value = 0;
        try {
            st.getStockBySymbol(symbol);
        } catch (Exception e) {
            throw new Exception("Es wurden leider keine Aktien zu \"" + symbol + "\"  gefunden");
        }


        try {


            Stock stock = checkIfUserHasStock(costumerID, symbol);
            if (stock == null){
                throw new Exception("Sie  haben keine Aktien zu diesem Symbol in Ihrem Depot");
            }

            else if(stock.getSharesAmount()< shares){
                throw new Exception("Sie haben leider nicht die geforderte Anzahl der Aktien zum Symbol " + stock.getStockID_Symbol() + ". Die verfügbare Stockanzahl ist: " + stock.getSharesAmount());
            }

            else {
                pricePerShareSell = st.sellStock(symbol, shares);
                output ="Sie haben " + shares + " Shares der Aktie " + st.getStockBySymbol(symbol).getCompanyName() + " für je " + pricePerShareSell + " Kröten verkauft";

                stock.setSharesAmount(stock.getSharesAmount()-shares);

                Bank bank = bankDAO.getBank();
                BigDecimal currentVolume = bank.getBankVolume();
                BigDecimal toAdd = new BigDecimal(shares).multiply(pricePerShareSell);
                BigDecimal newVolume = currentVolume.add(toAdd);
                bank.setBankVolume(newVolume);
                bankDAO.update(bank);

                //DepotVolumen wird aktualisiert
          //      Depot depot =  customerDAO.searchCustomerByCustomerId(costumerID).getDepot();
           //     BigDecimal depotVolumeAktuell = depot.getTotalValue();
            //    BigDecimal newDepotVolume = depotVolumeAktuell.subtract(toAdd);
            //    depot.setTotalValue(newDepotVolume);
            }

            return output;

        } catch (Exception e) {
            throw new Exception("Transaktion hat leider nicht geklappt");
        }




    }
    @Override
    @RolesAllowed({"customer", "employee"})
    public String getStocksbySymbol(String symbol) throws Exception {
        st.initService();
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

    private Stock checkIfUserHasStock(int customerId, String symbol){
        List<Stock> stocks = new ArrayList<>();
        Stock foundstock = null;
     try {
          stocks =  customerDAO.searchCustomerByCustomerId(customerId).getDepot().getStockList();

         for (Stock s : stocks){
             if(s.getStockID_Symbol().contains(symbol)){
                 foundstock = s;
             }
         }
    //    return foundstock;

        } catch (Exception e) {
            throw new RuntimeException("Aktien konnten nicht geladen werden");

        }
         return foundstock;


    }
    @Override
    @RolesAllowed({"employee"})
    public BigDecimal checkVolume(){

        return bankDAO.getBank().getBankVolume();

    }

    @Override
    @RolesAllowed({"employee"})
    public void createBank() throws Exception {

        String user = ctx.getCallerPrincipal().getName();
        if (user.equals("superuser")){

            bankDAO.persist(new Bank(new BigDecimal(1000000)));}
        else{ throw new Exception("Sie dürfen diesen Befehl nicht ausführen ");}


    }

    @Override
    @RolesAllowed({"employee"})
    public void checkIfUsrExists(int id) throws Exception {

        //wirft exception mit Message wenn Kunde nicht existiert
       customerDAO.searchCustomerByCustomerId(id);
    }

}
