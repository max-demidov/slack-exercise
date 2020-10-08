package pages;

import browser.Browser;
import java.util.function.Supplier;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;

/** Base page class with common page methods. Final page classes are supposed to extend this one. */
public abstract class AbstractPage {

  /** Page constructor that waits for page is loaded and initializes its web elements. */
  protected AbstractPage() {
    waitUntilReadyStateToBeComplete();
    initElements();
  }

  /**
   * Waits until js code 'return document.readyState' to return 'complete' to be sure the page
   * loading is complete.
   */
  protected void waitUntilReadyStateToBeComplete() {
    String js = "return document.readyState";
    Supplier<String> msg =
        () ->
            String.format(
                "Page got stuck in loading state at %s", Browser.driver().getCurrentUrl());
    Browser.waiting()
        .withMessage(msg)
        .until((ExpectedCondition<Boolean>) c -> "complete".equals(Browser.execute(js)));
  }

  /**
   * Initializes the page web elements with {@link PageFactory} as required by the Page Object
   * pattern.
   */
  protected void initElements() {
    PageFactory.initElements(Browser.driver(), this);
  }
}
