package kookmin.kookmin.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TosspayConfig {
    @Value("${payment.toss.test_client_api_key}")
    private String clientApiKey;

    @Value("${payment.toss.test_secret_api_key}")
    private String secretApiKey;

    @Value("${payment.toss.success_url}")
    private String successUrl;

    @Value("${payment.toss.failure_url}")
    private String failUrl;

    public static final String URL = "https://api.tosspayments.com/v1/payments/";
}
