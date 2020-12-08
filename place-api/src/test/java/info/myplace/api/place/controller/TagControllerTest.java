package info.myplace.api.place.controller;

import info.myplace.api.place.domain.Tag;
import info.myplace.api.place.dto.TagDto;
import info.myplace.api.place.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureWebTestClient
@ActiveProfiles("test")
@DisplayName("TagController 클래스")
class TagControllerTest {

  @Autowired private WebTestClient webTestClient;
  @Autowired private TagRepository tagRepository;

  @Nested
  @DisplayName("POST /tag 요청은")
  class Create {

    @Test
    @DisplayName("dto를 요청받아서 저장하고 dto를 리턴한다")
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

  @Nested
  @DisplayName("GET /tag 요청은")
  class Get {

    @Test
    @DisplayName("id를 요청받아서 조회한 dto를 리턴한다")
    void get() {

      // Given
      Tag tag = tagRepository.save(Tag.builder().name("태그").build());

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient.get().uri("/tag/{id}", tag.getId()).exchange();

      // Then
      responseSpec
          .expectStatus()
          .isOk()
          .expectBody()
          .jsonPath("$.id")
          .isEqualTo(tag.getId())
          .jsonPath("$.name")
          .isEqualTo(tag.getName());
    }

    @Test
    @DisplayName("keyword를 요청받아서 조회한 dto 리스트를 리턴한다")
    void getByKeyword() {

      // Given
      Tag tag = tagRepository.save(Tag.builder().name("태그1").build());
      tagRepository.save(Tag.builder().name("태그2").build());

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient
              .get()
              .uri(
                  uriBuilder ->
                      uriBuilder
                          .path("/tag")
                          .queryParam("keyword", tag.getName().substring(0, 1))
                          .build())
              .exchange();

      // Then
      responseSpec.expectStatus().isOk().expectBody().jsonPath("$", hasSize(2));
    }
  }

  @Nested
  @DisplayName("DELETE /tag 요청은")
  class Delete {

    @Test
    @DisplayName("dto를 요청받아서 entity를 삭제한다")
    void delete() {

      // Given
      Tag tag = tagRepository.save(Tag.builder().name("태그").build());

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient.delete().uri("/tag/{id}", tag.getId()).exchange();

      // Then
      responseSpec.expectStatus().isOk();
      if (tagRepository.findById(tag.getId()).isPresent()) {
        throw new AssertionError("Test failed");
      }
    }
  }
}
