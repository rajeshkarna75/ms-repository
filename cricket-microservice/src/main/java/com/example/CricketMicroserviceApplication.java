package com.example;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
public class CricketMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(CricketMicroserviceApplication.class, args);
	}
}

@RefreshScope
@RestController
@RequestMapping("cricketers")
class CricketerRestController {
	
	private static Logger logger = LoggerFactory.getLogger(CricketerRestController.class);
	
	@Value("${security.user.password}")
	String message;
	
	@Autowired CricketRepository cricketRepository;
	
	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public String message() {

		logger.info("\n\nHi............\n");
		return message + " World!";
	}
	
	@RequestMapping(value = "/getCricketPlayers", method = RequestMethod.GET)
	public Collection<Cricketer> cricketPlayers() {

		return cricketRepository.findAll();
	}
}

@RepositoryRestResource
interface CricketRepository extends JpaRepository<Cricketer, Long> {

}

@Entity
class Cricketer {

	@Id
	@GeneratedValue
	private Long id;
	private String playerName;

	public Cricketer(String playerName) {

		this.playerName = playerName;
	}

	public Cricketer() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPlayerName() {
		return playerName;
	}

	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}

	@Override
	public String toString() {

		return "Cricketer{id = " + id + ", playerName = " + playerName + "}";
	}
}