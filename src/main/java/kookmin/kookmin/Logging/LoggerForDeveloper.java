package kookmin.kookmin.Logging;

import jakarta.servlet.http.HttpServletResponse;
import kookmin.kookmin.controller.client.ErrorRedirectionController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@Component
public class LoggerForDeveloper implements CommonLoggerInterface {
    // C++에서 ifdef같은거로 개발PC 서버PC구분해서 변수 설정이 가능한데.. JAVA는 그런게 없고 복잡하게 설정해야해서 걍 이렇게 처리함
    private String logFilePath;
    private String logFileName;

    private static final String FILE_NAME_FORMAT = "yyyy-MM-dd";
    private static final String FILE_EXTENSION = ".log";
    private final boolean isServerPC = false;

    @Value("${logger.developer.mode}")
    private boolean developMode;

    @Autowired
    ErrorRedirectionController errorRedirectionController;

    public enum ExceptionList {
        SQL_EXCEPTION,
        MAIL_EXCEPTIOM,
        SIGNITURE_NOMATCH_EXCEPTION
    }

    LoggerForDeveloper() {
        if (isServerPC) {
            // 여기는 서버에 올릴 때, 에러찍을 경로 fix 해둔거에요~
            logFilePath = "/root/logFolder/";
        }
        else {
            // 여기서 모래씨 개발 환경에 맞게 로그찍을 폴더 경로 설정해서 편하게 사용하세요~
            logFilePath = "C:/Users/cmr/";
        }
        String newLogFileName = makeLogFileName();
        boolean ret = createLogFile(newLogFileName);
        if (!ret) {
            System.err.println("fail to create init log file : " + newLogFileName);
            System.err.println("exit program.. ");
            System.exit(1);
        }
        else {
            logFileName = newLogFileName;
        }
    }

    @Override
    public void info(String message) {
        // do not use this function at this class...
    }

    @Override
    public void error(Exception commonException, LoggerForDeveloper.ExceptionList exceptionList) {
        errorCommonProcess(commonException, exceptionList);
    }

    // 일부러 1분 여유줘서 생성함.. 혹시나 겹칠까 해서
    // C++ 에서는 system call로 커널에 이벤트 등록해서 커널이 알아서 SW를 call해주는데.. spring boot는 자체적으로 주기적으로 확인하는애 써야된다고함
    @Scheduled(cron = "0 1 0  * * ?")
    private void newLogFileAtMidnight() {
        String newLogFileName = makeLogFileName();
        boolean ret = createLogFile(newLogFileName);
        if (!ret) {
            System.err.println("fail to create new log file at Midnight : " + newLogFileName);
            System.err.println("so, error write at before date file");
        }
        else {
            logFileName = newLogFileName;
        }
    }

    private void errorCommonProcess(Exception commonException, LoggerForDeveloper.ExceptionList exceptionList) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        String message = "\n";
        if (exceptionList == ExceptionList.SQL_EXCEPTION) {
            message += "####################SQL Error!!####################\n";
            // 캐스팅 나중에 쓸 수도 있으니까 일단 주석처리
//            if (commonException instanceof SQLException) {
//                SQLException sqlException = (SQLException) commonException;
//                message = makeSQLErrorMsg(sqlException);
//            }
//            else {
//                System.out.println("Exception casting fail : " + e.getMessage());
//            }
        }
        else if (exceptionList == ExceptionList.MAIL_EXCEPTIOM) {
            message += "####################MAIL Error!!####################\n";
        }

        String commonErrorInfo = makeCommonErrorMsg(commonException);

        if (commonErrorInfo.isEmpty()) {
            System.err.println("make common error failed");
        }
        else {
            message += commonErrorInfo;
        }

        if (developMode) {
            System.err.println(message);
        }
        try (FileWriter fileWriter = new FileWriter(logFileName, true); // append 모드
             PrintWriter printWriter = new PrintWriter(fileWriter)) {
            printWriter.println(message);
            if (attributes != null) {
                HttpServletResponse response = attributes.getResponse();
                // 리다이렉션 처리
                try {
                    if (exceptionList != ExceptionList.MAIL_EXCEPTIOM) {
                        response.sendRedirect("http://localhost:8080/commonError");
                    }
                    else {
                        response.sendRedirect("http://localhost:8080/errorMailFailed");
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String makeCommonErrorMsg(Exception exception) {
        String errorMsg = "\n";
        errorMsg += exception.getMessage();
        errorMsg += "\n";
        errorMsg += exception.getCause();
        errorMsg += "\n";
        String stacktrace = getStackTraceAsString(exception);
        if (stacktrace != null && !stacktrace.isEmpty()) {
            errorMsg += stacktrace;
        }
        errorMsg += "\n";
        return errorMsg;
    }

    public static String getStackTraceAsString(Exception e) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        e.printStackTrace(printWriter);
        return stringWriter.toString();
    }

    private String makeLogFileName () {
        // 현재 날짜를 기준으로 파일 이름 생성
        String dateString = new SimpleDateFormat(FILE_NAME_FORMAT).format(new Date());
        String newLogFileName = logFilePath + dateString + FILE_EXTENSION;
        return newLogFileName;
    }

    private boolean createLogFile (String newLogFileName) {
        // 클래스 멤버변수의 logFileName으로만 만들어줘야함!
        File file = new File(newLogFileName);
        if (!file.exists()) {
            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + newLogFileName);
                    return true;
                } else {
                    System.err.println("fail to create log file : " + newLogFileName);
                    return false;
                }
            } catch (IOException e) {
                System.err.println("fail to create log file : " + newLogFileName);
                e.printStackTrace(); // 또는 로깅 라이브러리 사용
                return false;
            }
        }
        else {
            System.out.println("File already exists : " + newLogFileName);
            return true;
        }
    }
}
