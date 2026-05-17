package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    // 1. XPath az email beviteli mezőhöz
    private final By emailField = By.xpath("//form//input[@type='email' or @name='email']");
    
    // 2. XPath a jelszó mezőhöz
    private final By passwordField = By.xpath("//form//input[@type='password' or @name='password']");
    
    // 3. XPath a bejelentkezés gombhoz (ellenőrzi, hogy a formon belüli gomb tartalmazza-e a 'Log in' szöveget)
    private final By loginButton = By.xpath("//form//button[@type='submit' and contains(., 'Log in')]");

    // Konstruktor
    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // A funkció, ami kitölti az adatokat és megnyomja a gombot
    public void login(String email, String password) {
        // Megvárjuk, amíg az email mező láthatóvá válik, majd kitöltjük
        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(emailField));
        emailInput.clear();
        emailInput.sendKeys(email);

        // Kitöltjük a jelszót
        WebElement passwordInput = driver.findElement(passwordField);
        passwordInput.clear();
        passwordInput.sendKeys(password);

        // Rákattintunk a belépés gombra
        WebElement submitBtn = driver.findElement(loginButton);
        submitBtn.click();
    }
}
