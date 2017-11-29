package driver.helpers;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.PageFactory;

public class PageObjectHelper {
    protected WebDriver driver;
    protected AppiumDriver appiumDriverdriver;

    public PageObjectHelper(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public PageObjectHelper(AppiumDriver driver) {
        this.appiumDriverdriver = driver;
        PageFactory.initElements(new AppiumFieldDecorator(driver), this);
    }
}

