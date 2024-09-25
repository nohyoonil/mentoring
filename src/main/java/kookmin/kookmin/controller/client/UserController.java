package kookmin.kookmin.controller.client;

import jakarta.servlet.http.HttpSession;
import kookmin.kookmin.Utility.MailUtil;
import kookmin.kookmin.dto.client.*;
import kookmin.kookmin.enums.ReservationStatus;
import kookmin.kookmin.service.client.MentoService;
import kookmin.kookmin.service.client.ReservationService;
import kookmin.kookmin.service.client.UserService;
import kookmin.kookmin.config.message.MessageComponent;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    public void login(){}

    // 로그인 플로우는 내가 해야하는것이니까 재구성
    // 추가적으로 회원가입 플로우 구현하기
    @PostMapping("/login")
    public String loginSubmit(UserDto InputUserDto, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        UserDto userDto = userService.getUserInfo(InputUserDto.getEmail());
        if (userDto == null) {
            redirectAttributes.addFlashAttribute("userNotFound", "존재하지 않는 회원입니다. 회원가입을 진행해주세요.");
            return "redirect:/signup";
        }

        if (userService.validationInputPwd(InputUserDto.getPwd(), userDto.getPwd())) {
            session.setAttribute("loginUser", userDto);
            return "redirect:/";
        }
        else {
            redirectAttributes.addFlashAttribute("userNotFound", "비밀번호를 확인해주세요");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        //임시로 만든 것.
        session.removeAttribute("loginUser");
        return "redirect:/";
    }

/* ------------------------------------------ 비밀번호 찾기 start ------------------------------------------ */
    @PostMapping("/emailSendForChangePwd")
    @ResponseBody
    public String emailSendForChangePwd(String email) {
        String[] parts = email.split("@");
        if (parts.length != 2 || !parts[1].equals("kookmin.ac.kr")) {
            return "이메일 형식이 유효하지 않습니다. 국민대 이메일로만 이용가능합니다.";
        }
        if (!userService.isAlreadyRegister(email)) {
            return "가입되지 않은 회원입니다.";
        }
        if (!userService.sendEmail(email)) {
            return "메일 발송에 실패하였습니다. 관리자에게 문의해주세요.";
        }
        return "메일 발송이 완료되었습니다. 입력하신 국민대학교 이메일 수신함에서 인증번호를 확인해주세요.";
    }

    @PostMapping("/authEmailCodeForChangePwd")
    @ResponseBody
    public String authEmailCodeForChangePwd(String email, String emailCheckCode) {
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

    @GetMapping("/changePwd")
    public String changePwd(Model model) {
        return "changePassword";
    }

    @PostMapping("/changePwd")
    public String changePwdSubmit(ChangePwdDto changePwdDto, RedirectAttributes redirectAttributes) {
        boolean successOrFailFlag = false;
        if (!userService.pwdCheck(changePwdDto.getPwd())) {
            redirectAttributes.addFlashAttribute("pwdCheckFailed", messageComponent.getPWD_CHECK_FAILED());
        }

        else if (!userService.isPwdEqual(changePwdDto.getPwd(), changePwdDto.getPwdCheck())) {
            redirectAttributes.addFlashAttribute("pwdIsNotEqual", messageComponent.getPWD_IS_NOT_EQUAL());
        }

        else if (!userService.finishAuthEmail(changePwdDto.getEmail())) {
            redirectAttributes.addFlashAttribute("emailNotCheck", messageComponent.getEMAIL_CHECK_FAILED());
        }

        else {
            if (!userService.changePassword(changePwdDto.getEmail(), changePwdDto.getPwd())) {
                redirectAttributes.addFlashAttribute("pwdChangeFail", messageComponent.getSIGNUP_FAIL());
            }
            else {
                redirectAttributes.addFlashAttribute("pwdChangeSuccess", messageComponent.getSIGNUP_SUCCESS());
                successOrFailFlag = true;
            }
        }

        if (!successOrFailFlag) {
            redirectAttributes.addFlashAttribute("email", changePwdDto.getEmail());
            redirectAttributes.addFlashAttribute("emailCheckCode", changePwdDto.getEmailCheckCode());
            redirectAttributes.addFlashAttribute("pwd", changePwdDto.getPwd());
            redirectAttributes.addFlashAttribute("pwdCheck", changePwdDto.getPwdCheck());
            return "redirect:/changePwd";
        }
        else {
            return "redirect:/login";
        }
    }
/* ------------------------------------------ 비밀번호 찾기 end ------------------------------------------ */



/* ------------------------------------------ 회원가입 매핑 start ------------------------------------------ */
    @PostMapping("/emailSend")
    @ResponseBody
    public String emailSend(String email) {
        String[] parts = email.split("@");
        if (parts.length != 2 || !parts[1].equals("kookmin.ac.kr")) {
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
/* ------------------------------------------ 회원가입 매핑 end ------------------------------------------ */

    @GetMapping("/mypage")
    public String mypage(Model model, HttpSession session) {
        HashMap<String, Integer> myInfoNums;
        UserDto loginUser = (UserDto) session.getAttribute("loginUser");
        String userEmail = "";
        if(loginUser != null){
            userEmail = loginUser.getEmail();
        }else{
            return "redirect:/userInfoEnd";
        }
        myInfoNums = reservationService.myInfoNums(userEmail);
        model.addAttribute("myInfoNums", myInfoNums);
        for (ReservationStatus status : ReservationStatus.values()) {
            List<ReservationfullDto> list = reservationService.findByEmailSplitStatus(userEmail).get(status.getValue());;
            if(list == null || list.isEmpty()){
                list = null;
            }
            model.addAttribute(String.valueOf(status), list);
        }

        return "mypage";
    }

    // 하단 mapping은 정보수정 모달에서 '저장하기'하고 매핑
    @PostMapping("/userInfoUpdate")
    public String userInfoUpdate(UserDto InputUserDto, HttpSession session) {
        UserDto editedUser;
        InputUserDto.setEmail(((UserDto)session.getAttribute("loginUser")).getEmail());
        userService.update(InputUserDto);
        editedUser = userService.findByEmail(InputUserDto.getEmail());
        session.setAttribute("loginUser", editedUser);
        return "redirect:/mypage";
    }

    @GetMapping("/userInfoEnd")
    public void userInfoEnd(){}
}