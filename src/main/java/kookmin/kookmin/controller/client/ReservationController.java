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

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
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

    @PostMapping("/clipBankNums")
    public void clipBankNums(){
        String bankNums = "110-454-977350";
        StringSelection data = new StringSelection(bankNums);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(data, data);
    }

    @GetMapping("/reservation/{step}")
    public String reservationForm(@RequestParam(name = "mentoId", defaultValue = "null") String mentoId, @PathVariable("step") int step, HttpSession session, Model model){
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        if(loginUser == null){
            return "redirect:/userInfoEnd";
        }
        return switch (step) {
            case 1 -> {
                //타임리프는 해당 객체가 없으면 아예 오류를 내뱉으므로 필요
                if(session.getAttribute("newReservation") == null){
                    session.setAttribute("newReservation", new ReservationDto());
                }
                model.addAttribute("mentoId", mentoId);
                yield "reservationStep1";
            }
            case 2 -> "reservationStep2";
            case 3 -> "reservationStep3";
            default -> {
                ReservationDto reservationDto = (ReservationDto)session.getAttribute("newReservation");
                model.addAttribute("newReservation", reservationService.replaceFullDto(reservationDto));
                session.removeAttribute("newReservation");
                yield "reservationOk";
            }
        };
    }

    @PostMapping("/reservation/{step}")
    public String reservationSubmit(
            @PathVariable("step") int step,
            HttpSession session,
            UserDto InputUserDto,
            @RequestParam(name = "mentoId", required = false) String mentoId,
            @RequestParam(name = "desiredDateDay1", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desiredDateDay1,
            @RequestParam(name = "desiredDateTime1", required = false) @DateTimeFormat(pattern="HH:mm") LocalTime desiredDateTime1,
            @RequestParam(name = "desiredDateDay2", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desiredDateDay2,
            @RequestParam(name = "desiredDateTime2", required = false) @DateTimeFormat(pattern="HH:mm") LocalTime desiredDateTime2,
            ReservationDto InputReservation,
            Model model)
    {
        String loginUserId = ((UserDto) session.getAttribute("loginUser")).getUserId();
        switch (step){
            case 1 -> {
                ReservationDto reservationDto = reservationService.stepSetReservation01(mentoId, loginUserId, desiredDateDay1, desiredDateTime1, desiredDateDay2, desiredDateTime2, InputReservation);
                userService.updateBaseInfo(InputUserDto);
                session.setAttribute("newReservation", reservationDto);
                return "redirect:/reservation/2";
            }
            case 2 -> {
                ReservationDto reservationDto = (ReservationDto)session.getAttribute("newReservation");
                reservationDto.setPlanTitle(InputReservation.getPlanTitle());
                session.setAttribute("newReservation", reservationDto);  //이전으로 버튼때문에 model 이 아닌 session 에 저장 필요
                return "redirect:/reservation/3";
            }
            default -> {
                ReservationDto reservationDto = (ReservationDto)session.getAttribute("newReservation");
                reservationService.newReservation(reservationDto);
                return "redirect:/reservation/4";
            }
        }
    }
    /*
    @PostMapping("/reservation/{step}")
    public String reservationSubmit(
            @PathVariable("step") int step,
            HttpSession session,
            UserDto InputUserDto,
            @RequestParam(name = "mentoId", required = false) String mentoId,
            @RequestParam(name = "desiredDateDay1", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desiredDateDay1,
            @RequestParam(name = "desiredDateTime1", required = false) @DateTimeFormat(pattern="HH:mm") LocalTime desiredDateTime1,
            @RequestParam(name = "desiredDateDay2", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate desiredDateDay2,
            @RequestParam(name = "desiredDateTime2", required = false) @DateTimeFormat(pattern="HH:mm") LocalTime desiredDateTime2,
            ReservationDto InputReservation,
            Model model)
    {
        String loginUserId = ((UserDto) session.getAttribute("loginUser")).getUserId();

        switch (step){
            case 1 -> {
                ReservationDto reservationDto = new ReservationDto();
                LocalDateTime ldt1 = LocalDateTime.of(desiredDateDay1, desiredDateTime1);
                Date d1 = Date.from(ldt1.atZone(ZoneId.systemDefault()).toInstant());
                LocalDateTime ldt2 = LocalDateTime.of(desiredDateDay2, desiredDateTime2);
                Date d2 = Date.from(ldt2.atZone(ZoneId.systemDefault()).toInstant());
                reservationDto.setUserId(loginUserId);
                if(mentoId != null){
                    reservationDto.setMentoId(mentoId);
                }
                reservationDto.setAskType(InputReservation.getAskType());
                reservationDto.setAskContent(InputReservation.getAskContent());
                reservationDto.setPosition(InputReservation.getPosition());
                reservationDto.setDesiredDate1(d1);
                reservationDto.setDesiredDate2(d2);

                userService.updateBaseInfo(InputUserDto);
                session.setAttribute("r", reservationDto);
                return "redirect:/reservation/2";
            }
            case 2 -> {
                ReservationDto reservationDto = (ReservationDto)session.getAttribute("r");
                reservationDto.setPlanTitle(InputReservation.getPlanTitle());
                session.setAttribute("r", reservationDto);  //이전으로 버튼때문에 model 이 아닌 session 에 저장 필요
                return "redirect:/reservation/3";
            }
            default -> {
                ReservationDto reservationDto = (ReservationDto)session.getAttribute("r");
                return "redirect:/reservation/4";
            }
        }
    }*/
}
