package util;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailUtil {

    // Hardcoded configuration - replace with your actual values
    private static final String FROM_EMAIL = "duchai21092009@gmail.com";           // your Gmail address
    private static final String APP_PASSWORD = "zctx gyzm wmpi hrsl";    // your Gmail App Password

    private static Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.starttls.required", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        // Enable protocol debugging to see exact SMTP conversation in logs
        props.put("mail.debug", "true");

        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(FROM_EMAIL, APP_PASSWORD);
            }
        };
        Session session = Session.getInstance(props, auth);
        session.setDebug(true);
        return session;
    }

    public static boolean sendPlainText(String to, String subject, String body) {
        try {
            Session session = createSession();
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/plain; charset=UTF-8");
            msg.setFrom(new InternetAddress(FROM_EMAIL));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setText(body, "UTF-8");
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean sendHtml(String to, String subject, String html) {
        try {
            Session session = createSession();
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.setFrom(new InternetAddress(FROM_EMAIL));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject, "UTF-8");
            msg.setSentDate(new Date());
            msg.setContent(html, "text/html; charset=UTF-8");
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Example: send a simple verification code
    public static boolean sendVerificationEmail(String to, String verifyCode) {
        String subject = "Please verify your account - GroupTrip";
        String body = "You have been successfully registered. Your verification code is: " + verifyCode;
        return sendPlainText(to, subject, body);
    }

    // Backward-compatible method used by UserDAO
    public static boolean sendResetEmail(String toEmail, String token) {
        String resetUrl = "http://localhost:8080/group-trip/reset-password?token=" + token;
        String subject = "Reset your password - GroupTrip";
        String html = "<div style=\"font-family:Arial,sans-serif\">"
                + "<h2>Reset your password</h2>"
                + "<p>We received a request to reset your password. This link will expire in 30 minutes.</p>"
                + "<p><a href='" + resetUrl + "' style=\"display:inline-block;padding:10px 16px;background:#1a82e2;color:#fff;border-radius:6px;text-decoration:none\">Reset Password</a></p>"
                + "<p>If you didn’t request this, you can safely ignore this email.</p>"
                + "<p>Link: <a href='" + resetUrl + "'>" + resetUrl + "</a></p>"
                + "</div>";
        return sendHtml(toEmail, subject, html);
    }
}

