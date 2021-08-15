import static ch.qos.logback.classic.Level.DEBUG 
import ch.qos.logback.classic.encoder.PatternLayoutEncoder 
import ch.qos.logback.core.ConsoleAppender 
 
appender("CONSOLE", ConsoleAppender) { 
  encoder(PatternLayoutEncoder) { 
    pattern = "%d{yyyy/MM/dd-HH:mm:ss} %-5level [%thread] %class{5}:%line>>%msg%n" 
  } 
} 
root(DEBUG, ["CONSOLE"]) 