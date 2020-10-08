package steps;

import browser.Browser;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;

/**
 * Hooks useful for UI tests that use browser. This class starts a browser before to execute any UI
 * test and closes the browser after test is done (no mater passed or failed). Before to close the
 * browser it takes a screenshot.
 */
public class UiHooks {

  private Scenario scenario;

  @Before
  public void startBrowser(Scenario scenario) {
    this.scenario = scenario;
    Browser.start();
    Browser.resize();
  }

  @After
  public void closeBrowser() {
    if (Browser.isStarted()) {
      try {
        byte[] screenshot = Browser.takeScreenshot();
        scenario.attach(screenshot, "image/png", "Screenshot");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    Browser.quit();
  }
}
