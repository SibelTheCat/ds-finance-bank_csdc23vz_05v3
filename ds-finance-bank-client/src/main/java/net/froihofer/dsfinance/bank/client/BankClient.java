package net.froihofer.dsfinance.bank.client;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ctc.wstx.exc.WstxOutputException;
import dto.CustomerDTO;
import dto.EmployeeDTO;
import dto.StockDTO;
import interfaces.BankInterface;
import net.froihofer.util.AuthCallbackHandler;
import net.froihofer.util.WildflyJndiLookupHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main class for starting the bank client.
 *
 */
public class BankClient {
  private static Logger log = LoggerFactory.getLogger(BankClient.class);
  Scanner input = new Scanner(System.in);

  BankInterface bank;

  /**
   * Skeleton method for performing an RMI lookup
   */
  private void getRmiProxy(String id, String password) {
    AuthCallbackHandler.setUsername(id);
    AuthCallbackHandler.setPassword(password);
    Properties props = new Properties();
    props.put(Context.SECURITY_PRINCIPAL,AuthCallbackHandler.getUsername());
    props.put(Context.SECURITY_CREDENTIALS,AuthCallbackHandler.getPassword());
    try {
      WildflyJndiLookupHelper jndiHelper = new WildflyJndiLookupHelper(new InitialContext(props), "ds-finance-bank-ear", "ds-finance-bank-ejb", "");
      bank = jndiHelper.lookup("BankService", BankInterface.class);

      switch (bank.checkPersonRole()) {
        case "customer":
          customerMenu();
          break;
        case "employee":
          employeeMenu();
          break;
      }
    }
    catch (NamingException e) {
      log.error("Failed to initialize InitialContext.",e);
    } catch (Exception e) {
      System.out.println("Login fehlgeschlagen versuchen Sie es erneut");
      run();
    }
  }

  private void run() {
    System.out.println("Um fortzufahren wird Ihre ID und Ihr Passwort benötigt");
    System.out.println("ID: ");
    String id = input.nextLine();
    System.out.println("Passwort: ");
    String password = input.nextLine();
    getRmiProxy(id, password); // 21 : test --> customer // 26 : alex --> employee
  }

  private void createCustomer() {
    System.out.println("Bitte geben Sie die Daten des Kunden ein");
    System.out.println("Vorname: ");
    String firstName = input.nextLine();
    System.out.println("Nachname: ");
    String lastName = input.nextLine();
    System.out.println("Adresse: ");
    String address = input.nextLine();
    System.out.println("Passwort: ");
    String password = input.nextLine();

    try {
     CustomerDTO newCustomer = bank.createCustomer(firstName, lastName, address, password);

      System.out.println("Kunde " + newCustomer.getId() + " erfolgreich erstellt");

      //zum testen
     // System.out.println(newCustomer.getDepot().getDepotID());


      System.out.println("Der Login erfolgt durch die zugewiesene Kunden Nummer und das erstellte Passwort");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Etwas ist schiefgelaufen wenden Sie sich an den Technischen Support");
    }
    System.out.println("");
    switch (bank.checkPersonRole()) {
      case "customer":
        customerMenu();
        break;
      case "employee":
        employeeMenu();
        break;
    }
  }

  private void createEmployee() {
    System.out.println("Bitte geben Sie die Daten des Mitarbeiters ein");
    System.out.println("Vorname: ");
    String firstName = input.nextLine();
    System.out.println("Nachname: ");
    String lastName = input.nextLine();
    System.out.println("Passwort: ");
    String password = input.nextLine();

    try {
      EmployeeDTO newEmployee = bank.createEmployee(firstName, lastName, password);
      System.out.println("Mitarbeiter " + newEmployee.getId() + " erfolgreich erstellt");
      System.out.println("Der Login erfolgt durch die zugewiesene Mitarbeiter Nummer und das erstellte Passwort");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Etwas ist schiefgelaufen wenden Sie sich an den Technischen Support");
    }
    System.out.println("");
    switch (bank.checkPersonRole()) {
      case "customer":
        customerMenu();
        break;
      case "employee":
        employeeMenu();
        break;
    }
  }

  private void customerMenu()  {
    System.out.println("Customer Menu");
    System.out.println("Was wollen Sie machen?");

    System.out.println("1. Nach verfügbaren Aktien suchen");
    System.out.println("2. Aktien kaufen");
    System.out.println("3. Aktien verkaufen");
    System.out.println("4. Ihr Depot abrufen");
    System.out.println("5. Programm beenden");
    System.out.println("Wählen Sie Ihre Aktion: ");

    String userInput = input.next();
    input.nextLine();

    switch (userInput) {
      case "1":
        System.out.println("Nach verfügbaren Aktien suchen");
        lookforStock();
        break;
      case "2":
        System.out.println("Aktien kaufen");
        buyStock();
        break;
      case "3":
        System.out.println("Aktien verkaufen");
        sellStock();
        break;
      case "4":
        System.out.println("Ihr Depot enhält folgenden Aktieninhalt");
        showDepot();
        break;
      case "5":
        System.out.println("Programm wird nun beendet");
        System.exit(0);
      default:
        System.out.println("Ungültige Eingabe, bitte geben Sie eine Ziffer zwischen 1 und 8 ein");
        break;
    }
  }

