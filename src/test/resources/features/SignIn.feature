@Vital @Regression
Feature: Sign In
  As a Slack user
  I want to sign in my workspace using my credentials
  In order to chat with my team

  Background: User navigates to Slack workspace page
    Given I am on the page with URL provided by flag "workspace_url"
    Then I see a page with "Sign in to Slack" header

  Scenario: Successful sign in
    When I fill in "Email address" with value provided by flag "user_email"
    And I fill in "Password" with value provided by flag "user_pwd"
    And I click on the "Sign in" button
    Then I am in "Slack" workspace
    And I am in "general" channel
