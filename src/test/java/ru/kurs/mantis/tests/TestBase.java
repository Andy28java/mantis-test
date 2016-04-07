package ru.kurs.mantis.tests;


import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.BrowserType;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import ru.kurs.mantis.appmanager.ApplicationManager;

import static org.testng.Assert.assertTrue;

/**
 * Created by yana on 3/1/2016.
 */
public class TestBase {

    protected static final ApplicationManager app;

    static {
        String browser = System.getProperty("browser", "firefox");

        if (browser == null || browser.isEmpty()) {
            throw new RuntimeException("No browser property");
        }
        //= new ApplicationManager(BrowserType.FIREFOX);
        app = new ApplicationManager(System.getProperty("browser", BrowserType.CHROME));
    }

    protected WebDriver wd = null;

    public static boolean isAlertPresent(FirefoxDriver wd) {
        try {
            wd.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private void closeAlertAndGetItsText() {
        try {
            Alert a = wd.switchTo().alert();
            assertTrue(a != null, "Expected alert does not occur");

            a.accept();

        } catch (NoAlertPresentException e) {
            assertTrue(false, "Expected alert does not occur");
        }
    }

    @BeforeSuite
    public void setUp() throws Exception {
        System.out.println("app:" + app);
        app.init();

        //wd = app.getWebDriver();
    }

    @AfterSuite
    public void tearDown() {
        app.stop();
    }

    }