public enum GeneratedText {
    PAGE_MOBILE("package com.${suite}.screens;\n" +
            "\n" +
            "import driver.helpers.AbstractScreen;\n" +
            "import io.appium.java_client.AppiumDriver;\n" +
            "import io.appium.java_client.MobileElement;\n" +
            "import io.appium.java_client.ios.IOSDriver;\n" +
            "import org.openqa.selenium.By;\n" +
            "\n" +
            "public class ${class} extends AbstractScreen {\n" +
            "\n" +
            "    public ${class} (AppiumDriver driver) {\n" +
            "        super(driver);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "    Example:\n" +
            "        public MobileElement skipButton() {\n" +
            "            MobileElement element;\n" +
            "            if (driver instanceof IOSDriver) {\n" +
            "                element = findElementWithTimeout(By.xpath(\"//XCUIElementTypeButton[@name=\\\"Skip\\\"]\"), 20);\n" +
            "            } else {\n" +
            "                element = findElementWithTimeout(By.id(\"id/skip_button\"), 20);\n" +
            "            }\n" +
            "            return element;\n" +
            "        }\n" +
            "    **/\n" +
            "}"),
    PAGE_WEB("package com.${suite}.pages;\n" +
            "\n" +
            "import driver.helpers.PageObjectHelper;\n" +
            "import org.openqa.selenium.WebDriver;\n" +
            "import org.openqa.selenium.WebElement;\n" +
            "import org.openqa.selenium.support.FindAll;\n" +
            "import org.openqa.selenium.support.FindBy;\n" +
            "\n" +
            "public class ${class} extends PageObjectHelper {\n" +
            "\n" +
            "\n" +
            "    public ${class} (Object driver) {\n" +
            "        super((WebDriver) driver);\n" +
            "    }\n" +
            "\n" +
            "    /**\n" +
            "    Example:\n" +
            "        @FindBy(xpath = \"//input[@name='name[first]']\")\n" +
            "        public WebElement firstName;\n" +
            "    **/\n" +
            "\n" +
            "    }"),
    TEST_MOBILE("package com.${suite};\n" +
            "import driver.BaseMobileTest;\n" + "import driver.helpers.WebDriverHelper;\n" +
            "import org.testng.annotations.BeforeClass;\n" +
            "public class ${class} extends BaseMobileTest \n{\n" + "WebDriverHelper helper;\n" +
            " @BeforeClass\n" +
            "    public void setup() throws Exception {\n" +
            "        helper = new WebDriverHelper(appiumDriver);\n" +
            "}}"),
    TEST_WEB("package com.${suite};\n" + "import driver.BaseWebTest;\n" +
            "import driver.helpers.WebDriverHelper;\n" +
            "import org.testng.annotations.BeforeClass;\n" +
            "public class ${class} extends BaseWebTest \n{" +
            "WebDriverHelper helper;\n" +
            " @BeforeClass\n" +
            "    public void setup() throws Exception {\n" +
            "        helper = new WebDriverHelper(driver);\n" +
            "}}");

    private String text;

    GeneratedText(String text) {
        this.text = text;
    }

    public String text() {
        return text;
    }
}
