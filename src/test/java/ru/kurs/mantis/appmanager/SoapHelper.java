package ru.kurs.mantis.appmanager;

import biz.futureware.mantis.rpc.soap.client.*;
import ru.kurs.mantis.model.Issue;
import ru.kurs.mantis.model.Project;

import javax.xml.rpc.ServiceException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by yana on 4/14/2016.
 */
public class SoapHelper {
    private ApplicationManager app;
    private final String login;
    private final String passwd;
    public SoapHelper(ApplicationManager app) {
        this.app = app;
        login = app.getProperty("web.adminLogin");
        passwd = app.getProperty("web.adminPassword");
    }
    public Set<Project> getProjects() throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mc = getMantisConnect();
        ProjectData[] projects = mc.mc_projects_get_user_accessible(login, passwd);
        return Arrays.asList(projects).stream().map((p) -> new Project().withId(p.getId().intValue()).withName(p.getName()))
                .collect(Collectors.toSet());
    }

    private MantisConnectPortType getMantisConnect() throws ServiceException, MalformedURLException {
        return new MantisConnectLocator()
                    .getMantisConnectPort(new URL(app.getProperty("web.baseUrl") + "/api/soap/mantisconnect.php"));
    }

    public IssueData getIssue(BigInteger id) throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mc = getMantisConnect();
        IssueData data = mc.mc_issue_get(login, passwd, id);
        return data;
    }

    public boolean isIssueOpen (int issueId)  throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mc = getMantisConnect();
        //String username = app.getProperty("web.adminUsername");
        //String password = app.getProperty("web.adminPassword");
        IssueData issue = mc.mc_issue_get(login, passwd, BigInteger.valueOf(issueId));
        String status = issue.getStatus().getName();
        if (status.equals("closed")||status.equals("resolved")){
            return false;
        }
        return true;
    }

    public Issue addIssue(Issue issue) throws MalformedURLException, ServiceException, RemoteException {
        MantisConnectPortType mc = getMantisConnect();
        String[] categories = mc.mc_project_get_categories(login, passwd, BigInteger.valueOf(issue.getProject().getId()));
        IssueData issueData = new IssueData();
        issueData.setSummary(issue.getSummary());
        issueData.setDescription(issue.getDescription());
        issueData.setProject(new ObjectRef(BigInteger.valueOf(issue.getProject().getId()), issue.getProject().getName()));
        issueData.setCategory(categories[0]);
        BigInteger issueId = mc.mc_issue_add(login, passwd, issueData);
        IssueData createdIssueData = mc.mc_issue_get(login, passwd, issueId);
        return new Issue().withId(createdIssueData.getId().intValue()).withSummary(createdIssueData.getSummary())
                .withDescription(createdIssueData.getDescription()).withProject(new Project()
                        .withId(createdIssueData.getProject().getId().intValue())
                        .withName(createdIssueData.getProject().getName()));
    }
}
