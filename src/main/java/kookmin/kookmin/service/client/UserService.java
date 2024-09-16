package kookmin.kookmin.service.client;

import kookmin.kookmin.config.message.MessageComponent;
import kookmin.kookmin.dto.client.SignupDto;
import kookmin.kookmin.dto.client.UserDto;
import kookmin.kookmin.dto.client.user.UserInfoDto;
import kookmin.kookmin.mapper.client.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private MessageComponent messageComponent;

    final private HashMap<String, String> authCodeMap = new HashMap<>();
    final private HashMap<String, Integer> authEmailStatus = new HashMap<>();

    public List<UserDto> findAll() {
        return userMapper.findAll();
    }

    public void insert(UserDto userdto){
        userMapper.insert(userdto);
    }

    public UserDto findByEmail(String email){
        return userMapper.findByEmail(email);
    }

    public boolean validationInputPwd(String inputPwd , String pwdByDatabase) {
        String pwdByInput = "";
        try {
            pwdByInput = hashingSha256(inputPwd);
        }
        catch (NoSuchAlgorithmException e) {
            return false;
        }
        if (pwdByInput.equals(pwdByDatabase))
        {
            return true;
        }
        return false;
    }

    public UserDto getUserInfo(String email) {
        UserDto userDto = userMapper.findByEmail(email);
        if (userDto == null)
        {
            return null;
        }
        return userDto;
    }

    public void update(UserDto userdto){
        userMapper.update(userdto);
    }

    public UserDto findByUserId(String userId){
        return userMapper.findByUserId(userId);
    }

    public UserDto findByMentoUserInfoReservation(String reservationId){
        return userMapper.findByMentoUserInfoReservation(reservationId);
    }

    public boolean finishAuthEmail(String email) {
        if (authEmailStatus.containsKey(email)) {
            if (authEmailStatus.get(email) == 1) {
                authEmailStatus.remove(email);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    public boolean pwdCheck(String pwd){
        int totalLength = pwd.length();
        if (totalLength < 8) {
            return false;
        }

        int letterCount = 0;
        int numberCount = 0;
        for (char c : pwd.toCharArray()) {
            if (c > 127) {
                return false; // ASCII 범위를 넘어서는 문자가 발견되었음
            }

            // String에서 영문자의 갯수 카운트!
            if (Character.isLetter(c) && (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z')) {
                letterCount++;
            }
            else if (Character.isDigit(c)) {
                numberCount++;
            }
        }

        // 영문자와 숫자 갯수 외의 것은 특수문자로 간주
        int specialCharacterCount = totalLength - (letterCount + numberCount);

        if (letterCount == 0 || numberCount == 0 || specialCharacterCount == 0) {
            return false;
        }

        return true;
    }

    public boolean isPwdEqual(String pwd, String pwdCheck){
        return pwd.equals(pwdCheck);
    }

    public boolean isNicknameKorean(String nickname){
        for (char c : nickname.toCharArray()) {
            // 한글 음절, 자모, 호환 자모 범위 내의 문자만 허용
            if (!((c >= 0xAC00 && c <= 0xD7A3) ||  // 한글 음절
                    (c >= 0x1100 && c <= 0x11FF) ||  // 한글 자모
                    (c >= 0x3130 && c <= 0x318F) ||  // 한글 호환 자모
                    (c >= 0xA960 && c <= 0xA97F) ||  // 한글 자모 확장-A
                    (c >= 0xD7B0 && c <= 0xD7FF))) {  // 한글 자모 확장-B
                return false; // 한글 범위 밖의 문자가 발견되었음
            }
        }
        return true;
    }

    public boolean registerUserInfo(String email, String pwd, String nickname){
        String pwdHashing = "";
        try {
            pwdHashing = hashingSha256(pwd);
        }
        catch (NoSuchAlgorithmException e) {
            return false;
        }
        userMapper.insertBySignupInfo(email, pwdHashing, nickname);
        return true;
    }

    public boolean isAlreadyRegister(String email) {
        String pwd = userMapper.findPwdByEmail(email);
        if (pwd != null) {
            return true;
        }
        else {
            return false;
        }
    }

    public boolean sendEmail(String email) {
        try
        {
            SimpleMailMessage message = new SimpleMailMessage();
            String authCode = makeRandomAuthCode();
            message.setFrom(messageComponent.getCOWEEF_MAIL_ADDRESS());
            message.setTo(email);
            message.setSubject(messageComponent.getAUTH_MAIL_SEND());
            message.setText(messageComponent.getAUTH_CODE() + " : " + authCode);
            emailSender.send(message);
            authCodeMap.put(email, authCode);
            authEmailStatus.put(email, 0);
            return true;
        }
        catch (MailException e)
        {
            return false;
        }
    }

    public boolean emailCodeCheck(String email, String emailCheckCode) {
        String sendCode = authCodeMap.get(email);
        if (authCodeMap.containsKey(email))
        {
            if (sendCode.equals(emailCheckCode))
            {
                authCodeMap.remove(email);
                authEmailStatus.put(email, 1);
                return true;
            }
            else {
                return false;
            }
        }
        else {
            return false;
        }
    }

    // 모래씨 주목!! 여기서 message digest이자 단방향 암호화입니다.
    private String hashingSha256(String pwd) throws NoSuchAlgorithmException {
        // SHA-256 알고리즘을 사용하는 MessageDigest 인스턴스 생성
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(pwd.getBytes());

        // 해싱된 바이트 배열을 16진수 문자열로 변환
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private String makeRandomAuthCode () {
        Random random = new Random();
        // 100000에서 999999 사이의 랜덤 숫자 생성
        int randomNumber = 100000 + random.nextInt(900000);
        // 숫자를 문자열로 변환
        String randomNumberString = Integer.toString(randomNumber);
        return randomNumberString;
    }
}
