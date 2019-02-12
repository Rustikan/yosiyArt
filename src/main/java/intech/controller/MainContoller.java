package intech.controller;

import intech.model.Employee;
import intech.model.EmployeeAns;
import intech.service.WorkExcellService;
import intech.view.RepostEsiaView;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Valeev-RN on 08.02.2019.
 * https://esia-portal1.test.gosuslugi.ru
 */

@Controller
@RequestMapping("/yosiyArt_war")
public class MainContoller {

    @Autowired
    WorkExcellService excellService;

    @PostMapping(value = "/post", produces = "application/json")
    public ModelAndView postMyData(@RequestBody ArrayList<Employee> lst,
                                   HttpServletRequest request) {
        System.out.println("POST 1 8======>" + lst);
//        ArrayList lst = (ArrayList) excellService.getEmployesFromExcell(new HSSFWorkbook());
        //впихнем данные, здесь должен быть сервис, который загрузит в массив сотрудников из файла

        ModelAndView model = new ModelAndView();

        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<ArrayList<Employee>> entity = new HttpEntity<>(lst);
        String urlPost = "http://localhost:8081/yosiyArt_war" + "/rs/orgs/01/invts/force";
        ResponseEntity e2 = restTemplate.exchange(urlPost, HttpMethod.POST, entity, Object.class);

        System.out.println("POST 2 8======>" + e2);

        Map body = new HashMap();
        List<EmployeeAns> ans = new ArrayList<>();
        if (e2 != null && e2.getBody() != null && e2.getBody() instanceof Map)
            body = (Map) e2.getBody();

        ans = body.get("results") != null ? (List<EmployeeAns>) body.get("results") : null;

        model.setView(new RepostEsiaView());
        System.out.println("POST 1 8======>" + model);
        return model;
    }

    @PostMapping(value = "/readExcel")
    public Object readExcel(@RequestParam("result") MultipartFile uploadItem, ModelAndView model,
                            HttpServletResponse response) {
        List<Employee> employees = new ArrayList<>();

        ByteArrayInputStream bis = null;
        Workbook workbook;
        //  проверим можем ли считать загружаемый файл
        try {
            bis = new ByteArrayInputStream(uploadItem.getBytes());
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return model.addObject("I can not read this file");
        }
        // проверим подходит ли расширение
        try {
            if (uploadItem.getOriginalFilename().endsWith("xls")) {
                workbook = new HSSFWorkbook(bis);
            } else if (uploadItem.getOriginalFilename().endsWith("xlsx")) {
                workbook = new XSSFWorkbook(bis);
            } else {
                throw new IllegalArgumentException("The File is not Excel format");
            }

            employees = excellService.getEmployesFromExcell(workbook);
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return model.addObject("The File is not Excel format");
        }
        return employees;
    }
}
