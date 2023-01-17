package dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class StockDTO implements Serializable {
    private String companyName;
    private int sharesAmount;
    private String stockID_Symbol;

    public StockDTO(){};

    public StockDTO(String companyName, int sharesAmount, String stockID_Symbol) {
        this.companyName = companyName;
        this.sharesAmount = sharesAmount;
        this.stockID_Symbol = stockID_Symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getStockID_Symbol() {
        return stockID_Symbol;
    }

    public void setStockID_Symbol(String stockID_Symbol) {
        this.stockID_Symbol = stockID_Symbol;
    }

    public int getSharesAmount() {
        return sharesAmount;
    }

    public void setSharesAmount(int sharesAmount) {
        this.sharesAmount = sharesAmount;
    }
}
