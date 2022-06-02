package foo;





import org.springframework.context.annotation.Configuration; 
import org.springframework.context.annotation.ImportResource; 

@Configuration 
@ImportResource("classpath:root-context.xml") 
public class SpringConfig { }
