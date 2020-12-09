package info.myplace.api.place.service;

import info.myplace.api.place.dto.HolidayDto;
import info.myplace.api.place.mapper.HolidayMapper;
import info.myplace.api.place.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class HolidayServiceImpl implements HolidayService {

  private final HolidayRepository holidayRepository;
  private final HolidayMapper holidayMapper;

  @Override
  public Mono<HolidayDto> create(HolidayDto holidayDto) {
    return Mono.just(holidayMapper.toEntity(holidayDto))
        .map(holidayRepository::save)
        .map(holidayMapper::toDto);
  }
}
