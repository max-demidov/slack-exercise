package browser;

import static org.openqa.selenium.UnexpectedAlertBehaviour.DISMISS;
import static org.openqa.selenium.remote.CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION;
import static org.openqa.selenium.remote.CapabilityType.HAS_NATIVE_EVENTS;
import static org.openqa.selenium.remote.CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Browser {

  private static final Logger log = LoggerFactory.getLogger(Browser.class);
  private static final long WAIT_TIMEOUT_IN_SECONDS = 30;
  private static final Dimension BROWSER_SIZE = new Dimension(1440, 900);
  private static final ThreadLocal<WebDriver> WEB_DRIVER = new ThreadLocal<>();

  public static WebDriver driver() {
    return WEB_DRIVER.get();
  }

  public static void start() {
    if (isStarted()) {
      return;
    }
    log.info("Starting browser");
    ChromeOptions chromeOptions =
        new ChromeOptions()
            .addArguments(
                "silent",
                "test-type",
                "disable-extensions",
                "disable-infobars",
                "disable-plugins",
                "disable-print-preview");

    DesiredCapabilities caps = new DesiredCapabilities();
    caps.setCapability(ENSURING_CLEAN_SESSION, true);
    caps.setCapability(HAS_NATIVE_EVENTS, false);
    caps.setCapability(UNEXPECTED_ALERT_BEHAVIOUR, DISMISS);

    chromeOptions.merge(caps);

    WebDriver webDriver = new ChromeDriver(chromeOptions);
    WEB_DRIVER.set(webDriver);
    log.info("Browser started");
  }

  public static void quit() {
    if (!isStarted()) {
      return;
    }
    log.info("Closing browser");
    driver().quit();
    WEB_DRIVER.set(null);
  }

  public static WebDriverWait waiting() {
    return waiting(WAIT_TIMEOUT_IN_SECONDS);
  }

  public static WebDriverWait waiting(long sec) {
    return new WebDriverWait(driver(), sec);
  }

  public static boolean isStarted() {
    return driver() != null;
  }

  public static void resize() {
    driver().manage().window().setSize(BROWSER_SIZE);
  }

  public static byte[] takeScreenshot() {
    if (!isStarted()) {
      return null;
    }
    try {
      log.info("Taking screenshot");
      return ((TakesScreenshot) driver()).getScreenshotAs(OutputType.BYTES);
    } catch (Exception e) {
      log.warn("Failed to take a screenshot:\n{}", e.getMessage());
      return null;
    }
  }

  public static Object execute(String js, Object... args) {
    JavascriptExecutor executor = (JavascriptExecutor) driver();
    try {
      return executor.executeScript(js, args);
    } catch (Exception e) {
      log.error("Failed to execute JS", e);
      return null;
    }
  }

  public static Actions actions() {
    return new Actions(driver());
  }

  private Browser() {}
}
