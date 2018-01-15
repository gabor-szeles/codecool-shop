package com.codecool.shop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Properties;

public class EmailSender {

    private static final Logger logger = LoggerFactory.getLogger(EmailSender.class);
    private static String USER_NAME = "lavawebshop";
    private static String PASSWORD = "lavawebshop1";
    private String recipient;

    private EmailSender() {}

    public EmailSender(String recipient) {
        logger.debug("Entering EmailSender(recipient={})", recipient);
        this.recipient = recipient;
    }

    public void send() {
        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = {recipient};
        String subject = "Welcome To Lava's Webshop";
        String body = "Hope you'll have a great experience while using the webshop.";

        sendFromGMail(from, pass, to, subject, body);
    }

    private void sendFromGMail(String from, String pass, String[] to, String subject, String body) {
        Properties props = System.getProperties();
        String host = "smtp.gmail.com";
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.user", from);
        props.put("mail.smtp.password", pass);
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");

        Session session = Session.getDefaultInstance(props);

        MimeMessage message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(from));
            InternetAddress[] toAddress = new InternetAddress[to.length];

            // To get the array of addresses
            for( int i = 0; i < to.length; i++ ) {
                toAddress[i] = new InternetAddress(to[i]);
            }

            for( int i = 0; i < toAddress.length; i++) {
                message.addRecipient(Message.RecipientType.TO, toAddress[i]);
            }

            message.setSubject(subject);
            logger.trace("Email's subject: {}", subject);
            message.setText(body);
            logger.trace("Email's body: {}", body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            logger.info("Email sent to: {}", Arrays.toString(to));
        } catch (AddressException ae) {
            ae.printStackTrace();
            logger.error("Not valid email address format: {}", (Arrays.toString(to)));
        } catch (MessagingException me) {
            me.printStackTrace();
            logger.error("Error while sending email to user.", me);
        }
    }
}
