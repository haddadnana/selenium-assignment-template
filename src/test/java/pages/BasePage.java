package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;

    // Ez a konstruktor, ami minden aloldal megnyitásakor elindul
    public BasePage(WebDriver driver) {
        this.driver = driver;
        // Beállítunk egy maximum 10 másodperces intelligens várakozást az elemekre
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Segédfunkció, amivel bármelyik oldal meg tud majd nyitni egy URL-t
    public void visit(String url) {
        driver.get(url);
    }
}