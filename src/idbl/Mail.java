package idbl;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author Agarimo
 */
public class Mail {

    private static final org.apache.logging.log4j.Logger log = LogManager.getLogger(Mail.class);
    private final String to;
    private final String subject;
    private final String msg;
    InetAddress localHost;

    public Mail() {
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            log.error("GETIP - "+ex);
        }
        
        this.to="carlos.datamer@gmail.com,agarimosoft@gmail.com";
//        this.to="noseponuncanada@gmail.com";
        this.subject = "IDBL ERROR on "+localHost.getHostName()+" - "+localHost.getHostAddress();
        this.msg = "Ha ocurrido un error durante la carga de Multas.\n"
                + "Consulte el Log para más información.";
    }

    public Mail(String subject, String msg) {
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException ex) {
            log.error("GETIP - "+ex);
        }
        
        this.to="carlos.datamer@gmail.com,agarimosoft@gmail.com";
//        this.to="noseponuncanada@gmail.com";
        this.subject = subject+" on "+localHost.getHostName()+" - "+localHost.getHostAddress();
        this.msg = msg;
    }

    public void run() throws Exception {
            Properties props = new Properties();
            props.setProperty("mail.smtp.host", "smtp.gmail.com");
            props.setProperty("mail.smtp.starttls.enable", "true");
            props.setProperty("mail.smtp.port", "587");
            props.setProperty("mail.smtp.user", "idblnotify@gmail.com");
            props.setProperty("mail.smtp.auth", "true");

            Session session = Session.getDefaultInstance(props);
            
            String destinos[] = this.to.split(",");

            MimeMessage message = new MimeMessage(session);
            
            message.setFrom(new InternetAddress("idblnotify@gmail.com"));
            
            Address[] receptores = new Address[destinos.length];
            int j = 0;
            while (j < destinos.length) {
                receptores[j] = new InternetAddress(destinos[j]);
                j++;
            }
            
            message.addRecipients(Message.RecipientType.TO, receptores);
            message.setSubject(this.subject);
            message.setText(this.msg);

            Transport t = session.getTransport("smtp");
            t.connect("idblnotify@gmail.com", "Port@tebien84");
            t.sendMessage(message, message.getAllRecipients());

            t.close();
    }
}
