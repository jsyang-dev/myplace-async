package info.myplace.api.place.service;

import info.myplace.api.place.dto.HolidayDto;
import info.myplace.api.place.dto.HolidayGenerateDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HolidayService {

  Mono<HolidayDto> create(HolidayDto holidayDto);

  Mono<HolidayDto> read(long id);

  Flux<HolidayDto> readList(int year);

  Flux<HolidayDto> readList(int year, int month);

  Flux<HolidayDto> readList(int year, int month, int day);

  Mono<HolidayDto> update(long id, HolidayDto holidayDto);

  void delete(long id);

  Flux<HolidayDto> generate(HolidayGenerateDto holidayGenerateDto);
}
