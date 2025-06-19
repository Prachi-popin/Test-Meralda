import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;
import org.testng.annotations.*;
import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.time.Duration;
import java.util.Arrays;

public class TestReceivingCallsFromPopinSeller {
    AndroidDriver driver;

    @BeforeTest
    public void testSetup() {
        try {
            UiAutomator2Options options = new UiAutomator2Options();
            options.setPlatformName("Android");
            options.setPlatformVersion("13");
            options.setDeviceName("Pixel_5");
            options.setApp(System.getProperty("user.dir") + "/apk/app-debug.apk");
            options.setAppPackage("in.popin.seller");
            options.setAppActivity("in.popin.seller.ui.main.MainActivity");
            options.setAutomationName("UiAutomator2");
            options.setUiautomator2ServerLaunchTimeout(Duration.ofMillis(60000));
            options.setAutoGrantPermissions(true);
            options.setNewCommandTimeout(Duration.ofSeconds(300));


            URL url = new URL("http://127.0.0.1:4723/");
            driver = new AndroidDriver(url, options);

            if (driver.getSessionId() == null) {
                throw new RuntimeException("❌ Appium session failed to start!"); }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            System.out.println("✅ Appium session started.");
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

            // Start button
            WebElement startButton = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.id("in.popin.seller:id/buttonStart")));
            AssertJUnit.assertTrue(startButton.isDisplayed());
            System.out.println("Start button Displayed!");
            startButton.click();
            System.out.println("Start button clicked.");

            // Email field
            WebElement emailField = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.id("in.popin.seller:id/textEmail")));
            AssertJUnit.assertTrue(emailField.isDisplayed());
            System.out.println("Email address Displayed!");
            emailField.click();
            emailField.sendKeys("menonaiswarya0@gmail.com");
            System.out.println("Email entered.");

            // Login button
            WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(
                    AppiumBy.id("in.popin.seller:id/buttonLogin")));
            AssertJUnit.assertTrue(loginButton.isDisplayed());
            System.out.println("Log-in button Displayed!");
            loginButton.click();
            System.out.println("Log-in button clicked.");

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            WebElement OTPField= wait.until(ExpectedConditions.visibilityOfElementLocated(
                    AppiumBy.androidUIAutomator("new UiSelector().className(\"android.widget.EditText\")")));
            AssertJUnit.assertTrue(OTPField.isDisplayed());
            System.out.println("OTp field Displayed!");
            OTPField.click();
            OTPField.sendKeys("1234");
            System.out.println("OTP Entered");

            // === Handle Permissions (if shown) ===
            try {
                WebElement permissionContainer = wait.until(ExpectedConditions.presenceOfElementLocated(
                        AppiumBy.id("com.android.permissioncontroller:id/content_container")));
                if (permissionContainer.isDisplayed()) {
                    WebElement allowForeground = driver.findElement(AppiumBy.id("com.android.permissioncontroller:id/permission_allow_foreground_only_button"));
                    allowForeground.click();
                    System.out.println("Foreground permission allowed");

                    for (int i = 0; i < 2; i++) {
                        try {
                            WebElement allowButton = wait.until(ExpectedConditions.elementToBeClickable(
                                    AppiumBy.id("com.android.permissioncontroller:id/permission_allow_button")));
                            allowButton.click();
                            System.out.println("General permission allowed");
                        } catch (Exception ignore) {
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("No permission dialog appeared.");
            }

            File readyFlag = new File("/tmp/ready_for_call.flag");
            if (readyFlag.createNewFile()) {
                System.out.println("✅ Seller is ready. Flag created: " + readyFlag.getAbsolutePath()); }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWaitForAndAcceptCall() throws InterruptedException {
        int maxWaitSeconds = 0;
        try {
            File callStartedFlag = new File("/tmp/call_started.flag");
            File sellerJoinedFlag = new File("/tmp/seller_joined.flag");
            File callEndedFlag = new File("/tmp/call_ended.flag");

            maxWaitSeconds = 90;
            System.out.println("⏳ Waiting for Script 1 to initiate the call...");

            boolean flagDetected = false;
            for (int i = 0; i < maxWaitSeconds; i++) {
                if (callStartedFlag.exists()) {
                    System.out.println("✅ Detected call start flag.");
                    flagDetected = true;
                    break;
                }
                Thread.sleep(1000);
                if (i % 5 == 0) {
                    System.out.println("...still waiting (" + (i + 1) + "s)");
                }
            }
            if (!flagDetected) {
                throw new RuntimeException("❌ Timeout: Script 1 did not initiate the call within " + maxWaitSeconds + " seconds.");
            }

//            // Try to find and click Accept button
//            int retries = 5;
//            boolean accepted = false;
//            for (int i = 0; i < retries; i++) {
//                try {
//
//                    // Open Notification Panel with swipe
//                    final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
//                    var start = new Point(500, 0);
//                    var end = new Point(500, 1500);
//
//                    var swipe = new Sequence(finger, 1);
//                    swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), start.getX(), start.getY()));
//                    swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
//                    swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), end.getX(), end.getY()));
//                    swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
//                    driver.perform(Arrays.asList(swipe));
//
//                    System.out.println("🔽 Notification panel opened.");
//
//                    Thread.sleep(5000);
//
//                    WebElement acceptBtn = driver.findElement(AppiumBy.accessibilityId("Accept"));
//                    acceptBtn.click();
//                    System.out.println("📞✅ Call accepted.");
//                    accepted = true;
//
//                    // Create the seller_joined.flag to notify Script 1
//                    Files.write(sellerJoinedFlag.toPath(), "joined".getBytes());
//                    System.out.println("✅ Seller join flag created at: " + sellerJoinedFlag.getAbsolutePath());
//                    break;
//
//                } catch (Exception e) {
//                    System.out.println("⏳ Waiting for Accept button... (" + (i + 1) + "/" + retries + ")");
//                    Thread.sleep(1000);
//                }
//            }
//            if (!accepted) {
//                System.out.println("❌ 'Accept' button not found after retries.");
//            }
            try {
                // Swipe down notification panel (already in your code)
                final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                var swipe = new Sequence(finger, 1);
                swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), 500, 0));
                swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), 500, 1500));
                swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(Arrays.asList(swipe));

                Thread.sleep(2000); // Let the notification fully open

                // Simulate tap on Accept button using ADB
                System.out.println("📱 Sending ADB tap command to click Accept...");
                Runtime.getRuntime().exec("adb shell input tap 749 879");

                // Create flag file
                Files.write(sellerJoinedFlag.toPath(), "joined".getBytes());
                System.out.println("✅ Seller join flag created at: " + sellerJoinedFlag.getAbsolutePath());

            } catch (Exception e) {
                System.out.println("❌ Failed to tap Accept using ADB: " + e.getMessage());
            }

        } catch (NoSuchSessionException nse) {
            System.out.println("❌ No active session: " + nse.getMessage());
        }
        Thread.sleep(10000);
    }
    @AfterTest
    public void teardown() {
        try {
            if (driver != null && driver.getSessionId() != null) {
                driver.quit();
                System.out.println("🔚 Appium session ended.");
            }
        } catch (NoSuchSessionException e) {
            System.out.println("⚠️ Session already closed.");
        }
    }
}
