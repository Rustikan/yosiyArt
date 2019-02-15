package intech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Valeev-RN on 15.02.2019.
 */
@Controller
@RequestMapping(value = "/auth")
public class EsiaController {

    @Autowired
    RestTemplate restTemplate;

    public Object getToken(){



        return null;
    }
}
