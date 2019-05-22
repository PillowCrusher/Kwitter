Feature: kwet

  Scenario: Creating an account
    Given I do not have an account and want to create one
    When I fill in my information into the registration page
    Then I will create an account