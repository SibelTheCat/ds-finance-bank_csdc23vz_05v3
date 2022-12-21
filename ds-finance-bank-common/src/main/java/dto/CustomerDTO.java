package dto;

public class CustomerDTO extends PersonDTO {

    private String address;

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
}
