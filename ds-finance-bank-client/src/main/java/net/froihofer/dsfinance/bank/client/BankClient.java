package net.froihofer.dsfinance.bank.client;

import java.util.Properties;
import java.util.Scanner;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import dto.CustomerDTO;
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
          createCustomer();
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
    getRmiProxy(id, password); // 21 : test --> customer
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
      System.out.println("Der Login erfolgt durch die zugewiesene Kunden Nummer und das erstellte Passwort");
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Etwas ist schiefgelaufen wenden Sie sich an den Technischen Support");
    }
  }

  private void customerMenu() {
    System.out.println("Customer Menu");
  }

  private void employeeMenu() {
    System.out.println("Employee Menu");
  }


  public static void main(String[] args) {
    BankClient client = new BankClient();
    client.run();
  }
}
