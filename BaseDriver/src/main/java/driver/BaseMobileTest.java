package driver;

import com.mashape.unirest.http.Unirest;
import driver.extentReport.ExtentReportManager;
import driver.helpers.WebDriverHelper;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class BaseMobileTest extends ExtentReportManager {
    public static AppiumDriver appiumDriver;
    public static BrowserMobProxy proxy;
    public static WebDriverWait wait;

    @BeforeSuite
    public void setUpDriver(ITestContext context) throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String proxyOn = context.getCurrentXmlTest().getParameter("proxyOn");
        String driverSetting = context.getCurrentXmlTest().getParameter("driver");

        //Throw an exception if the test does not require WebDriver
        //Set Proxy if true
        if (proxyOn.equals("true")) {
            proxy = new BrowserMobProxyServer();
            proxy.setTrustAllServers(true);
            proxy.start(0);
            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
            proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.REQUEST_COOKIES, CaptureType.REQUEST_HEADERS, CaptureType.RESPONSE_HEADERS);
            capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
        }

        //Get String of capabilities ("javaScriptEnabled=true,unexpectedAlertBehaviour=accept)
        String capability = context.getCurrentXmlTest().getParameter("capabilities");
        if (!(capability.isEmpty())) {
            for (Map.Entry<String, String> map : readCapabilities(capability).entrySet()) {
                capabilities.setCapability(map.getKey(), map.getValue());
            }
        }
        capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        URL url = null;
        if (driverSetting.equals("appium")) {
            String testing = context.getCurrentXmlTest().getParameter("testingApp");
            String osSetting = context.getCurrentXmlTest().getParameter("mobilePlatform");
            url = new URL(context.getCurrentXmlTest().getParameter("hubURL"));
            System.out.println(testing);
            capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, context.getCurrentXmlTest().getParameter("deviceName"));
            switch (testing) {
                case "web":
                    capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, context.getCurrentXmlTest().getParameter("browser"));
                    break;
                case "native":
                    capabilities.setCapability(MobileCapabilityType.APP, context.getCurrentXmlTest().getParameter("appPath"));
                    break;
                default:
                    Assert.fail("Please select if the application you are testing, a web or a native application.");
                    break;
            }
            switch (osSetting) {
                case "android":
                    capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "Android");
                    appiumDriver = AppiumDriverBuilder.forAndroid().withCapabilities(capabilities).withEndpoint(url).build();
                    break;
                case "iOS":
                    capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, "iOS");
                    capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, "XCUITest");
                    appiumDriver = AppiumDriverBuilder.forIOS().withCapabilities(capabilities).withEndpoint(url).build();
                    break;
                default:
                    Assert.fail("Please select an operating system for the mobile");
                    break;
            }
        }
        wait = new WebDriverWait(appiumDriver, 20);
    }

    /**
     * Convert a text string into a map
     *
     * @param text
     * @return capabilities - Map <String, String>
     */
    private Map<String, String> readCapabilities(String text) {
        Map<String, String> capabilities = new HashMap<>();
        String[] array = text.split(",");
        for (String item : array) {
            String[] capability = item.split("=");
            capabilities.put(capability[0], capability[1]);
        }
        return capabilities;
    }

    @AfterSuite(alwaysRun = true)
    public void full_shutdown() {
        try {
            appiumDriver.quit();
            proxy.stop();
            Unirest.shutdown();
        } catch (Exception e) {

        }
    }

    @AfterMethod(alwaysRun = true)
    public void takeScreenShotOnFailure(ITestResult testResult) throws IOException {
        if (testResult.getStatus() == ITestResult.FAILURE) {
            WebDriverHelper helper = new WebDriverHelper(appiumDriver);
            helper.captureScreen();
        }
    }
}
