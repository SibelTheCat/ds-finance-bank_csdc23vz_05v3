package net.froihofer.util.mapper;

import dto.BankDTO;
import persistence.entity.Bank;

public class BankMapper {

    public static BankDTO bankToDTO(Bank bank) {
        BankDTO bankDTO = new BankDTO();
        bankDTO.setBankVolume(bank.getBankVolume());
        bankDTO.setBankId(bank.getBankID());
        return bankDTO;
    }

    /*public static Bank DTOtoBank(BankDTO bankDTO) {
        Bank bank = new Bank();
        bank.setBankVolume(bankDTO.getBankVolume());
        bank.setBankID(bankDTO.getBankId());
        return bank;
    }*/
}