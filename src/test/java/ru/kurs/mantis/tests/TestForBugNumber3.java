package ru.kurs.mantis.tests;

import org.testng.SkipException;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * Created by yana on 4/16/2016.
 */
   /* public class TestForBugNumber3 extends TestForBug {
        public TestForBugNumber3() {
            super(BigInteger.valueOf(3));
        }

        @Test
        public void doTest() {
            System.out.println("Done for bug: " + bugId);
        }
    }*/

public class TestForBugNumber3 extends TestBase {
    public BigInteger bugId;

    public TestForBugNumber3() {
        bugId = BigInteger.valueOf(3);
    }

    @BeforeTest
    public void checkStatus() throws RemoteException, ServiceException, MalformedURLException {
       if (app.soap().isIssueOpen(bugId.intValue())) {
            throw new SkipException("Bug is not fixed yet");
        }
    }

    @Test
    public void doTest() {
        System.out.println("Done for bug: " + bugId);
    }
}



