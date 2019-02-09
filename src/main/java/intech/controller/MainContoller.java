package intech.controller;

import intech.model.Employee;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Valeev-RN on 08.02.2019.
 */

@Controller
@RequestMapping("/")
public class MainContoller {

    @RequestMapping(value = "/post",method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public List<Employee> postMyData() {
        System.out.println("POST2 8======>");
        List lst =new ArrayList();
        //впихнем данные, здесь должен быть сервис, который загрузить в массив сотрудников из файла
        lst.add(new Employee("1","2","3","4"));
        lst.add(new Employee("1","2","3","4"));
        lst.add(new Employee("1","2","3","4"));
        lst.add(new Employee("1","2","3","4"));

        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        final HttpEntity<String> entity = new HttpEntity<>("application/json", headers);
        final URI uri = restTemplate.postForLocation("http://localhost:8080/yosiyArt_war" + "/rs/orgs/01/invts/force", lst, List.class);
        return  lst;
    }
}
