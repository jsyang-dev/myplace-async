package info.myplace.api.place.service;

import info.myplace.api.place.dto.HolidayDto;
import info.myplace.api.place.mapper.HolidayMapper;
import info.myplace.api.place.repository.HolidayRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

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

  @Override
  public Flux<HolidayDto> getList(int year) {

    Flux<HolidayDto> commonHolidayDtoFlux =
        Flux.fromIterable(
                holidayRepository.findByPeriod(
                    LocalDate.of(1900, 1, 1), LocalDate.of(1900, 12, 31)))
            .map(holidayMapper::toDto);

    Flux<HolidayDto> holidayDtoFlux =
        Flux.fromIterable(
                holidayRepository.findByPeriod(
                    LocalDate.of(year, 1, 1), LocalDate.of(year, 12, 31)))
            .map(holidayMapper::toDto);

    return Flux.concat(commonHolidayDtoFlux, holidayDtoFlux);
  }
}
