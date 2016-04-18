package ru.kurs.mantis.tests;

import biz.futureware.mantis.rpc.soap.client.IssueData;
import org.testng.SkipException;
import org.testng.annotations.BeforeTest;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.rmi.RemoteException;

/**
 * Created by yana on 4/15/2016.
 */
public class TestForBug extends TestBase {
    public final BigInteger bugId;

    public TestForBug(BigInteger id) {
        bugId = id;
    }

    private boolean isIssueOpen(String status) {
        return "resolved".equals(status) || "closed".equals(status);
    }
    @BeforeTest
    public  void testGetStatus() throws RemoteException, ServiceException, MalformedURLException {
        IssueData data = app.soap().getIssue(bugId);
        String status = data.getStatus().getName();

        if (!isIssueOpen(status)) {
            throw new SkipException("Bug is not fixed yet");
        }
    }
}

