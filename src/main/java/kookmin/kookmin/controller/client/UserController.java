package kookmin.kookmin.controller.client;

import jakarta.servlet.http.HttpSession;
import kookmin.kookmin.Utility.MailUtil;
import kookmin.kookmin.dto.client.MentoDto;
import kookmin.kookmin.dto.client.ReservationDto;
import kookmin.kookmin.dto.client.SignupDto;
import kookmin.kookmin.dto.client.UserDto;
import kookmin.kookmin.service.client.MentoService;
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
    @Autowired
    private MentoService mentoService;


    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("mentoList", mentoService.findMentoBig3());
        System.out.println(mentoService.findMentoBig3());
        return "index";
    }
    @GetMapping("/login")
    public void login(){

    }

    // 로그인 플로우는 내가 해야하는것이니까 재구성
    // 추가적으로 회원가입 플로우 구현하기
    @PostMapping("/login")
    public String loginSubmit(UserDto InputUserDto, Model model, HttpSession session) {
        UserDto userDto = userService.getUserInfo(InputUserDto.getEmail());
        if (userDto == null) {
            model.addAttribute("userNotFound", "존재하지 않는 회원입니다. 회원가입을 진행해주세요.");
            return "redirect:/signup";
        }

        if (userService.validationInputPwd(InputUserDto.getPwd(), userDto.getPwd())) {
            session.setAttribute("loginUser", userDto);
            return "redirect:/";
        }
        else {
            model.addAttribute("userNotFound", "비밀번호를 확인해주세요");
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
        MailUtil.EmailAuthStatus emailAuthStatus = userService.emailCodeCheck(email, emailCheckCode);

        if (emailAuthStatus == MailUtil.EmailAuthStatus.EMAIL_AUTH_SUCCESS) {
            return "이메일 인증을 완료하였습니다.";
        }
        else if (emailAuthStatus == MailUtil.EmailAuthStatus.EMAIL_AUTH_ALREADY_SUCCESS) {
            return "이메일 인증이 이미 완료되었습니다.";
        }
        else if (emailAuthStatus == MailUtil.EmailAuthStatus.EMAIL_AUTH_CODE_DIFF) {
            return "이메일 인증번호가 다릅니다. 재입력 해주세요";
        }
        else if (emailAuthStatus == MailUtil.EmailAuthStatus.EMAIL_AUTH_CODE_NOT_SEND) {
            return "이메일 인증번호가 발송되지 않은 상태입니다.";
        }

        return "이메일 인증을 실패하였습니다. 다시 메일 발송 후, 재입력 해주세요. 문제가 반복될시, 관리자에게 문의부탁드립니다.";
    }

    @GetMapping("/signup")
    public void signup(Model model) {

    }

    @PostMapping("/signup")
    public void signupSubmit(SignupDto signupDto, Model model) {
        boolean successOrFailFlag = false;
        if (!userService.pwdCheck(signupDto.getPwd())) {
            model.addAttribute("pwdCheckFailed", messageComponent.getPWD_CHECK_FAILED());
        }

        else if (!userService.isPwdEqual(signupDto.getPwd(), signupDto.getPwdCheck())) {
            model.addAttribute("pwdIsNotEqual", messageComponent.getPWD_IS_NOT_EQUAL());
        }

        else if (!userService.isNicknameKorean(signupDto.getNickname())) {
            model.addAttribute("nicknameIsNotKorean", messageComponent.getNICKNAME_IS_NOT_KOREAN());
        }

        else if (!userService.finishAuthEmail(signupDto.getEmail())) {
            model.addAttribute("emailNotCheck", messageComponent.getEMAIL_CHECK_FAILED());
        }

        else {
            if (!userService.registerUserInfo(signupDto.getEmail(), signupDto.getPwd(), signupDto.getNickname())) {
                model.addAttribute("signupFail", messageComponent.getSIGNUP_FAIL());
            }
            else {
                model.addAttribute("signupSuccess", messageComponent.getSIGNUP_SUCCESS());
                successOrFailFlag = true;
            }
        }

        if (!successOrFailFlag) {
            model.addAttribute("email", signupDto.getEmail());
            model.addAttribute("emailCheckCode", signupDto.getEmailCheckCode());
            model.addAttribute("pwd", signupDto.getPwd());
            model.addAttribute("pwdCheck", signupDto.getPwdCheck());
            model.addAttribute("nickname", signupDto.getNickname());
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
        System.out.println("userEmail : "+userEmail);
        model.addAttribute("myInfoNums", reservationService.myInfoNums(userEmail));
        model.addAttribute("noMoneyList", reservationService.findByEmailSplitStatus(userEmail).get(1));
        model.addAttribute("reservationStayList", reservationService.findByEmailSplitStatus(userEmail).get(2));
        model.addAttribute("reservationHistorysList", reservationService.findByEmailSplitStatus(userEmail).get(3));
        model.addAttribute("reservationRefundList", reservationService.findByEmailSplitStatus(userEmail).get(4));


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