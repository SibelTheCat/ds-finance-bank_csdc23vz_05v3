package service;




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

        public BigDecimal buyStock(String symbol, int shares) throws Exception {

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


    //////////////
    ////////////
    // METHODS WITH OUTPUT /////////

//    /**  Get StockQuotes (Symbol, Company Name, last known trade price)*/
//    public Boolean getStockByCompanyNamelWithOutput(String companyName) {
//        try {
//            List<PublicStockQuote> stocks = getStockByCompanyName(companyName);
//            if (stocks.isEmpty()) {
//                System.out.println("Keine Firma zu der Eingabe \"" + companyName + "\" gefunden");
//                return false;
//            } else {
//                stocks.forEach((x) -> System.out.println("Symbol: " + x.getSymbol() + " /Company Name: " + x.getCompanyName() + " /Last known trade-price: " + x.getLastTradePrice() + " /Shares left: " + x.getFloatShares()));
//                return true;
//            }
//        } catch (
//                Exception e) {
//            System.out.println("Die Suchabfrage hat nicht gekplappt");
//            return false;
//        }
//    }
//
//    /** Buy Shares of Stock
//     * first check if symbol exists
//     * then if enough shares are avaiable*/
//    public BigDecimal buyStockWithOutput(String symbol, int shares) throws Exception {
//        BigDecimal pricePerShare = BigDecimal.valueOf(0);
//        PublicStockQuote quote;
//
//        //check if Symbol exists, if not Message + return value = 0;
//        try {
//           quote = getStockBySymbol(symbol);
//        } catch (Exception e) {
//            System.out.println("Es wurden leider keine Aktien zu \"" + symbol + "\"  gefunden");
//            return pricePerShare;
//        }
//        //check if Stock has enough shares left
//        //check fist if avaiable stocks are "null"
//        if(quote.getFloatShares() == null){
//            System.out.println("Aktien kaufen: Es sind leider nicht genug Shares übrig. Shares die noch für die Firma " + quote.getCompanyName() +" verfügbar sind :"+ quote.getFloatShares());
//            return pricePerShare;
//        }
//        // then if the wanted shares are too much
//        if(quote.getFloatShares().intValue() < shares){
//            System.out.println("Aktien kaufen: Es sind leider nicht genug Shares übrig. Shares die noch für die Firma " + quote.getCompanyName() +" verfügbar sind :"+ quote.getFloatShares());
//            return pricePerShare;
//        }
//
//        try {
//            pricePerShare = buyStock(symbol, shares);
//            System.out.println("Sie haben " + shares + " Shares von der Aktie " + quote.getCompanyName() + " für je " + pricePerShare + " gekauft");
//            return pricePerShare;
//
//        } catch (Exception e) {
//            System.out.println("Stock konnte leider nicht gekauft werden, da nicht mehr genug Shares zur Verfügung stehen.");
//            return pricePerShare;
//        }
//    }
//
//    /** Sell Shares of Stock
//     * first check if symbol exists */
//
//    public BigDecimal sellStockWithOutput(String symbol, int shares) throws Exception {
//        BigDecimal pricePerShareSell= BigDecimal.valueOf(0);
//        //check if Symbol exists, if not Message + return value = 0;
//        try {
//            getStockBySymbol(symbol);
//        } catch (Exception e) {
//            System.out.println("Aktien verkaufen: Es wurden leider keine Aktien zu \"" + symbol + "\"  gefunden");
//            return pricePerShareSell;
//        }
//        try {
//            pricePerShareSell = sellStock(symbol, shares);
//            System.out.println("Sie haben " + shares + " Shares der Aktie " + getStockBySymbol(symbol).getCompanyName() + " für je " + pricePerShareSell + " verkauft");
//            return pricePerShareSell;
//        } catch (Exception e) {
//            System.out.println("Transaktion hat leider nicht geklappt");
//            return pricePerShareSell;
//        }
//    }
//
//    public PublicStockQuote getStockBySymbolWithOutput(String symbol) throws Exception {
//    PublicStockQuote stockQuoteToGetName;
//        try
//    {
//        stockQuoteToGetName = getStockBySymbol(symbol);
//        System.out.println("Der Name der Firma zum eingegebenen Symbol \"" + symbol+ "\"  ist: " + stockQuoteToGetName.getCompanyName());
//        return stockQuoteToGetName;
//    } catch (Exception e) {
//        System.out.println("Es wurden leider keine Aktien zum Symbol \"" + symbol+ "\"  gefunden");
//        return null;
//    }}
}

