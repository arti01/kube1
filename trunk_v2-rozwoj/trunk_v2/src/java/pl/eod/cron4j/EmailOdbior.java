/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pl.eod.cron4j;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeUtility;
import org.apache.commons.io.IOUtils;

@ManagedBean(name = "EmailOdbior")
@ApplicationScoped
public class EmailOdbior {

    private List<EmailMoj> emaile;
    private static final int MAXMAIL = 30;
    private static final int KROKPOBIERANIA = 5;
    private final Properties props;
    private final Session session;

    public EmailOdbior() {
        this.props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");
        props.setProperty("mail.mime.decodetext.strict", "false");
        session = Session.getDefaultInstance(props, null);
        emaile = new ArrayList<>();
    }

    @PostConstruct
    void init() {
        try {
            odbierz();
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(EmailOdbior.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void autoObsluga() throws MessagingException, NoSuchProviderException, IOException {
        odbierz();
        czyscKolejke();
    }

    private void odbierz() throws NoSuchProviderException, MessagingException, UnsupportedEncodingException, IOException {
        Folder folder = null;
        Store store = null;
        if (emaile.size() >= MAXMAIL) {
            return;
        }
        try {
            //session.setDebug(true);
            store = session.getStore("imaps");
            store.connect("imap.googlemail.com", "arti4077@gmail.com", "4077Atos");
            folder = store.getFolder("Inbox");
            /* Others GMail folders :
       * [Gmail]/All Mail   This folder contains all of your Gmail messages.
       * [Gmail]/Drafts     Your drafts.
       * [Gmail]/Sent Mail  Messages you sent to other people.
       * [Gmail]/Spam       Messages marked as spam.
       * [Gmail]/Starred    Starred messages.
       * [Gmail]/Trash      Messages deleted from Gmail.
             */
            folder.open(Folder.READ_ONLY);
            Message messages[] = folder.getMessages();
            //System.out.println("No of Messages : " + folder.getMessageCount());
            //System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());

            int naRaz = KROKPOBIERANIA;
            przegladanieKolejki:
            for (int i = messages.length - 1; i > 0 && messages.length - naRaz <= i; --i) {
                //System.out.println("MESSAGE #" + (i + 1) + ":");
                Message msg = messages[i];
                /*
          if we don''t want to fetch messages already processed
          if (!msg.isSet(Flags.Flag.SEEN)) {
             String from = "unknown";
             ...
          }
                 */
                String from = "unknown";

                /*if (msg.getReplyTo().length >= 1) {
                    from = msg.getReplyTo()[0].toString();
                } else if (msg.getFrom().length >= 1) {
                    from = msg.getFrom()[0].toString();
                }*/
                if (msg.getFrom().length >= 1) {
                    from = msg.getFrom()[0].toString();
                }
                from = MimeUtility.decodeText(from);
                String subject = msg.getSubject();
                //System.out.println("Saving ... " + subject + " " + from);
                //sprawdzenie czy juz jest
                for (EmailMoj emm : emaile) {
                    if (Arrays.equals(emm.getIdMsg(), msg.getHeader("Message-Id"))) {
                        naRaz++;
                        continue przegladanieKolejki;
                    }
                }
                EmailMoj em = new EmailMoj();
                em.setIdMsg(msg.getHeader("Message-Id"));
                em.setNadawca(from);
                em.setTemat(subject);
                em.setData(msg.getSentDate());
                em.setTresc(getText(msg));
                em.setZalaczniki(getZalaczniki(msg));
                emaile.add(em);
                //System.err.println("dodalem " + em.getTemat());
                // you may want to replace the spaces with "_"
                // the TEMP directory is used to store the files
                //String filename = "c:/temp/" + subject;
                //saveParts(msg.getContent(), filename);
                //msg.setFlag(Flags.Flag.SEEN, true);
                // to delete the message
                // msg.setFlag(Flags.Flag.DELETED, true);
            }
        } finally {
            if (folder != null) {
                folder.close(true);
            }
            if (store != null) {
                store.close();
            }
        }
    }

    private String getText(Part p) throws
            MessagingException, IOException {

        if (p.isMimeType("text/*")) {
            String s = (String) p.getContent();
            //textIsHtml = p.isMimeType("text/html");
            return s;
        }

        if (p.isMimeType("multipart/alternative")) {
            // prefer html text over plain text
            Multipart mp = (Multipart) p.getContent();
            String text = null;
            for (int i = 0; i < mp.getCount(); i++) {
                Part bp = mp.getBodyPart(i);
                if (bp.isMimeType("text/plain")) {
                    if (text == null) {
                        text = getText(bp);
                    }
                } else if (bp.isMimeType("text/html")) {
                    String s = getText(bp);
                    if (s != null) {
                        return s;
                    }
                } else {
                    return getText(bp);
                }
            }
            return text;
        } else if (p.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) p.getContent();
            for (int i = 0; i < mp.getCount(); i++) {
                String s = getText(mp.getBodyPart(i));
                if (s != null) {
                    return s;
                }
            }
        }
        return null;
    }

    private List<EmailZalacznik> getZalaczniki(Message msg) throws IOException, MessagingException {
        List<EmailZalacznik> wynik = new ArrayList<>();
        if (!msg.isMimeType("multipart/*")) {
            return wynik;
        }
        Multipart multipart = (Multipart) msg.getContent();
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bodyPart = multipart.getBodyPart(i);
            if (!Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition())
                    && bodyPart.getFileName() == null) {
                continue; // dealing with attachments only
            }
            System.err.println(MimeUtility.decodeText(bodyPart.getFileName()));
            EmailZalacznik ez = new EmailZalacznik();
            ez.setPlik(IOUtils.toByteArray(bodyPart.getInputStream()));
            ez.setNazwa(MimeUtility.decodeText(bodyPart.getFileName()));
            wynik.add(ez);
        }
        return wynik;
    }

    private void czyscKolejke() throws MessagingException {
        Folder folder = null;
        Store store = null;
        List<EmailMoj> nowaKolejka = new ArrayList<>();
        try {
            store = session.getStore("imaps");
            store.connect("imap.googlemail.com", "arti4077@gmail.com", "4077Atos");
            folder = store.getFolder("Inbox");
            folder.open(Folder.READ_ONLY);
            Message messages[] = folder.getMessages();

            for (EmailMoj em : emaile) {
                for (int i = messages.length - 1; i > 0 && (messages.length - i - 1) < MAXMAIL; --i) {
                    Message msg = messages[i];
                    if (Arrays.equals(em.getIdMsg(), msg.getHeader("Message-Id"))) {
                        nowaKolejka.add(em);
                        break;
                    }
                }
            }
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(EmailOdbior.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(EmailOdbior.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (folder != null) {
                folder.close(true);
            }
            if (store != null) {
                store.close();
            }
        }
        emaile.clear();
        emaile.addAll(nowaKolejka);
    }

    public List<EmailMoj> getEmaile() {
        return emaile;
    }

    public void setEmaile(List<EmailMoj> emaile) {
        this.emaile = emaile;
    }

}
