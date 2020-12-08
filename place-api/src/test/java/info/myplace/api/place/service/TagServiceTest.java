package info.myplace.api.place.service;

import info.myplace.api.place.dto.TagDto;
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

  @Nested
  @DisplayName("create 메소드는")
  class Create {

    @Test
    @DisplayName("Dto를 저장하고 저장된 Dto를 리턴한다")
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
}
