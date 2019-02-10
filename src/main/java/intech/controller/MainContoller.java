package intech.controller;

import intech.model.EmployeeAns;
import intech.service.WorkExcellService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Valeev-RN on 08.02.2019.
 */

@Controller
@RequestMapping("/")
public class MainContoller {

    @Autowired
    WorkExcellService excellService;

    @RequestMapping(value = "/post", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object postMyData() {
        System.out.println("POST 1 8======>");
        ArrayList lst = (ArrayList) excellService.getEmployesFromExcell(new HSSFWorkbook());
        //впихнем данные, здесь должен быть сервис, который загрузит в массив сотрудников из файла


        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<ArrayList> entity = new HttpEntity<>(lst);
        String urlPost = "http://localhost:8080/yosiyArt_war" + "/rs/orgs/01/invts/force";
        ResponseEntity e2 = restTemplate.exchange(urlPost, HttpMethod.POST, entity, Object.class);

        System.out.println("POST 2 8======>" + e2);

        Map body = new HashMap();
        List<EmployeeAns> ans = new ArrayList<>();
        if (e2 != null && e2.getBody() != null && e2.getBody() instanceof Map)
            body = (Map) e2.getBody();

        ans = body.get("results") != null ? (List<EmployeeAns>) body.get("results") : null;



        return excellService.createReport(ans);
    }
}
