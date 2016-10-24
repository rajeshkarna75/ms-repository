package com.reservation;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication // IWantToGoHomeEarly
public class ReservationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationApplication.class, args);
	}

	@Bean
	CommandLineRunner runner(ReservationRepository rr) {

		return args -> {
			
			Arrays.asList("Gavaskar,Kapil,Amarnath,Tendulkar,Kohli".split(","))
					.forEach(n -> rr.save(new Reservation(n)));
			rr.findAll().forEach(System.out::println);
			rr.findByReservationName("Gavaskar").forEach(System.out::println);
		};
	}
	
	@Bean
	HealthIndicator healthIndicator() {

		return () ->  Health.status("I <3 SFJUG!").build();
	}
}

@Component
class ReservationResourceProcessor implements ResourceProcessor<Resource<Reservation>> {

	@Override
	public Resource<Reservation> process(Resource<Reservation> reservationResource) {

		reservationResource.add(new Link(
				"http://s3.com/imgs/" + reservationResource.getContent().getId() + ".jpg", "profile-photo"));
		return reservationResource;
	}

}


@RepositoryRestResource
interface ReservationRepository extends JpaRepository<Reservation, Long> {

	List<Reservation> findByReservationName(@Param(value = "reservationName") String reservationName);
}

@Controller
class ReservationMvcController {

	@Autowired
	ReservationRepository reservationRepository;
	
	@RequestMapping("/reservations.php")
	String reservations(Model model) {
		
		model.addAttribute("reservations", this.reservationRepository.findAll());
		return "reservations";
	}
}

/*@RestController
class ReservationRestController {
	
	@Autowired
	ReservationRepository reservationRepository;
	
	@RequestMapping("/reservations")
	Collection<Reservation> reservations() {
		
		return this.reservationRepository.findAll();
	}
}*/

@Entity
class Reservation {

	@Id
	@GeneratedValue
	private Long id;

	private String reservationName;

	Reservation() {

	}

	public Reservation(String reservationName) {

		this.reservationName = reservationName;
	}

	@Override
	public String toString() {

		return "Reservation{id = " + id + ", reservationName = " + reservationName + "}";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReservationName() {
		return reservationName;
	}

	public void setReservationName(String reservationName) {
		this.reservationName = reservationName;
	}
}
