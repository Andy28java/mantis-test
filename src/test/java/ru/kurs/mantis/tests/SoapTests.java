package ru.kurs.mantis.tests;

import org.testng.annotations.Test;
import ru.kurs.mantis.model.Issue;
import ru.kurs.mantis.model.Project;

import javax.xml.rpc.ServiceException;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.util.Set;

import static org.testng.Assert.assertEquals;


/**
 * Created by yana on 4/14/2016.
 */
public class SoapTests extends TestBase{

    @Test
    public void testGetProjects() throws ServiceException, MalformedURLException, RemoteException {
        Set<Project> projects = app.soap().getProjects();
        System.out.println(projects.size());
        for (Project project : projects) {
            System.out.println(project.getName());

        }
    }

    @Test
    public void testCreateIssue()throws ServiceException, MalformedURLException, RemoteException {
        Set<Project> projects = app.soap().getProjects();
        Issue issue = new Issue().withSummary("Test issue")
                .withDescription("Test issue description").withProject(projects.iterator().next());
        Issue created = app.soap().addIssue(issue);
        assertEquals(issue.getSummary(), created.getSummary());
    }

}
