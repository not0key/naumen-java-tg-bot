package io.proj3ct.SpringNaumenBot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class SpringNaumenBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringNaumenBotApplication.class, args);
	}

}