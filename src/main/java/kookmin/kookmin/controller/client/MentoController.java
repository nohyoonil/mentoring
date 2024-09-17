package kookmin.kookmin.controller.client;

import jakarta.servlet.http.HttpSession;
import kookmin.kookmin.dto.client.MentoDto;
import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.dto.client.UserDto;
import kookmin.kookmin.service.client.MentoService;
import kookmin.kookmin.service.client.ReservationService;
import kookmin.kookmin.service.client.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class MentoController {
    @Autowired
    private MentoService mentoService;

    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/mento")
    public String mentoInfo(@RequestParam("mentoId") String id, Model model, HttpSession session) {
        //임시
        String aid = "e64a76aa-6dba-11ef-8422-4ccc6a800e07";

        MentoDto m = mentoService.findByMentoId(aid);
        model.addAttribute("mento", m);
        model.addAttribute("mentoU", userService.findByUserId(m.getUserId()));
        model.addAttribute("mentoInfoNums", mentoService.mentoNumInfo(aid));
        model.addAttribute("mentoActCampus", mentoService.findActByMentoId(aid));
        session.removeAttribute("r");

        return "mentoInfo";
    }
}