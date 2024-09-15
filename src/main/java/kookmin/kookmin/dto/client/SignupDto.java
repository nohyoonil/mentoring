package kookmin.kookmin.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupDto {
    private String email;
    private String emailCheckCode;
    private String pwd;
    private String pwdCheck;
    private String nickname;
}