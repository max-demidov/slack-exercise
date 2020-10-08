package pages;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

import browser.Browser;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents the Slack client web page with the corresponding web elements and the page
 * functionality.
 */
public final class ClientPage extends AbstractPage {

  private static final String CHANNEL_XPATH_PATTERN =
      "//*[contains(@data-qa, 'virtual-list-item')][.='%s']";
  private static final String MESSAGE_CONTAINER_XPATH_PATTERN =
      ".//*[@data-qa='message_container'][contains(., '%s')]";
  private static final String MESSAGE_CONTAINER_IN_SEARCH_RESULTS_XPATH_PATTERN =
      "//*[@class='c-focus_manage_list__item'][.//*[.='%s']]";

  @FindBy(css = "[data-qa=virtual-list-item][aria-selected=true]")
  private WebElement selectedChannel;

  @FindBy(css = "[data-qa=team-menu-trigger]")
  private WebElement workspaceTrigger;

  @FindBy(css = "[aria-label^=Message]")
  private WebElement messageInput;

  @FindBy(css = "button[data-qa=save_message][aria-label=Save]")
  private WebElement saveMessageButton;

  @FindBy(css = "button[data-qa=save_message][aria-label='Remove from saved items']")
  private WebElement removeFromSavedItemsButton;

  @FindBy(css = "[data-qa=top_nav_search]")
  private WebElement topSearchButton;

  @FindBy(css = "[data-qa=focusable_search_input] .ql-editor")
  private WebElement topSearchInput;

  @FindBy(css = "[data-qa=search_input_close]")
  private WebElement closeTopSearchButton;

  @FindBy(css = "[data-qa=saved_flexpane]")
  private WebElement savedItemsPane;

  @FindBy(css = "[aria-label=Saved] .p-rich_text_section")
  private List<WebElement> savedMessages;

  public void selectSidebarItem(String itemName) {
    By xpath = By.xpath(String.format(CHANNEL_XPATH_PATTERN, itemName));
    Browser.waiting().until(elementToBeClickable(xpath)).click();
  }

  public String getSelectedWorkspace() {
    return Browser.waiting().until(elementToBeClickable(workspaceTrigger)).getText().trim();
  }

  public String getSelectedChannel() {
    return selectedChannel.getText().trim();
  }

  public void sendMessage(String message) {
    messageInput.click();
    messageInput.clear();
    messageInput.sendKeys(message, Keys.ENTER);
    // Make sure the message is displayed in the chat of the current channel
    waitForMessageIsDisplayed(message);
  }

  public void saveMessage(String message) {
    WebElement messageContainer = waitForMessageIsDisplayed(message);

    // Hover over the message container
    Browser.actions().moveToElement(messageContainer).build().perform();
    Browser.waiting().until(elementToBeClickable(saveMessageButton)).click();

    // Make sure the message has been saved
    Browser.waiting()
        .withMessage("Message has not been saved: " + message)
        .until(
            condition ->
                waitForMessageIsDisplayed(message).getText().contains("Added to your saved items"));
  }

  private WebElement waitForMessageIsDisplayed(String message) {
    By messageXpath = By.xpath(String.format(MESSAGE_CONTAINER_XPATH_PATTERN, message));
    // Waiting for the message is displayed in the chat window
    return Browser.waiting()
        .withMessage("Message is not displayed: " + message)
        .until(visibilityOfElementLocated(messageXpath));
  }

  public void searchFor(String searchText) {
    topSearchButton.click();
    // Waiting for the search input is intractable
    Browser.waiting().until(elementToBeClickable(topSearchInput));
    topSearchInput.click();
    topSearchInput.clear();
    // Appending Enter key at the end to submit the search query right away
    topSearchInput.sendKeys(searchText, Keys.ENTER);
  }

  public void waitForMessageInSearchResults(String message) {
    By messageContainerXpath =
        By.xpath(String.format(MESSAGE_CONTAINER_IN_SEARCH_RESULTS_XPATH_PATTERN, message));
    // Waiting for the message is displayed in the search results with the timeout of 2 min
    Browser.waiting(120L)
        .withMessage("The message has not been found in search results: " + message)
        .pollingEvery(Duration.ofSeconds(5)) // keep trying every 5 sec
        .until(
            condition -> {
              if (!Browser.driver().findElements(messageContainerXpath).isEmpty()) {
                return true;
              }
              // re-submit the search query to get the refresh the search results
              topSearchInput.sendKeys(Keys.ENTER);
              return false;
            });
  }

  public void closeTopSearchPopupWindow() {
    closeTopSearchButton.click();
  }

  public void waitForSavedItemsPaneIsDisplayed() {
    Browser.waiting().until(visibilityOf(savedItemsPane));
  }

  public List<String> getSavedMessages() {
    // Waiting for the Saved items list is rendered and not empty
    Browser.waiting()
        .withMessage("Saved items list is empty")
        .until(condition -> !savedMessages.isEmpty());
    return savedMessages.stream().map(WebElement::getText).collect(Collectors.toList());
  }

  public void removeMessageFromSavedItems(String message) {
    WebElement messageContainer = waitForMessageIsDisplayed(message);

    // Hover over the message container
    Browser.actions().moveToElement(messageContainer).build().perform();
    Browser.waiting().until(elementToBeClickable(removeFromSavedItemsButton)).click();

    // Make sure the message has been removed
    Browser.waiting()
        .withMessage("Message has not been removed from Saved items: " + message)
        .until(
            condition ->
                !waitForMessageIsDisplayed(message)
                    .getText()
                    .contains("Added to your saved items"));
  }
}
