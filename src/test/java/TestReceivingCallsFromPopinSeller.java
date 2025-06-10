import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class TestReceivingCallsFromPopinSeller {
    private AndroidDriver driver;

    @BeforeClass
    public void testSetup() {
        try {

            UiAutomator2Options options = new UiAutomator2Options();
            options.setPlatformName("Android");
            options.setDeviceName("V87HEECQ49WSPNZL");
            options.setAppPackage("in.popin.seller");
            options.setAppActivity("in.popin.seller.ui.main.MainActivity");
            options.setAutomationName("UiAutomator2");
            options.setNoReset(true);

            driver = new AndroidDriver(new URL("http://127.0.0.1:4723"), options);

        }catch (Exception e) {
            throw new RuntimeException("Setup failed: " + e.getMessage());
        }
    }

    @Test(priority = 1)
    public void testWaitForCallAndAnswer() {
        try {
            Path readyFlag = Paths.get("/tmp/ready_for_call.flag");
            Path callStartedFlag = Paths.get("/tmp/call_started.flag");
            Path sellerJoinedFlag = Paths.get("/tmp/seller_joined.flag");

            // ✅ Step 0: Signal readiness to Script 1
            Files.write(readyFlag, "ready".getBytes());
            System.out.println("✅ Seller is ready. Flag created: " + readyFlag.toAbsolutePath());

            // ⏳ Step 1: Wait for call initiation
            System.out.println("📞 Waiting for Script 1 to initiate the call...");
            int retries = 120;
            while (!Files.exists(callStartedFlag) && retries-- > 0) {
                Thread.sleep(1000);
            }
            if (!Files.exists(callStartedFlag)) {
                System.out.println("❌ Timeout: call_started.flag not found. Exiting.");
                return;
            }

            System.out.println("✅ Script 1 has initiated the call. Waiting for incoming call banner...");

            // ⏳ Step 2: Wait for call banner and click it
            boolean barFound = false;
            for (int i = 0; i < 90; i++) {
                try {
                    WebElement callBar = driver.findElement(AppiumBy.id("com.miui.home:id/drop_target_bar"));
                    callBar.click();
                    System.out.println("📲 Call bar clicked.");
                    barFound = true;
                    break;
                } catch (NoSuchElementException e) {
                    System.out.println("🔍 Waiting for call bar...");
                    Thread.sleep(1000);
                }
            }

            if (!barFound) {
                System.out.println("❌ Call bar not found. Exiting.");
                return;
            }

            // ✅ Step 3: Accept the call
            Thread.sleep(3000);
            try {
                WebElement acceptButton = driver.findElement(AppiumBy.xpath("//android.widget.FrameLayout[@resource-id=\"com.miui.home:id/drop_target_bar\"]"));
                acceptButton.click();
                System.out.println("✅ Call accepted.");
            } catch (NoSuchElementException e) {
                System.out.println("❌ Accept button not found.");
                return;
            }

            // ✅ Step 4: Signal to Script 1 that call was joined
            Files.write(sellerJoinedFlag, "joined".getBytes());
            System.out.println("✅ seller_joined.flag created.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @AfterTest
    public void teardown() {
        if (driver != null) {
            driver.quit();
        }

        try {
            Files.deleteIfExists(Paths.get("/tmp/call_started.flag"));
            Files.deleteIfExists(Paths.get("/tmp/seller_joined.flag"));
            Files.deleteIfExists(Paths.get("/tmp/ready_for_call.flag"));
        } catch (Exception e) {
            System.out.println("⚠️ Error cleaning up flags: " + e.getMessage());
        }
    }
}




//WebElement el1 = driver.findElement(AppiumBy.id("com.miui.home:id/drop_target_bar"));
//el1.click();
//WebElement el5 = driver.findElement(AppiumBy.id("com.miui.home:id/drop_target_bar"));
//el5.click();
//WebElement el6 = driver.findElement(AppiumBy.id("in.popin.seller:id/buttonAccept"));
//el6.click();
//WebElement el7 = driver.findElement(AppiumBy.id("in.popin.seller:id/exit"));
//el7.click();

