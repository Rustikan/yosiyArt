package intech.model;

/**
 * Created by Valeev-RN on 08.02.2019.
 * <p>
 * Сотрудник
 */
public class Employee {

    private String name;
    private String surname;
    private String middleName;
    private String snils;
    private String message;

    public Employee() {
    }

    public Employee(String name, String surname, String middleName, String snils, String message) {
        this.name = name;
        this.surname = surname;
        this.middleName = middleName;
        this.snils = snils;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getSnils() {
        return snils;
    }

    public void setSnils(String snils) {
        this.snils = snils;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", middleName='" + middleName + '\'' +
                ", snils='" + snils + '\'' +
                '}';
    }
}
