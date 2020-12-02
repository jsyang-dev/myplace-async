package info.myplace.api.place.service;

import info.myplace.api.place.domain.Tag;
import info.myplace.api.place.dto.TagDto;
import info.myplace.api.place.repository.TagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("TagService 클래스")
class TagServiceTest {

  @Autowired private TagService tagService;
  @Autowired private TagRepository tagRepository;

  @Nested
  @DisplayName("create 메소드는")
  class Create {

    @Test
    @DisplayName("dto를 요청받아 저장하고 저장된 dto를 리턴한다")
    void create() {

      // Given
      TagDto tagDto = TagDto.builder().name("태그").build();

      // When
      TagDto savedTagDto = tagService.create(tagDto);

      // Then
      assertThat(savedTagDto.getId()).isNotNull();
      assertThat(savedTagDto.getName()).isEqualTo(tagDto.getName());
    }
  }

  @Nested
  @DisplayName("get 메소드는")
  class Get {

    @Test
    @DisplayName("id를 요청받아서 dto를 리턴한다")
    void get() {

      // Given
      Tag tag = tagRepository.save(Tag.builder().name("태그").build());

      // When
      TagDto tagDto = tagService.get(tag.getId());

      // Then
      assertThat(tagDto.getId()).isEqualTo(tag.getId());
      assertThat(tagDto.getName()).isEqualTo(tag.getName());
    }

    @Test
    @DisplayName("존재하지 않는 id를 요청받아서 예외를 발생한다")
    void tagNotFoundException() {}
  }
}
