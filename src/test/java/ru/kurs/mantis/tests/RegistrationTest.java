package ru.kurs.mantis.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.kurs.mantis.model.MailMessage;
import ru.lanwen.verbalregex.VerbalExpression;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

import static org.testng.Assert.assertTrue;

/**
 * Created by yana on 4/8/2016.
 */
public class RegistrationTest extends TestBase {
    @BeforeMethod
    public void startMailServer(){
        app.mail().start();
    }

    @Test
    public void  testRegistration() throws IOException, MessagingException {
        long now = System.currentTimeMillis();
        String email = String.format("user%s@localhost", now);
        String user = String.format("user%s", now);
        String password = "password";
        app.registration().start(user, email);
        List<MailMessage> mailMessages = app.mail().waitForMail(2, 12000);
        String confirmationLink = findConfirmationLink(mailMessages, email);
        app.registration().finish(confirmationLink, password);
        assertTrue(app.newSession().login(user, password));

    }

    public String findConfirmationLink(List<MailMessage> mailMessages, String email) {
        MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);
    }

    @AfterMethod(alwaysRun = true)
    public void stopMailServer(){
        app.mail().stop();
    }
}
