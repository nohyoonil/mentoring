package kookmin.kookmin.Logging;

import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.stereotype.Component;
//
//@Configuration
//@Component
public class LoggerForPM implements CommonLoggerInterface{

    @Value("${logger.PM.mode}")
    private boolean PM_MODE;

    @Override
    public void info(String message) {
        // 스프링부트가 혼자 다처리하면 응답시간이 느려질 것 같아서
        // 파이썬 flask에서 해당 작업을 하기로함..
        // 일단 나중을 대비해서 skeleton code 용도로 남겨는 두기로~
    }

    @Override
    public void error(Exception commonException, LoggerForDeveloper.ExceptionList exceptionList) {
        // do not use this function at this class...
    }
}
