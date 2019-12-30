package ejbs;

import exceptions.MyNoRecipientException;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless(name = "EmailEJB")
public class EmailBean {

    @Resource(name = "java:/jboss/mail/gmail")
    private Session mailSession;

    private static final Logger logger = Logger.getLogger("EmailBean.logger");

    public void send(String to, String subject, String text) throws MyNoRecipientException {
        if(to.equals("")){
            throw new MyNoRecipientException("Select at least one user to send e-mail");
        }
        Thread emailJob = new Thread(() -> {
            Message message = new MimeMessage(mailSession);
            Date timestamp = new Date();
            String[] recipientList = to.split(",");
            InternetAddress[] recipientAddress = new InternetAddress[recipientList.length];
            int counter = 0;
            try {
                //message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
                for (String recipient : recipientList) {
                    recipientAddress[counter] = new InternetAddress(recipient.trim());
                    counter++;
                }
                message.setRecipients(Message.RecipientType.TO, recipientAddress);
                message.setSubject(subject);
                message.setText(text);
                message.setSentDate(timestamp);
                Transport.send(message);
            } catch (MessagingException e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        });
        emailJob.start();
    }
}


