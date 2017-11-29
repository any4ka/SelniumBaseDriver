package driver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public abstract class AppiumDriverBuilder<SELF, DRIVER extends AppiumDriver> {

    protected URL endpoint;
    protected DesiredCapabilities capabilities = new DesiredCapabilities();

    public static AndroidDriverBuilder forAndroid() {
        return new AndroidDriverBuilder();
    }

    public static IOSDriverBuilder forIOS() {
        return new IOSDriverBuilder();
    }

    public SELF withEndpoint(URL endpoint) {
        this.endpoint = endpoint;

        return (SELF) this;
    }

    public SELF withCapabilities(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
        return (SELF) this;
    }

    public abstract DRIVER build();

    public static class AndroidDriverBuilder extends AppiumDriverBuilder<AndroidDriverBuilder, AndroidDriver> {
        public AndroidDriver build() {
            return new AndroidDriver(endpoint, capabilities);
        }
    }

    public static class IOSDriverBuilder extends AppiumDriverBuilder<IOSDriverBuilder, IOSDriver> {
        public IOSDriver build() {
            return new IOSDriver(endpoint, capabilities);
        }

    }
}