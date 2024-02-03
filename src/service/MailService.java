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
                    + "<body style='font-family: Arial, sans-serif; color: #333;'>"
                    + "<div style='max-width: 600px; margin: 0 auto;'>"
                    + "<h2 style='color: #4CAF50;'>ECO-JB Password Reset Request</h2>"
                    + "<p>"
                    + "You have requested to reset your password. If you did not make this request, you can safely ignore this email."
                    + "</p>"
                    + "<p>"
                    + "To reset your password, click the button below:"
                    + "</p>"
                    + "<a href='" + resetLink + "' style='display: inline-block; font-weight: 400; color: #212529; text-align: center; vertical-align: middle; cursor: pointer; background-color: #4CAF50; border: 1px solid transparent; padding: .375rem .75rem; font-size: 1rem; line-height: 1.5; border-radius: .25rem; text-decoration: none;'>Reset Password</a>"
                    + "</div>"
                    + "</body>"
                    + "</html>";

            message.setContent(htmlMessage, "text/html");

            Transport.send(message);
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}