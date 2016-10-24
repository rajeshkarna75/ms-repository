package com.example;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
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
	
	@Autowired CricketFeignClient cricketFeignClient;
	
	@Autowired TableTennisRepository ttRepository;

	@RequestMapping(value = "/message", method = RequestMethod.GET)
	public String message() {

		return message + " World!";
	}
	
	@RequestMapping(value = "/getCricketPlayers", method = RequestMethod.GET)
	public Collection<Cricketer> cricketPlayers() {

		return cricketFeignClient.getCricketers();
	}
	
	@RequestMapping(value = "/getAllSportsPlayersNames", method = RequestMethod.GET)
	public Collection<String> getAllSportsPlayersNames() {
		
		List<String> cricketPlayerNames = cricketFeignClient.getCricketers().stream().map(cricketer -> cricketer.getPlayerName()).collect(Collectors.toList());
		List<String> ttPlayerNames = ttRepository.findAll().stream().map(ttPlayer -> ttPlayer.getPlayerName()).collect(Collectors.toList());
		cricketPlayerNames.addAll(ttPlayerNames);
		return cricketPlayerNames;
	}


}

@FeignClient(name = "access-cricket-micro-service", url = "http://localhost:9999/cricketservice/cricketers")
interface CricketFeignClient {

	@RequestMapping(value = "/getCricketPlayers", method = RequestMethod.GET)
	Collection<Cricketer> getCricketers();
}

class Cricketer {

	private String playerName;

	public String getPlayerName() {
		return this.playerName;
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