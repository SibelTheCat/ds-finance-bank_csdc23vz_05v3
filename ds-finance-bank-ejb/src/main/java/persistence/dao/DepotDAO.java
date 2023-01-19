package persistence.dao;

import persistence.entity.Depot;
import persistence.entity.Stock;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DepotDAO {
    @PersistenceContext
    private EntityManager entityManager;


    public Depot createDepot() {
        List<Stock> stockList = new ArrayList<>();
        Depot depot = new Depot();
        //depot.setTotalValue(new BigDecimal(0));
        depot.setStockList(stockList);
        entityManager.persist(depot);
        return depot;
    }

    public Depot findById(int id) {
        return entityManager.find(Depot.class, id);
    }


    public void update(Depot depot) {
        entityManager.merge(depot);
    }


    public void delete(Depot depot) {
        entityManager.remove(depot);
    }
}
