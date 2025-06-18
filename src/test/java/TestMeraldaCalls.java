import org.testng.annotations.Test;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.file.StandardOpenOption;
import java.time.Duration;
import org.testng.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.WebDriver;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;

public class TestMeraldaCalls {
    WebDriver driver;

    @Test(priority = 1)
    public void testVideoCallFlow() throws InterruptedException {

        Path readyFlag = Paths.get("/tmp/ready_for_call.flag");
        Path callStartedFlag = Paths.get("/tmp/call_started.flag");
        Path sellerJoinedFlag = Paths.get("/tmp/seller_joined.flag");
        Path callEndedFlag = Paths.get("/tmp/call_ended.flag");

        try {
            Files.deleteIfExists(callStartedFlag);
            Files.deleteIfExists(sellerJoinedFlag);
            Files.deleteIfExists(callEndedFlag);

        } catch (Exception e) {
            System.out.println("⚠️ Couldn't clear old flags: " + e.getMessage());
        }

        // ⏳ Wait for Script 2 to indicate it's ready
        System.out.println("⏳ Waiting for seller script to be ready (ready_for_call.flag)...");
        int waitForReady = 60;
        boolean isReady = false;
        for (int i = 0; i < waitForReady; i++) {
            if (Files.exists(readyFlag)) {
                System.out.println("✅ Seller script is ready.");
                isReady = true;
                break;
            }
            Thread.sleep(1000);
        }
        if (!isReady) {
            throw new RuntimeException("Timeout: Seller script not ready.");
        }

        System.out.println("Setting Firefox media preferences...");
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("media.navigator.permission.disabled", true);
        options.addPreference("media.navigator.streams.fake", true);
        options.addPreference("media.peerconnection.enabled", true);

        System.out.println("Launching Firefox driver...");
        driver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));


        System.out.println("Maximizing browser window...");
        driver.manage().window().maximize();

        try {
            driver.get("https://meralda.scalenext.io/product-detail/fiori-emerald-beads-ring");
            System.out.println("URL opened: " + driver.getCurrentUrl());
            Assert.assertEquals(driver.getCurrentUrl(), "https://meralda.scalenext.io/product-detail/fiori-emerald-beads-ring");

            Thread.sleep(3000);
            WebElement videoCallButton = driver.findElement(By.xpath("//*[@id='homeMainContent']/section[1]/div/div/div[2]/div/form/div[4]/button[1]"));

            // Scroll into view
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", videoCallButton);
            Thread.sleep(500); // wait a moment for layout adjustments

            // Click using JS to avoid click interception
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", videoCallButton);

            System.out.println("Waiting for iframe to appear...");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("iframe")));
            System.out.println("Iframe appeared");

            System.out.println("Switching to iframe...");
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));

            System.out.println("Waiting for loading overlay to disappear...");
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-page")));
            System.out.println("Loading overlay gone");

            System.out.println("Locating Name input field...");
            WebElement nameInput = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@placeholder='Name']")));
            Assert.assertTrue(nameInput.isDisplayed());

            System.out.println("Locating Number input field...");
            WebElement numberInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"inlineFormInputGroup\"]")));
            Assert.assertTrue(numberInput.isDisplayed());

//            System.out.println("Locating Pin code input field...");
//            WebElement pinCodeInput = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"popin-panel\"]/div/div[3]/div[1]/div/div/div/div/div[3]/input")));
//            Assert.assertTrue(pinCodeInput.isDisplayed());

            Thread.sleep(4000);

            System.out.println("Entering name, number, and pin code...");
            nameInput.click();
            nameInput.sendKeys("Prachi Test");
            numberInput.click();
            numberInput.sendKeys("8435627503");
//            pinCodeInput.click();
//            pinCodeInput.sendKeys("452005");

            System.out.println("Clicking confirm button...");
            WebElement confirmBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"popin-panel\"]/div/div[3]/div[1]/div/div/div/button")));
            confirmBtn.click();

            Thread.sleep(6000);

