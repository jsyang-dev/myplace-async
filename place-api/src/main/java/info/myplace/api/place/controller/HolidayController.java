package info.myplace.api.place.controller;

import info.myplace.api.place.dto.HolidayDto;
import info.myplace.api.place.service.HolidayService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.net.URI;

@RestController
@RequestMapping("/holiday")
@RequiredArgsConstructor
public class HolidayController {

  private final HolidayService holidayService;

  @PostMapping
  public Mono<ResponseEntity<HolidayDto>> create(@RequestBody HolidayDto holidayDto) {
    return holidayService
        .create(holidayDto)
        .map(t -> ResponseEntity.created(URI.create("/holiday/" + t.getId())).body(t))
        .defaultIfEmpty(ResponseEntity.noContent().build());
  }
}
