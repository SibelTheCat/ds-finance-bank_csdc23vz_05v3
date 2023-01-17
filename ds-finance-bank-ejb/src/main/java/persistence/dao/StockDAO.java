package persistence.dao;

import persistence.entity.Stock;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

public class StockDAO {
    @PersistenceContext
    private EntityManager entityManager;



    public Stock createNewStockInDepot(String companyName, int sharesAmount, String stockID_Symbol) {
        Stock stock = new Stock(companyName, sharesAmount, stockID_Symbol);
        entityManager.persist(stock);
        return stock;
    }

    public void updateSharesAmount(Stock stock, int newAmount){
        stock.setSharesAmount(newAmount);
        entityManager.merge(stock);
    }

    public Stock findStockByStockID_Symbol(String stockID_Symbol) {
        return entityManager.find(Stock.class, stockID_Symbol);
    }

    public List<Stock> findAllStocksInDepot() {
        return entityManager.createQuery("SELECT s FROM Stock s", Stock.class).getResultList();
    }


    public void delete(String stockId) {
        Stock stock = entityManager.find(Stock.class, stockId);
        entityManager.remove(stock);
    }
}
