package driver.extentReport;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;

public class ExtentReportManager {
    public static ExtentTest testChild;
    private static ExtentReports extent;
    private static ExtentTest test;

    public synchronized static ExtentReports getReporter() {
        if (extent == null) {
            extent = new ExtentReports("extent.html", true);
        }

        return extent;
    }

    @BeforeSuite(alwaysRun = true)
    public void setReporter() {
        extent = getReporter();
    }

    @BeforeClass(alwaysRun = true)
    public void setTest(ITestContext testName) {
        test = extent.startTest(testName.getName());
    }

    @BeforeMethod(alwaysRun = true)
    public void setMethod(Method testMethod) {
        testChild = extent.startTest(testMethod.getName());
        test.appendChild(testChild);
    }

    @AfterMethod(alwaysRun = true)
    public void endMethod() {
        extent.endTest(testChild);
    }

    @AfterClass(alwaysRun = true)
    public void endClass() {
        extent.endTest(test);
    }

    @AfterSuite(alwaysRun = true)
    public void endReport() {
        extent.flush();
    }
}
