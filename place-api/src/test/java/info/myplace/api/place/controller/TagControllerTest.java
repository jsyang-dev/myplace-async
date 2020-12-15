package info.myplace.api.place.controller;

import info.myplace.api.place.dto.TagDto;
import info.myplace.api.place.service.TagService;
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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@WebFluxTest(TagController.class)
@ActiveProfiles("test")
@DisplayName("TagController 클래스")
class TagControllerTest {

  @Autowired private WebTestClient webTestClient;

  @MockBean private TagService tagService;

  @Nested
  @DisplayName("POST /tag 요청은")
  class Create {

    @Test
    @DisplayName("dto를 입력받아서 저장하고 dto를 리턴한다")
    void create() {

      // Given
      TagDto tagDto = TagDto.builder().id(1L).name("태그").build();
      given(tagService.create(tagDto)).willReturn(Mono.just(tagDto));

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient.post().uri("/tag").body(Mono.just(tagDto), TagDto.class).exchange();

      // Then
      responseSpec
          .expectStatus()
          .isCreated()
          .expectHeader()
          .contentType(MediaType.APPLICATION_JSON)
          .expectBody()
          .jsonPath("$.id")
          .isEqualTo(tagDto.getId())
          .jsonPath("$.name")
          .isEqualTo(tagDto.getName());

      verify(tagService, times(1)).create(any());
    }
  }

  @Nested
  @DisplayName("GET /tag 요청은")
  class Get {

    @Test
    @DisplayName("id를 입력받아서 조회한 dto를 리턴한다")
    void get() {

      // Given
      TagDto tagDto = TagDto.builder().id(1L).name("태그").build();
      given(tagService.get(tagDto.getId())).willReturn(Mono.just(tagDto));

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient.get().uri("/tag/{id}", tagDto.getId()).exchange();

      // Then
      responseSpec
          .expectStatus()
          .isOk()
          .expectHeader()
          .contentType(MediaType.APPLICATION_JSON)
          .expectBody()
          .jsonPath("$.id")
          .isEqualTo(tagDto.getId())
          .jsonPath("$.name")
          .isEqualTo(tagDto.getName());
    }

    @Test
    @DisplayName("keyword를 입력받아서 조회한 dto 리스트를 리턴한다")
    void getByKeyword() {

      // Given
      List<TagDto> tagDtos =
          Arrays.asList(
              TagDto.builder().id(1L).name("태그1").build(),
              TagDto.builder().id(2L).name("태그2").build());
      given(tagService.getByKeyword(tagDtos.get(0).getName().substring(0, 1)))
          .willReturn(Flux.fromIterable(tagDtos));

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient
              .get()
              .uri(
                  uriBuilder ->
                      uriBuilder
                          .path("/tag")
                          .queryParam("keyword", tagDtos.get(0).getName().substring(0, 1))
                          .build())
              .exchange();

      // Then
      responseSpec
          .expectStatus()
          .isOk()
          .expectHeader()
          .contentType(MediaType.APPLICATION_JSON)
          .expectBody()
          .jsonPath("$", hasSize(2));
    }
  }

  @Nested
  @DisplayName("DELETE /tag 요청은")
  class Delete {

    @Test
    @DisplayName("dto를 입력받아서 entity를 삭제한다")
    void delete() {

      // When
      WebTestClient.ResponseSpec responseSpec =
          webTestClient.delete().uri("/tag/{id}", 1L).exchange();

      // Then
      responseSpec.expectStatus().isOk().expectBody().isEmpty();
    }
  }
}
