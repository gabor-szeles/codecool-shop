package com.codecool.shop.controller;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * Sends welcome email to a specific user address.
 * <p>
 * The recipient's address is specified in the constructor and sent through the <strong>gmail</strong> address specified in the fields.
 *
 */
public class EmailSender {

    private static String USER_NAME = "lavawebshop";
    private static String PASSWORD = "lavawebshop1";
    private String recipient;

    /**
     * Private constructor to prevent unparameterized instantiation.
     */
    private EmailSender() {}

    /**
     * Create an instance of EmailSender.
     *
     * @param recipient The email address of the recipient.
     */
    public EmailSender(String recipient) {
        this.recipient = recipient;
    }

    /**
     * Sends the email with the specified subject and message.
     *
     * Uses {@link #sendFromGMail(String, String, String[], String, String)} to send email.
     */
    public void send() {
        String from = USER_NAME;
        String pass = PASSWORD;
        String[] to = {recipient};
        String subject = "Welcome To Lava's Webshop";
        String body = "Hope you'll have a great experience while using the webshop.";

        sendFromGMail(from, pass, to, subject, body);
    }

    /**
     * Establishes the connection to the given <strong>gmail</strong> account and sends the email to the given addresses.
     *
     * @param from    The gmail account's address <strong>without @gmail.com</strong>, which the email will sent through.
     * @param pass    The password for that gmail account.
     * @param to      The address of the recipient.
     * @param subject The subject of the email.
     * @param body    The inner text of the email.
     */
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
            message.setText(body);
            Transport transport = session.getTransport("smtp");
            transport.connect(host, from, pass);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
            System.out.println("Email sent to user.");
        } catch (AddressException ae) {
            ae.printStackTrace();
        } catch (MessagingException me) {
            me.printStackTrace();
        }
    }
}
