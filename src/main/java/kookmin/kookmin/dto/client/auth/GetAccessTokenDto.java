package kookmin.kookmin.dto.client.auth;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GetAccessTokenDto {
    private String name;
    private String accessToken;
}
