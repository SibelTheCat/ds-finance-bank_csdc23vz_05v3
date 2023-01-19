package net.froihofer.dsfinance.bank.client;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dto.CustomerDTO;
import dto.DepotDTO;
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
    getRmiProxy(id, password); // 43 : 1234 --> customer // user1 : 1234 --> employee  // superuser:1234 --> employee der Bank erstellen kann
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
        checkBankVolume();
        break;
      case "9":
        System.out.println("Programm wird nun beendet");
        System.exit(0);
      case "secret10":
        System.out.println("Bank anlegen");
        setBank();
        break;
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
    System.out.println("Bitte geben Sie die Anzahl der Aktien ein, die Sie verkaufen wollen");
    String shares1 = input.nextLine();
    try {
      Integer.parseInt(shares1);
    }
    catch (NumberFormatException e){
      System.out.println(" Bitte geben Sie eine Zahl ein UPSI wieder von Beginn");
      sellStock();
    }


    //System.out.println(bank.getID());

    try{
      int customerId = Integer.parseInt(bank.getID());

      output2= bank.sellStocks(customerId, symbol, Integer.parseInt(shares1));
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
    System.out.println("Bitte geben Sie die Anzahl der Aktien ein, die Sie für Ihren Kunden/ Ihre Kundin verkaufen wollen");

    String shares = input.nextLine();
    //überprüft ob Eingabe eine Zahl ist
    try {
      Integer.parseInt(shares);
    }
    catch (NumberFormatException e){
      System.out.println(" Bitte geben Sie eine Zahl ein - > Die Abfrage wird erneut gestartet");
      sellStockForCustomer();
    }


    System.out.println("Bitte geben Sie die Kundennummer des Kunden ein:");
    String customerID = input.nextLine();
   //Überprüft ob Kundennummer-Eingabe eine Zahl ist
    try {
      Integer.parseInt(customerID);
    }
    catch (NumberFormatException e){
      System.out.println(" Bitte geben Sie eine Zahl als KundenID ein -> die Abfrage wird erneut gestartet");
      sellStockForCustomer();
    }

    //Überprüft ob Kunde existiert
    try {
      bank.checkIfUsrExists(Integer.parseInt(customerID));
    }
    catch (Exception e){
      System.out.println(e.getMessage());
      System.out.println("Bitte führen Sie die komplette Eingabe erneut durch");
      sellStockForCustomer();
    }

    try{
      output2 = bank.sellStocks(Integer.parseInt(customerID), symbol, Integer.parseInt(shares));
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

    String shares = input.nextLine();
    try {
      Integer.parseInt(shares);
    }
    catch (NumberFormatException e){
      System.out.println(" Bitte geben Sie eine Zahl ein -> ");
      buyStock();
    }

    try{
      int customerId = Integer.parseInt(bank.getID());

     output2=  bank.buyStocks(customerId, symbol, Integer.parseInt(shares));
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

    System.out.println("Bitte geben Sie die das Symbol der Aktie ein, die Sie für Ihren Kunden/ Ihre Kundin kaufen wollen");
    String symbol = input.nextLine();

    String output;
    String output2;

// überprüft ob Symbol existiert
    try{
      output = bank.getStocksbySymbol(symbol);
      System.out.println(output);
    }catch (Exception e) {
     // e.printStackTrace();
      System.out.println(e.getMessage());
      buyStockForCostumer();
    }

    System.out.println("Bitte geben Sie die Anzahl der Aktien ein (die Zahl darf nicht mehr 9 Stellen haben), die Sie für Ihren Kunden/ Ihre Kundin kaufen wollen");
  //  überprüft ob Eingabe der Aktien-Aktienanzahl eine Zahl ist
    String shares = input.nextLine();
    try {
      Integer.parseInt(shares);
    }
    catch (NumberFormatException e){
      System.out.println(" Bitte geben Sie eine gültige Zahl ein -> ");
      buyStockForCostumer();
    }
    System.out.println("Bitte geben Sie die Kundennummer des Kunden ein:");

    String customerID = input.nextLine();
    try {
      Integer.parseInt(customerID);
    }
    catch (NumberFormatException e){
      System.out.println(" Bitte geben Sie eine Zahl -> Eingabe startet erneut");
      buyStockForCostumer();
    }

    try {
      bank.checkIfUsrExists(Integer.parseInt(customerID));
    }
    catch (Exception e){
      System.out.println(e.getMessage());
      System.out.println("Bitte führen Sie die komplette Eingabe erneut durch");
      buyStockForCostumer();
    }

    try{
      output2 = bank.buyStocks(Integer.parseInt(customerID), symbol, Integer.parseInt(shares));
      System.out.println(output2);
    }

    catch (Exception e){
     // e.printStackTrace();
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
  //  List<StockDTO> stockList = new ArrayList<>();
    DepotDTO depotDTO = null;
    try{
      depotDTO = bank.getUserDepot(customerId);}
    catch (Exception e){
      System.out.println("Depot konnte nicht abgerufen werden");
    }
    if (depotDTO.getStockList().isEmpty()){
      System.out.println(" Sie haben noch keine Aktien in Ihrem Depot");
    }
    else {
      System.out.println("***** DAS VOLUMEN IHRES DEPOTS BETRÄGT: " + depotDTO.getTotalValue());
   depotDTO.getStockList().forEach((x) -> System.out.println("Aktienname " +x.getCompanyName() + " Symbol der Aktie " +x.getStockID_Symbol()+ " Anzahl die Sie von dieser Aktie  haben: "+ x.getSharesAmount()  ));
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
    String customerID = input.nextLine();
    try {
      Integer.parseInt(customerID);
    }
    catch (NumberFormatException e){
      System.out.println(" Bitte geben Sie eine Zahl für die Kundennummer ein -> Die Abfrage wird neu gestartet");
      buyStockForCostumer();
    }

    try {
      bank.checkIfUsrExists(Integer.parseInt(customerID));
    }
    catch (Exception e){
      System.out.println(e.getMessage());
      System.out.println("bitte führen Sie die komplette Eingabe erneut druch");
      buyStockForCostumer();
    }
    //List<StockDTO> stockList = new ArrayList<>();
    DepotDTO depotDTO = null;

    try {
      depotDTO = bank.getUserDepot(Integer.parseInt(customerID));
    }
    catch (Exception e){
      System.out.println("Depot konnte nicht abgerufen werden");
    }
    if (depotDTO.getStockList().isEmpty()){
      System.out.println("Es befinden sich noch kein Aktien in dem gesuchten Depot");
    }
    else {

      System.out.println("***** DAS VOLUMEN DES DEPOTS BETRÄGT: " + depotDTO.getTotalValue());
      depotDTO.getStockList().forEach((x) -> System.out.println("Aktienname " +x.getCompanyName() + " Symbol der Aktie " +x.getStockID_Symbol()+ " Anzahl die Sie von dieser Aktie  haben: "+ x.getSharesAmount()  ));

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

    private void checkBankVolume(){
      try{
        BigDecimal volume = bank.checkVolume();
        System.out.println(volume);


      }catch(Exception e){

        System.out.println("Something went wrong");
      }
      switch (bank.checkPersonRole()) {
        case "customer":
          customerMenu();
          break;
        case "employee":
          employeeMenu();
          break;
      }
    }
  private void setBank(){
    String user = bank.getID();
    try { bank.createBank();
      System.out.println("Die Bank wurde von " + user + " erstellt");}
    catch (Exception e){
      System.out.println(e.getMessage());
    }
  }
}



