package persistence.dao;

import persistence.entity.Bank;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;

public class BankDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public Bank createBank(BigDecimal bankVolume) {
        Bank bank = new Bank(bankVolume);
        bank.setBankVolume(bankVolume);
        entityManager.persist(bank);
        entityManager.flush();
        return bank;
    }

    public void persist(Bank bank){entityManager.persist(bank);}


    public Bank findById(int id) {
        return entityManager.find(Bank.class, id);
    }

    public Bank getBank(){
        return entityManager.createQuery("SELECT b FROM Bank b where b.id = :id", Bank.class)
                .setParameter("id", 1).getSingleResult();
    }

    public void update(Bank bank) {
        entityManager.merge(bank);
    }

    public void delete(Bank bank) {
        entityManager.remove(bank);
    }

    public BigDecimal getBankVolume(int id) {
        Bank bank = entityManager.find(Bank.class, id);
        return bank.getBankVolume();
    }
}
