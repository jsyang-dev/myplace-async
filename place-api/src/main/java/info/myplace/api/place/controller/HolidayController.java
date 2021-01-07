package info.myplace.api.place.controller;

import info.myplace.api.place.dto.HolidayDto;
import info.myplace.api.place.dto.HolidayGenerateDto;
import info.myplace.api.place.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/holidays")
@RequiredArgsConstructor
public class HolidayController {

  private final HolidayService holidayService;

  @PostMapping
  public Mono<ResponseEntity<HolidayDto>> create(@RequestBody @Valid HolidayDto holidayDto) {
    return holidayService
        .create(holidayDto)
        .map(h -> ResponseEntity.created(URI.create("/holidays/" + h.getId())).body(h))
        .defaultIfEmpty(ResponseEntity.noContent().build());
  }

  @GetMapping("/{id}")
  public Mono<ResponseEntity<HolidayDto>> read(@PathVariable long id) {
    return holidayService
        .read(id)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.noContent().build());
  }

  // TODO: ResponseEntity 적용
  @GetMapping
  public Flux<HolidayDto> readList(
      @RequestParam int year, @RequestParam int month, @RequestParam int day) {
    return holidayService.readList(year, month, day);
  }

  @PutMapping("/{id}")
  public Mono<ResponseEntity<HolidayDto>> update(
      @PathVariable long id, @RequestBody @Valid HolidayDto holidayDto) {
    return holidayService
        .update(id, holidayDto)
        .map(ResponseEntity::ok)
        .defaultIfEmpty(ResponseEntity.noContent().build());
  }

  @DeleteMapping("/{id}")
  public Mono<ResponseEntity<Void>> delete(@PathVariable long id) {
    holidayService.delete(id);
    return Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
  }

  @PostMapping("/generate")
  public Flux<HolidayDto> generate(@RequestBody @Valid HolidayGenerateDto holidayGenerateDto) {
    return holidayService.generate(holidayGenerateDto);
  }
}
