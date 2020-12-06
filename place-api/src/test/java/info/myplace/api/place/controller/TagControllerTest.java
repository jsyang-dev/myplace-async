package info.myplace.api.place.controller;

import info.myplace.api.place.dto.TagDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@DisplayName("TagController 클래스")
class TagControllerTest {

  @Autowired private WebTestClient webTestClient;

  @Nested
  @DisplayName("POST /tag 요청은")
  class Create {

    @Test
    @DisplayName("dto를 요청받아 저장하고 저장된 dto를 리턴한다")
    void create() {

      // Given
      TagDto tagDto = TagDto.builder().name("태그").build();

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient.post().uri("/tag").body(Mono.just(tagDto), TagDto.class).exchange();

      // Then
      responseSpec
          .expectStatus()
          .isCreated()
          .expectBody()
          .jsonPath("$.id")
          .isNumber()
          .jsonPath("$.name")
          .isEqualTo(tagDto.getName());
    }
  }
}
