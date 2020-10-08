package pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Represents the Slack login page with the corresponding web elements and the page functionality.
 */
public class LoginPage extends AbstractPage {

  @FindBy(tagName = "h1")
  private WebElement header;

  @FindBy(id = "email")
  private WebElement emailInput;

  @FindBy(id = "password")
  private WebElement passwordInput;

  @FindBy(id = "signin_btn")
  private WebElement signInButton;

  public String getHeader() {
    return header.getText().trim();
  }

  public void fill(String fieldName, String value) {
    switch (fieldName.toLowerCase()) {
      case "email address":
        emailInput.clear();
        emailInput.sendKeys(value);
        break;
      case "password":
        passwordInput.clear();
        passwordInput.sendKeys(value);
        break;
      default:
        throw new IllegalArgumentException(fieldName + " field is not defined at LoginPage");
    }
  }

  public void clickSignInButton() {
    signInButton.click();
  }
}
