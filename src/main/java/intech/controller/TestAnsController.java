package intech.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import intech.model.EmployeeAns;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@Controller
@RequestMapping("/yosiyArt_war/rs/orgs")
public class TestAnsController {

    @RequestMapping(value = {"/{orgId}/invts/force", "/{orgId}/rcs/{orgId}//invts/force"}, method = RequestMethod.POST, produces = "application/json")
    @ResponseBody
    public Object registerEmployes(@PathVariable(value = "orgId") String orgId,
                                   @RequestParam(value = "rcOid", required = false) String rcOid,
                                   @RequestBody String employes) {
        List<LinkedHashMap> employees = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        try {

            employees = mapper.readValue(employes, List.class);

            System.out.println(employees);
//            JsonNode actualObj = mapper.readTree(employes);
//            System.out.println(actualObj);
        } catch (IOException e) {
            e.printStackTrace();
        }

        List lst = new ArrayList();
        int i = 0;
        for (LinkedHashMap e : employees) {
            i++;
            String message = "success";
            if (i % 5 == 0) {
                message = "not success";
            }
            lst.add(new EmployeeAns(e.get("snils").toString(), message));
        }
        System.out.println("---------------  " + lst.size());

        Map mav = new HashMap();
        mav.put("message", "Partial success");
        mav.put("results", lst);
        return mav;
    }
}
