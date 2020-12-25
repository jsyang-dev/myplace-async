package info.myplace.api.place.controller;

import info.myplace.api.place.dto.HolidayDto;
import info.myplace.api.place.service.HolidayService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;

@ExtendWith(SpringExtension.class)
@WebFluxTest(HolidayController.class)
@ActiveProfiles("test")
@DisplayName("HolidayController 클래스")
class HolidayControllerTest {

  @Autowired private WebTestClient webTestClient;

  @MockBean private HolidayService holidayService;

  @Nested
  @DisplayName("POST /holidays 요청은")
  class Create {

    @Test
    @DisplayName("dto를 입력받아서 저장하고 dto를 리턴한다")
    void create() {

      // Given
      HolidayDto holidayDto = HolidayDto.builder().date(LocalDate.now()).name("공휴일").build();
      given(holidayService.create(holidayDto)).willReturn(Mono.just(holidayDto));

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient
              .post()
              .uri("/holidays")
              .body(Mono.just(holidayDto), HolidayDto.class)
              .exchange();

      // Then
      responseSpec
          .expectStatus()
          .isCreated()
          .expectHeader()
          .contentType(MediaType.APPLICATION_JSON)
          .expectBody()
          .jsonPath("$.date")
          .isEqualTo(holidayDto.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
          .jsonPath("$.name")
          .isEqualTo(holidayDto.getName());
    }
  }

  @Nested
  @DisplayName("GET /holidays 요청은")
  class Get {

    @Test
    @DisplayName("연도, 월, 일을 입력받아서 조회한 dto 리스트를 리턴한다")
    void getList() {

      // Given
      List<HolidayDto> holidayDtos =
          Arrays.asList(
              HolidayDto.builder().id(1L).date(LocalDate.of(1900, 1, 1)).name("설날").build(),
              HolidayDto.builder().id(2L).date(LocalDate.of(1900, 1, 1)).name("신정").build());
      given(
              holidayService.getList(
                  holidayDtos.get(0).getDate().getYear(),
                  holidayDtos.get(0).getDate().getMonthValue(),
                  holidayDtos.get(0).getDate().getDayOfMonth()))
          .willReturn(Flux.fromIterable(holidayDtos));

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient
              .get()
              .uri(
                  uriBuilder ->
                      uriBuilder
                          .path("/holidays")
                          .queryParam("year", holidayDtos.get(0).getDate().getYear())
                          .queryParam("month", holidayDtos.get(0).getDate().getMonthValue())
                          .queryParam("day", holidayDtos.get(0).getDate().getDayOfMonth())
                          .build())
              .exchange();

      // Then
      responseSpec
          .expectStatus()
          .isOk()
          .expectHeader()
          .contentType(MediaType.APPLICATION_JSON)
          .expectBody()
          .jsonPath("$", hasSize(holidayDtos.size()));
    }
  }

  @Nested
  @DisplayName("PUT /holidays 요청은")
  class Update {

    @Test
    @DisplayName("id와 dto를 입력받아서 수정하고 dto를 리턴한다")
    void update() {

      // Given
      HolidayDto holidayDto = HolidayDto.builder().id(1L).date(LocalDate.now()).name("공휴일").build();
      given(holidayService.update(holidayDto.getId(), holidayDto))
          .willReturn(Mono.just(holidayDto));

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient
              .put()
              .uri("/holidays/{id}", holidayDto.getId())
              .body(Mono.just(holidayDto), HolidayDto.class)
              .exchange();

      // Then
      responseSpec
          .expectStatus()
          .isOk()
          .expectHeader()
          .contentType(MediaType.APPLICATION_JSON)
          .expectBody()
          .jsonPath("$.id")
          .isEqualTo(holidayDto.getId())
          .jsonPath("$.date")
          .isEqualTo(holidayDto.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
          .jsonPath("$.name")
          .isEqualTo(holidayDto.getName());
    }
  }

  @Nested
  @DisplayName("DELETE /holidays 요청은")
  class Delete {

    @Test
    @DisplayName("id를 입력받아서 entity를 삭제한다")
    void delete() {

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient.delete().uri("/holidays/{id}", 1L).exchange();

      // Then
      responseSpec.expectStatus().isOk().expectBody().isEmpty();
    }
  }

  @Nested
  @DisplayName("GET /holidays/actions/generate 요청은")
  class Generate {

    @Test
    @DisplayName("연도, 월을 입력받아서 리스트를 생성하고 리턴한다")
    void generateWithYearAndMonth() {

      // Given
      List<HolidayDto> holidayDtos =
          Arrays.asList(
              HolidayDto.builder().id(1L).date(LocalDate.of(1900, 1, 1)).name("설날").build(),
              HolidayDto.builder().id(2L).date(LocalDate.of(1900, 1, 1)).name("신정").build());
      given(
              holidayService.generate(
                  holidayDtos.get(0).getDate().getYear(),
                  holidayDtos.get(0).getDate().getMonthValue()))
          .willReturn(Flux.fromIterable(holidayDtos));

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient
              .post()
              .uri(
                  "/holidays/actions/generate/{year}/{month}",
                  holidayDtos.get(0).getDate().getYear(),
                  holidayDtos.get(0).getDate().getMonthValue())
              .exchange();

      // Then
      responseSpec
          .expectStatus()
          .isOk()
          .expectHeader()
          .contentType(MediaType.APPLICATION_JSON)
          .expectBody()
          .jsonPath("$", hasSize(holidayDtos.size()));
    }
  }
}
