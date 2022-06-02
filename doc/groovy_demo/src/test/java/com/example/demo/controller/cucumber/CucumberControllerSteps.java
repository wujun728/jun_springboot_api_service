package com.example.demo.controller.cucumber;

import com.example.demo.controller.CucumberController;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertEquals;

public class CucumberControllerSteps {

    private String today;

    private boolean actualAnswer;

    private CucumberController cucumberController = new CucumberController();

    @Given("today is {string}")
    public void today_is(String today) {
        this.today = today;
    }

    @When("I ask whether it's Friday yet")
    public void i_ask_whether_it_s_Friday_yet() {
        actualAnswer = cucumberController.isFriday(today);
    }

    @Then("I should be told {string}")
    public void i_should_be_told(String expectAnswer) {
        assertEquals(Boolean.parseBoolean(expectAnswer), actualAnswer);
    }
}