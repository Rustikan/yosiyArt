package intech.view;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.servlet.view.document.AbstractXlsxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by Valeev-RN on 12.02.2019.
 */
public class RepostEsiaView extends AbstractXlsxView {
    @Override
    protected void buildExcelDocument(Map<String, Object> map, Workbook workbook, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        httpServletResponse.setHeader("Content-Disposition", "attachment;filename=\"" + "someFile".toString() + ".xlsx\"");
        httpServletResponse.setContentType("application/octet-stream");

        Sheet sheet = workbook.createSheet("first");

        int rowindex = 0;
        Row row = sheet.createRow(rowindex++);
        Cell cell = row.createCell(0);
        cell.setCellValue("Отчет по повышениям приоритетов за ");
    }
}
