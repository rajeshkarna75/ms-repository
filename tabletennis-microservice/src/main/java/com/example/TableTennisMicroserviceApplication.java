package com.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
public class TableTennisMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TableTennisMicroserviceApplication.class, args);
	}
}

@RefreshScope
@RestController
@RequestMapping("tabletennis")
class CricketerRestController {
	
	@Value("${security.user.password}")
	String message;
	
	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public String message() {

		return message + " World!";
	}
}

@RepositoryRestResource
interface TableTennisRepository extends JpaRepository<Tabletennisplayer, Long> {

}

@Entity
class Tabletennisplayer {

	@Id
	@GeneratedValue
	private Long id;
	private String playerName;

	public Tabletennisplayer(String playerName) {

		this.playerName = playerName;
	}

	public Tabletennisplayer() {

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

		return "TableTennis{id = " + id + ", playerName = " + playerName + "}";
	}
}