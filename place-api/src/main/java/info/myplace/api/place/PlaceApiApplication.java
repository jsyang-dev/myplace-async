package info.myplace.api.place;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PlaceApiApplication {

  public static void main(String[] args) {
    SpringApplication.run(PlaceApiApplication.class, args);
  }
}
