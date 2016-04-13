package ru.kurs.mantis.appmanager;

//import org.openqa.selenium.By;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
//import org.openqa.selenium.WebElement;
import java.util.List;

/**
 * Created by yana on 4/11/2016.
 */
public class AdminHelper extends HelperBase {

        public AdminHelper(ApplicationManager app) {
            super(app);
        }

        public void loginAs(String username, String password) {
            wd.get(app.getProperty("web.baseUrl") + "login_page.php");
            type(By.name("username"),username);
            type(By.name("password"), password);
            click(By.cssSelector("input[value='Login']"));
        }
          public void manageUsers() {
            if (isElementPresent(By.cssSelector("input[value='Create New Account']"))){
                return;
           }
            click(By.linkText("Manage Users"));
          }

    public void selectUser() {

        List<WebElement> elements = wd.findElements(By.xpath(".//table[3]/tbody/tr[position() > 2]/td[1]/a"));
        System.out.println(elements);
            WebElement chosenUser = elements.stream().filter((e) -> ! e.getText().equals("administrator")).findAny().get();
            chosenUser.click();
        }

        public void resetPassword() {
            wd.findElement(By.cssSelector("input[value='Reset Password']")).click();
        }

        public void startResetPassword() {
            String username = app.getProperty("web.adminLogin");
            String password = app.getProperty("web.adminPassword");
            loginAs(username, password);
            manageUsers();
            selectUser();
        }

        public String getUserEmail() {
            WebElement email = wd.findElement(By.name("email"));
            return email.getAttribute("value");
        }

        public void completeResetPassword(String confirmationLink, String newPassword) {
            wd.get(confirmationLink);
            type(By.name("password"), newPassword);
            type(By.name("password_confirm"), newPassword);
            click(By.cssSelector("input[value='Update User']"));
        }

        public String getUsername() {
            WebElement username = wd.findElement(By.name("username"));
            return username.getAttribute("value");
        }


}
