package info.myplace.api.place.service;

import info.myplace.api.place.dto.HolidayDto;
import reactor.core.publisher.Mono;

public interface HolidayService {
  Mono<HolidayDto> create(HolidayDto holidayDto);
}
