package com.aserto.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.aserto")
public class TodoApplication {
	public static void main(String[] args) {
		SpringApplication.run(TodoApplication.class, args);
	}

}
