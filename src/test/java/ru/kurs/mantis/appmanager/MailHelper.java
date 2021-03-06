package ru.kurs.mantis.appmanager;

import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;
import javax.mail.MessagingException;

import javax.mail.internet.MimeMessage;
import ru.kurs.mantis.model.MailMessage;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by yana on 4/8/2016.
 */
public class MailHelper {
    private  ApplicationManager app;
    private  final Wiser wiser;

    public MailHelper(ApplicationManager app) {
        this.app = app;
        wiser = new Wiser();
        /*try {
            wiser.getServer().setBindAddress(Inet4Address.getLocalHost());
        } catch  (UnknownHostException e) {
            e.printStackTrace(System.out);
        }
        wiser.setHostname("localhost");
        wiser.setPort(25);
        */
    }

    public List<MailMessage> waitForMail(int count, long timeout) throws MessagingException, IOException {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() < start + timeout){
            if (wiser.getMessages().size() >= count) {
                return wiser.getMessages().stream().map((m) -> toModelMail(m)).collect(Collectors.toList());
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        throw new Error("No mail : (");
    }

    public static MailMessage toModelMail(WiserMessage m) {
        try{
            MimeMessage mm = m.getMimeMessage();
        return new MailMessage(mm.getAllRecipients()[0].toString(), (String) mm.getContent());
    } catch (MessagingException e)  {
        e.printStackTrace();
        return null;
    } catch (IOException e) {
        e.printStackTrace();
        return null;
    }
   }
    public void start() {wiser.start();}
    public void stop() {wiser.stop();}
}
