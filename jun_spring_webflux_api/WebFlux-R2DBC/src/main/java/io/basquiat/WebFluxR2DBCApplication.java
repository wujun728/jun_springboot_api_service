package io.basquiat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.web.reactive.config.EnableWebFlux;

@EnableR2dbcRepositories
@SpringBootApplication
public class WebFluxR2DBCApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebFluxR2DBCApplication.class, args);
	}

}
