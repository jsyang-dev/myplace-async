package info.myplace.api.place.service;

import info.myplace.api.place.dto.TagDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TagService {

  Mono<TagDto> create(TagDto tagDto);

  Mono<TagDto> read(long id);

  Flux<TagDto> readByKeyword(String keyword);

  void delete(long id);
}
