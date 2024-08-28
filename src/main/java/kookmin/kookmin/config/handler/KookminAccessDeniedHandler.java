//package kookmin.kookmin.config.handler;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import io.jsonwebtoken.io.IOException;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.security.access.AccessDeniedException;
//import org.springframework.security.web.access.AccessDeniedHandler;
//import org.springframework.stereotype.Component;
//import kookmin.kookmin.dto.client.auth.ErrorResponseDto;
//
//import java.time.LocalDateTime;
//
//@Slf4j(topic = "FORBIDDEN_EXCEPTION_HANDLER")
//@AllArgsConstructor
//@Component
//public class KookminAccessDeniedHandler implements AccessDeniedHandler {
//
//    private final ObjectMapper objectMapper;
//
//    @Override
//    public void handle(HttpServletRequest request,
//                       HttpServletResponse response,
//                       AccessDeniedException accessDeniedException) throws IOException, ServletException, java.io.IOException {
//        log.error("No Authorities", accessDeniedException);
//
//        ErrorResponseDto errorResponseDto = new ErrorResponseDto(HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage(), LocalDateTime.now());
//
//        String responseBody = objectMapper.writeValueAsString(errorResponseDto);
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.setStatus(HttpStatus.FORBIDDEN.value());
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(responseBody);
//    }
//}