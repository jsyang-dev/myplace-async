package info.myplace.api.place.client;

import info.myplace.api.place.dto.HolidayDto;
import reactor.core.publisher.Flux;

public interface HolidayClient {
  Flux<HolidayDto> getHolidayList(int year, int month);
}
