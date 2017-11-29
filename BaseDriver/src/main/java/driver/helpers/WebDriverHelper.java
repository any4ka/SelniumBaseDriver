package driver.helpers;


import com.mashape.unirest.http.exceptions.UnirestException;
import io.appium.java_client.AppiumDriver;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;

import java.io.File;
import java.util.ArrayList;


/**
 * Created by akruglyanskaya on 7/19/17.
 */
@SuppressWarnings({"unused", "deprecation"})
public class WebDriverHelper {
    private WebDriver driver;
    private WebDriverWait wait;

    public WebDriverHelper(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, 120);
    }

    public void waitForElement(WebElement element) {
        wait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void waitForElementNotPresent(WebElement element) {
        try {
            wait.until(ExpectedConditions.not(ExpectedConditions.visibilityOf(element)));
        } catch (WebDriverException e) {
            e.getMessage();
        }
    }

    public void switchWindows() {
        ArrayList<String> tabs = new ArrayList<>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        Reporter.log("Switched to window to " + driver.getTitle());
    }

    public void captureScreen(String sessionId) {
        try {
            CBTHelper.takeSnapshot(sessionId);
        } catch (UnirestException e) {
            Assert.fail("Failed to capture screenshot: " + e.getMessage());
        }
    }

    public String captureScreen() {
        String path;
        try {
            WebDriver augmentedDriver = new Augmenter().augment(driver);
            File source = ((TakesScreenshot) augmentedDriver).getScreenshotAs(OutputType.FILE);
            path = "./screenshots/" + source.getName();
            FileUtils.copyFile(source, new File(path));
            return path;
        } catch (Exception e) {
            path = "Failed to capture screenshot: " + e.getMessage();
        }
        return path;
    }

    public boolean searchTextOnAllPage(String text) {
        return driver.getPageSource().contains(text);
    }

    public boolean elementIsPresent(WebElement element) {
        try {
            ExpectedConditions.visibilityOf(element);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void mobileScrollDownToViewElement(WebElement element) {
        try {
            Dimension dimensions = driver.manage().window().getSize();
            Double screenHeightStart = dimensions.getHeight() * 0.5;
            int scrollStart = screenHeightStart.intValue();
            Double screenHeightEnd = dimensions.getHeight() * 0.2;
            int scrollEnd = screenHeightEnd.intValue();
            for (int i = 0; i < dimensions.getHeight(); i++) {
                ((AppiumDriver) driver).swipe(0, scrollStart, 0, scrollEnd, 5000);
                if (element.getSize().width > 0)
                    break;
            }
        } catch (Exception e) {
            System.out.println("Testing on a Desktop");
        }
    }

    public void mobileScrollUpToViewElement(WebElement element) {
        Dimension dimensions = driver.manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * 0.5;
        int scrollStart = screenHeightStart.intValue();
        Double screenHeightEnd = dimensions.getHeight() * 0.2;
        int scrollEnd = screenHeightEnd.intValue();
        for (int i = 0; i < dimensions.getHeight(); i++) {
            ((AppiumDriver) driver).swipe(0, scrollEnd, 0, scrollStart, 5000);
            if (element.getSize().width > 0)
                break;
        }
    }

    public void mobileScrollToTheBottomOfPage() {
        Dimension dimensions = driver.manage().window().getSize();
        int screenHeightStart = dimensions.getHeight();
        Double screenHeightEnd = dimensions.getHeight() * 0.2;
        int scrollEnd = screenHeightEnd.intValue();
        ((AppiumDriver) driver).swipe(0, screenHeightStart, 0, scrollEnd, 5000);
    }

    @SuppressWarnings("deprecated")
    public void mobileScrollToTheTopOfPage() {
        Dimension dimensions = driver.manage().window().getSize();
        Double screenHeightStart = dimensions.getHeight() * 0.5;
        int scrollStart = screenHeightStart.intValue();
        Double screenHeightEnd = dimensions.getHeight() * 0.2;
        int scrollEnd = screenHeightEnd.intValue();
        ((AppiumDriver) driver).swipe(0, scrollEnd, 0, scrollStart, 5000);
    }

    public Object convertStringToJSON(String stringToParse) {
        JSONParser parser = new JSONParser();
        try {
            if (parser.parse(stringToParse) instanceof JSONArray) return parser.parse(stringToParse);
            return parser.parse(stringToParse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}

