package net.froihofer.service;




import net.froihofer.dsfinance.ws.trading.PublicStockQuote;
import net.froihofer.dsfinance.ws.trading.TradingWSException_Exception;
import net.froihofer.dsfinance.ws.trading.TradingWebService;
import net.froihofer.dsfinance.ws.trading.TradingWebServiceService;

import javax.annotation.PostConstruct;
import javax.annotation.security.PermitAll;
import javax.ejb.Stateless;
import javax.xml.ws.BindingProvider;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Stateless(name="Stockmarket")
@PermitAll
public class Stockmarket {


    TradingWebService proxy;

    @PostConstruct
    public void initService() {
        TradingWebServiceService service = new TradingWebServiceService();
        //TradingWSException_Exception exception = new TradingWSException_Exception();

        try {
           proxy = service.getTradingWebServicePort();

            BindingProvider bindingProvider = (BindingProvider) proxy;
            bindingProvider.getRequestContext().put(
                    BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                    "https://edu.dedisys.org/ds-finance/ws/TradingService");
            bindingProvider.getRequestContext().put(
                    BindingProvider.USERNAME_PROPERTY, "csdc23vz_05");
            bindingProvider.getRequestContext().put(
                    BindingProvider.PASSWORD_PROPERTY, "EeCei1A");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     /**  @WSDLDocumentation(value="Returns a list of the first 100 stock quotes where the company name contains the string specified in the \'partOfCompanyName\' parameter")
     * public List<PublicStockQuote> findStockQuotesByCompanyName(String partOfCompanyName)
     */
    public List<PublicStockQuote> getStockByCompanyName(String companyName) throws Exception {
        try {
            return proxy.findStockQuotesByCompanyName(companyName);
        } catch (TradingWSException_Exception e) {
            throw new Exception();
        }
    }


    /**
     *  First checks if there are enough stocks for the quotes identified by the 'symbols' parameter.
     * if yes ->                                                                                                                            throws TradingWSException
     * Buys shares and returns the price per share effective for the buying transaction.
     * Parameters:
     * symbol - the symbol identifying a specific share
     * shares - the number of shares to buy.
     * Returns:
     * the price per share.
     * Throws:
     * TradingWSException - in case of errors.
     */

        public BigDecimal buyStock( String symbol, int shares) throws Exception {

            try{
                return proxy.buy(symbol, shares);
            } catch (TradingWSException_Exception e) {
        throw new Exception();
            }}

    /**
     * @WSDLDocumentation(value="Sells shares and returns the price per share effective for the selling transaction.")
     * public BigDecimal sell(String symbol, int shares)
     * Parameters:
     * symbol - the symbol identifying the share.
     * shares - the number of shares to sell
     * Returns:
     * the price per share effective for the transaction.
     * Throws:
     * TradingWSException - in case of errors.
     */
    public BigDecimal sellStock(String symbol, int shares) throws Exception {
        try {
            return proxy.sell(symbol, shares);
        } catch (TradingWSException_Exception e) {
            throw new Exception("Transaktion hat nicht geklappt)");
        }
    }

    /**
     *@WSDLDocumentation(value="Returns the stock quotes for the stock options identified by the \'symbols\' parameter.")
     * public List<PublicStockQuote> getStockQuotes(List<String> symbols)
     *                                                                                                                                                            throws TradingWSException
     * Returns the stock quotes for the stock options identified by the 'symbols' parameter.
     * Parameters:
     * symbols - list of symbols identifying the stocks for which
     * Returns:
     * the stock quotes corresponding to the 'symbols' parameter.
     * Throws:
     * TradingWSException - in case of errors.
     */
    public PublicStockQuote getStockBySymbol(String symbol) throws Exception {
        List<String> symbolsList = new ArrayList<>();
        symbolsList.add(symbol);
        List<PublicStockQuote> stock = null;
        try {
            stock = proxy.getStockQuotes(symbolsList);
            return stock.get(0);
        } catch (TradingWSException_Exception e) {
            throw new Exception("Keine Stocks für dieses Symbol gefunden)");
        }
    }

}

