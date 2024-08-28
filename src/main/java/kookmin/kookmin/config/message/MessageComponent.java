package kookmin.kookmin.config.message;

import lombok.Getter;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@Component
@Getter
@Setter
public class MessageComponent {
    private String LOG_IN;
    private String NO_MEMBER;
    private String REGISTER_SUCCESS;
    private String AUTH_FAIL;
    private String AUTH_SUCCESS;
    private String EXPIRED_TOKEN;
    private String SUCCESS;
    private String FAIL;
    private String INVAILD_ACCESS;
}
