package kookmin.kookmin.Utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


public class MailUtil {

    public enum EmailAuthStatus {
        EMAIL_AUTH_CODE_NOT_SEND,
        EMAIL_AUTH_CODE_DIFF,
        EMAIL_AUTH_ALREADY_SUCCESS,
        EMAIL_AUTH_SUCCESS
    }

    public static boolean sendEmail(JavaMailSender emailSender, String fromEmail, String toEmail, String subject, String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(content);
        emailSender.send(message);
        return true;
    }
}
