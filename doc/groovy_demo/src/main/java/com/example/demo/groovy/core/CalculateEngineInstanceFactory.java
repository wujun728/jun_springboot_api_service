package com.example.demo.groovy.core;

import com.example.demo.groovy.calculate.GroovyParserEngine;

public class CalculateEngineInstanceFactory {

    private static GroovyParserEngine groovyParserEngine;

    public static GroovyParserEngine getGroovyParserEngine() {
        return groovyParserEngine;
    }

    public static void setGroovyParserEngine(GroovyParserEngine groovyParserEngine) {
        CalculateEngineInstanceFactory.groovyParserEngine = groovyParserEngine;
    }
}
