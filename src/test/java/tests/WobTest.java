package tests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.LoggedInPage;
import pages.ContactPage;
import pages.CoursesPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class WobTest {
    protected WebDriver driver;
    protected Properties properties;
    protected LoginPage loginPage;
    protected LoggedInPage loggedInPage;
    protected ContactPage contactPage;
    protected CoursesPage coursesPage;

    @BeforeClass
    public void setUp() throws IOException {
        properties = new Properties();
        FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
        properties.load(fis);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        try {
            driver = new RemoteWebDriver(new URL("http://docker-sandbox-selenium:4444/wd/hub"), options);
        } catch (Exception e) {
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
        }

        loginPage = new LoginPage(driver);
        loggedInPage = new LoggedInPage(driver);
        contactPage = new ContactPage(driver);
        coursesPage = new CoursesPage(driver);
    }

    @Test(description = "1. Bejelentkező oldal megnyitása és a cím ellenőrzése")
    public void testOpenLoginPage() {
        loginPage.visit(properties.getProperty("base.url"));
        Assert.assertTrue(driver.getTitle().contains("Test Login") || driver.getTitle().contains("Practice"), 
            "Nem a megfelelő oldal nyílt meg!");
    }

    @Test(dependsOnMethods = "testOpenLoginPage", description = "2. Bejelentkezési űrlap kitöltése")
    public void testUserLogin() {
        loginPage.login(
            properties.getProperty("user.email"), 
            properties.getProperty("user.password")
        );
        Assert.assertTrue(loginPage.isLoginSuccessful(), "A bejelentkezés sikertelen volt!");
    }

    @Test(dependsOnMethods = "testUserLogin", description = "3. Kijelentkezés végrehajtása és ellenőrzése")
    public void testUserLogout() {
        loggedInPage.clickLogout();
        Assert.assertTrue(driver.getCurrentUrl().contains("login"), "Nem sikerült a kijelentkezés!");
    }

    @Test(dependsOnMethods = "testUserLogout", description = "4. Több aloldal ellenőrzése egy ciklusban")
    public void testMultiplePagesWithLoop() {
        String[] urlsToTest = {
            "https://practicetestautomation.com/",
            "https://practicetestautomation.com/practice/",
            "https://practicetestautomation.com/courses/"
        };

        for (String pageUrl : urlsToTest) {
            driver.get(pageUrl);
            String title = driver.getTitle();
            Assert.assertNotNull(title);
            Assert.assertFalse(title.isEmpty());
        }
    }

    @Test(dependsOnMethods = "testMultiplePagesWithLoop", description = "5. Statikus oldal, Textarea, Dropdown, Radio és History tesztelése")
    public void testExtraFeatures() {
        // 1. Statikus oldal megnyitása és szöveg ellenőrzése (static_page_test)
        driver.get("https://practicetestautomation.com/contact/");
        Assert.assertTrue(driver.getPageSource().contains("Contact"), "A Contact szöveg nem található!");

        // 2. Textarea kitöltése (textarea)
        contactPage.fillComment("Automated Selenium Test Message");

        // 3. Dropdown kiválasztás (dropdown)
        coursesPage.testDropdownMenu();

        // 4. Rádió gomb kijelölése (radio_button)
        WebElement radio = driver.findElement(By.id("my-radio-2"));
        radio.click();

        // 5. Böngésző előzmények tesztelése (history_test)
        driver.navigate().back();
        driver.navigate().forward();

        System.out.println("Minden extra pont és haladó feladat sikeresen tesztelve!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}