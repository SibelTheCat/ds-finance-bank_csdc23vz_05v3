package dto;

import java.math.BigDecimal;

public class BankDTO {

    private BigDecimal bankVolume;
    private int bankId;

    public BankDTO() {}

    public BankDTO(BigDecimal bankVolume) {
        this.bankVolume = bankVolume;
    }

    public BigDecimal getBankVolume() {
        return bankVolume;
    }

    public void setBankVolume(BigDecimal bankVolume) {
        this.bankVolume = bankVolume;
    }

    public int getBankId() {
        return bankId;
    }

    public void setBankId(int bankId) {
        this.bankId = bankId;
    }
}

