package kookmin.kookmin.controller.client;

import jakarta.servlet.http.HttpSession;
import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.dto.client.SignupDto;
import kookmin.kookmin.dto.client.UserDto;
import kookmin.kookmin.service.client.ReservationService;
import kookmin.kookmin.service.client.UserService;
import kookmin.kookmin.config.message.MessageComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private MessageComponent messageComponent;

    @GetMapping("/login")
    public void login(){

    }

    // 로그인 플로우는 내가 해야하는것이니까 재구성
    // 추가적으로 회원가입 플로우 구현하기
    @PostMapping("/login")
    public String loginSubmit(UserDto InputUserDto, Model model, HttpSession session) {
        if (userService.validationInputPwd(InputUserDto)) {
            UserDto loginUser = new UserDto();
            loginUser.setEmail(InputUserDto.getEmail());
            session.setAttribute("loginUser", loginUser);
            return "redirect:/";
        }
        else {
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

    @PostMapping("/emailSend")
    @ResponseBody
    public String emailSend(String email) {
        String[] parts = email.split("@");
        if (parts.length != 2 && parts[1].equals("gmail.com")) {
            return "이메일 형식이 유효하지 않습니다. 국민대 이메일로만 가입 가능합니다.";
        }
        if (userService.isAlreadyRegister(email)) {
            return "이미 가입된 회원입니다.";
        }
        if (!userService.sendEmail(email)) {
            return "메일 발송에 실패하였습니다. 관리자에게 문의해주세요.";
        }
        return "메일 발송이 완료되었습니다. 입력하신 국민대학교 이메일 수신함에서 인증번호를 확인해주세요.";
    }

    @PostMapping("/authEmailCode")
    @ResponseBody
    public String authEmailCode(String email, String emailCheckCode) {
        if (userService.emailCodeCheck(email, emailCheckCode)) {
            return "이메일 인증을 완료하였습니다.";
        }
        else {
            return "이메일 인증을 실패하였습니다. 다시 메일 발송 후, 재입력 해주세요";
        }
    }

    @GetMapping("/signup")
    public void signup(Model model) {

    }

    @PostMapping("/signup")
    public void signupSubmit(SignupDto signupDtoDto, Model model) {
        if (!userService.finishAuthEmail(signupDtoDto.getEmail())) {
            model.addAttribute("emailNotCheck", messageComponent.getEMAIL_CHECK_FAILED());
        }

        else if (!userService.pwdCheck(signupDtoDto.getPwd())) {
            model.addAttribute("pwdCheckFailed", messageComponent.getPWD_CHECK_FAILED());
        }

        else if (!userService.isPwdEqual(signupDtoDto.getPwd(), signupDtoDto.getPwdCheck())) {
            model.addAttribute("pwdIsNotEqual", messageComponent.getPWD_IS_NOT_EQUAL());
        }

        else if (!userService.isNicknameKorean(signupDtoDto.getNickname())) {
            model.addAttribute("nicknameIsNotKorean", messageComponent.getNICKNAME_IS_NOT_KOREAN());
        }

        else {
            if (!userService.registerUserInfo(signupDtoDto.getEmail(), signupDtoDto.getPwd(), signupDtoDto.getNickname())) {
                model.addAttribute("signupFail", messageComponent.getSIGNUP_FAIL());
            }
            else {
                model.addAttribute("signupSuccess", messageComponent.getSIGNUP_SUCCESS());
            }
        }
    }

    @GetMapping("/mypage")
    public String mypage(Model model, HttpSession session) {
        List<ReservationDto> userReservationlist;
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        String userEmail = "";

        if(loginUser != null){
            userEmail = loginUser.getEmail();
        }else{
            return "redirect:/userInfoEnd";
        }
        userReservationlist =  reservationService.findByEmail(userEmail);
        if(userReservationlist != null && !(userReservationlist.isEmpty())){
            model.addAttribute("myReservations", "존재함.");
        }
        model.addAttribute("myInfoNums", reservationService.myInfoNums(userEmail));
        model.addAttribute("reservationStayList", reservationService.findByEmailSplitStatus(userEmail).get(2));
        model.addAttribute("reservationHistorysList", reservationService.findByEmailSplitStatus(userEmail).get(3));
        return "mypage";
    }

    // 하단 mapping은 정보수정 모달에서 '저장하기'하고 매핑
    @PostMapping("/userInfoUpdate")
    public String userInfoUpdate(UserDto InputUserDto, HttpSession session) {
        InputUserDto.setEmail(((UserDto)session.getAttribute("loginUser")).getEmail());
        userService.update(InputUserDto);
        session.setAttribute("loginUser", userService.findByEmail(InputUserDto.getEmail()));
        return "redirect:/mypage";
    }

    @GetMapping("/userInfoEnd")
    public void userInfoEnd(){}
}