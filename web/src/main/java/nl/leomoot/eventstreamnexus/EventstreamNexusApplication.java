package nl.leomoot.eventstreamnexus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application entry point bootstrapping the Spring context 
 * via {@link SpringApplication}.
 */
@EnableScheduling
@SpringBootApplication
public class EventstreamNexusApplication {

  public static void main(String[] args) {
    SpringApplication.run(EventstreamNexusApplication.class, args);
  }
}
