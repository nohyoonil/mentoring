package kookmin.kookmin.dto.client;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class MessageCodeAndResDto<ResponseDto> {
    private String message;
    private ResponseDto data;
}