package service;

import org.springframework.stereotype.Service;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;
import java.io.UnsupportedEncodingException;

@Service
public class MailService {

    public void sendPasswordResetEmail(String toEmail, String resetLink) {
        final String fromEmail = "taskmagnet69@gmail.com";
        final String password = "vnzdejwzxrwdcopl";

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS

        Authenticator auth = new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        };

        Session session = Session.getInstance(props, auth);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail, "TaskMagnet"));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
            message.setSubject("Password Reset Request");

            String htmlMessage = "<html>"
                    + "<body style='color: green;'>"
                    + "<h2 style='font-family: Arial, sans-serif;'>ECO-JB Password Reset Request</h2>"
                    + "<p style='font-family: Arial, sans-serif;'>"
                    + "You have requested to reset your password. If you did not make this request, you can safely ignore this email."
                    + "</p>"
                    + "<p style='font-family: Arial, sans-serif;'>"
                    + "To reset your password, click the link below:"
                    + "</p>"
                    + "<a href='" + resetLink + "' style='font-family: Arial, sans-serif; color: blue;'>Reset Password</a>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlMessage, "text/html");

            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}