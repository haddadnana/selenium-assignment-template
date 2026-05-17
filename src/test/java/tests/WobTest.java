package tests;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver; // EZ AZ ÚJ IMPORT
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.LoginPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL; // EZ AZ ÚJ IMPORT
import java.util.Properties;

public class WobTest {
    protected WebDriver driver;
    protected Properties properties;
    
    protected HomePage homePage;
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

        // A sima ChromeDriver helyett átirányítjuk a kérést a Selenium konténer felé
        try {
            driver = new RemoteWebDriver(new URL("http://docker-sandbox-selenium:4444/wd/hub"), options);
        } catch (Exception e) {
            // Ha a lokális hálózaton más néven futna, megpróbáljuk localhost-tal is
            driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
        }

        homePage = new HomePage(driver);
        loginPage = new LoginPage(driver);
    }

    @Test(description = "1. Nyissuk meg a főoldalt és fogadjuk el a sütiket")
    public void testHomePageAndCookies() {
        homePage.visit(properties.getProperty("base.url"));
        Assert.assertTrue(driver.getTitle().contains("World of Books") || driver.getTitle().contains("Wob"), 
            "Nem a megfelelő oldal nyílt meg!");
        homePage.acceptCookiesIfPresent();
    }

    @Test(dependsOnMethods = "testHomePageAndCookies", description = "2. Kattintsunk a loginra és lépjünk be")
    public void testUserLogin() {
        homePage.clickLoginIcon();
        loginPage.login(
            properties.getProperty("user.email"),
            properties.getProperty("user.password")
        );
        System.out.println("A bejelentkezési űrlap elküldve.");
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}