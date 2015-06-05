package com.mikeycaine.jobserve;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Application implements CommandLineRunner {
	
	@Autowired
	JobserveAPI jobserve;

	@Override
	public void run(String... args) throws Exception {
	}
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}