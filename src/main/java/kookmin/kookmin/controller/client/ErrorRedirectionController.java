package kookmin.kookmin.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorRedirectionController {
//  공통 에러페이지
    @GetMapping("/commonError")
    public String error() {
        return "errorMsg/commonError";
    }

    @GetMapping("/errorMailFailed")
    public String errorMailFailed() {
        return "errorMsg/errorMailFailed";
    }
}
