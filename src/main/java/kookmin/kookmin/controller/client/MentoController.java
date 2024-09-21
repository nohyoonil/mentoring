package kookmin.kookmin.controller.client;

import jakarta.servlet.http.HttpSession;
import kookmin.kookmin.dto.client.ActDto;
import kookmin.kookmin.dto.client.MentoDto;
import kookmin.kookmin.dto.client.UserDto;
import kookmin.kookmin.service.client.MentoService;
import kookmin.kookmin.service.client.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class MentoController {
    @Autowired
    private MentoService mentoService;

    @Autowired
    private UserService userService;

    @GetMapping("/mento")
    public String mentoInfo(@RequestParam("mentoId") String id, Model model, HttpSession session) {
        MentoDto m = mentoService.findByMentoId(id);
        UserDto mentoUserInfo = userService.findByUserId(m.getUserId());
        Map<String, Integer> mentoInfoNums =  mentoService.mentoNumInfo(id);
        Map<Boolean, List<ActDto>> mentoActCampus =  mentoService.findActByMentoId(id);

        model.addAttribute("mento", m);
        model.addAttribute("mentoU", mentoUserInfo);
        model.addAttribute("mentoInfoNums", mentoInfoNums);
        model.addAttribute("mentoActCampus", mentoActCampus);
        session.removeAttribute("r");

        return "mentoInfo";
    }
}