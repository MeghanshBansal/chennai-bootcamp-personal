package main.com.playground.service;

import main.com.playground.domain.OTP;

import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.SQLException;
import java.util.Properties;

public class OTPService {
    String email;

    public OTPService(String email) {
        this.email = email;
    }

    public void sendOTP(OTP otp) throws SQLException, ClassNotFoundException {
        String host = "smtp.gmail.com";
        final String user = "playground.kata@gmail.com";
        final String password = "<playground>";


        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "smtp.gmail.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");
        props.put("mail.debug", "true");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        Session session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(user, password);
                    }
                });

        System.out.println("#########################################");
        System.out.println("Auth Successful");

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(otp.getEmail()));
            message.setSubject("Email verification for ToDos");
            message.setText(String.format("Your otp is %d", otp.generateOTP()));
            System.out.println("All good here -------------------");

            Transport.send(message);

            System.out.println("message sent successfully...");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
