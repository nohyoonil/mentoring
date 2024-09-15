package kookmin.kookmin.controller.client;

import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.service.client.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReservationController {
    @Autowired
    private ReservationService reservationService;

    @PostMapping("/refund")
    public String refund(ReservationDto reservationDto, Model model){
        reservationService.refund(reservationDto);
        return "redirect:/mypage";
    }

    @GetMapping("/mypage/reviewWrite")
    public String reviewWrite(@RequestParam("reservationId") String id, Model model){
        ReservationDto reservationDto= reservationService.findById(id);
        model.addAttribute("r", reservationService.replaceFullDto(reservationDto));
        return "reviewWrite";
    }

    @PostMapping("/mypage/reviewSuccess")
    public String reviewSuccess(ReservationDto r){

        return "reviewWriteOk";
    }
}
