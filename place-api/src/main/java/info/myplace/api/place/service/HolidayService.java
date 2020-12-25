package info.myplace.api.place.service;

import info.myplace.api.place.dto.HolidayDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface HolidayService {

  Mono<HolidayDto> create(HolidayDto holidayDto);

  Flux<HolidayDto> getList(int year);

  Flux<HolidayDto> getList(int year, int month);

  Flux<HolidayDto> getList(int year, int month, int day);

  Mono<HolidayDto> update(long id, HolidayDto holidayDto);

  void delete(long id);

  Flux<HolidayDto> generate(int year);

  Flux<HolidayDto> generate(int year, int month);
}
