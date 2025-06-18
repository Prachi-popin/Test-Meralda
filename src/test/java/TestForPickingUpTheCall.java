import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.testng.annotations.*;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.Arrays;
import java.nio.file.Files;

public class TestForPickingUpTheCall {
    AndroidDriver driver;

    @BeforeTest
    public void testSetup() {
        try {
            UiAutomator2Options options = new UiAutomator2Options();
            options.setDeviceName("RZ8M40QTTSB");
            options.setPlatformName("Android");
            options.setAppPackage("in.popin.seller");
            options.setAppActivity("in.popin.seller.ui.main.MainActivity");
            options.setNoReset(true);

            driver = new AndroidDriver(new URL("http://127.0.0.1:4723/"), options);

            if (driver.getSessionId() == null) {
                throw new RuntimeException("❌ Appium session failed to start!");
            }

            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            System.out.println("✅ Appium session started.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWaitForAndAcceptCall() {
        try {
            File callStartedFlag = new File("/tmp/call_started.flag");
            File sellerJoinedFlag = new File("/tmp/seller_joined.flag");

            int maxWaitSeconds = 60;
            System.out.println("⏳ Waiting for Script 1 to start the call...");

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

            // Try to find and click Accept button
            int retries = 15;
            boolean accepted = false;
            for (int i = 0; i < retries; i++) {
                try {

                    // Open Notification Panel with swipe
                    final var finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                    var start = new Point(500, 0);       // very top center
                    var end = new Point(500, 1500);      // swipe down

                    var swipe = new Sequence(finger, 1);
                    swipe.addAction(finger.createPointerMove(Duration.ofMillis(0), PointerInput.Origin.viewport(), start.getX(), start.getY()));
                    swipe.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                    swipe.addAction(finger.createPointerMove(Duration.ofMillis(1000), PointerInput.Origin.viewport(), end.getX(), end.getY()));
                    swipe.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                    driver.perform(Arrays.asList(swipe));

                    System.out.println("🔽 Notification panel opened.");

                    WebElement acceptBtn = driver.findElement(AppiumBy.accessibilityId("Accept"));
                    acceptBtn.click();
                    System.out.println("📞✅ Call accepted.");
                    accepted = true;

                    Thread.sleep(3000);

                    // Create the seller_joined.flag to notify Script 1
                    Files.write(sellerJoinedFlag.toPath(), "joined".getBytes());
                    System.out.println("✅ Seller join flag created at: " + sellerJoinedFlag.getAbsolutePath());
                    break;
                } catch (Exception e) {
                    System.out.println("⏳ Waiting for Accept button... (" + (i + 1) + "/" + retries + ")");
                    Thread.sleep(1000);
                }
            }

            if (!accepted) {
                System.out.println("❌ 'Accept' button not found after retries.");
            }

        } catch (NoSuchSessionException nse) {
            System.out.println("❌ No active session: " + nse.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
