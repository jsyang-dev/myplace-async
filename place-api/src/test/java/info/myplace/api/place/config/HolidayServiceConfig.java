package info.myplace.api.place.config;

import info.myplace.api.place.client.HolidayClient;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class HolidayServiceConfig {

  @Bean
  public HolidayClient holidayClient() {
    return (year, month) -> null;
  }
}
