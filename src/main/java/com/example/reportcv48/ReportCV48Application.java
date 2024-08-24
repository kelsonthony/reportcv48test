package com.example.reportcv48;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class ReportCV48Application {

	public static void main(String[] args) {
		SpringApplication.run(ReportCV48Application.class, args);
	}

}