//            System.out.println("Waiting for 'Video Call Now' and 'Schedule Call Later' buttons...");
//            WebElement video_call_now = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"popin-panel\"]/div/div[3]/div[1]/div/button[1]")));
//            WebElement schedule_call_later = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"popin-panel\"]/div/div[3]/div[1]/div/button[2]")));
//
//            System.out.println("Verifying 'Video Call Now' and 'Schedule Call Later' buttons...");
//            Assert.assertTrue(video_call_now.isDisplayed() && video_call_now.isEnabled());
//            Assert.assertTrue(schedule_call_later.isDisplayed() && schedule_call_later.isEnabled());
//
//            System.out.println("Clicking on 'Video Call Now' button...");
//            video_call_now.click();
//            Thread.sleep(6000);
//
//            System.out.println("Switching back to iframe...");
//            driver.switchTo().defaultContent();
//            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));

            System.out.println("Locating 'Allow Access' button...");
            WebElement allowAccessBtn = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"popin-panel\"]/div/div[3]/div[1]/div/div/div/button")));
            Assert.assertTrue(allowAccessBtn.isDisplayed() && allowAccessBtn.isEnabled());

            System.out.println("Clicking 'Allow Access'...");
            allowAccessBtn.click();

            Thread.sleep(4000);

            System.out.println("Switching to iframe again after access...");
            driver.switchTo().defaultContent();
            Thread.sleep(3000);
            driver.switchTo().frame(0);

            Thread.sleep(3000);

            // ✅ Create call started flag to notify Script 2
            Files.write(callStartedFlag, "initiated".getBytes(), StandardOpenOption.CREATE);
            System.out.println("✅ Call started flag created: " + callStartedFlag.toAbsolutePath());

            // ⏳ Wait for seller to join the call
            System.out.println("⏳ Waiting for seller to join the call...");
            int waitSeconds = 60;
            boolean sellerJoined = false;
            for (int i = 0; i < waitSeconds; i++) {
                if (Files.exists(sellerJoinedFlag)) {
                    System.out.println("✅ Seller joined flag detected.");
                    sellerJoined = true;
                    break;
                }
                Thread.sleep(1000);
            }

            if (!sellerJoined) {
                throw new RuntimeException("Timeout: Seller did not answer call in time.");
            }

            Thread.sleep(4000);

            // Now perform UI assertions or continue the flow
            System.out.println("Proceeding with video call validations...");

            System.out.println("Waiting for call connected UI...");
            WebElement callConnected = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"popin-panel\"]/div/div/div[1]/div/div/div/div[3]/div[2]/ul/li[4]")));
            Assert.assertTrue(callConnected.isDisplayed());

            System.out.println("Verifying video call UI buttons...");
            Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"popin-panel\"]/div/div/div[1]/div/div/div/div[3]/div[2]/ul/li[1]"))).isDisplayed(), "Camera icon missing");
            Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"popin-panel\"]/div/div/div[1]/div/div/div/div[3]/div[2]/ul/li[2]"))).isDisplayed(), "Mute icon missing");
            Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"popin-panel\"]/div/div/div[1]/div/div/div/div[3]/div[2]/ul/li[3]"))).isDisplayed(), "Chat icon missing");

            System.out.println("Locating and clicking 'Cut Call'...");
            WebElement cutCall = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"popin-panel\"]/div/div/div[1]/div/div/div/div[3]/div[2]/ul/li[4]")));
            Assert.assertTrue(cutCall.isDisplayed());

            System.out.println("Clicking on 'Three Dots' menu...");
            WebElement threeDots = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"popin-panel\"]/div/div/div[1]/div/div/div/div[3]/div[2]/ul/li[5]")));
            threeDots.click();

            System.out.println("Checking screen share and zoom...");
            Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"popin-panel\"]/div/div/div[1]/div/div/div/div[3]/div[2]/ul/li[5]/ul/li[1]"))).isDisplayed());
            Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"popin-panel\"]/div/div/div[1]/div/div/div/div[3]/div[2]/ul/li[5]/ul/li[2]"))).isDisplayed());

            System.out.println("Ending call...");
            cutCall.click();

            System.out.println("Switching to iframe for rating...");
            driver.switchTo().defaultContent();
            wait.until(ExpectedConditions.frameToBeAvailableAndSwitchToIt(By.tagName("iframe")));

            // ✅ Create call Ended flag to notify Script 2
            Files.write(callEndedFlag, "initiated".getBytes(), StandardOpenOption.CREATE);
            System.out.println("✅ Call Ended flag created: " + callEndedFlag.toAbsolutePath());

            System.out.println("Waiting for rating screen...");
            WebElement ratingTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Please rate your call.')]")));
            Assert.assertTrue(ratingTitle.isDisplayed());

            System.out.println("Clicking 5-star rating...");
            WebElement fiveStar = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//label[@for='rate5']")));
            fiveStar.click();

            System.out.println("Filling comment box...");
            WebElement commentBox = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@id=\"popin-panel\"]/div/div[3]/div[1]/div/div/textarea")));
            commentBox.sendKeys("Great call on Firefox!");

            System.out.println("Submitting rating...");
            WebElement submitRating = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"popin-panel\"]/div/div[3]/div[1]/div/div/button")));
            submitRating.click();

            System.out.println("Checking for thank you message...");
            WebElement thankYouMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),\"We're thrilled to be part of your pleasure journey.\")]")));
            Assert.assertTrue(thankYouMsg.isDisplayed());

            System.out.println("Clicking 'Continue Browsing'...");
            WebElement continueBrowsing = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//*[@id=\"popin-panel\"]/div/div[3]/div[1]/div/div/div[2]/button")));
            continueBrowsing.click();

            sendTelegramNotification("✅Test run successfully!");

        } catch (Exception e) {
            System.out.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Closing browser...");
            driver.quit();
        }
    }

    // 📢 Hardcoded Telegram notification method
    private void sendTelegramNotification(String message) {
        try {
            String botToken = "8051881078:AAE1ky4RVDknzNa7qu8LxtCbPGHIfh1LPu8";
            String chatId = "6907899696";

            String urlString = String.format(
                    "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s",
                    botToken,
                    chatId,
                    URLEncoder.encode(message, "UTF-8")
            );

            HttpURLConnection conn = (HttpURLConnection) new URL(urlString).openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                System.out.println("✅ Telegram notification sent.");
            } else {
                System.out.println("❌ Failed to send Telegram message. HTTP response code: " + responseCode);
            }
        } catch (Exception ex) {
            System.out.println("❌ Error while sending Telegram message: " + ex.getMessage());
        }
    }
}