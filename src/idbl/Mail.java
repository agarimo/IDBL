package idbl;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Agarimo
 */
public class Mail {

    String msg;

    public Mail() {
        this.msg = "Ha ocurrido un error durante la carga de Multas.\n"
                + "Consulte el Log para más información.";
    }

    public void run() {
        try {
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", "idblnotify@gmail.com");
            props.setProperty("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress("idblnotify@gmail.com"));
            message.addRecipient(Message.RecipientType.TO,new InternetAddress("carlos.datamer@gmail.com"));
            message.setSubject("IDBL ERROR");
            message.setText(this.msg);

            Transport t = session.getTransport("smtp");
            t.connect("idblnotify@gmail.com", "Port@tebien84");
            t.sendMessage(message, message.getAllRecipients());

            t.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
