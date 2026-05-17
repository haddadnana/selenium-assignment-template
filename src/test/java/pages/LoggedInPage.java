package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LoggedInPage extends BasePage {

    // Pontos és tiszta XPath a kijelentkezés gombhoz
    private final By logoutButton = By.xpath("//a[contains(@class, 'wp-block-button__link') and contains(text(), 'Log out')]");

    public LoggedInPage(WebDriver driver) {
        super(driver);
    }

    // Funkció, ami rákattint a kijelentkezés gombra
    public void clickLogout() {
        WebElement btn = wait.until(ExpectedConditions.elementToBeClickable(logoutButton));
        btn.click();
    }
}
