package info.myplace.api.place.service;

import info.myplace.api.place.dto.HolidayDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("HolidayService 클래스")
class HolidayServiceTest {

  @Autowired private HolidayService holidayService;

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
}
