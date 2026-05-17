package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    // 1. Komplett XPath a süti elfogadó gombhoz
    private final By cookieAcceptButton = By.xpath("//button[@id='onetrust-accept-btn-handler']");
    
    // 2. Az új, bombabiztos, rugalmas XPath a bejelentkezési gombhoz
    private final By loginIconLink = By.xpath("//header//a[contains(@href, '/account/login') or contains(@href, '/login') or .//*[local-name()='svg']]");

    // Konstruktor, ami átadja a böngészőt a szülő BasePage-nek
    public HomePage(WebDriver driver) {
        super(driver);
    }

    // Funkció a sütik elfogadására
    public void acceptCookiesIfPresent() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(cookieAcceptButton));
            button.click();
            System.out.println("Sütik sikeresen elfogadva.");
        } catch (Exception e) {
            System.out.println("Süti banner nem jelent meg, haladunk tovább.");
        }
    }

    // EZ A METÓDUS KELL A FÁJL VÉGÉRE, HOGY KATTINTANI TUDJUNK!
    public void clickLoginIcon() {
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(loginIconLink));
        loginBtn.click();
    }
}