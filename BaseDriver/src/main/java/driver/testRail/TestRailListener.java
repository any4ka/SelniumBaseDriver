package driver.listners;

import com.relevantcodes.extentreports.LogStatus;
import driver.extentReport.ExtentReportManager;
import driver.helpers.WebDriverHelper;
import driver.testRail.APIClient;
import driver.testRail.TestRailCase;
import org.json.simple.JSONObject;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.lang.reflect.Method;
import java.util.NavigableMap;
import java.util.TreeMap;

public class TestRailListener extends ExtentReportManager implements ITestListener {

    private String testCaseNumber;
    private NavigableMap<String, String> data = new TreeMap<String, String>();

    @Override
    public void onTestStart(ITestResult iTestResult) {
        Reporter.log("~~~~~~ Start of Test: " + iTestResult.getName());
    }

    public void sendResults() {
        try {
            APIClient client = new APIClient("http://testrail.in.creditcards.com/");
            client.setUser("...");
            client.setPassword("...");
            JSONObject r = (JSONObject) client.sendPost("add_result/" + testCaseNumber, data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        testChild.log(LogStatus.PASS, iTestResult.getParameters().toString());
        Method method = iTestResult.getMethod().getConstructorOrMethod().getMethod();
        try {
            testCaseNumber = method.getAnnotation(TestRailCase.class).value();
            data.put("status_id", "1");
            data.put("comment", "Run by TestNG");
            data.put("elapsed", String.valueOf(iTestResult.getEndMillis() - iTestResult.getStartMillis()) + "s");
            sendResults();
        } catch (Exception e) {
        }
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        testChild.log(LogStatus.FAIL, iTestResult.getParameters().toString(), iTestResult.getThrowable());
        Method method = iTestResult.getMethod().getConstructorOrMethod().getMethod();
        try {
            testCaseNumber = method.getAnnotation(TestRailCase.class).value();
            data.put("status_id", "5");
            data.put("comment", "Run by TestNG");
            data.put("elapsed", String.valueOf(iTestResult.getEndMillis() - iTestResult.getStartMillis()) + "s");
            sendResults();
        } catch (Exception e) {
        }
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        testChild.log(LogStatus.SKIP, iTestResult.getParameters().toString());
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
    }
}
