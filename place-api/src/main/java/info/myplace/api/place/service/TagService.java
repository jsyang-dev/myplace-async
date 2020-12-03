package info.myplace.api.place.service;

import info.myplace.api.place.dto.TagDto;
import reactor.core.publisher.Mono;

public interface TagService {

  Mono<TagDto> create(TagDto tagDto);

  TagDto get(long id);
}
