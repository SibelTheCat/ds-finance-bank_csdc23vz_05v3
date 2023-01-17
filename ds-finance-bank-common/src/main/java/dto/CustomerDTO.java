package dto;

public class CustomerDTO extends PersonDTO {

    private String address;

    private DepotDTO depot;

    public CustomerDTO(String address) {
        this.address = address;
    }

    public CustomerDTO(int id, String firstName, String lastName, String address, String password) {
        super(id, firstName, lastName, password);
        this.address = address;
    }

    public CustomerDTO() {}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DepotDTO getDepot() {
        return depot;
    }

    public void setDepot(DepotDTO depot) {
        this.depot = depot;
    }
}
