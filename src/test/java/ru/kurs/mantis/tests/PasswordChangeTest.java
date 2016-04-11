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
 * Created by yana on 4/11/2016.
 */
public class PasswordChangeTest extends TestBase{

        @BeforeMethod
        public void startMailServer(){
            app.mail().start();
        }

        @Test
        public void testPasswordChange () throws IOException, MessagingException {
            app.admin().startResetPassword();
            String email = app.admin().getUserEmail();
            System.out.println(email);
            String username = app.admin().getUsername();
            System.out.println(username);
            app.admin().resetPassword();
            List<MailMessage> mailMessages = app.mail().waitForMail(1, 20000);
            String confirmationLink = findConfirmationLink(mailMessages, email);
            String newPassword = "newpassword";
            app.admin().completeResetPassword(confirmationLink, newPassword);
            assertTrue(app.newSession().login(username, newPassword));
        }

    private String findConfirmationLink(List<MailMessage> mailMessages, String email) {
        MailMessage mailMessage = mailMessages.stream().filter((m) -> m.to.equals(email)).findFirst().get();
        VerbalExpression regex = VerbalExpression.regex().find("http://").nonSpace().oneOrMore().build();
        return regex.getText(mailMessage.text);
    }

    @AfterMethod(alwaysRun = true)
        public void stopMailServer(){
            app.mail().stop();
        }
    }

