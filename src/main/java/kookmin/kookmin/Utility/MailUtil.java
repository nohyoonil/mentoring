package kookmin.kookmin.Utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;


public class MailUtil {
    public static boolean sendEmail(JavaMailSender emailSender, String fromEmail, String toEmail, String subject, String content) {
        try
        {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(content);
            emailSender.send(message);
            return true;
        }
        catch (MailException e)
        {
            return false;
        }
    }
}
