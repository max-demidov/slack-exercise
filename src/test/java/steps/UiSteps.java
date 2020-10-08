package steps;

import static org.openqa.selenium.support.ui.ExpectedConditions.urlToBe;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import browser.Browser;
import com.google.common.base.Ascii;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.util.Calendar;
import java.util.List;
import pages.ClientPage;
import pages.LoginPage;
import pages.PageProvider;

/**
 * Implementation of test steps logic. Every step in a feature file matches one method in this
 * class.
 */
public class UiSteps {

  private Scenario scenario;
  private String lastMessage;

  @Before
  public void initScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  @Given("I am on the page with URL provided by flag {string}")
  public void openPageWithUrlProvidedByFlag(String flagName) {
    String workspaceUrl = System.getProperty(flagName, "");
    if (workspaceUrl.isEmpty()) {
      throw new IllegalArgumentException(flagName + " flag value has not been provided");
    }
    openPage(workspaceUrl);
  }

  @Given("I am on the page with URL {string}")
  public void openPage(String url) {
    if (!Ascii.equalsIgnoreCase(Browser.driver().getCurrentUrl(), url)) {
      Browser.driver().get(url);
    }
    Browser.waiting().until(urlToBe(url));
    takeScreenshot();
  }

  @Then("I see a page with {string} header")
  public void checkPageHeader(String header) {
    LoginPage loginPage = (LoginPage) PageProvider.getCurrentPage();
    assertEquals(loginPage.getHeader(), header, "Incorrect header on the page:");
    takeScreenshot();
  }

  @When("I fill in {string} with value provided by flag {string}")
  public void fillInFieldWithFlagValue(String fieldName, String flagName) {
    String flagValue = System.getProperty(flagName, "");
    if (flagValue.isEmpty()) {
      throw new IllegalArgumentException(flagName + " flag value has not been provided");
    }
    LoginPage loginPage = (LoginPage) PageProvider.getCurrentPage();
    loginPage.fill(fieldName, flagValue);
  }

  @When("I click on the \"Sign in\" button")
  public void clickSignInButton() {
    LoginPage loginPage = (LoginPage) PageProvider.getCurrentPage();
    loginPage.clickSignInButton();
  }

  @Then("I am in {string} workspace")
  public void checkSelectedWorkspace(String workspaceName) {
    ClientPage clientPage = (ClientPage) PageProvider.getCurrentPage();
    assertEquals(clientPage.getSelectedWorkspace(), workspaceName, "Incorrect workspace selected:");
    takeScreenshot();
  }

  @Then("I am in {string} channel")
  public void checkSelectedChannel(String channelName) {
    ClientPage clientPage = (ClientPage) PageProvider.getCurrentPage();
    assertEquals(clientPage.getSelectedChannel(), channelName, "Incorrect channel selected:");
    takeScreenshot();
  }

  @Given("I sign in the workspace at URL provided by flag {string}")
  public void signInWorkspaceWithUrlProvidedByFlag(String flagName) {
    String workspaceUrl = System.getProperty(flagName, "");
    if (workspaceUrl.isEmpty()) {
      throw new IllegalArgumentException(flagName + " flag value has not been provided");
    }
    signInAtPageWithUrl(workspaceUrl);
  }

  @When("I sign in the workspace at URL {string}")
  public void signInAtPageWithUrl(String url) {
    log("Sign in workspace at " + url);
    openPage(url);
    fillInFieldWithFlagValue("Email address", "user_email");
    fillInFieldWithFlagValue("Password", "user_pwd");
    clickSignInButton();
    Browser.waiting()
        .withMessage("Client page is not found")
        .until(condition -> PageProvider.getCurrentPage() instanceof ClientPage);
    takeScreenshot();
  }

  @When("I select sidebar item {string}")
  public void iSelectSidebarItem(String itemName) {
    ClientPage clientPage = (ClientPage) PageProvider.getCurrentPage();
    clientPage.selectSidebarItem(itemName);
    takeScreenshot();
  }

  @When("I send my local time")
  public void sendMessageWithMyLocalTime() {
    sendMessage("My local time is " + Calendar.getInstance().getTime());
  }

  @When("I send message {string}")
  public void sendMessage(String message) {
    log("Sending the message to the current channel: " + message);
    ClientPage clientPage = (ClientPage) PageProvider.getCurrentPage();
    clientPage.sendMessage(message);
    lastMessage = message;
    takeScreenshot();
  }

  @When("I save the last message")
  public void saveLastMessage() {
    saveMessage(lastMessage);
  }

  @When("I save the message {string}")
  public void saveMessage(String message) {
    log("Saving (starring) the message: " + message);
    ClientPage clientPage = (ClientPage) PageProvider.getCurrentPage();
    clientPage.saveMessage(message);
    takeScreenshot();
  }

  @When("I search for {string}")
  public void searchFor(String searchText) {
    ClientPage clientPage = (ClientPage) PageProvider.getCurrentPage();
    clientPage.searchFor(searchText);
    takeScreenshot();
  }

  @Then("the last message appears in search results")
  public void lastMessageAppearsInSearchResults() {
    messageAppearsInSearchResults(lastMessage);
  }

  @Then("message {string} appears in search results")
  public void messageAppearsInSearchResults(String message) {
    log("Waiting for the message is displayed in the search results: " + message);
    ClientPage clientPage = (ClientPage) PageProvider.getCurrentPage();
    clientPage.waitForMessageInSearchResults(message);
    takeScreenshot();
  }

  @When("I close top search popup window")
  public void closeTopSearchPopupWindow() {
    ClientPage clientPage = (ClientPage) PageProvider.getCurrentPage();
    clientPage.closeTopSearchPopupWindow();
    takeScreenshot();
  }

  @Then("the last message appears in Saved items")
  public void lastMessageAppearsInSavedItems() {
    messageAppearsInSavedItems(lastMessage);
  }

  @Then("I see Saved items pane on the right")
  public void verifyThatSavedItemsAreDisplayedOnTheRight() {
    ClientPage clientPage = (ClientPage) PageProvider.getCurrentPage();
    clientPage.waitForSavedItemsPaneIsDisplayed();
    takeScreenshot();
  }

  @Then("message {string} appears in Saved items")
  public void messageAppearsInSavedItems(String message) {
    log("Verify that the message is displayed in the Saved items: " + message);
    ClientPage clientPage = (ClientPage) PageProvider.getCurrentPage();
    List<String> savedMessages = clientPage.getSavedMessages();
    String errorMessage =
        String.format("Message [%s] not found among the Saved items: %s", message, savedMessages);
    assertTrue(savedMessages.contains(message), errorMessage);
    takeScreenshot();
  }

  @Then("I remove the last message from Saved items")
  public void removeLastMessageFromSavedItems() {
    removeMessageFromSavedItems(lastMessage);
  }

  @Then("I remove message {string} from Saved items")
  public void removeMessageFromSavedItems(String message) {
    log("Removing the message from Saved items: " + message);
    ClientPage clientPage = (ClientPage) PageProvider.getCurrentPage();
    clientPage.removeMessageFromSavedItems(message);
    takeScreenshot();
  }

  private void log(String message) {
    scenario.attach(message, "text/plain", "Details");
  }

  private void takeScreenshot() {
    scenario.attach(Browser.takeScreenshot(), "image/png", "Screenshot");
  }
}
