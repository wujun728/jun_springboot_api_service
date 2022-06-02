package com.example.demo.controller.cucumber;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"pretty"}, features = {"src/test/resources/features/cucumber/is_it_friday_yet.feature"})
public class CucumberControllerRunCucumberTest {
}
