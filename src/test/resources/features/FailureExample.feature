@FailureExampleDemo
Feature: Example of a test failure
  As a Slack user
  I want to change a channel
  In order to chat on different topics

  Background: User is signed in and staying in #general channel
    When I sign in the workspace at URL provided by flag "workspace_url"
    Then I am in "Slack" workspace

  Scenario: Star a message and find it
    When I am in "general" channel
    And I select sidebar item "random"
    # The last step is incorrect and put there intentionally
    # to demo a test failure example in the Cucumber test report
    And I am in "general" channel
