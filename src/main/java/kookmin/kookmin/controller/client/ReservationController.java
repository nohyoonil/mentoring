package kookmin.kookmin.controller.client;

import jakarta.servlet.http.HttpSession;
import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.dto.client.UserDto;
import kookmin.kookmin.service.client.ReservationService;
import kookmin.kookmin.service.client.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

@Controller
public class ReservationController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private UserService userService;

    @PostMapping("/refund")
    public String refund(ReservationDto reservationDto, Model model){
        reservationService.refund(reservationDto);
        return "redirect:/mypage";
    }

    @GetMapping("/mypage/reviewWrite")
    public String reviewWrite(@RequestParam("reservationId") String id, Model model, HttpSession session){
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if(loginUser == null){
            return "redirect:/userInfoEnd";
        }
        ReservationDto reservationDto= reservationService.findById(id);
        model.addAttribute("r", reservationService.replaceFullDto(reservationDto));
        return "reviewWrite";
    }

    @PostMapping("/mypage/reviewSuccess")
    public String reviewSuccess(ReservationDto reservationDto){
        reservationService.review(reservationDto);
        return "reviewWriteOk";
    }

    @GetMapping("/reservation/{step}")
    public String reservationForm(@RequestParam(name = "mentoId", defaultValue = "null") String mentoId, @PathVariable int step, HttpSession session, Model model){
        System.out.println("Get 동작함 : "+step);
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        model.addAttribute("mentoId", mentoId);
        if(loginUser == null){
            return "redirect:/userInfoEnd";
        }
        return switch (step) {
            case 1 -> "reservationStep1";
            case 2 -> "reservationStep2";
            case 3 -> "reservationStep3";
            default -> "reservationOk";
        };
    }
    @PostMapping("/reservation/{step}")
    public String reservationSubmit(
            @PathVariable int step,
            HttpSession session,
            UserDto InputUserDto,
            String mentoId,
            @RequestParam(name = "desiredDateDay1", defaultValue = "null") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desiredDateDay1,
            @RequestParam(name = "desiredDateTime1", defaultValue = "null") @DateTimeFormat(pattern="HH:mm") LocalTime desiredDateTime1,
            @RequestParam(name = "desiredDateDay2", defaultValue = "null") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desiredDateDay2,
            @RequestParam(name = "desiredDateTime2", defaultValue = "null") @DateTimeFormat(pattern="HH:mm") LocalTime desiredDateTime2,
            ReservationDto InputReservation,
            Model model)
    {
        System.out.println("Post 동작함 : "+step);
        String loginUserId = ((UserDto) session.getAttribute("loginUser")).getUserId();
        ReservationDto reservationDto= new ReservationDto();
        model.addAttribute("mentoId", mentoId);

        switch (step){
            case 1 -> {
                System.out.println("Step1 Post 동작하는 중임");
                LocalDateTime ldt1 = LocalDateTime.of(desiredDateDay1, desiredDateTime1);
                Date d1 = Date.from(ldt1.atZone(ZoneId.systemDefault()).toInstant());
                LocalDateTime ldt2 = LocalDateTime.of(desiredDateDay2, desiredDateTime2);
                Date d2 = Date.from(ldt2.atZone(ZoneId.systemDefault()).toInstant());

                reservationDto.setUserId(loginUserId);
                if(reservationDto.getMentoId() == null){
                    reservationDto.setMentoId(mentoId);
                }
                reservationDto.setAskType(InputReservation.getAskType());
                reservationDto.setAskContent(InputReservation.getAskContent());
                reservationDto.setPosition(InputReservation.getPosition());
                reservationDto.setDesiredDate1(d1);
                reservationDto.setDesiredDate2(d2);

                System.out.println(InputUserDto);
                userService.updateBaseInfo(InputUserDto);
                // model.addAttribute("r", reservationDto); 유지 시킬건지
                System.out.println("왜 안될까?");
                System.out.println(reservationDto);
                return "redirect:/reservation/2?mentoId="+mentoId;
            }
            case 2 -> {

            }
        }
        return "";
    }
    /*
    UserDto InputUserDto,
    ReservationDto InputReservation,




    */
}
