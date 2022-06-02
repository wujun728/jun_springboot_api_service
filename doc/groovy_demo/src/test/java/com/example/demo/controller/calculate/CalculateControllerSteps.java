package com.example.demo.controller.calculate;

import com.example.demo.controller.CalculateController;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertEquals;

public class CalculateControllerSteps {

    private int number1;

    private int number2;

    private int actualAnswer;

    private CalculateController calculateController = new CalculateController();

    @Given("number1 is {int}")
    public void number1_is(int number1) {
        this.number1 = number1;
    }

    @Given("number2 is {int}")
    public void number2_is(int number2) {
        this.number2 = number2;
    }

    @When("I ask What's the reward today")
    public void i_ask_what_it_is_the_reward_today() {
        actualAnswer = calculateController.calculate(number1, number2);
    }

    @Then("I should be told {int}")
    public void i_should_be_told(int expectAnswer) {
        assertEquals(expectAnswer, actualAnswer);
    }
}