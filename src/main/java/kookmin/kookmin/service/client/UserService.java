package kookmin.kookmin.service.client;

import kookmin.kookmin.dto.client.SignupDto;
import kookmin.kookmin.dto.client.UserDto;
import kookmin.kookmin.mapper.client.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public List<UserDto> findAll() {
        return userMapper.findAll();
    }

    public void insert(UserDto userdto){
        userMapper.insert(userdto);
    }

    public UserDto findByEmail(String email){
        return userMapper.findByEmail(email);
    }

    public boolean validationInputPwd(UserDto userdto) {
        String pwdByInput = "";
        try {
            pwdByInput = hashingSha256(userdto.getPwd());
        }
        catch (NoSuchAlgorithmException e) {
            return false;
        }

        String pwdByDatabase = userMapper.findPwdByEmail(userdto.getEmail());
        if (pwdByInput.equals(pwdByDatabase))
        {
            return true;
        }
        return false;
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

    public boolean emailCodeCheck(String email){
        // do something for emailCodeCheck
        return true;
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

    public boolean registerUserInfo(SignupDto signupDto){
        String pwdHashing = "";
        try {
            pwdHashing = hashingSha256(signupDto.getPwd());
        }
        catch (NoSuchAlgorithmException e) {
            return false;
        }
        userMapper.insertBySignupInfo(signupDto.getEmail(), pwdHashing, signupDto.getNickname());
        return true;
    }

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
}
