package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.LoggedInPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class WobTest {
    protected WebDriver driver;
    protected Properties properties;
    protected LoginPage loginPage;
    protected LoggedInPage loggedInPage;

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

    // ÚJ TESZTESET: Több oldalas iterációs teszt (multiple_page_test feladathoz!)
    @Test(dependsOnMethods = "testUserLogout", description = "4. Több aloldal ellenőrzése egy ciklusban")
    public void testMultiplePagesWithLoop() {
        // Definiálunk egy tömböt a különböző URL-ekkel
        String[] urlsToTest = {
            "https://practicetestautomation.com/",
            "https://practicetestautomation.com/practice/",
            "https://practicetestautomation.com/courses/"
        };

        // Végigmegyünk rajtuk egy ciklussal
        for (String pageUrl : urlsToTest) {
            driver.get(pageUrl);
            // Ellenőrizzük, hogy az oldal címe sikeresen beolvasható és nem üres
            String title = driver.getTitle();
            Assert.assertNotNull(title, "Az oldal címe null!");
            Assert.assertFalse(title.isEmpty(), "Az oldal címe üres ezen az URL-en: " + pageUrl);
            System.out.println("Sikeresen ellenőrizve: " + pageUrl + " -> Cím: " + title);
        }
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}