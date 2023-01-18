package persistence.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Depot implements Serializable {



    @OneToMany(targetEntity = Stock.class)
    private List<Stock> stockList;
    private BigDecimal totalValue;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int depotID;


    public Depot() {
  //  this.stockList = new ArrayList<>();
  //  this.totalValue= new BigDecimal("0");
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    public BigDecimal getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(BigDecimal totalValue) {
        this.totalValue = totalValue;
    }

    public int getDepotID() {
        return depotID;
    }

    public void setDepotID(int depotID) {
        this.depotID = depotID;
    }

}
