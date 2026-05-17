package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;

public class CoursesPage extends BasePage {

    public CoursesPage(WebDriver driver) {
        super(driver);
    }

    // Elnavigálunk a Selenium saját hivatalos dropdown tesztoldalára
    public void testDropdownMenu() {
        driver.get("https://www.selenium.dev/selenium/web/web-form.html");
        Select selectMenu = new Select(driver.findElement(By.name("my-select")));
        selectMenu.selectByValue("2"); // Kiválasztjuk a kettes opciót
    }
}
