@Feature @Regression
Feature: Starring a message
  As a Slack user
  I want to save a message
  In order to easily find it

  Background: User is signed in
    When I sign in the workspace at URL provided by flag "workspace_url"
    Then I am in "Slack" workspace
    And I am in "general" channel

  Scenario: Star a message and find it
    When I send message "Hi there"
    And I send my local time
    And I save the last message
    And I search for "has:star"
    Then the last message appears in search results
    When I close top search popup window
    And I select sidebar item "Saved items"
    Then I see Saved items pane on the right
    And the last message appears in Saved items
    Then I remove the last message from Saved items
