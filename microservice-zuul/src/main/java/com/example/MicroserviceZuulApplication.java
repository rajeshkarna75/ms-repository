package com.example;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
@EnableHystrix
public class MicroserviceZuulApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceZuulApplication.class, args);
	}
}

@RestController
@RequestMapping("/players")
class PlayerAPIGatewayRestController {
	
	private static Logger logger = LoggerFactory.getLogger(PlayerAPIGatewayRestController.class);

	@Autowired
	RestTemplate restTemplate;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@RequestMapping(value = "/names", method = RequestMethod.GET)
	@HystrixCommand(fallbackMethod = "getPlayerNamesFallback")
	public Collection<String> getPlayerNames() {

		Collection<String> ttPlayers = getTableTennisPlayerNames();
		Collection<String> cricketers = getCricketPlayerNames();
		
		cricketers.addAll(ttPlayers);
		return cricketers;
	}
	
	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public String message() {

		return "Hello World!";
	}

	public Collection<String> getCricketPlayerNames() {
		ParameterizedTypeReference<Resources<Cricketer>> ptr = new ParameterizedTypeReference<Resources<Cricketer>>() {
		};

		ResponseEntity<Resources<Cricketer>> entity = this.restTemplate
				.exchange("http://localhost:9999/cricketservice/cricketers", HttpMethod.GET, null, ptr);
		return entity.getBody().getContent().stream().map(Cricketer::getPlayerName).collect(Collectors.toList());
	}

	public Collection<String> getTableTennisPlayerNames() {
		ParameterizedTypeReference<Resources<Tabletennisplayer>> ptr = new ParameterizedTypeReference<Resources<Tabletennisplayer>>() {
		};

		ResponseEntity<Resources<Tabletennisplayer>> entity = this.restTemplate
				.exchange("http://localhost:9999/tabletennisservice/tabletennisplayers", HttpMethod.GET, null, ptr);
		return entity.getBody().getContent().stream().map(Tabletennisplayer::getPlayerName)
				.collect(Collectors.toList());
	}
	
	public Collection<String> getPlayerNamesFallback() {
		
		logger.info("Inside getPlayerNamesFallback method.");
		return new ArrayList<String>();
	}
}

class Cricketer {

	private String playerName;

	public String getPlayerName() {
		return this.playerName;
	}
}

class Tabletennisplayer {

	private String playerName;

	public String getPlayerName() {
		return this.playerName;
	}
}