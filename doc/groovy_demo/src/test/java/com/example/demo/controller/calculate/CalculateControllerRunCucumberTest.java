package com.example.demo.controller.calculate;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = {"src/test/resources/features/calculate/calculate_reward.feature"})
public class CalculateControllerRunCucumberTest {
}
