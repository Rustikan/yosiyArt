package intech.view;

import intech.model.Employee;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by Valeev-RN on 12.02.2019.
 */
public class RepostEsiaView extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse response) throws Exception {
        //response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment;filename=reportGosUs.xlsx");

        Sheet sheet = workbook.createSheet("Отчет");
        Sheet sheet2 = workbook.createSheet("Отчет по неудачным");

        setCellWidth(sheet);
        setCellWidth(sheet2);

        int rowindex = 0;
        int rowindex2 = 0;
        Row row = sheet.createRow(rowindex++);
        Cell cell = row.createCell(0);
        cell.setCellValue("Отчет по загруженным сотрудникам ");

        row = sheet2.createRow(rowindex2++);
        cell = row.createCell(0);
        cell.setCellValue("Отчет по незагруженным сотрудникам ");

        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));
        sheet2.addMergedRegion(new CellRangeAddress(0, 0, 0, 4));

        List<Employee> employees = (List<Employee>) map.get("empl");
        int notLoad=0;
        System.out.println("{ | } - > " + employees.size());
        rowindex=3;
        if (employees != null && !employees.isEmpty()) {

            for (Employee employee : employees) {
                rowindex = createRowToSheet(sheet, rowindex, employee);

                if (!"success".equals(employee.getMessage())){
                    notLoad++;
                    rowindex2 = createRowToSheet(sheet2, rowindex2, employee);
                }
            }
        }

        row = sheet.createRow(1);
        cell = row.createCell(0);
        cell.setCellValue("Всего сотрудников:");

        cell = row.createCell(1);
        cell.setCellValue(employees.size());

        row = sheet.createRow(2);
        cell = row.createCell(0);
        cell.setCellValue("Из них не загружено: ");

        cell = row.createCell(1);
        cell.setCellValue(notLoad);
    }

    private void setCellWidth(Sheet sheet) {
        sheet.setColumnWidth(0, 6000);
        sheet.setColumnWidth(1, 6000);
        sheet.setColumnWidth(2, 6000);
        sheet.setColumnWidth(3, 6000);
        sheet.setColumnWidth(4, 6000);
    }

    private int createRowToSheet(Sheet sheet, int rowindex, Employee employee) {
        int cellIndex = 0;
        Row row;
        Cell cell;
        row = sheet.createRow(rowindex++);
        cell = row.createCell(cellIndex++);
        cell.setCellValue(employee.getSnils());

        cell = row.createCell(cellIndex++);
        cell.setCellValue(employee.getMessage());

        cell = row.createCell(cellIndex++);
        cell.setCellValue(employee.getSurname());

        cell = row.createCell(cellIndex++);
        cell.setCellValue(employee.getName());

        cell = row.createCell(cellIndex++);
        cell.setCellValue(employee.getMiddleName());
        return rowindex;
    }
}
