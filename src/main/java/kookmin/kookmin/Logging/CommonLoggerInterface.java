package kookmin.kookmin.Logging;

public interface CommonLoggerInterface {
    void info(String message);
    void error(Exception commonException, LoggerForDeveloper.ExceptionList exceptionList);
}
