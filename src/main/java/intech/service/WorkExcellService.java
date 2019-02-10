package intech.service;

import intech.model.Employee;
import intech.model.EmployeeAns;
import org.apache.poi.ss.usermodel.Workbook;

import java.util.List;

public interface WorkExcellService {
    public List<Employee> getEmployesFromExcell(Workbook workbook);

    public Workbook createReport(List<EmployeeAns> ans);
}
