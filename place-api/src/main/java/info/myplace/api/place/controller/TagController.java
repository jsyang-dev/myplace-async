package info.myplace.api.place.controller;

import info.myplace.api.place.dto.TagDto;
import info.myplace.api.place.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/tag")
@RequiredArgsConstructor
public class TagController {

  private final TagService tagService;

  @PostMapping
  public Mono<ResponseEntity<TagDto>> create(@RequestBody TagDto tagDto) {
    return tagService
        .create(tagDto)
        .map(t -> ResponseEntity.created(URI.create("/tag/" + t.getId())).body(t))
        .defaultIfEmpty(ResponseEntity.noContent().build());
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<TagDto>> get(@PathVariable long id) {
    return tagService
        .get(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.noContent().build());
  }

  // TODO: ResponseEntity 적용
  @GetMapping
  public Flux<TagDto> getByKeyword(@RequestParam String keyword) {
    return tagService.getByKeyword(keyword);
    //        .map(ResponseEntity::ok)
    //        .defaultIfEmpty(ResponseEntity.noContent().build());
  }

  @DeleteMapping("/{id}")
  public void delete(@PathVariable long id) {
    tagService.delete(id);
  }
}
