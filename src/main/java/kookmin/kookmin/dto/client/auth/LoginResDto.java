package kookmin.kookmin.dto.client.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginResDto {
    private String name;
    private String phoneNumber;
    private String accessToken;
    private String refreshToken;
}
