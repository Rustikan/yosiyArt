package intech.controller;

import intech.model.Employee;
import intech.model.EmployeeAns;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/rs/orgs")
public class TestAnsController {

    @RequestMapping(value = "/01/invts/force", method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object registerEmployes(@RequestBody ArrayList<Employee> employes) {
        List lst = new ArrayList();

        for (Employee e : employes) {
            lst.add(new EmployeeAns(e.getSnils(), "success"));
        }
        System.out.println("ans -> " + lst);
        Map mav = new HashMap();
        mav.put("message", "Partial success");
        mav.put("results", lst);
        return mav;
    }
}
