package info.myplace.api.place.controller;

import info.myplace.api.place.dto.HolidayDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@DisplayName("HolidayController 클래스")
class HolidayControllerTest {

  @Autowired private WebTestClient webTestClient;

  @Nested
  @DisplayName("POST /holiday 요청은")
  class Create {

    @Test
    @DisplayName("dto를 요청받아서 저장하고 dto를 리턴한다")
    void create() {

      // Given
      HolidayDto holidayDto = HolidayDto.builder().date(LocalDate.now()).name("공휴일").build();

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient
              .post()
              .uri("/holiday")
              .body(Mono.just(holidayDto), HolidayDto.class)
              .exchange();

      // Then
      responseSpec
          .expectStatus()
          .isCreated()
          .expectBody()
          .jsonPath("$.id")
          .isNumber()
          .jsonPath("$.date")
          .isEqualTo(holidayDto.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
          .jsonPath("$.name")
          .isEqualTo(holidayDto.getName());
    }
  }
}
