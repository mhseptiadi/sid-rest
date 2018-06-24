package com.sidindonesia.rest;

import java.io.IOException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import service.ParseDataService;

@SpringBootApplication
@ComponentScan({"controller","service"})
public class RestApplication {

	public static void main(String[] args) throws IOException {
		//polpulate data at first run
		ParseDataService.parseSource("https://raw.githubusercontent.com/onaio/ona-tech/master/data/water_points.json");
		SpringApplication.run(RestApplication.class, args);
	}
}
