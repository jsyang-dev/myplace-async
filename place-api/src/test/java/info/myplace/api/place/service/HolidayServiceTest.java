package info.myplace.api.place.service;

import info.myplace.api.place.domain.Holiday;
import info.myplace.api.place.dto.HolidayDto;
import info.myplace.api.place.repository.HolidayRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("HolidayService 클래스")
class HolidayServiceTest {

  @Autowired private HolidayService holidayService;
  @Autowired private HolidayRepository holidayRepository;

  @Nested
  @DisplayName("create 메소드는")
  class Create {

    @Test
    @DisplayName("dto를 요청받아서 저장하고 dto를 리턴한다")
    void create() {

      // Given
      HolidayDto holidayDto = HolidayDto.builder().date(LocalDate.now()).name("공휴일").build();

      // When
      Mono<HolidayDto> holidayDtoMono = holidayService.create(holidayDto);

      // Then
      StepVerifier.create(holidayDtoMono)
          .assertNext(
              t -> {
                assertThat(t.getId()).isNotNull();
                assertThat(t.getDate()).isEqualTo(holidayDto.getDate());
                assertThat(t.getName()).isEqualTo(holidayDto.getName());
              })
          .verifyComplete();
    }
  }

  @Nested
  @DisplayName("getList 메소드는")
  class GetList {

    @Test
    @DisplayName("연도를 요청받아서 조회한 dto 리스트를 리턴한다")
    void getListByYear() {

      // Given
      holidayRepository.save(
          Holiday.builder().date(LocalDate.of(2017, 5, 9)).name("제19대 대통령선거").build());
      holidayRepository.save(Holiday.builder().date(LocalDate.of(1900, 3, 1)).name("삼일절").build());
      holidayRepository.save(
          Holiday.builder().date(LocalDate.of(2020, 4, 15)).name("제21대 국회의원선거").build());

      // When
      Flux<HolidayDto> holidayDtoFlux = holidayService.getList(2020);

      // Then
      // TODO: 내용 검증
      StepVerifier.create(holidayDtoFlux).expectNextCount(2).verifyComplete();
    }

    @Test
    @DisplayName("연도, 월을 요청받아서 조회한 dto 리스트를 리턴한다")
    void getListByYearAndMonth() {}

    @Test
    @DisplayName("연도, 월, 일을 요청받아서 조회한 dto 리스트를 리턴한다")
    void getListByYearAndMonthAndDay() {}
  }
}
