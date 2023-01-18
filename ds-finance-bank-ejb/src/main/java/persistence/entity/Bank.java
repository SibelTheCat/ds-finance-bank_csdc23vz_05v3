package persistence.entity;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;


@Entity
public class Bank implements Serializable{

    @Id
    private int bankID = 1;
    private BigDecimal bankVolume;


    public Bank(){}
    public Bank(BigDecimal bankVolume) {
        this.bankVolume = bankVolume;
    }

    public BigDecimal getBankVolume() {return bankVolume;
    }
    public void setBankVolume(BigDecimal bankVolume) {
        this.bankVolume = bankVolume;
    }

    public int getBankID() {
        return bankID;
    }

    public void setBankID(int bankID) {
        this.bankID = bankID;
    }
}


