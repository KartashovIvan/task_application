package ru.kartashov.task_application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.util.UriComponentsBuilder;

@SpringBootApplication
public class TaskApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext run = SpringApplication.run(TaskApplication.class, args);
	}

}
