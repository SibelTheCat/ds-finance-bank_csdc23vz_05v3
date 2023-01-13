package dto;

public class EmployeeDTO extends PersonDTO {

    public EmployeeDTO() {}

    public EmployeeDTO(int id, String firstName, String lastName, String password) {
        super(id, firstName, lastName, password);
    }
}
