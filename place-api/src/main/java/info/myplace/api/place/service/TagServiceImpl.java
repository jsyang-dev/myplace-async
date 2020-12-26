package info.myplace.api.place.service;

import info.myplace.api.place.dto.TagDto;
import info.myplace.api.place.exception.TagNotFoundException;
import info.myplace.api.place.mapper.TagMapper;
import info.myplace.api.place.repository.TagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

  private final TagRepository tagRepository;
  private final TagMapper tagMapper;

  @Override
  @Transactional
  public Mono<TagDto> create(TagDto tagDto) {
    return Mono.just(tagMapper.toEntity(tagDto)).map(tagRepository::save).map(tagMapper::toDto);
  }

  @Override
  public Mono<TagDto> get(long id) {
    return Mono.just(tagRepository.findById(id).orElseThrow(() -> new TagNotFoundException(id)))
        .map(tagMapper::toDto);
  }

  @Override
  public Flux<TagDto> getByKeyword(String keyword) {
    return Flux.fromIterable(tagRepository.findByNameStartsWith(keyword)).map(tagMapper::toDto);
  }

  @Override
  @Transactional
  public void delete(long id) {
    tagRepository.deleteById(id);
  }
}