  private void employeeMenu() {

    System.out.println("Was wollen Sie machen?");

    System.out.println("1. Neues Kundenkonto anlegen");
    System.out.println("2. Neues Mitarbeiterkonto anlegen");
    System.out.println("3. Benutzerkonto suchen");
    System.out.println("4. Nach verfügbaren Aktien suchen");
    System.out.println("5. Aktien kaufen für Kunden");
    System.out.println("6. Aktien verkaufen für Kunden");
    System.out.println("7. Depot vom Kunden abrufen");
    System.out.println("8. Volumenabfrage");
    System.out.println("9. Programm beenden");
    System.out.println("Wählen Sie Ihre Aktion: ");

    String userInput = input.next();
    input.nextLine();

    switch (userInput) {
      case "1":
        createCustomer();
        break;
      case "2":
        createEmployee();
        break;
      case "3":
        searchCustomer();
        break;
      case "4":
        System.out.println("Nach verfügbaren Aktien suchen");
        lookforStock();
        break;
      case "5":
        System.out.println("Aktien kaufen für Kunden");
        buyStockForCostumer();
        break;
      case "6":
        System.out.println("Aktien verkaufen für Kunden");
        sellStockForCustomer();
        break;
      case "7":
        System.out.println("Depot vom Kunden abrufen");
        showDepotforCustomer();
        break;
      case "8":
        System.out.println("Volumenabfrage");
        break;
      case "9":
        System.out.println("Programm wird nun beendet");
        System.exit(0);
      default:
        System.out.println("Ungültige Eingabe, bitte geben Sie eine Ziffer zwischen 1 und 8 ein");
        break;
    }
  }

