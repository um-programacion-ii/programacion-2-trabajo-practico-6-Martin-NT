package com.example.TP6;

import org.springframework.boot.SpringApplication;

public class TestTp6Application {

	public static void main(String[] args) {
		SpringApplication.from(Tp6Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
