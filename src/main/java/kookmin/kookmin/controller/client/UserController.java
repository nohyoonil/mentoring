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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private ReservationService reservationService;

    @GetMapping("/login")
    public void login(){

    }

    @Autowired
    private MessageComponent messageComponent;

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

    @GetMapping("/signup")
    public void signup(Model model) {

    }

    @PostMapping("/signup")
    public void signupSubmit(SignupDto signupDto, Model model) {
        // 구현해야 하는 로직
        // 1. 이메일 인증 발송 코드 확인
        // 2. 비밀번호 규격에 맞는가?
        // 3. 비밀번호 둘이 동일한가?
        // 4. DB에 밀어넣기
        //
        // 추가적으로 남은거,
        // 이메일 확인 버튼 누르면 팝업 뜨면서 기 가입된 이메일인지 확인하면서 이메일 발송 및 인증 코드 확인
        // 이메일 인증코드 입력하는 칸 하나만 만들어주세요 모래느님 ㅠㅠ
        if (!userService.emailCodeCheck(signupDto.getEmail())) {
            model.addAttribute("emailCheckFailed", messageComponent.getEMAIL_CHECK_FAILED());
        }

        else if (!userService.pwdCheck(signupDto.getPwd())) {
            model.addAttribute("pwdCheckFailed", messageComponent.getPWD_CHECK_FAILED());
        }

        else if (!userService.isPwdEqual(signupDto.getPwd(), signupDto.getPwdCheck())) {
            model.addAttribute("pwdIsNotEqual", messageComponent.getPWD_IS_NOTEQUAL());
        }

        else if (!userService.isNicknameKorean(signupDto.getNickname())) {
            model.addAttribute("nicknameIsNotKorean", messageComponent.getNICKNAME_IS_NOT_KOREAN());
        }

        else {
            if (!userService.registerUserInfo(signupDto)) {
                // 이거 메세지 바꿔야함
                model.addAttribute("signupFail", messageComponent.getSIGNUP_SUCCESS());
                return;
            }
            model.addAttribute("signupSuccess", messageComponent.getSIGNUP_SUCCESS());
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