//
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.testng.annotations.Test;
//        public class browserStack {
//            @Test
//            public void runTestInBrowserStack() {
//
//                DesiredCapabilities capabilities = new DesiredCapabilities();
//                capabilities.setca
//
//    }
//
//}































//import io.appium.java_client.remote.options.BaseOptions;
//import io.appium.java_client.AppiumBy;
//import io.appium.java_client.android.AndroidDriver;
//import org.openqa.selenium.Capabilities;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.remote.http.HttpClient;
//import org.openqa.selenium.remote.http.jdk.JdkHttpClient;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.net.URL;
//import java.net.MalformedURLException;
//import java.util.Map;
//
//public class browserStack{
//
//    private AndroidDriver driver;
//
//    @BeforeEach
//    public void setUp() {
//        Capabilities options = new BaseOptions()
//                .amend("platformName", "Android")
//                .amend("appium:automationName", "UiAutomator2")
//                .amend("appium:app", "bs://7199c67b34128764156d2f7b2d1b5aa0d77f2b69")
//                .amend("appium:autoGrantPermissions", true)
//                .amend("appium:appPackage", "in.popin.seller")
//                .amend("appium:appActivity", "in.popin.seller.ui.onboarding.splash.SplashActivity")
//                .amend("appium:adbExecTimeout", 60000)
//                .amend("appium:uiautomator2ServerLaunchTimeout", 120000)
//                .amend("bstack:options", Map.ofEntries(
//                        Map.entry("userName", "prachiverma_WcBAzl"),
//                        Map.entry("accessKey", "pqW9q4ZY8v6zz4ckpapd"),
//                        Map.entry("projectName", "My Project"),
//                        Map.entry("buildName", "Build 1"),
//                        Map.entry("sessionName", "Splash Activity Test"),
//                        Map.entry("deviceName", "Samsung Galaxy S22 Ultra"),
//                        Map.entry("osVersion", "12.0"),
//                        Map.entry("source", "appiumdesktop")))
//                .amend("appium:ensureWebviewsHavePages", true)
//                .amend("appium:nativeWebScreenshot", true)
//                .amend("appium:newCommandTimeout", 3600)
//                .amend("appium:connectHardwareKeyboard", true);
//
//        // Initialize AndroidDriver with explicit HTTP client factory
//        driver = new AndroidDriver(this.getUrl(), options, JdkHttpClient.Factory.createDefault());
//    }
//
//    @Test
//    public void sampleTest() {
//        WebElement el19 = driver.findElement(AppiumBy.id("in.popin.seller:id/buttonStart"));
//        el19.click();
//        WebElement el20 = driver.findElement(AppiumBy.id("in.popin.seller:id/textEmail"));
//        el20.sendKeys("menonaiswarya0@gmail.com");
//        WebElement el21 = driver.findElement(AppiumBy.id("in.popin.seller:id/buttonLogin"));
//        el21.click();
//        WebElement el22 = driver.findElement(AppiumBy.className("android.widget.EditText"));
//        el22.sendKeys("1234");
//        WebElement el23 = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"in.popin.seller:id/navigation_bar_item_icon_view\").instance(1)"));
//        el23.click();
//        WebElement el24 = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"in.popin.seller:id/navigation_bar_item_icon_view\").instance(2)"));
//        el24.click();
//        WebElement el25 = driver.findElement(AppiumBy.androidUIAutomator("new UiSelector().resourceId(\"in.popin.seller:id/navigation_bar_item_icon_view\").instance(3)"));
//        el25.click();
//        WebElement el26 = driver.findElement(AppiumBy.id("in.popin.seller:id/buttonSettings"));
//        el26.click();
//        driver.executeScript("mobile: pressKey", Map.ofEntries(Map.entry("keycode", 4)));
//        WebElement el27 = driver.findElement(AppiumBy.id("in.popin.seller:id/imageStore"));
//        el27.click();
//    }
//
//    @AfterEach
//    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//        }
//    }
//
//    private URL getUrl() {
//        try {
//            return new URL("https://hub-cloud.browserstack.com:443/wd/hub");
//        } catch (MalformedURLException e) {
//            throw new RuntimeException("Invalid URL for BrowserStack hub", e);
//        }
//    }
//}
