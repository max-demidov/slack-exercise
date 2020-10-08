package pages;

import browser.Browser;

/**
 * Determines which page is currently open in the browser and returns its corresponding page class.
 */
public class PageProvider {

  public static AbstractPage getCurrentPage() {
    String currentUrl = Browser.driver().getCurrentUrl();
    if (currentUrl.matches(".*/(slack-exercise-md.)?slack.com/.*")) {
      return new LoginPage();
    }
    if (currentUrl.matches(".*/app.slack.com/.*")) {
      return new ClientPage();
    }
    throw new IllegalStateException("Could not determine page with current URL " + currentUrl);
  }
}
