package intech.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import intech.model.Employee;
import intech.model.EmployeeAns;
import intech.service.WorkExcellService;
import intech.view.ReportEsiaView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

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

    @Autowired
    RestTemplate restTemplate;

    @PostMapping(value = "/post")
    public ModelAndView postMyData(@RequestParam(value = "restText") String restText,
                                   @RequestParam(value = "host") String host,
                                   @RequestParam(value = "orgId") String orgId,
                                   @RequestParam(value = "rcOid", required = false) String rcOid,
                                   @RequestParam(value = "btnPostEmpl", required = false) String btnPostEmpl,
                                   @RequestParam(value = "btnPostToOrg", required = false) String btnPostToOrg
    ) {

        List<Employee> employees = new ArrayList<>();
        host = (host == null || host.isEmpty()) ? "https://esia-portal1.test.gosuslugi.ru" : host;
//        ObjectMapper mapper = new ObjectMapper();
//        try {
//            employees = mapper.readValue(restText, new TypeReference<List<Employee>>(){});
//        } catch (IOException e) {
//            e.printStackTrace();
//        }


        restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<String> entity = new HttpEntity<>(restText);

        String urlPost = host + "/rs/orgs/" + orgId + ((btnPostToOrg != null && !btnPostToOrg.isEmpty()) ? "/rcs/" + rcOid : "") + "/invts/force";

        ResponseEntity e2 = restTemplate.exchange(urlPost, HttpMethod.POST, entity, String.class);

        Map body = new HashMap();
        List<EmployeeAns> ans = new ArrayList<>();
        if (e2 != null && e2.getBody() != null && e2.getBody() instanceof Map)
            body = (Map) e2.getBody();

//        ans = body.get("results") != null ? (List<EmployeeAns>) body.get("results") : null;

        Object o1 = e2.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        JsonNode jsonNodeAns = null;
        try {
            jsonNode = objectMapper.readTree(restText);
            jsonNodeAns = objectMapper.readTree(o1.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        JsonNode messageNode = jsonNodeAns.get("message");
        JsonNode ansNode = jsonNodeAns.get("results");

        Map<String, String> ansMap = new HashMap<>();

        for (int i = 0; i < ansNode.size(); i++) {

            JsonNode answer = ansNode.get(i);
            ansMap.put(answer.get("snils").textValue(), answer.get("message").textValue());
        }


        System.out.println("POST 1 8======>" + ansMap.size());
        String snils = "";
        for (int i = 0; i < jsonNode.size(); i++) {
            JsonNode startNode = jsonNode.get(i);

            snils = startNode.get("snils").textValue();

            String name = startNode.get("name").textValue();
            String surname = startNode.get("surname").textValue();
            String middlename = startNode.get("middlename").textValue();
            String message = ansMap.get(snils);
            employees.add(new Employee(name, surname, middlename, snils, message));
        }


        ModelAndView model = new ModelAndView();
        model.addObject("empl", employees);
        model.setView(new ReportEsiaView());
        //System.out.println("POST 1 8======>" + model);
        return model;
    }

    /*@PostMapping(value = "/readExcel")
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
    }*/


    @GetMapping(value = "/get")
    public Object getMyHost(@RequestParam(value = "hostGet") String host) {


        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<String> entity = new HttpEntity<>("");
        String urlPost = host;

        ResponseEntity e2 = restTemplate.exchange(urlPost, HttpMethod.GET, entity, String.class);

        System.out.println(e2);

        return e2;
    }
}
