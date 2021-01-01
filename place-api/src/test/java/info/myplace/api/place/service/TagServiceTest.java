package info.myplace.api.place.service;

import info.myplace.api.place.domain.Tag;
import info.myplace.api.place.dto.TagDto;
import info.myplace.api.place.exception.TagNotFoundException;
import info.myplace.api.place.mapper.TagMapper;
import info.myplace.api.place.repository.TagRepository;
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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("TagService 클래스")
class TagServiceTest {

  @Autowired private TagService tagService;
  @Autowired private TagRepository tagRepository;
  @Autowired private TagMapper tagMapper;

  @BeforeEach
  void setUp() {
    tagRepository.deleteAll();
  }

  @Nested
  @DisplayName("create 메소드는")
  class Create {

    @Test
    @DisplayName("dto를 입력받아서 저장하고 dto를 리턴한다")
    void create() {

      // Given
      TagDto tagDto = TagDto.builder().name("태그").build();

      // When
      Mono<TagDto> tagDtoMono = tagService.create(tagDto);

      // Then
      StepVerifier.create(tagDtoMono)
          .assertNext(
              t -> {
                assertThat(t.getId()).isNotNull();
                assertThat(t.getName()).isEqualTo(tagDto.getName());
              })
          .verifyComplete();
    }
  }

  @Nested
  @DisplayName("read 메소드는")
  class Read {

    @Test
    @DisplayName("id를 입력받아서 조회한 dto를 리턴한다")
    void read() {

      // Given
      Tag tag = tagRepository.save(Tag.builder().name("태그").build());

      // When
      Mono<TagDto> tagDtoMono = tagService.read(tag.getId());

      // Then
      StepVerifier.create(tagDtoMono)
          .assertNext(
              t -> {
                assertThat(t.getId()).isEqualTo(tag.getId());
                assertThat(t.getName()).isEqualTo(tag.getName());
              })
          .verifyComplete();
    }

    @Test
    @DisplayName("존재하지 않는 id를 요청받아서 예외를 발생한다")
    void tagNotFoundException() {

      // Given
      long id = 0L;

      // When & Then
      assertThatThrownBy(() -> tagService.read(id))
          .isInstanceOf(TagNotFoundException.class)
          .hasMessageContaining("유효한 Tag가 존재하지 않습니다")
          .hasMessageContaining(String.valueOf(id));
    }
  }

  @Nested
  @DisplayName("readByKeyword 메소드는")
  class ReadByKeyword {

    @Test
    @DisplayName("keyword를 입력받아서 조회한 dto 리스트를 리턴한다")
    void readByKeyword() {

      // Given
      List<TagDto> tagDtos =
          Arrays.asList(TagDto.builder().name("태그1").build(), TagDto.builder().name("태그2").build());

      tagRepository.saveAll(tagDtos.stream().map(tagMapper::toEntity).collect(Collectors.toList()));

      // When
      Flux<TagDto> tagDtoFlux = tagService.readByKeyword(tagDtos.get(0).getName().substring(0, 1));

      // Then
      StepVerifier.create(tagDtoFlux).expectNextCount(2).verifyComplete();
    }
  }

  @Nested
  @DisplayName("delete 메소드는")
  class Delete {

    @Test
    @DisplayName("id를 입력받아서 entity를 삭제한다")
    void delete() {

      // Given
      Tag tag = tagRepository.save(Tag.builder().name("태그").build());

      // When
      tagService.delete(tag.getId());

      // Then
      if (tagRepository.findById(tag.getId()).isPresent()) {
        throw new AssertionError("Test failed");
      }
    }
  }
}
