package kookmin.kookmin.controller.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.dto.client.UserDto;
import kookmin.kookmin.service.client.ReservationService;
import kookmin.kookmin.service.client.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/login")
    public void login(){

    }
    @PostMapping("/login")
    public String loginSubmit(UserDto InputUserDto, Model model, HttpSession session) {
        //일단 이메일만 DB에 있으면 로그인에 성공한 것으로 가정한다.
        UserDto loginUser = userService.findByEmail(InputUserDto.getEmail());
        if(loginUser != null){
            session.setAttribute("loginUser", loginUser);
            return "redirect:/";
        }else{
            model.addAttribute("userNotFound", "이메일 또는 비밀번호를 확인해주세요");
            return "/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        //임시로 만든 것.
        session.removeAttribute("loginUser");
        return "redirect:/";
    }

    @GetMapping("/mypage")
    public void mypage(Model model, HttpSession session) {
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        String userEmail = loginUser.getEmail();

        if(reservationService.findByEmail(userEmail) != null){
            model.addAttribute("myReservations", "존재함.");
        }
        model.addAttribute("myInfoNums", reservationService.findByMyInfo(userEmail));
        model.addAttribute("beforeCurrent", reservationService.findByEmailSplitCurrent(userEmail).get("before"));
        model.addAttribute("afterCurrent", reservationService.findByEmailSplitCurrent(userEmail).get("after"));
    }

    @PostMapping("/userInfoUpdate")
    public String userInfoUpdate(UserDto InputUserDto, HttpSession session) {
        InputUserDto.setEmail(((UserDto)session.getAttribute("loginUser")).getEmail());
        userService.update(InputUserDto);
        session.setAttribute("loginUser", userService.findByEmail(InputUserDto.getEmail()));
        return "redirect:/mypage";
    }
}
