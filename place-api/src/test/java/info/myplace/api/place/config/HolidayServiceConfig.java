package info.myplace.api.place.config;

import info.myplace.api.place.client.HolidayClient;
import info.myplace.api.place.dto.HolidayDto;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@TestConfiguration
public class HolidayServiceConfig {

  @Bean
  public HolidayClient holidayClient() {
    return (year, month) -> {
      List<HolidayDto> holidayDtos = new ArrayList<>();

      if (year == 2020) {
        if (month == 1 || month == 0) {
          Collections.addAll(
              holidayDtos,
              HolidayDto.builder().date(LocalDate.of(2020, 1, 1)).name("1월1일").build(),
              HolidayDto.builder().date(LocalDate.of(2020, 1, 24)).name("설날").build(),
              HolidayDto.builder().date(LocalDate.of(2020, 1, 25)).name("설날").build(),
              HolidayDto.builder().date(LocalDate.of(2020, 1, 26)).name("설날").build(),
              HolidayDto.builder().date(LocalDate.of(2020, 1, 27)).name("설날").build());
        }
      }

      return Flux.fromIterable(holidayDtos);
    };
  }
}
