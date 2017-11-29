package driver;

import driver.extentReport.ExtentReportManager;
import driver.helpers.WebDriverHelper;
import io.github.bonigarcia.wdm.ChromeDriverManager;
import io.github.bonigarcia.wdm.EdgeDriverManager;
import io.github.bonigarcia.wdm.FirefoxDriverManager;
import io.github.bonigarcia.wdm.InternetExplorerDriverManager;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.proxy.CaptureType;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BaseWebTest extends ExtentReportManager {

    public static BrowserMobProxy proxy;
    public static WebDriverWait wait;
    public static WebDriverHelper webDriverHelper;
    public static String cbtUsername;
    public static String cbtAuthKey;
    public static WebDriver driver;


    /**
     * Parameter Expected:
     * proxyOn - boolean (lower case)
     * driver - webDriver
     * capabilities - example: "javaScriptEnabled=true,unexpectedAlertBehaviour=accept"
     * browser - chrome, firefox, ieEdge, or ie
     *
     * @param context
     */
    @BeforeSuite(alwaysRun = true)
    public void setUp(ITestContext context) throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        String proxyOn = context.getCurrentXmlTest().getParameter("proxyOn");

        //Throw an exception if the test does not require WebDriver
        //Set Proxy if true
        try {
            if (proxyOn.equals("true") && !(proxyOn.isEmpty())) {
                proxy = new BrowserMobProxyServer();
                proxy.setTrustAllServers(true);
                proxy.start(0);
                proxy.getHarCaptureTypes();
                Assert.assertTrue(proxy.isStarted());
                Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
                proxy.enableHarCaptureTypes(CaptureType.REQUEST_CONTENT, CaptureType.REQUEST_COOKIES, CaptureType.REQUEST_HEADERS, CaptureType.RESPONSE_HEADERS);
                capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);
            }

            //Get String of capabilities ("javaScriptEnabled=true,unexpectedAlertBehaviour=accept)
            String capability = context.getCurrentXmlTest().getParameter("capabilities");
            if (!(capability.isEmpty())) {
                for (Map.Entry<String, String> map : readCapabilities(capability).entrySet()) {
                    capabilities.setCapability(map.getKey().trim(), map.getValue().trim());
                }
            }
            capabilities.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            capabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);

            if (context.getCurrentXmlTest().getParameter("driver").contains("cbt")) {
                StringBuilder cbtUsernameBuilder = new StringBuilder();
                cbtUsernameBuilder.append(context.getCurrentXmlTest().getParameter("username")).append("%40creditcards.com");
                cbtUsername = cbtUsernameBuilder.toString();
                cbtAuthKey = context.getCurrentXmlTest().getParameter("authkey");
                driver = new RemoteWebDriver(new URL("http://" + cbtUsername + ":" + cbtAuthKey + "@hub.crossbrowsertesting.com:80/wd/hub"), capabilities);
            }
            if (context.getCurrentXmlTest().getParameter("driver").contains("webDriver")) {
                String browser = context.getCurrentXmlTest().getParameter("browser");
                switch (browser) {
                    case "chrome":
                        ChromeDriverManager.getInstance().setup();
                        driver = new ChromeDriver(capabilities);
                        break;
                    case "firefox":
                        FirefoxDriverManager.getInstance().setup();
                        driver = new FirefoxDriver(capabilities);
                        break;
                    case "ieEdge":
                        EdgeDriverManager.getInstance().setup();
                        driver = new EdgeDriver(capabilities);
                        break;
                    case "ie":
                        InternetExplorerDriverManager.getInstance().setup();
                        driver = new InternetExplorerDriver(capabilities);
                        break;
                    case "safari":
                        driver = new SafariDriver(capabilities);
                        break;
                    default:
                        ChromeDriverManager.getInstance().setup();
                        driver = new ChromeDriver(capabilities);
                        break;
                }
            } else {
                ChromeDriverManager.getInstance().setup();
                driver = new ChromeDriver(capabilities);
            }

            driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
            wait = new WebDriverWait(driver, 120);
            webDriverHelper = new WebDriverHelper(driver);
        } catch (NullPointerException e) {
            ChromeDriverManager.getInstance().setup();
            driver = new ChromeDriver(capabilities);
            driver.manage().timeouts().pageLoadTimeout(120, TimeUnit.SECONDS);
            wait = new WebDriverWait(driver, 120);
            webDriverHelper = new WebDriverHelper(driver);
        }
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
            driver.quit();
            proxy.stop();
        } catch (Exception e) {

        }
    }

    @AfterMethod(alwaysRun = true)
    public void takeScreenShotOnFailure(ITestResult testResult) throws IOException {
        if (testResult.getStatus() == ITestResult.FAILURE) {
            WebDriverHelper helper = new WebDriverHelper(driver);
            helper.captureScreen();
        }
    }

}
