package info.myplace.api.place.service;

import info.myplace.api.place.domain.Tag;
import info.myplace.api.place.dto.TagDto;
import info.myplace.api.place.exception.TagNotFoundException;
import info.myplace.api.place.mapper.TagMapper;
import info.myplace.api.place.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  @Override
  public Mono<TagDto> create(TagDto tagDto) {
    return Mono.just(tagMapper.toEntity(tagDto)).map(tagRepository::save).map(tagMapper::toDto);
  }

  @Override
  public TagDto get(long id) {

    Tag tag = tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(id));
    return tagMapper.toDto(tag);
  }
}
