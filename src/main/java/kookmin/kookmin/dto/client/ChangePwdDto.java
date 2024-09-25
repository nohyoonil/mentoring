package kookmin.kookmin.dto.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePwdDto {
    private String email;
    private String emailCheckCode;
    private String pwd;
    private String pwdCheck;
}