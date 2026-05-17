package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoginPage extends BasePage {

    // Tisztán strukturált, pontos XPath kifejezések a tesztoldalhoz
    private final By usernameField = By.xpath("//input[@id='username' or @name='username']");
    private final By passwordField = By.xpath("//input[@id='password' or @name='password']");
    private final By submitButton = By.xpath("//button[@id='submit' or contains(text(), 'Submit')]");
    
    // Sikeres bejelentkezés után megjelenő üzenet azonosítása (Assertion-höz)
    private final By successMessage = By.xpath("//h1[contains(@class, 'post-title') or contains(text(), 'Logged In')]");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void login(String username, String password) {
        // Megvárjuk, amíg a felhasználónév mező láthatóvá válik, majd kitöltjük
        WebElement userInput = wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        userInput.clear();
        userInput.sendKeys(username);

        // Kitöltjük a jelszót
        WebElement passInput = wait.until(ExpectedConditions.visibilityOfElementLocated(passwordField));
        passInput.clear();
        passInput.sendKeys(password);

        // Rákattintunk a Submit gombra
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(submitButton));
        btn.click();
    }

    // Segédfunkció, amivel ellenőrizzük, hogy sikeres volt-e a belépés
    public boolean isLoginSuccessful() {
        try {
            WebElement message = wait.until(ExpectedConditions.visibilityOfElementLocated(successMessage));
            return message.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}