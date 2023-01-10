package springframework.project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LandingController {

    @GetMapping(value = "/")
    public String landing(){
        System.out.println(System.getProperty("java.version"));
        return System.getProperty("java.version");
    }
}
