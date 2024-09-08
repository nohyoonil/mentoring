package kookmin.kookmin.controller.client;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class KookminTestPage {
//
    @GetMapping("/")
    public String homePage() {
        return "index"; // home.html 파일을 의미
    }
//
//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
//
//    @GetMapping("/signup")
//    public String signup() {
//        return "signup";
//    }
//
//    @GetMapping("/mentoInfo")
//    public String mentoInfo()
//    {
//
//        return "mentoInfo";
//    }
//
//    @GetMapping("/mypage")
//    public String mypage() {
//        return "mypage";
//    }
//
//    @GetMapping("/reservationOk")
//    public String reservationOk() {
//        return "reservationOk";
//    }
//
//    @GetMapping("/reservationStep1")
//    public String reservationStep1() {
//        return "reservationStep1";
//    }
//
//    @GetMapping("/reservationStep2")
//    public String reservationStep2() {return "reservationStep2";}
//
//    @GetMapping("/reservationStep3")
//    public String reservationStep3() {return "reservationStep3";}
//
//    @GetMapping("/reviewWrite")
//    public String reviewWrite() {return "reviewWrite";}
//
//    @GetMapping("/reviewWriteOk")
//    public String reviewWriteOk() {return "reviewWriteOk";}

}
