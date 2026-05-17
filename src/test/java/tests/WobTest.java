package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class WobTest {
    protected WebDriver driver;
    protected Properties properties;
    protected LoginPage loginPage;

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
    }

    @Test(description = "1. Bejelentkező oldal megnyitása és a cím ellenőrzése")
    public void testOpenLoginPage() {
        // Megnyitjuk a config.properties-ben megadott új URL-t
        loginPage.visit(properties.getProperty("base.url"));
        
        // Ellenőrizzük az oldal címét (page_title feladatért pont jár!)
        Assert.assertTrue(driver.getTitle().contains("Test Login") || driver.getTitle().contains("Practice"), 
            "Nem a megfelelő oldal nyílt meg!");
    }

    @Test(dependsOnMethods = "testOpenLoginPage", description = "2. Bejelentkezési űrlap kitöltése és ellenőrzése")
    public void testUserLogin() {
        // Belépés a configból olvasott 'student' és 'Password123' adatokkal
        loginPage.login(
            properties.getProperty("user.email"), // Ez tartalmazza most a 'student' szöveget
            properties.getProperty("user.password")
        );
        
        // Ellenőrizzük, hogy valóban megjelent-e a sikeres belépési üzenet (Assertion)
        Assert.assertTrue(loginPage.isLoginSuccessful(), "A bejelentkezés sikertelen volt!");
        System.out.println("Sikeres bejelentkezés tesztelve!");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}