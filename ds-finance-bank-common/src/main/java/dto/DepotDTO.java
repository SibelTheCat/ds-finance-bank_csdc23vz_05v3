package dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DepotDTO implements Serializable {
    private List<StockDTO> stockList;
    private BigDecimal totalValue;
    private int depotID;

    public DepotDTO() {
        this.stockList = new ArrayList<>();
        this.totalValue = new BigDecimal("0");
    }

    public List<StockDTO> getStockList() {
        return stockList;
    }

    public void setStockList(List<StockDTO> stockList) {
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
