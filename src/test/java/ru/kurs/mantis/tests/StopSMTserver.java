package ru.kurs.mantis.tests;

import org.testng.annotations.Test;
import ru.kurs.mantis.appmanager.HelperBase;

/**
 * Created by yana on 4/9/2016.
 */
public class StopSMTserver extends TestBase {
    @Test
    /** Stops the SMTP Server */
       public void stopMailServer(){
        app.mail().stop();
    }
}
