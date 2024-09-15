package kookmin.kookmin.config.handler;

import kookmin.kookmin.config.message.MessageComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class KookminExceptionHandler {
    @Autowired
    private MessageComponent messageComponent;

    // 여기서 계속 필요한 exception class 정의해서 갖다 쓰기
    @ExceptionHandler(value = SQLException.class)
    public void sqlException(SQLException e) {
        // log class 정의해서 로그를 뿌려주고
        // 리다이렉션도 여기서 가능하려나??? 하아 골치아파지네... 설계하기 싫다....
        System.out.println("sql ");
    }


    // 기타 예외 클래스 처리
//    @ExceptionHandler(value = Exception.class)
//    public void commonException(Exception e) {
//        // log class 정의해서 로그를 뿌려주고
//        // 리다이렉션도 여기서 가능하려나??? 하아 골치아파지네... 설계하기 싫다....
//        System.out.println("common error");
//    }
}
