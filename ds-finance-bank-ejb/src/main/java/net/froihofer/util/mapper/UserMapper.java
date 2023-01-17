package net.froihofer.util.mapper;

import dto.CustomerDTO;
import dto.DepotDTO;
import dto.EmployeeDTO;
import dto.StockDTO;
import persistence.entity.Customer;
import persistence.entity.Depot;
import persistence.entity.Employee;
import persistence.entity.Stock;

import java.util.ArrayList;
import java.util.List;

public class UserMapper {

    public static CustomerDTO customerToDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getAddress());
        customerDTO.setAddress(customer.getAddress());
        customerDTO.setPassword(customer.getPassword());
        customerDTO.setId(customer.getId());

        /** neu
         *
         */
        customerDTO.setDepot(depotToDTO(customer.getDepot()));


        return customerDTO;
    }

    public static Customer DtoToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        customer.setFirstName(customerDTO.getFirstName());
        customer.setLastName(customerDTO.getLastName());
        customer.setAddress(customerDTO.getAddress());
        customer.setPassword(customerDTO.getPassword());
        customer.setId(customerDTO.getId());

        /** neu
         *
         */
        customer.setDepot(DTO_ToDepot(customerDTO.getDepot()));

        return customer;
    }

    public static EmployeeDTO employeeToDTO(Employee employee) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setFirstName(employee.getFirstName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setPassword(employee.getPassword());
        employeeDTO.setId(employee.getId());

        return employeeDTO;
    }

    /**
     *
     */

    public static DepotDTO depotToDTO(Depot depot){
        DepotDTO depotDTO = new DepotDTO();
        depotDTO.setDepotID(depot.getDepotID());
        depotDTO.setStockList(helperFunktionForList((depot.getStockList())));
        depotDTO.setTotalValue(depot.getTotalValue());

        return depotDTO;
    }

    public static Depot DTO_ToDepot(DepotDTO depotDTO){
        Depot depot = new Depot();
        depot.setDepotID(depotDTO.getDepotID());
        depot.setTotalValue(depotDTO.getTotalValue());
        depot.setStockList(helperFunktionForList2(depotDTO.getStockList()));

        return depot;
    }

    public static List<StockDTO> helperFunktionForList(List<Stock> stockList){
        List<StockDTO> stockDTOList = new ArrayList<>();
        stockList.forEach((x)-> stockDTOList.add(stockToDTO(x)));

        return stockDTOList;
    }

    public static List<Stock> helperFunktionForList2(List<StockDTO> stockDTOList){
        List<Stock> stockList = new ArrayList<>();
        stockDTOList.forEach((x)-> stockList.add(DTO_ToStock(x)));

        return stockList;
    }

    public static StockDTO stockToDTO(Stock stock){
        StockDTO stockDTO = new StockDTO();
        stockDTO.setStockID_Symbol(stock.getStockID_Symbol());
        stockDTO.setCompanyName(stock.getCompanyName());
        stockDTO.setSharesAmount(stock.getSharesAmount());

        return stockDTO;
    }

    public static Stock DTO_ToStock(StockDTO stockDTO){
        Stock stock = new Stock();
        stock.setStockID_Symbol(stockDTO.getStockID_Symbol());
        stock.setCompanyName(stockDTO.getCompanyName());
        stock.setSharesAmount(stockDTO.getSharesAmount());
        return stock;
    }
}
