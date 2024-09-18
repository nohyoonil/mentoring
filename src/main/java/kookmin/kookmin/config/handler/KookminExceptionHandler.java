package kookmin.kookmin.config.handler;

import kookmin.kookmin.Logging.LoggerForDeveloper;
import kookmin.kookmin.config.message.MessageComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
public class KookminExceptionHandler {

    @Autowired
    private LoggerForDeveloper loggerForDeveloper;

    // 여기서 계속 필요한 exception class 정의해서 갖다 쓰기
    @ExceptionHandler(value = SQLException.class)
    public void sqlException(SQLException e) {
        loggerForDeveloper.error(e, LoggerForDeveloper.ExceptionList.SQL_EXCEPTION);
    }

    @ExceptionHandler(value = MailException.class)
    public void mailException(MailException e) {
        loggerForDeveloper.error(e, LoggerForDeveloper.ExceptionList.MAIL_EXCEPTION);
    }

//    이거는 모든 에러 잡아가는 괴물임 ㅋ.ㅋ
//    @ExceptionHandler(value = Exception.class)
//    public void commonException(Exception e) {
//        System.out.println("common error");
//    }
}
