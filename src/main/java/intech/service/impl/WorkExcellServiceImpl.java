package intech.service.impl;

import intech.model.Employee;
import intech.model.EmployeeAns;
import intech.service.WorkExcellService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Работа с экселем
 * Загрзука из файла в массив сотрудников
 * Формирование отчета по итогам
 */
@Service("workExcellService")
public class WorkExcellServiceImpl implements WorkExcellService {

    public List<Employee> getEmployesFromExcell(Workbook workbook) {
        List list = new ArrayList();

        list.add(new Employee("1", "2", "3", "4", "4"));
        list.add(new Employee("1", "2", "3", "3", "4"));
        list.add(new Employee("1", "2", "3", "2", "4"));
        list.add(new Employee("1", "2", "3", "1", "4"));

        return list;
    }

    public Workbook createReport(List<EmployeeAns> ans) {
        Workbook workbook = new HSSFWorkbook();

        Sheet sheet = workbook.createSheet("first");

        return workbook;
    }
}
