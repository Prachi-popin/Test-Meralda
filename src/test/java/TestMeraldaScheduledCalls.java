import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.time.Duration;
import java.time.LocalDate;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class TestMeraldaScheduledCalls {

    @Test
    public void testScheduledCallFlow() throws Exception  {

        System.out.println("Setting Firefox media preferences...");
        FirefoxOptions options = new FirefoxOptions();
        options.addPreference("media.navigator.permission.disabled", true);
        options.addPreference("media.navigator.streams.fake", true);
        options.addPreference("media.peerconnection.enabled", true);

        System.out.println("Launching Firefox driver...");
        WebDriver driver = new FirefoxDriver(options);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        Actions actions = new Actions(driver);

        System.out.println("Maximizing browser window...");
        driver.manage().window().maximize();

        try {
            System.out.println("Navigating to Meralda website...");
            driver.get("https://meralda.scalenext.io/");
            System.out.println("URL opened: " + driver.getCurrentUrl());
            Assert.assertEquals("https://meralda.scalenext.io/", driver.getCurrentUrl());

            System.out.println("Waiting and clicking on 'Jewellery'...");
            WebElement jewellery = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"homeMainnavBarDrop\"]/div/ul/li[2]/a")));
            jewellery.click();
            System.out.println("'Jewellery' clicked");

            System.out.println("Waiting and clicking on 'Necklaces and Pendants'...");
            WebElement necklaces = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"homeMainnavBarDrop\"]/div/ul/li[2]/div/div/div/ul[1]/li[2]/a")));
            necklaces.click();
            System.out.println("'Necklaces and Pendants' clicked");

            System.out.println("Waiting for products to load...");
            Thread.sleep(4000);

            System.out.println("Locating the first product...");
            WebElement firstProduct = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[@id=\"homeMainContent\"]/section[2]/div/div[2]/div/div/div[1]/div/div/div[1]/div[1]/div/div[3]/a/div/img")));

            System.out.println("Scrolling to the first product...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", firstProduct);
            Thread.sleep(1500);
            System.out.println("Clicking on the first product...");
            actions.moveToElement(firstProduct).click().perform();

            Thread.sleep(4000);

            System.out.println("Locating video call button...");
            WebElement videoCallBtn = wait.until(ExpectedConditions.presenceOfElementLocated(
                    By.xpath("//*[@id=\"homeMainContent\"]/section[1]/div/div/div[2]/div/form/div[3]/button[1]")));

            System.out.println("Scrolling to video call button...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", videoCallBtn);
            Thread.sleep(5000);
            System.out.println("Clicking video call button using JavaScript...");
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", videoCallBtn);

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

            Thread.sleep(4000);

            WebElement video_call_now = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"popin-panel\"]/div/div[3]/div[1]/div/button[1]")));
            WebElement Schedule_call_later = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id=\"popin-panel\"]/div/div[3]/div[1]/div/button[2]")));
            Assert.assertTrue(video_call_now.isDisplayed(), "'Video Call Now' option should be visible");
            Assert.assertTrue(Schedule_call_later.isDisplayed(), "'Schedule Call Later' option should be visible");
            Assert.assertTrue(video_call_now.isEnabled(), "'Video Call Now' option should be clickable");
            Assert.assertTrue(Schedule_call_later.isEnabled(), "'Schedule Call Later' option should be clickable");
            System.out.println("Assertion  of video call now button and schedule call later is done successfully");

            //  Click "Schedule for Later" option
            WebElement scheduleLaterBtn = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"popin-panel\"]/div/div[3]/div[1]/div/button[2]")));
            scheduleLaterBtn.click();
            System.out.println("Clicked on Schedule for Later!");

            // Wait for loading overlay to disappear
            wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("div.loading-page")));
            System.out.println("Loading overlay disappeared");

            // Select the product from collection
            WebElement product = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div/div/div/div[3]/div[1]/div[2]/div[1]/div")));
            product.click();
            System.out.println("Clicked on a product");

            // Switch to iframe again
            driver.switchTo().defaultContent();
            driver.switchTo().frame(0);

            // Add to cart
            WebElement addBtn = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div/div/div/div[3]/div[1]/div[2]/div[1]/div/div/div[3]/button")));
            addBtn.click();
            System.out.println("Clicked on +Add button");

            // Click on view cart
            WebElement viewCart = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div/div/div/div[3]/div[1]/div[3]/div[2]/button")));
            viewCart.click();
            System.out.println("Clicked on view cart");

            // Click on schedule button
            WebElement schedule = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div/div/div/div[3]/div[1]/div[3]/div/button")));
            schedule.click();
            System.out.println("Clicked on schedule button");

            // Switch to iframe again
            driver.switchTo().defaultContent();
            driver.switchTo().frame(0);

            // Select the date

            // Wait for calendar to load and fetch all enabled date elements (not disabled/greyed out)
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/div/div/div/div/div/div[3]/div[1]/div[1]/div/div[2]/div/div[1]")));

            LocalDate today = LocalDate.now();
            String todayDay = String.valueOf(today.getDayOfMonth());

            try {
                // XPath targets the correct structure for the clickable date element
                WebElement todayElement = driver.findElement(By.xpath(
                        "//div[@role='button' and @aria-disabled='false' and normalize-space(text())='" + todayDay + "']"));

                if (todayElement.isDisplayed() && todayElement.isEnabled()) {
                    wait.until(ExpectedConditions.elementToBeClickable(todayElement)).click();
                    System.out.println("✅ Clicked today’s date: " + today);
                } else {
                    System.out.println("⛔ Today's date is not clickable.");
                }

            } catch (NoSuchElementException e) {
                System.out.println("❌ Today's date not found or not enabled.");
            }

            // Now wait for time slots to appear and click the first one
            WebElement timeSlot = wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("(//span[contains(@class,'time-slot') or contains(@style,'cursor')])[1]")));
            timeSlot.click();
            System.out.println("Selected time slot: " + timeSlot.getText());

            // Confirm button
            WebElement ConfirmBtn =  driver.findElement(By.xpath("/html/body/div/div/div/div/div/div/div[3]/div[1]/div[1]/div/div[2]/button"));
            ConfirmBtn.click();
            System.out.println("Confirm button clicked!");

            // Switch to iframe again
            driver.switchTo().defaultContent();
            driver.switchTo().frame(0);

            String ExpectedText = "Your call has been scheduled";
            // Wait for the confirmation popup and get its text
            WebElement confirmationPopup = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[contains(text(),'Your call has been scheduled')]")));
            String ActualText = confirmationPopup.getText();
            Assert.assertTrue(ActualText.contains(ExpectedText), "Schedule confirmation popup details matched");
            System.out.println("Schedule call info is displayed!");

        } catch (Exception e) {
            System.out.println("❌ Test failed: " + e.getMessage());
            e.printStackTrace(System.err);
        } finally {
            System.out.println("Closing browser...");
            driver.quit();
        }
    }
}


