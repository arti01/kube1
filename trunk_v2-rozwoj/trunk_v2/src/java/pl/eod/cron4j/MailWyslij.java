/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.cron4j;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import pl.eod.encje.ConfigJpaController;

/**
 *
 * @author arti01
 */
public class MailWyslij {

    MailWyslij(String temat, String tresc, String adresat) {
        this.adresat = adresat;
        this.temat = temat;
        this.tresc = tresc;
        ConfigJpaController confC = new ConfigJpaController();
        this.mail_smtp_host = confC.findConfigNazwa("mail_smtp_host").getWartosc();
        this.mail_smtp_socketFactory_port = confC.findConfigNazwa("mail_smtp_socketFactory_port").getWartosc();
        this.mail_smtp_port = confC.findConfigNazwa("mail_smtp_port").getWartosc();
        this.username = confC.findConfigNazwa("username").getWartosc();
        this.password = confC.findConfigNazwa("password").getWartosc();
        this.link=confC.findConfigNazwa("email_link").getWartosc();
        this.czy_ssl=confC.findConfigNazwa("czy_ssl").getWartosc();
        this.tresc=this.tresc+"\n\r"+this.link;
    }
    String temat;
    String tresc;
    String adresat;
    String mail_smtp_host;
    String mail_smtp_socketFactory_port;
    String mail_smtp_port;
    String username;
    String password;
    String link;
    String czy_ssl;

    public void wyslij() {
        Properties props = new Properties();

        /*props.put("mail.smtp.host", "smtp.gmail.com");
         props.put("mail.smtp.socketFactory.port", "465");
         props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
         props.put("mail.smtp.auth", "true");
         props.put("mail.smtp.port", "465");*/

        props.put("mail.smtp.host", mail_smtp_host);
        props.put("mail.smtp.socketFactory.port", mail_smtp_socketFactory_port);
        if(czy_ssl.equals("tak")) props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", mail_smtp_port);

        Session session;
        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });


        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("from@no-spam.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(adresat));
            message.setSubject(temat);
            message.setText(tresc);
            //System.out.println(adresat);
            Transport.send(message);

            System.out.println("Done");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
