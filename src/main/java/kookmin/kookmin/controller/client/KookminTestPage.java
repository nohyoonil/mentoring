package kookmin.kookmin.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class KookminTestPage {

    @GetMapping("/")
    public String homePage() {
        return "index"; // home.html 파일을 의미
    }
}
