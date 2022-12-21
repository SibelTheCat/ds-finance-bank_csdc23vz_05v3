package service;

import net.froihofer.dsfinance.ws.trading.PublicStockQuote;

import java.math.BigDecimal;

public class Main {
    public static void main(String[] args) throws Exception {

        Stockmarket st = new Stockmarket();
        st.initService();

        /**  Get StockQuotes (Symbol, Company Name, last known trade price)*/
        System.out.println("////Get StockQuotes by company Name (or letters in the company name) : Symbol, Company Name, last known trade price\n");
        String companyName = "a";
        // if something went wrong: didItWork = false
        Boolean didItWork = st.getStockByCompanyNamelWithOutput(companyName);


        System.out.println("\n////Buy Shares of Stock\n");


       /** Buy Shares of Stock
        * first check if symbol exists
        * then if enough shares are avaiable*/
        String symbol = "AACC";
        int shares = 5002001;
        int shares2 = 2;
       // if something went wrong pricePerShaere = 0
        BigDecimal pricePerShare = st.buyStockWithOutput(symbol, shares);
        pricePerShare = st.buyStockWithOutput(symbol, shares2);



        System.out.println("\n////Sell Shares of Stock\n");


        /** Sell Shares of Stock */
        BigDecimal pricePerShareSell;

        int sharesSell = 1;

        //symbol gibt es nicht
        String symbolSell = "kkkk";
        pricePerShareSell= st.sellStockWithOutput(symbolSell, sharesSell);

        //symbol gibt es aber die Bank hat nicht genug Shares von dieser Aktie: Exception wird geworfen, Ausgabe:
        //Transaktion hat leider nicht geklappt
        String symbolSell2 = "AC";
        pricePerShareSell= st.sellStockWithOutput(symbolSell2, sharesSell);

        // Test zuerst kaufen dann verkaufen:
        st.buyStockWithOutput(symbolSell2, sharesSell);
        st.sellStockWithOutput(symbolSell2, sharesSell);


        System.out.println(" \n////Find Stock by Symbol \n");

        /** Find Stock by Symbol */
        // if none is found null got returned

        PublicStockQuote stockFromSymbol;

        //gibt es nicht:
        String symbolToGetStockname = "hhhhjhkh";
        stockFromSymbol = st.getStockBySymbolWithOutput(symbolToGetStockname);

        //gibt es:
        String symbolToGetStockname2 = "ACAB";
        stockFromSymbol = st.getStockBySymbolWithOutput(symbolToGetStockname2);


    }
}
