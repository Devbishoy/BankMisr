package banquemisr.challenge05;

import banquemisr.challenge05.config.BankProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;

@SpringBootApplication
@EnableConfigurationProperties({BankProperties.class})
@EnableScheduling
public class BankMaserApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankMaserApplication.class, args);
	}

}