  private void searchCustomer() {
    System.out.println("Um einen Kunden zu suchen geben Sie dessen Vor- und Nachnamen ein.");
    System.out.println("Vorname: ");
    String firstName = input.nextLine();
    System.out.println("Nachname: ");
    String lastName = input.nextLine();

    try {
      ArrayList<CustomerDTO> customers = bank.searchCustomer(firstName, lastName);
      System.out.println("Folgende Kunden wurden gefunden: ");
      for (CustomerDTO customerDTO : customers) {
        System.out.println("ID: " + customerDTO.getId() + "\nVorname: " + customerDTO.getFirstName() + "\nNachname: " + customerDTO.getLastName());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("");
    switch (bank.checkPersonRole()) {
      case "customer":
        customerMenu();
        break;
      case "employee":
        employeeMenu();
        break;
    }
  }


  public static void main(String[] args) {
    BankClient client = new BankClient();
    client.run();
  }

  public void lookforStock() {
    List<String> output;
    System.out.println("Bitte geben Sie den Suchbegriff für die gewünschte Aktie ein (mind 2 Buchstaben):");
    String stockSearch = input.nextLine();
    try {
      output = bank.getStocksbyCompanyName(stockSearch);
      if (!output.equals(null)){
        output.forEach((x) -> System.out.println(x));

      }}
    catch (Exception e) {
      System.out.println("Leider hat die Suche nach " + stockSearch+ " keine Resultate ergeben." );
      lookforStock();
    }
    System.out.println("");
    switch (bank.checkPersonRole()) {
      case "customer":
        customerMenu();
        break;
      case "employee":
        employeeMenu();
        break;
    }
  }
  public void sellStock()  {
    System.out.println("Bitte geben Sie das Symbol für die gewünschte Aktie ein:");
    String symbol = input.nextLine();
    String output;
    String output2;
    try{
      output = bank.getStocksbySymbol(symbol);
      System.out.println(output);
    }catch (Exception e) {
      System.out.println(e.getMessage());
      sellStock();
    }
    System.out.println("Bitte geben Sie die Anzahl der Aktien ein, die Sie für Ihren Kunden/ Ihre Kundin verkaufen wollen");
    int shares = Integer.valueOf(input.nextLine());

    System.out.println(bank.getID());
    try{
      int customerId = Integer.parseInt(bank.getID());

      output2= bank.sellStocks(customerId, symbol, shares);
      System.out.println(output2);
    }
    catch (Exception e){
      System.out.println(e.getMessage());
    }
    System.out.println("");
    switch (bank.checkPersonRole()) {
      case "customer":
        customerMenu();
        break;
      case "employee":
        employeeMenu();
        break;
    }
  }

  public void sellStockForCustomer(){


    System.out.println("Bitte geben Sie das Symbol für die gewünschte Aktie ein:");
    String symbol = input.nextLine();
    String output;
    String output2;
    try{
      output = bank.getStocksbySymbol(symbol);
      System.out.println(output);
    }catch (Exception e) {
      System.out.println(e.getMessage());
      sellStockForCustomer();
    }
    System.out.println("Bitte geben Sie die Anzahl der Aktien ein, die Sie verkaufen wollen");
    int shares = Integer.valueOf(input.nextLine());
    System.out.println("Bitte geben Sie die Kundennummer des Kunden ein:");
    int customerID = Integer.valueOf(input.nextLine());
    /** TO DO: überprüfen ob Kundennummer korrekt ist
     *
     */

    try{
      // TO DO, ID von KUNDEN bekommen

      output2 = bank.sellStocks(customerID, symbol, shares);
      System.out.println(output2);
    }
    catch (Exception e){
      System.out.println(e.getMessage());
    }
    System.out.println("");
    switch (bank.checkPersonRole()) {
      case "customer":
        customerMenu();
        break;
      case "employee":
        employeeMenu();
        break;
    }
  }

  public void buyStock()  {
    System.out.println("Bitte geben Sie das Symbol für die gewünschte Aktie ein:");
    String symbol = input.nextLine();
    String output;
    String output2;
    try{
      output = bank.getStocksbySymbol(symbol);
      System.out.println(output);
    }catch (Exception e) {
      System.out.println(e.getMessage());
      buyStock();
    }
    System.out.println("Bitte geben Sie die Anzahl der Aktien ein, die Sie kaufen wollen");
    int shares = Integer.valueOf(input.nextLine());
    try{
      int customerId = Integer.parseInt(bank.getID());

     output2=  bank.buyStocks(customerId, symbol, shares);
     System.out.println(output2);
    }
    catch (Exception e){
      System.out.println(e.getMessage());
    }
    System.out.println("");
    switch (bank.checkPersonRole()) {
      case "customer":
        customerMenu();
        break;
      case "employee":
        employeeMenu();
        break;
    }
  }

  public void buyStockForCostumer(){

    System.out.println("Bitte geben Sie das Symbol für die gewünschte Aktie ein:");
    String symbol = input.nextLine();
    String output;
    String output2;

    try{
      output = bank.getStocksbySymbol(symbol);
      System.out.println(output);
    }catch (Exception e) {
      System.out.println(e.getMessage());
      buyStockForCostumer();
    }

    System.out.println("Bitte geben Sie die Anzahl der Aktien ein, die Sie für Ihren Kunden/ Ihre Kundin kaufen wollen");
    int shares = Integer.valueOf(input.nextLine());
    System.out.println("Bitte geben Sie die Kundennummer des Kunden ein:");
    int customerID = Integer.valueOf(input.nextLine());

    /** TO DO: überprüfen ob Kundennummer korrekt ist
     *
     */
    try{
      // TO DO, ID von KUNDEN bekommen

      output2 = bank.buyStocks(customerID, symbol, shares);
      System.out.println(output2);
    }
    catch (Exception e){
      System.out.println(e.getMessage());
    }
    System.out.println("");
    switch (bank.checkPersonRole()) {
      case "customer":
        customerMenu();
        break;
      case "employee":
        employeeMenu();
        break;
    }
  }

  public void showDepot() {
    int customerId = Integer.parseInt(bank.getID());
    List<StockDTO> stockList = new ArrayList<>();
    try{
      stockList = bank.getUserDepot(customerId);}
    catch (Exception e){
      System.out.println("Depot konnte nicht abgerufen werden");
    }
    if (stockList.isEmpty()){
      System.out.println(" Sie haben noch keine Aktien in Ihrem Depot");
    }
    else {
   stockList.forEach((x) -> System.out.println("Aktienname " +x.getCompanyName() + " Symbol der Aktie " +x.getStockID_Symbol()+ " Anzahl die Sie von dieser Aktie  haben: "+ x.getSharesAmount()  ));
  }
    System.out.println("");
    switch (bank.checkPersonRole()) {
      case "customer":
        customerMenu();
        break;
      case "employee":
        employeeMenu();
        break;
    }}

  public void showDepotforCustomer() {
    System.out.println("Bitte geben Sie die Kundennummer des Kunden ein:");
    int customerID = Integer.valueOf(input.nextLine());
    List<StockDTO> stockList = new ArrayList<>();

    try {
      stockList = bank.getUserDepot(customerID);
    }
    catch (Exception e){
      System.out.println("Depot konnte nicht abgerufen werden");
    }
    if (stockList.isEmpty()){
      System.out.println("Es befinden sich noch kein Aktien in dem gesuchten Depot");
    }
    else {
      stockList.forEach((x) -> System.out.println("Aktienname " +x.getCompanyName() + " Symbol der Aktie " +x.getStockID_Symbol()+ " Anzahl die Sie von dieser Aktie  haben: "+ x.getSharesAmount()  ));

    }
    System.out.println("");
    switch (bank.checkPersonRole()) {
      case "customer":
        customerMenu();
        break;
      case "employee":
        employeeMenu();
        break;
    }}
}


