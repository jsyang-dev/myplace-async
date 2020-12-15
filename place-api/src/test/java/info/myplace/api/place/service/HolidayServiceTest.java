package info.myplace.api.place.service;

import info.myplace.api.place.dto.HolidayDto;
import info.myplace.api.place.mapper.HolidayMapper;
import info.myplace.api.place.repository.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("HolidayService 클래스")
class HolidayServiceTest {

  @Autowired private HolidayService holidayService;
  @Autowired private HolidayRepository holidayRepository;
  @Autowired private HolidayMapper holidayMapper;

  @BeforeEach
  void setUp() {
    holidayRepository.deleteAll();
  }

  @Nested
  @DisplayName("create 메소드는")
  class Create {

    @Test
    @DisplayName("dto를 입력받아서 저장하고 dto를 리턴한다")
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
    @DisplayName("연도를 입력받아서 조회한 dto 리스트를 리턴한다")
    void getListByYear() {

      // Given
      List<HolidayDto> holidayDtos =
          Arrays.asList(
              HolidayDto.builder().date(LocalDate.of(2017, 5, 9)).name("제19대 대통령선거").build(),
              HolidayDto.builder().date(LocalDate.of(1900, 3, 1)).name("삼일절").build(),
              HolidayDto.builder().date(LocalDate.of(2020, 4, 15)).name("제21대 국회의원선거").build());

      holidayRepository.saveAll(
          holidayDtos.stream().map(holidayMapper::toEntity).collect(Collectors.toList()));

      // When
      Flux<HolidayDto> holidayDtoFlux = holidayService.getList(2020);

      // Then
      StepVerifier.create(holidayDtoFlux)
          .expectNextMatches(holidayDtos::contains)
          .expectNextMatches(holidayDtos::contains)
          .verifyComplete();
    }

    @Test
    @DisplayName("연도, 월을 입력받아서 조회한 dto 리스트를 리턴한다")
    void getListByYearAndMonth() {

      // Given
      List<HolidayDto> holidayDtos =
          Arrays.asList(
              HolidayDto.builder().date(LocalDate.of(2017, 5, 9)).name("제19대 대통령선거").build(),
              HolidayDto.builder().date(LocalDate.of(1900, 3, 1)).name("삼일절").build(),
              HolidayDto.builder().date(LocalDate.of(2020, 4, 15)).name("제21대 국회의원선거").build());

      holidayRepository.saveAll(
          holidayDtos.stream().map(holidayMapper::toEntity).collect(Collectors.toList()));

      // When
      Flux<HolidayDto> holidayDtoFlux = holidayService.getList(2020, 3);

      // Then
      StepVerifier.create(holidayDtoFlux).expectNextMatches(holidayDtos::contains).verifyComplete();
    }

    @Test
    @DisplayName("연도, 월, 일을 입력받아서 조회한 dto 리스트를 리턴한다")
    void getListByYearAndMonthAndDay() {

      // Given
      List<HolidayDto> holidayDtos =
          Arrays.asList(
              HolidayDto.builder().date(LocalDate.of(2017, 5, 9)).name("제19대 대통령선거").build(),
              HolidayDto.builder().date(LocalDate.of(1900, 3, 1)).name("삼일절").build(),
              HolidayDto.builder().date(LocalDate.of(2020, 4, 15)).name("제21대 국회의원선거").build());

      holidayRepository.saveAll(
          holidayDtos.stream().map(holidayMapper::toEntity).collect(Collectors.toList()));

      // When
      Flux<HolidayDto> holidayDtoFlux = holidayService.getList(2020, 3, 1);

      // Then
      StepVerifier.create(holidayDtoFlux).expectNextMatches(holidayDtos::contains).verifyComplete();
    }
  }
}
