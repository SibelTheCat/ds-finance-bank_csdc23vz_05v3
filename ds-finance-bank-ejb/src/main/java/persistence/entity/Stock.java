package persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.math.BigDecimal;
@Entity
public class Stock implements Serializable {
    private String companyName;
    @JoinColumn(name="DEPOT_FK")

    private int sharesAmount;
    @Id
    private String stockID_Symbol;

    public Stock(String companyName, int sharesAmount, String stockID_Symbol) {
        this.companyName = companyName;
        this.sharesAmount = sharesAmount;
        this.stockID_Symbol = stockID_Symbol;
    }

    public Stock() {

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
