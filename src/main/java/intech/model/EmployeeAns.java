package intech.model;

public class EmployeeAns {
    private String snils;
    private String message;

    public EmployeeAns() {
    }

    public EmployeeAns(String snils, String message) {
        this.snils = snils;
        this.message = message;
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
}
