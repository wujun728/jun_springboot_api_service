Feature: calculate reward
  we can calculate daily reward

  Scenario Outline: calculate
    Given number1 is <number1>
    Given number2 is <number2>
    When I ask What's the reward today
    Then I should be told <answer>

    Examples:
      | number1 | number2 | answer |
      | 10      | 5       | 15     |
      | 5       | 4       | 9      |